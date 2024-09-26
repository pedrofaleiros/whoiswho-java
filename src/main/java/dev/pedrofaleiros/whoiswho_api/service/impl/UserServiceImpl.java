package dev.pedrofaleiros.whoiswho_api.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import dev.pedrofaleiros.whoiswho_api.entity.UserEntity;
import dev.pedrofaleiros.whoiswho_api.exception.not_found.RoomNotFoundException;
import dev.pedrofaleiros.whoiswho_api.exception.not_found.UserNotFoundException;
import dev.pedrofaleiros.whoiswho_api.repository.RoomRepository;
import dev.pedrofaleiros.whoiswho_api.repository.UserRepository;
import dev.pedrofaleiros.whoiswho_api.service.RoomService;
import dev.pedrofaleiros.whoiswho_api.service.UserService;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private UserRepository repository;
    private RoomRepository roomRepository;

    @Override
    public UserEntity findByUsername(String username) {
        return repository.findByUsername(username).orElseThrow(() -> new UserNotFoundException());
    }

    @Override
    public List<UserEntity> listByRoom(String roomId) {
        var room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RoomNotFoundException(roomId));
                
        var users = repository.findByRoomsPlaying(room);
        return users;
    }

}
