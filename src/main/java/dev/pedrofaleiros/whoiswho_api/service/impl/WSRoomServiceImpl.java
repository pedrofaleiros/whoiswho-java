package dev.pedrofaleiros.whoiswho_api.service.impl;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import dev.pedrofaleiros.whoiswho_api.dto.request.UpdateRoomDTO;
import dev.pedrofaleiros.whoiswho_api.dto.response.UserResponseDTO;
import dev.pedrofaleiros.whoiswho_api.entity.Game;
import dev.pedrofaleiros.whoiswho_api.entity.Room;
import dev.pedrofaleiros.whoiswho_api.exception.bad_request.CustomBadRequestException;
import dev.pedrofaleiros.whoiswho_api.exception.not_found.CustomEntityNotFoundException;
import dev.pedrofaleiros.whoiswho_api.exception.websocket.WsErrorException;
import dev.pedrofaleiros.whoiswho_api.service.GameService;
import dev.pedrofaleiros.whoiswho_api.service.RoomService;
import dev.pedrofaleiros.whoiswho_api.service.RoomUserService;
import dev.pedrofaleiros.whoiswho_api.service.WSRoomService;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class WSRoomServiceImpl implements WSRoomService {

    private RoomService roomService;
    private GameService gameService;
    private RoomUserService roomUserService;

    @Override
    public List<UserResponseDTO> addUserToRoom(String roomId, String username, String sessionId) {
        try {
            var users = getRoomUsers(roomId);
            if(users.size() >= 10){
                throw new RuntimeException("A sala esta lotada.");
            }

            roomUserService.create(username, roomId, sessionId);
            return getRoomUsers(roomId);
        } catch (CustomEntityNotFoundException e) {
            throw new WsErrorException(e.getMessage());
        } catch (CustomBadRequestException e) {
            throw new WsErrorException(e.getMessage());
        }
    }

    @Override
    public List<UserResponseDTO> removeUserFromRoom(String sessionId, String roomId) {
        roomUserService.remove(sessionId);
        return getRoomUsers(roomId);
    }

    @Override
    public List<UserResponseDTO> getRoomUsers(String roomId) {
        var roomUsers = roomUserService.listRoomUsers(roomId);
        List<UserResponseDTO> users = roomUsers.stream()
            .map(user -> new UserResponseDTO(user.getId(), user.getUsername()))
            .collect(Collectors.toList());
        return users;
    }

    @Override
    public Room updateRoomData(UpdateRoomDTO data) {
        return roomService.updateRoom(data);
    }

    @Override
    public Game startGame(String roomId, String username) {
        var game = gameService.createGame(roomId, username);
        roomService.startGame(roomId);
        return game;
    }

    @Override
    public Room finishGame(String roomId, String username) {
        var room = roomService.finishGame(roomId, username);
        return room;
    }

    @Override
    public Game getLatestGame(String roomId) {
        var games = gameService.listGames(roomId);
        if (games.isEmpty())
            return null;
        return games.get(games.size() - 1);
    }

    @Override
    public List<Game> listRoomGames(String roomId) {
        return gameService.listGames(roomId);
    }

    @Override
    public Room getRoomData(String roomId) {
        return roomService.findById(roomId);
    }

    @Override
    public String extractUsername(Principal principal) {
        if(principal == null){
            throw new WsErrorException("Não autenticado");
        }
        return principal.getName();
    }

}
