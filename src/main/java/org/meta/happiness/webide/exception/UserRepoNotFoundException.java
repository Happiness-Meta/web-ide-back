package org.meta.happiness.webide.exception;

public class UserRepoNotFoundException extends RuntimeException{
    public UserRepoNotFoundException() {
        super();
    }

    public UserRepoNotFoundException(String message) {
        super(message);
    }

    public UserRepoNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
