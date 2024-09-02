package dev.pedrofaleiros.whoiswho_api.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import dev.pedrofaleiros.whoiswho_api.entity.GameEnvironment;

public interface GameEnvRepository extends JpaRepository<GameEnvironment, String> {

    List<GameEnvironment> findByUserUsername(String username);

    List<GameEnvironment> findByUserIsNullOrUserUsername(String username);

    List<GameEnvironment> findByUserIsNull();

    List<GameEnvironment> findAllByUserIsNullOrderByNameAsc();
}
