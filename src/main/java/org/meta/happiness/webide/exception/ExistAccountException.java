package org.meta.happiness.webide.exception;

public class ExistAccountException extends RuntimeException{
    public ExistAccountException() {
    }
    public ExistAccountException(String message) {
        super(message);
    }

    public ExistAccountException(String message, Throwable cause) {
        super(message, cause);
    }
}
