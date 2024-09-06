package dev.pedrofaleiros.whoiswho_api.service.impl;

import java.util.List;
import org.springframework.stereotype.Service;
import dev.pedrofaleiros.whoiswho_api.dto.request.CreateGameEnvDTO;
import dev.pedrofaleiros.whoiswho_api.dto.request.UpdateGameEnvDTO;
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
    private GameCategoryService categoryService;
    private UserService userService;

    @Override
    public GameEnvironment create(CreateGameEnvDTO data) {
        var gameEnv = GameEnvironment.builder();
        gameEnv.name(data.getName());

        var user = userService.findByUsername(data.getUsername());
        gameEnv.user(user);

        if (data.getGameCategoryId() != null) {
            var category = categoryService.findById(data.getGameCategoryId());
            gameEnv.gameCategory(category);
        }
        return repository.save(gameEnv.build());
    }

    @Override
    public GameEnvironment update(UpdateGameEnvDTO data) {
        var gameEnv = findFromUserById(data.getGameEnvId(), data.getUsername());
        gameEnv.setName(data.getName());
        return repository.save(gameEnv);
    }

    @Override
    public void delete(String id, String username) {
        var gameEnv = findFromUserById(id, username);
        repository.delete(gameEnv);
    }

    @Override
    public List<GameEnvironment> listAll() {
        return repository.findAllByUserIsNullOrderByNameAsc();
    }

    @Override
    public List<GameEnvironment> listByUser(String username) {
        return repository.findByUserUsernameOrderByNameAsc(username);
    }

    @Override
    public GameEnvironment findById(String id) {
        return repository.findById(id).orElseThrow(() -> new GameEnvNotFoundException());
    }

    @Override
    public GameEnvironment findAuthorizedById(String gameEnvId, String username) {
        var gameEnv = findById(gameEnvId);
        if (gameEnv.getUser() == null) {
            return gameEnv;
        }
        if (!gameEnv.getUser().getUsername().equals(username)) {
            throw new NotAuthException();
        }
        return gameEnv;
    }

    @Override
    public GameEnvironment findFromUserById(String gameEnvId, String username) {
        var gameEnv = findById(gameEnvId);
        if (gameEnv.getUser() == null) {
            throw new NotAuthException();
        }
        if (!gameEnv.getUser().getUsername().equals(username)) {
            throw new NotAuthException();
        }
        return gameEnv;
    }
}
