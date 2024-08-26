package dev.pedrofaleiros.whoiswho_api.exception;

import org.springframework.security.core.AuthenticationException;

public class NotAuthException extends AuthenticationException {

    public NotAuthException() {
        super("UNAUTHORIZED");
    }
}
