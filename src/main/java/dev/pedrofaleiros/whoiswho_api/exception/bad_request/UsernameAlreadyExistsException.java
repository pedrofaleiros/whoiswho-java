package dev.pedrofaleiros.whoiswho_api.exception.bad_request;

public class UsernameAlreadyExistsException extends CustomBadRequestException {

    public UsernameAlreadyExistsException() {
        super("Username já cadastrado");
    }

}
