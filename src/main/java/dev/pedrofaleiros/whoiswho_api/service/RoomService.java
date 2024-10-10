package dev.pedrofaleiros.whoiswho_api.service;

import dev.pedrofaleiros.whoiswho_api.dto.request.UpdateRoomDTO;
import dev.pedrofaleiros.whoiswho_api.entity.Room;

public interface RoomService {
    String generateRoomId();

    String createRoom(String username);

    Room findById(String id);

    Room updateRoom(UpdateRoomDTO data);

    Room startGame(String roomId);

    Room finishGame(String roomId, String username);
}
