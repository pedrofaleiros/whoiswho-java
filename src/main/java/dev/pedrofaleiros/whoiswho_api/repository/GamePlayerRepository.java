package dev.pedrofaleiros.whoiswho_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import dev.pedrofaleiros.whoiswho_api.entity.GamePlayer;

public interface GamePlayerRepository extends JpaRepository<GamePlayer, String> {

}
