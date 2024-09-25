package dev.pedrofaleiros.whoiswho_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import dev.pedrofaleiros.whoiswho_api.entity.Game;

public interface GameRepository extends JpaRepository<Game, String> {

}
