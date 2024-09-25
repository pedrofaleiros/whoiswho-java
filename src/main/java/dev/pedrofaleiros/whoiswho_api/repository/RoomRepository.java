package dev.pedrofaleiros.whoiswho_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import dev.pedrofaleiros.whoiswho_api.entity.Room;

public interface RoomRepository extends JpaRepository<Room, String> {
    boolean existsById(String id);
}
