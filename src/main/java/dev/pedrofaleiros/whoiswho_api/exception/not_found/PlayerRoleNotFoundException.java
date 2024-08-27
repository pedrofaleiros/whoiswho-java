package dev.pedrofaleiros.whoiswho_api.exception.not_found;

public class PlayerRoleNotFoundException extends CustomEntityNotFoundException {

    public PlayerRoleNotFoundException() {
        super("Papel nao encontrado");
    }

}
