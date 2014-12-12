package com.github.nuclearg.nagisa.interceptor.ex;

/**
 * 当在执行代码时解释器遇到无法处理的意外情况时抛出此异常，这通常表示解释器或前端的内部bug
 * 
 * @author ng
 *
 */
public final class NagisaInterceptorInternalException extends RuntimeException {
    private static final long serialVersionUID = 2130083232092206702L;

    public NagisaInterceptorInternalException(String message) {
        super(message);
    }

    public NagisaInterceptorInternalException(Throwable cause) {
        super(cause);
    }

    public NagisaInterceptorInternalException(String message, Throwable cause) {
        super(message, cause);
    }
}
