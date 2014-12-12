package com.github.nuclearg.nagisa.interceptor;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;

import com.github.nuclearg.nagisa.frontend.ast.DefineFunctionStmt;
import com.github.nuclearg.nagisa.frontend.ast.DefineFunctionStmtBase;
import com.github.nuclearg.nagisa.frontend.ast.DefineNativeFunctionStmt;
import com.github.nuclearg.nagisa.frontend.ast.DefineNativeSubStmt;
import com.github.nuclearg.nagisa.frontend.ast.DefineSubStmt;
import com.github.nuclearg.nagisa.frontend.ast.Stmt;
import com.github.nuclearg.nagisa.frontend.identifier.TypeIdentifierInfo;
import com.github.nuclearg.nagisa.frontend.identifier.VariableIdentifierInfo;
import com.github.nuclearg.nagisa.interceptor.ex.FunctionReturnException;
import com.github.nuclearg.nagisa.interceptor.ex.NagisaCodeRuntimeException;
import com.github.nuclearg.nagisa.interceptor.ex.NagisaInterceptorInternalException;

/**
 * 函数的解释器
 * 
 * @author ng
 *
 */
final class FunctionInterceptor extends StmtInterceptor {
    private static final String NATIVE_CLASS_PACKAGE = "com.github.nuclearg.nagisa.rtlib.nativelib";

    /**
     * 函数名
     */
    private final String name;
    /**
     * 形参列表
     */
    private final List<VariableIdentifierInfo> parameters;
    /**
     * 返回值类型
     */
    private final TypeIdentifierInfo type;
    /**
     * 返回类型是不是void
     */
    private final boolean isVoid;

    /**
     * 是否是本地函数
     */
    private final boolean isNative;
    /**
     * 函数体
     */
    private final List<StmtInterceptor> stmts;
    /**
     * 如果是本地方法，指向实际被调用的java方法
     */
    private final Method nativeMethod;

    FunctionInterceptor(Stmt stmt) {
        if (!(stmt instanceof DefineFunctionStmtBase))
            throw new NagisaInterceptorInternalException("stmt不是函数定义语句. stmt: " + stmt);

        DefineFunctionStmtBase funcStmt = (DefineFunctionStmtBase) stmt;
        this.name = funcStmt.getName();
        this.parameters = funcStmt.getParameters();
        this.type = funcStmt.getType();
        this.isVoid = this.type == TypeIdentifierInfo.VOID;

        if (stmt instanceof DefineFunctionStmt) {
            DefineFunctionStmt s = (DefineFunctionStmt) stmt;

            this.isNative = false;
            this.stmts = StmtInterceptor.buildInterceptors(s.getStmts());
            this.nativeMethod = null;
        } else if (stmt instanceof DefineSubStmt) {
            DefineSubStmt s = (DefineSubStmt) stmt;
            this.isNative = false;
            this.stmts = StmtInterceptor.buildInterceptors(s.getStmts());
            this.nativeMethod = null;
        } else if (stmt instanceof DefineNativeFunctionStmt) {
            DefineNativeFunctionStmt s = (DefineNativeFunctionStmt) stmt;
            this.isNative = true;
            this.stmts = null;
            this.nativeMethod = locateNativeMethod(s.getJavaClassName(), s.getJavaMethodName());
        } else if (stmt instanceof DefineNativeSubStmt) {
            DefineNativeSubStmt s = (DefineNativeSubStmt) stmt;
            this.isNative = true;
            this.stmts = null;
            this.nativeMethod = locateNativeMethod(s.getJavaClassName(), s.getJavaMethodName());
        } else {
            throw new NagisaInterceptorInternalException("unsupported function stmt: " + stmt);
        }

    }

    /** 函数名 */
    public String getName() {
        return this.name;
    }

    /** 形参列表 */
    public List<VariableIdentifierInfo> getParameters() {
        return this.parameters;
    }

    @Override
    protected void eval(Context ctx) {
        // 检查实参的数量和类型
        long paramCount = this.parameters.size();// 形参数量
        long argCount = ctx.getIntegerVariableValue("$ARG_COUNT");// 实参数量

        if (argCount != paramCount)
            throw new NagisaCodeRuntimeException("函数或方法 " + this.name + " 的形参和实参的数量不匹配");

        for (int i = 0; i < this.parameters.size(); i++) {
            Value argument = ctx.getVariableValue("$ARG_" + i);
            VariableIdentifierInfo parameter = this.parameters.get(i);

            // 检查类型
            if (argument.getType() != parameter.getType())
                throw new NagisaCodeRuntimeException("函数或方法 " + this.name + " 的第 " + i + " 个参数（" + parameter.getName() + "）的类型不匹配。期望的类型是 " + parameter.getType() + "，但实际的值是 " + argument + "，其类型为 " + argument.getType());

            // 将实参设到形参上
            ctx.setVariableValue(parameter.getName(), argument);
        }

        // 执行函数
        if (this.isNative)
            this.evalNativeFunction(ctx);
        else
            this.evalNagisaFunction(ctx);

        // 检查返回值
        if (!this.isVoid) {
            ctx.getVariableValue("$RET_VALUE");
        }
    }

    /**
     * 执行用户函数
     */
    private void evalNagisaFunction(Context ctx) {
        try {
            StmtInterceptor.eval(this.stmts, ctx);
        } catch (FunctionReturnException ex) {
        }
    }

    /**
     * 执行本地函数
     */
    private void evalNativeFunction(Context ctx) {
        Object[] args = new Object[this.parameters.size()];
        try {
            for (int i = 0; i < this.parameters.size(); i++) {
                Object v = ctx.getVariableValue("$ARG_" + i).getValue();
                args[i] = v;
            }

            Object value = this.nativeMethod.invoke(null, args);

            if (!this.isVoid)
                ctx.setVariableValue("$RET_VALUE", new Value(value, this.type));
        } catch (Exception ex) {
            throw new NagisaCodeRuntimeException("调用本地方法 " + this.nativeMethod.getDeclaringClass().getName() + "." + this.nativeMethod.getName() + " 时发生异常. " + ExceptionUtils.getStackTrace(ex));
        }
    }

    private static Method locateNativeMethod(String className, String methodName) {
        try {
            Class<?> cls = Class.forName(NATIVE_CLASS_PACKAGE + "." + className);
            for (Method method : cls.getMethods())
                if (method.getName().equals(methodName))
                    if (Modifier.isPublic(method.getModifiers()) && Modifier.isStatic(method.getModifiers()))
                        // TODO 检查本地方法的参数列表与声明的形参列表是否匹配
                        return method;
                    else
                        throw new NagisaCodeRuntimeException("native method not public and static. javaClassName: " + className + ", javaMethodName: " + methodName);

            throw new NagisaCodeRuntimeException("native method not found. javaClassName: " + className + ", javaMethodName: " + methodName);
        } catch (Exception ex) {
            throw new NagisaInterceptorInternalException(ex);
        }
    }

}
