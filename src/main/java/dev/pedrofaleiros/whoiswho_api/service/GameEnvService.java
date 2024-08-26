package dev.pedrofaleiros.whoiswho_api.service;

import java.util.List;
import dev.pedrofaleiros.whoiswho_api.dto.request.GameEnvRequestDTO;
import dev.pedrofaleiros.whoiswho_api.entity.GameEnvironment;

public interface GameEnvService {
    GameEnvironment findById(String id);
    GameEnvironment create(GameEnvRequestDTO data, String username, String categoryId);
    List<GameEnvironment> findAll();
    GameEnvironment update(GameEnvRequestDTO data, String gameEnvId, String username);
    void delete(String id, String username);
    List<GameEnvironment> findByUser(String username);
    GameEnvironment getGameEnvFromUser(String gameEnvId, String username);
}
