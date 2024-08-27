package dev.pedrofaleiros.whoiswho_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import dev.pedrofaleiros.whoiswho_api.entity.PlayerRole;

public interface PlayerRoleRepository extends JpaRepository<PlayerRole, String> {

    List<PlayerRole> findByGameEnvironmentId(String id);
}
