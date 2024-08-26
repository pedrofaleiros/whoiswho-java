package dev.pedrofaleiros.whoiswho_api.service;

import java.util.List;
import dev.pedrofaleiros.whoiswho_api.dto.request.CreateGameEnvDTO;
import dev.pedrofaleiros.whoiswho_api.dto.request.UpdateGameEnvDTO;
import dev.pedrofaleiros.whoiswho_api.entity.GameEnvironment;

public interface GameEnvService {
    GameEnvironment findById(String id);
    GameEnvironment create(CreateGameEnvDTO data);
    List<GameEnvironment> findAll();
    GameEnvironment update(UpdateGameEnvDTO data);
    void delete(String id, String username);
    List<GameEnvironment> findByUser(String username);
    GameEnvironment getGameEnvFromUser(String gameEnvId, String username);
}
