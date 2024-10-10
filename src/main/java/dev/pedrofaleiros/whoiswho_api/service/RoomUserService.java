package dev.pedrofaleiros.whoiswho_api.service;

import java.util.List;
import java.util.Set;
import dev.pedrofaleiros.whoiswho_api.entity.RoomUser;
import dev.pedrofaleiros.whoiswho_api.entity.UserEntity;

public interface RoomUserService {

    RoomUser findBySessionId(String sessionId);

    RoomUser create(String username, String roomId, String sessionId);

    void remove(String sessionId);

    Set<RoomUser> findByRoom(String roomId);

    List<UserEntity> listRoomUsers(String roomId);
}
