package dev.pedrofaleiros.whoiswho_api.repository;

import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import dev.pedrofaleiros.whoiswho_api.entity.RoomUser;

public interface RoomUserRepository extends JpaRepository<RoomUser, String> {
    
    Set<RoomUser> findByRoomId(String id);
}
