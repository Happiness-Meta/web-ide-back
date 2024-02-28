package org.meta.happiness.webide.common.exception;

public class FileAlreadyExistsException extends RuntimeException {
    public FileAlreadyExistsException() {
        super();
    }

    public FileAlreadyExistsException(String message) {
        super(message);
    }

    public FileAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}

