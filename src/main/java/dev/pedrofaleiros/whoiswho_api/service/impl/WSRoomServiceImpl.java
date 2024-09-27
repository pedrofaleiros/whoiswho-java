package dev.pedrofaleiros.whoiswho_api.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import dev.pedrofaleiros.whoiswho_api.dto.request.UpdateRoomDTO;
import dev.pedrofaleiros.whoiswho_api.dto.response.UserResponseDTO;
import dev.pedrofaleiros.whoiswho_api.entity.Room;
import dev.pedrofaleiros.whoiswho_api.service.RoomService;
import dev.pedrofaleiros.whoiswho_api.service.UserService;
import dev.pedrofaleiros.whoiswho_api.service.WSRoomService;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class WSRoomServiceImpl implements WSRoomService {

    private RoomService roomService;
    private UserService userService;

    @Override
    public Room addUserToRoom(String roomId, String username) {
        return roomService.addUser(roomId, username);
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

}
