package dev.pedrofaleiros.whoiswho_api.service.impl;

import org.springframework.stereotype.Service;
import dev.pedrofaleiros.whoiswho_api.entity.UserEntity;
import dev.pedrofaleiros.whoiswho_api.exception.not_found.UserNotFoundException;
import dev.pedrofaleiros.whoiswho_api.repository.UserRepository;
import dev.pedrofaleiros.whoiswho_api.service.UserService;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private UserRepository repository;

    @Override
    public UserEntity findByUsername(String username) {
        return repository.findByUsername(username).orElseThrow(() -> new UserNotFoundException());
    }

}
