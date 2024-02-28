package org.meta.happiness.webide.common.exception;

public class FileMetaDataPathException extends RuntimeException {
    public FileMetaDataPathException() {
        super();
    }

    public FileMetaDataPathException(String message) {
        super(message);
    }

    public FileMetaDataPathException(String message, Throwable cause) {
        super(message, cause);
    }
}
