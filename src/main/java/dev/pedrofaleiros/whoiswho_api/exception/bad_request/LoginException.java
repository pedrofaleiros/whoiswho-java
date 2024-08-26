package dev.pedrofaleiros.whoiswho_api.exception.bad_request;

public class LoginException extends CustomBadRequestException {
    public LoginException() {
        super("Login e/ou senha incorretos.");
    }
}
