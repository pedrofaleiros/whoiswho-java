package dev.pedrofaleiros.whoiswho_api.service;

import java.util.List;
import dev.pedrofaleiros.whoiswho_api.dto.request.CreateGameEnvDTO;
import dev.pedrofaleiros.whoiswho_api.dto.request.UpdateGameEnvDTO;
import dev.pedrofaleiros.whoiswho_api.entity.GameEnvironment;

public interface GameEnvService {
    GameEnvironment create(CreateGameEnvDTO data);
    GameEnvironment update(UpdateGameEnvDTO data);
    void delete(String id, String username);
    
    List<GameEnvironment> listAll();
    List<GameEnvironment> listByUser(String username);

    GameEnvironment findById(String id);
    GameEnvironment findAuthorizedById(String gameEnvId, String username);
    GameEnvironment findFromUserById(String gameEnvId, String username);
}
