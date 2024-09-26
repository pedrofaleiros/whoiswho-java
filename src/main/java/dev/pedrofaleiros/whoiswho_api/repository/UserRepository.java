package dev.pedrofaleiros.whoiswho_api.repository;

import dev.pedrofaleiros.whoiswho_api.entity.Room;
import dev.pedrofaleiros.whoiswho_api.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;


public interface UserRepository extends JpaRepository<UserEntity, String> {

    Optional<UserEntity> findByUsername(String username);

    List<UserEntity> findByRoomsPlaying(Room room);
}
