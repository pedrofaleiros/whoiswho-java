package dev.pedrofaleiros.whoiswho_api.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import dev.pedrofaleiros.whoiswho_api.entity.RoomUser;
import dev.pedrofaleiros.whoiswho_api.entity.UserEntity;
import dev.pedrofaleiros.whoiswho_api.exception.not_found.CustomEntityNotFoundException;
import dev.pedrofaleiros.whoiswho_api.repository.RoomUserRepository;
import dev.pedrofaleiros.whoiswho_api.service.RoomService;
import dev.pedrofaleiros.whoiswho_api.service.RoomUserService;
import dev.pedrofaleiros.whoiswho_api.service.UserService;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RoomUserServiceImpl implements RoomUserService {

    private RoomUserRepository repository;
    private UserService userService;
    private RoomService roomService;

    //TODO: custom exception
    @Override
    public RoomUser create(String username, String roomId, String sessionId) {
        var findRoomUser = repository.findById(sessionId);
        
        if (findRoomUser.isPresent()) {
            throw new RuntimeException("Ja existe uma sessao com esse ID");
        }

        var user = userService.findByUsername(username);
        var room = roomService.findById(roomId);

        var roomUser = RoomUser.builder();

        roomUser.sessionId(sessionId);
        roomUser.user(user);
        roomUser.room(room);

        return repository.save(roomUser.build());
    }

    @Override
    public void remove(String sessionId) {
        var findRoomUser = findBySessionId(sessionId);
        repository.delete(findRoomUser);
    }

    //TODO: custom exception
    @Override
    public RoomUser findBySessionId(String sessionId) {
        return repository.findById(sessionId).orElseThrow(() -> new CustomEntityNotFoundException("Sessao nao encontrada"));
    }

    @Override
    public Set<RoomUser> findByRoom(String roomId) {
        var room = roomService.findById(roomId);
        return repository.findByRoomId(room.getId());
    }

    @Override
    public List<UserEntity> listRoomUsers(String roomId) {
        Set<RoomUser> roomUserSet = findByRoom(roomId);

        Set<UserEntity> uniqueUsers = roomUserSet.stream()
            .map(RoomUser::getUser)
            .collect(Collectors.toSet());

        List<UserEntity> users = new ArrayList<>(uniqueUsers);

        return users;
    }

}
