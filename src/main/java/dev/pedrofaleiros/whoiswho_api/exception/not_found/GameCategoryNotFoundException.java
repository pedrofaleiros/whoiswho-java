package dev.pedrofaleiros.whoiswho_api.exception.not_found;

public class GameCategoryNotFoundException extends CustomEntityNotFoundException {

    public GameCategoryNotFoundException() {
        super("Categoria nao encontrada");
    }
}
