package dev.pedrofaleiros.whoiswho_api.service.impl;

import java.util.List;
import org.springframework.stereotype.Service;
import dev.pedrofaleiros.whoiswho_api.dto.request.GameEnvRequestDTO;
import dev.pedrofaleiros.whoiswho_api.entity.GameEnvironment;
import dev.pedrofaleiros.whoiswho_api.exception.NotAuthException;
import dev.pedrofaleiros.whoiswho_api.exception.not_found.GameEnvNotFoundException;
import dev.pedrofaleiros.whoiswho_api.repository.GameEnvRepository;
import dev.pedrofaleiros.whoiswho_api.service.GameCategoryService;
import dev.pedrofaleiros.whoiswho_api.service.GameEnvService;
import dev.pedrofaleiros.whoiswho_api.service.UserService;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class GameEnvServiceImpl implements GameEnvService {

    private GameEnvRepository repository;
    private UserService userService;
    private GameCategoryService categoryService;

    public GameEnvironment create(GameEnvRequestDTO data, String username, String categoryId) {
        var gameEnv = GameEnvironment.builder();
        gameEnv.name(data.getName());

        var user = userService.findByUsername(username);
        gameEnv.user(user);

        if (categoryId != null) {
            var category = categoryService.findById(categoryId);
            gameEnv.gameCategory(category);
        }

        return repository.save(gameEnv.build());
    }

    public GameEnvironment update(GameEnvRequestDTO data, String gameEnvId, String username) {
        var gameEnv = getGameEnvFromUser(gameEnvId, username);
        gameEnv.setName(data.getName());
        return repository.save(gameEnv);
    }

    public void delete(String id, String username) {
        var gameEnv = getGameEnvFromUser(id, username);
        repository.delete(gameEnv);
    }

    public List<GameEnvironment> findAll() {
        return repository.findByUserIsNull();
    }

    public List<GameEnvironment> findByUser(String username) {
        return repository.findByUserUsername(username);
    }

    public GameEnvironment findById(String id) {
        return repository.findById(id).orElseThrow(() -> new GameEnvNotFoundException());
    }

    public GameEnvironment getGameEnvFromUser(String gameEnvId, String username) {
        var gameEnv = findById(gameEnvId);
        if (gameEnv.getUser() == null) {
            throw new NotAuthException();
        }
        var user = userService.findByUsername(username);
        if (!user.getId().equals(gameEnv.getUser().getId())) {
            throw new NotAuthException();
        }
        return gameEnv;
    }
}
