package org.meta.happiness.webide.exception;

public class PasswordPatternException extends RuntimeException{

    public PasswordPatternException() {
    }

    public PasswordPatternException(String message) {
        super(message);
    }

    public PasswordPatternException(String message, Throwable cause) {
        super(message, cause);
    }
}