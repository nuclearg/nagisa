package com.github.nuclearg.nagisa.interceptor.ex;

/**
 * 当在执行代码时遇到了运行时错误（例如被0除之类）抛出此异常，表示用户代码中存在逻辑错误
 * 
 * @author ng
 *
 */
public class NagisaCodeRuntimeException extends RuntimeException {
    private static final long serialVersionUID = 3697576220006043197L;

    public NagisaCodeRuntimeException(String message) {
        super(message);
    }
}
