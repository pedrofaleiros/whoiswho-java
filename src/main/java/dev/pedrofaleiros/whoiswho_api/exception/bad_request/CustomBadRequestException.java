package dev.pedrofaleiros.whoiswho_api.exception.bad_request;

public class CustomBadRequestException extends RuntimeException {
    public CustomBadRequestException(String message) {
        super(message);
    }
}
