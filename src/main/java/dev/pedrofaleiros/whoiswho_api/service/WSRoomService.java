package dev.pedrofaleiros.whoiswho_api.service;

import dev.pedrofaleiros.whoiswho_api.dto.request.UpdateRoomDTO;
import dev.pedrofaleiros.whoiswho_api.dto.response.UserResponseDTO;
import dev.pedrofaleiros.whoiswho_api.entity.Game;
import dev.pedrofaleiros.whoiswho_api.entity.Room;
import java.security.Principal;
import java.util.List;

public interface WSRoomService {

    List<UserResponseDTO> addUserToRoom(String roomId, String username, String sessionId);

    List<UserResponseDTO> removeUserFromRoom(String sessionId, String roomId);

    List<UserResponseDTO> getRoomUsers(String roomId);

    Room getRoomData(String roomId);

    Room updateRoomData(UpdateRoomDTO data);

    Game startGame(String roomId, String username);

    Room finishGame(String roomId, String username);

    Game getLatestGame(String roomId);

    List<Game> listRoomGames(String roomId);

    String extractUsername(Principal principal);
}
