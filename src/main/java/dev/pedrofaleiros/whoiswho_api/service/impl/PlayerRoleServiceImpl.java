package dev.pedrofaleiros.whoiswho_api.service.impl;

import java.util.List;
import org.springframework.stereotype.Service;
import dev.pedrofaleiros.whoiswho_api.dto.request.PlayerRoleRequestDTO;
import dev.pedrofaleiros.whoiswho_api.entity.PlayerRole;
import dev.pedrofaleiros.whoiswho_api.repository.PlayerRoleRepository;
import dev.pedrofaleiros.whoiswho_api.service.GameEnvService;
import dev.pedrofaleiros.whoiswho_api.service.PlayerRoleService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class PlayerRoleServiceImpl implements PlayerRoleService {

    private PlayerRoleRepository repository;
    private GameEnvService gameEnvService;

    @Override
    public PlayerRole create(PlayerRoleRequestDTO data) {
        var playerRole = PlayerRole.builder();
        playerRole.name(data.getName());

        var gameEnv = gameEnvService.getGameEnvFromUser(data.getGameEnvId(), data.getUsername());
        playerRole.gameEnvironment(gameEnv);

        return repository.save(playerRole.build());
    }

    @Override
    public PlayerRole findById(String id) {
        throw new UnsupportedOperationException("Unimplemented method 'findById'");
    }

    @Override
    public List<PlayerRole> findByGameEnv(String gameEnvId, String username) {
        throw new UnsupportedOperationException("Unimplemented method 'findByGameEnv'");
    }

    @Override
    public void delete(String id, String username) {
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }
    
}
