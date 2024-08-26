package dev.pedrofaleiros.whoiswho_api.exception.not_found;

public class GameEnvNotFoundException extends CustomEntityNotFoundException {
    public GameEnvNotFoundException() {
        super("Ambiente não encontrado");
    }
}
