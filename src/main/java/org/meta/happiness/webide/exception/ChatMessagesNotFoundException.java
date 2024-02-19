package org.meta.happiness.webide.exception;

public class ChatMessagesNotFoundException extends RuntimeException {
    public ChatMessagesNotFoundException() {
        super();
    }

    public ChatMessagesNotFoundException(String message) {
        super(message);
    }

    public ChatMessagesNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
