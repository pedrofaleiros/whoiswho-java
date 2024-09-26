package dev.pedrofaleiros.whoiswho_api.service;

import java.util.List;
import dev.pedrofaleiros.whoiswho_api.entity.UserEntity;

public interface UserService {
    UserEntity findByUsername(String username);

    List<UserEntity> listByRoom(String roomId);
}
