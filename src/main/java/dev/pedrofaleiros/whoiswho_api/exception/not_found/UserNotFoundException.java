package dev.pedrofaleiros.whoiswho_api.exception.not_found;

public class UserNotFoundException extends CustomEntityNotFoundException {

    public UserNotFoundException() {
        super("Usuario nao encontrado");
    }
}
