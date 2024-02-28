package org.meta.happiness.webide.common.exception;

public class FileMetaDataNotFoundException extends RuntimeException {
    public FileMetaDataNotFoundException() {
        super();
    }

    public FileMetaDataNotFoundException(String message) {
        super(message);
    }

    public FileMetaDataNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
