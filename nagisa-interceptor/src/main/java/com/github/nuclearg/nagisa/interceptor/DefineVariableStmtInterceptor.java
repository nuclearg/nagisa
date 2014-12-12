package com.github.nuclearg.nagisa.interceptor;

import com.github.nuclearg.nagisa.frontend.ast.DefineVariableStmt;
import com.github.nuclearg.nagisa.frontend.identifier.TypeIdentifierInfo;
import com.github.nuclearg.nagisa.interceptor.ex.NagisaInterceptorInternalException;

/**
 * 变量定义语句的解释器
 * 
 * @author ng
 *
 */
final class DefineVariableStmtInterceptor extends StmtInterceptor {
    /**
     * 变量名称
     */
    private final String name;
    /**
     * 变量类型
     */
    private final TypeIdentifierInfo type;

    DefineVariableStmtInterceptor(DefineVariableStmt stmt) {
        this.name = stmt.getName();
        this.type = stmt.getType();
    }

    @Override
    protected void eval(Context ctx) {
        ctx.setVariableValue(this.name, this.getTypeDefaultValue(this.type));
    }

    private Value getTypeDefaultValue(TypeIdentifierInfo type) {
        if (type == TypeIdentifierInfo.INTEGER)
            return new Value(0);
        if (type == TypeIdentifierInfo.STRING)
            return new Value("");
        if (type == TypeIdentifierInfo.BOOLEAN)
            return new Value(false);
        throw new NagisaInterceptorInternalException("unsupported value type " + type);
    }
}
