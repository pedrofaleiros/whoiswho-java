package dev.pedrofaleiros.whoiswho_api.service;

import java.util.List;
import dev.pedrofaleiros.whoiswho_api.dto.request.PlayerRoleRequestDTO;
import dev.pedrofaleiros.whoiswho_api.entity.PlayerRole;

public interface PlayerRoleService {
    PlayerRole create(PlayerRoleRequestDTO data);

    PlayerRole findById(String id);

    List<PlayerRole> listByGameEnv(String gameEnvId, String username);

    void delete(String id, String username);
}
