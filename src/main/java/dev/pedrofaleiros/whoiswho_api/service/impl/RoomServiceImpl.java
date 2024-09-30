package dev.pedrofaleiros.whoiswho_api.service.impl;

import java.util.Random;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import dev.pedrofaleiros.whoiswho_api.dto.request.UpdateRoomDTO;
import dev.pedrofaleiros.whoiswho_api.entity.Room;
import dev.pedrofaleiros.whoiswho_api.entity.RoomStatus;
import dev.pedrofaleiros.whoiswho_api.exception.not_found.RoomNotFoundException;
import dev.pedrofaleiros.whoiswho_api.repository.RoomRepository;
import dev.pedrofaleiros.whoiswho_api.service.RoomService;
import dev.pedrofaleiros.whoiswho_api.service.UserService;
import jakarta.transaction.Transactional;

@Service
public class RoomServiceImpl implements RoomService {

    private RoomRepository repository;
    private UserService userService;

    // Circular Dependencies
    public RoomServiceImpl(RoomRepository repository, @Lazy UserService userService) {
        this.repository = repository;
        this.userService = userService;
    }

    @Override
    public String generateRoomId() {
        String id;
        do {
            id = String.format("%04d", new Random().nextInt(10000));
        } while (repository.existsById(id));
        return id;
    }

    @Override
    public String createRoom(String username) {
        var room = Room.builder();

        var user = userService.findByUsername(username);
        room.owner(user);

        var id = generateRoomId();
        room.id(id);

        room.status(RoomStatus.IDLE);
        room.impostors(1);
        room.includeDefaultGameEnvs(true);
        room.includeUserGameEnvs(false);

        var created = repository.save(room.build());

        return created.getId();
    }

    @Override
    public Room findById(String id) {
        return repository.findById(id).orElseThrow(() -> new RoomNotFoundException(id));
    }

    @Transactional
    @Override
    public Room addUser(String roomId, String username) {
        var room = findById(roomId);
        var user = userService.findByUsername(username);

        if (room.getUsers().contains(user)) {
            throw new RuntimeException("Usuario ja esta na sala");
        }

        room.getUsers().add(user);
        return repository.save(room);
    }

    @Transactional
    @Override
    public Room removeUser(String roomId, String username) {
        var room = findById(roomId);
        var user = userService.findByUsername(username);

        if (room.getUsers().contains(user)) {
            room.getUsers().remove(user);
            return repository.save(room);
        }

        return room;
    }


    @Override
    public Room updateRoom(UpdateRoomDTO data) {
        var room = findById(data.getRoom());
        var user = userService.findByUsername(data.getUsername());

        if (!room.getOwner().getId().equals(user.getId())) {
            // TODO: custom exception
            throw new RuntimeException("Apenas o ADM pode atualizar os dados da sala");
        }

        room.setImpostors(data.getImpostors());
        room.setIncludeDefaultGameEnvs(data.isIncludeDefaultGameEnvs());
        room.setIncludeUserGameEnvs(data.isIncludeUserGameEnvs());

        return repository.save(room);
    }

    @Override
    public Room startGame(String roomId) {
        //TODO: verificar se ja esta playing

        var room = findById(roomId);
        room.setStatus(RoomStatus.PLAYING);
        return repository.save(room);
    }
    
    @Override
    public Room finishGame(String roomId, String username) {
        //TODO: verificar se ja esta idle

        var room = findById(roomId);

        if (!room.getOwner().getUsername().equals(username)) {
            throw new RuntimeException("Apenas o ADM pode finalizar a partida");
        }

        room.setStatus(RoomStatus.IDLE);
        return repository.save(room);
    }

}
