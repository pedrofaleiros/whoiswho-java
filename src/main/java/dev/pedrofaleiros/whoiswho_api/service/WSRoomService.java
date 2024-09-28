package dev.pedrofaleiros.whoiswho_api.service;

import dev.pedrofaleiros.whoiswho_api.dto.request.UpdateRoomDTO;
import dev.pedrofaleiros.whoiswho_api.dto.response.UserResponseDTO;
import dev.pedrofaleiros.whoiswho_api.entity.Game;
import dev.pedrofaleiros.whoiswho_api.entity.Room;
import java.util.List;

public interface WSRoomService {

    Room addUserToRoom(String roomId, String username);

    List<UserResponseDTO> removeUserFromRoom(String roomId, String username);

    List<UserResponseDTO> getRoomUsers(String roomId);

    Room updateRoomData(UpdateRoomDTO data);

    Game createGame(String roomId);
}
