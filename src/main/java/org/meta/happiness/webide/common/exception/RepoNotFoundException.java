package org.meta.happiness.webide.common.exception;

public class RepoNotFoundException extends RuntimeException{
    public RepoNotFoundException() {
        super();
    }

    public RepoNotFoundException(String message) {
        super(message);
    }

    public RepoNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
