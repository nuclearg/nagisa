package com.github.nuclearg.nagisa.lang.util;

public class NagisaException extends RuntimeException {
    private static final long serialVersionUID = -3535291531051637257L;

    public NagisaException(String message) {
        super(message);
    }

    public NagisaException(String message, Throwable cause) {
        super(message, cause);
    }
}
