package dev.pedrofaleiros.whoiswho_api.exception;

public class LoginException extends RuntimeException {
    public LoginException() {
        super("Login e/ou senha incorretos.");
    }
}
