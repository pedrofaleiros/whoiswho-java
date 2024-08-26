package dev.pedrofaleiros.whoiswho_api.exception.not_found;

public class CustomEntityNotFoundException extends RuntimeException {
    public CustomEntityNotFoundException(String message) {
        super(message);
    }
}
