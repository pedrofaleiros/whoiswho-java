package dev.pedrofaleiros.whoiswho_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import dev.pedrofaleiros.whoiswho_api.entity.GameCategory;

public interface GameCategoryRepository extends JpaRepository<GameCategory, String> {

}
