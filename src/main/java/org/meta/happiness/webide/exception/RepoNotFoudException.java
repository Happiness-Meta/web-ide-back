package org.meta.happiness.webide.exception;

public class RepoNotFoudException extends RuntimeException{
    public RepoNotFoudException() {
        super();
    }

    public RepoNotFoudException(String message) {
        super(message);
    }

    public RepoNotFoudException(String message, Throwable cause) {
        super(message, cause);
    }
}
