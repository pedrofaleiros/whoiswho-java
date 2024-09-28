package dev.pedrofaleiros.whoiswho_api.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import dev.pedrofaleiros.whoiswho_api.entity.Game;
import dev.pedrofaleiros.whoiswho_api.entity.Room;

public interface GameRepository extends JpaRepository<Game, String> {

    List<Game> findByRoomIdOrderByCreatedAt(String id);
}
