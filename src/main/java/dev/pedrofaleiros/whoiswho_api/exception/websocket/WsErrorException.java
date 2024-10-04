package dev.pedrofaleiros.whoiswho_api.exception.websocket;

public class WsErrorException extends RuntimeException {
    public WsErrorException(String message) {
        super(message);
    }
}
