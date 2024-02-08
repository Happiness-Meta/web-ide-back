package org.meta.happiness.webide.exception;

public class UserNicknameDuplicatedException extends RuntimeException{
    public UserNicknameDuplicatedException() {

    }
    public UserNicknameDuplicatedException(String message) {
        super(message);
    }

    public UserNicknameDuplicatedException(String message, Throwable cause) {
        super(message, cause);
    }
}
