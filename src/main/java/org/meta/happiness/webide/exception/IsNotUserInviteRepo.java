package org.meta.happiness.webide.exception;

public class IsNotUserInviteRepo extends RuntimeException{
    public IsNotUserInviteRepo() {
        super();
    }

    public IsNotUserInviteRepo(String message) {
        super(message);
    }

    public IsNotUserInviteRepo(String message, Throwable cause) {
        super(message, cause);
    }
}
