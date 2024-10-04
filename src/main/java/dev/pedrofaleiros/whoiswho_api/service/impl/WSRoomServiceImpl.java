package dev.pedrofaleiros.whoiswho_api.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import dev.pedrofaleiros.whoiswho_api.dto.request.UpdateRoomDTO;
import dev.pedrofaleiros.whoiswho_api.dto.response.UserResponseDTO;
import dev.pedrofaleiros.whoiswho_api.entity.Game;
import dev.pedrofaleiros.whoiswho_api.entity.Room;
import dev.pedrofaleiros.whoiswho_api.exception.not_found.CustomEntityNotFoundException;
import dev.pedrofaleiros.whoiswho_api.exception.websocket.WsErrorException;
import dev.pedrofaleiros.whoiswho_api.service.GameService;
import dev.pedrofaleiros.whoiswho_api.service.RoomService;
import dev.pedrofaleiros.whoiswho_api.service.UserService;
import dev.pedrofaleiros.whoiswho_api.service.WSRoomService;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class WSRoomServiceImpl implements WSRoomService {

    private RoomService roomService;
    private UserService userService;
    private GameService gameService;

    @Override
    public List<UserResponseDTO> addUserToRoom(String roomId, String username) {
        try {
            var updatedRoom = roomService.addUser(roomId, username);
            return getRoomUsers(updatedRoom.getId());
        } catch (RuntimeException e) {
            throw new WsErrorException(e.getMessage());
        }
    }

    @Override
    public List<UserResponseDTO> removeUserFromRoom(String roomId, String username) {
        var updatedRoom = roomService.removeUser(roomId, username);
        return getRoomUsers(updatedRoom.getId());
    }

    @Override
    public List<UserResponseDTO> getRoomUsers(String roomId) {
        var list = userService.listByRoom(roomId);

        var users = new ArrayList<UserResponseDTO>();
        for (var el : list) {
            users.add(new UserResponseDTO(el.getId(), el.getUsername()));
        }

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

}
