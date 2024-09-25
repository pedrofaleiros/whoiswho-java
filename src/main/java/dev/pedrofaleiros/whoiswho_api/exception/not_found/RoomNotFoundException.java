package dev.pedrofaleiros.whoiswho_api.exception.not_found;

public class RoomNotFoundException extends CustomEntityNotFoundException {
    public RoomNotFoundException(String id) {
        super("Sala '" + id + "' não encontrada");
    }
}

