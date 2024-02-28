package org.meta.happiness.webide.common.exception;

public class RepositoryCreatorMismatchException extends RuntimeException {
    public RepositoryCreatorMismatchException() {
        super();
    }

    public RepositoryCreatorMismatchException(String message) {
        super(message);
    }

    public RepositoryCreatorMismatchException(String message, Throwable cause) {
        super(message, cause);
    }
}