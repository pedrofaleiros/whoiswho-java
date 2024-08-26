package dev.pedrofaleiros.whoiswho_api.service;

import dev.pedrofaleiros.whoiswho_api.entity.UserEntity;

public interface UserService {
    UserEntity findByUsername(String username);
}
