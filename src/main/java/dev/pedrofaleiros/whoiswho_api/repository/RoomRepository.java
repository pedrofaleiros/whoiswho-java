package dev.pedrofaleiros.whoiswho_api.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import dev.pedrofaleiros.whoiswho_api.entity.Room;

public interface RoomRepository extends JpaRepository<Room, String> {
    boolean existsById(String id);

    List<Room> findByOrderByCreatedAtAsc();
}
