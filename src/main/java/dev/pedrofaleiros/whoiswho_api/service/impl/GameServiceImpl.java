package dev.pedrofaleiros.whoiswho_api.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;
import org.springframework.stereotype.Service;
import dev.pedrofaleiros.whoiswho_api.entity.Game;
import dev.pedrofaleiros.whoiswho_api.entity.GameEnvironment;
import dev.pedrofaleiros.whoiswho_api.entity.GamePlayer;
import dev.pedrofaleiros.whoiswho_api.entity.PlayerRole;
import dev.pedrofaleiros.whoiswho_api.entity.Room;
import dev.pedrofaleiros.whoiswho_api.entity.UserEntity;
import dev.pedrofaleiros.whoiswho_api.exception.bad_request.CustomBadRequestException;
import dev.pedrofaleiros.whoiswho_api.repository.GamePlayerRepository;
import dev.pedrofaleiros.whoiswho_api.repository.GameRepository;
import dev.pedrofaleiros.whoiswho_api.service.GameEnvService;
import dev.pedrofaleiros.whoiswho_api.service.GameService;
import dev.pedrofaleiros.whoiswho_api.service.RoomService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class GameServiceImpl implements GameService {

    private GameRepository repository;
    private RoomService roomService;
    private GameEnvService gameEnvService;
    private GamePlayerRepository gamePlayerRepository;

    @Transactional
    @Override
    public Game createGame(String roomId, String username) {
        var room = roomService.findById(roomId);

        if(!room.getOwner().getUsername().equals(username)){
            throw new RuntimeException("Apenas o ADM pode iniciar a partida");
        }

        validateImpostors(room, room.getUsers().size());

        var game = Game.builder();

        game.room(room);

        var gameEnv = gatGameRandGameEnv(room, room.getUsers().size());
        game.gameEnvironment(gameEnv);

        var createdGame = repository.save(game.build());

        var gamePlayers = getPlayerRoles(gameEnv.getPlayerRoles(), room.getUsers(), room.getImpostors(), createdGame);
        game.gamePlayers(gamePlayers);

        createdGame.setGamePlayers(gamePlayers);

        return repository.save(createdGame);
    }

    @Override
    public GamePlayer createGamePlayer(Game game, UserEntity user, PlayerRole playerRole) {
        var gamePlayer = GamePlayer.builder();

        gamePlayer.game(game);
        gamePlayer.user(user);

        if (playerRole != null) {
            gamePlayer.playerRole(playerRole);
            gamePlayer.isImpostor(false);
        } else {
            gamePlayer.isImpostor(true);
        }

        return gamePlayerRepository.save(gamePlayer.build());
    }

    private GameEnvironment gatGameRandGameEnv(Room room, int usersSize){
        List<GameEnvironment> gameEnvs = getGameEnvs(room, usersSize);

        var randomIndex = new Random().nextInt(gameEnvs.size());
        var selectedGameEnv = gameEnvs.get(randomIndex);

        return selectedGameEnv;
    }

    private List<GameEnvironment> getGameEnvs(Room room, int usersSize) {
        List<GameEnvironment> gameEnvs;
        
        if (room.isIncludeDefaultGameEnvs()) {
            if (room.isIncludeUserGameEnvs()) {
                gameEnvs = getAllGameEnvs(room.getOwner().getUsername(), usersSize);
            } else {
                gameEnvs = getDefaultGameEnvs(usersSize);
            }
        } else {
            gameEnvs = getUserGameEnvs(room.getOwner().getUsername(), usersSize);
        }

        if (gameEnvs.isEmpty()) {
            throw new CustomBadRequestException(
                    "Nenhum local encontrado para essa quantidade de jogadores");
        }

        return gameEnvs;
    }

    private List<GameEnvironment> getAllGameEnvs(String username, int usersSize) {
        var l1 = getDefaultGameEnvs(usersSize);
        var l2 = getUserGameEnvs(username, usersSize);
        
        var list = new ArrayList<GameEnvironment>();

        for (var i = 0; i < l1.size(); i++) {
            list.add(l1.get(i));
        }
        for (var i = 0; i < l2.size(); i++) {
            list.add(l2.get(i));
        }

        return list;
    }

    private List<GameEnvironment> getDefaultGameEnvs(int usersSize) {
        var gameEnvs = gameEnvService.listAll();
        var list = new ArrayList<GameEnvironment>();

        for (var i = 0; i < gameEnvs.size(); i++) {
            var el = gameEnvs.get(i);
            if (el.getPlayerRoles().size() >= usersSize)
                list.add(el);
        }
        return list;
    }

    private List<GameEnvironment> getUserGameEnvs(String username, int usersSize) {
        var gameEnvs = gameEnvService.listByUser(username);
        var list = new ArrayList<GameEnvironment>();

        for (var i = 0; i < gameEnvs.size(); i++) {
            var el = gameEnvs.get(i);
            if (el.getPlayerRoles().size() >= usersSize)
                list.add(el);
        }
        return list;
    }
 
    private List<GamePlayer> getPlayerRoles(List<PlayerRole> playerRoles, Set<UserEntity> users, int impostors, Game game) {
        
        if (playerRoles.size() < users.size() - impostors) {
            throw new CustomBadRequestException("Nenhum local encontrado para essa quantidade de jogadores.");
        }

        Collections.shuffle(playerRoles);

        //Preencha lista
        var players = new ArrayList<GamePlayer>();
        for (var user : users) {
            players.add(GamePlayer.builder()
                .game(game)
                .user(user)
                .isImpostor(false)
                .playerRole(null)
                .build());
            // list.add(new GamePlayerRole(players.get(i), ""));
        }

        
        //Marca impostores
        int total = 0;
        while (total < impostors) {
            var rand = new Random().nextInt(users.size());
            
            // if (list.get(rand).getProfession() != null) {
            //     list.get(rand).setProfession(null);
            //     total++;
            // }

            if(!players.get(rand).isImpostor()){
                players.get(rand).setImpostor(true);
                total++;
            }
        }
        
        //Marca papeis
        int j = 0;
        for (var i = 0; i < players.size(); i++) {
            // if (list.get(i).getProfession() != null) {
            //     list.get(i).setProfession(playerRoles.get(j++).getName());
            // }

            if(!players.get(i).isImpostor()){
                players.get(i).setPlayerRole(playerRoles.get(j++));
            }
        }

        // for(var player:players){
        //     createGamePlayer(game, player.getUser(), player.getPlayerRole());
        // }

        return players;
    }

    private void validateImpostors(Room room, int usersSize) {
        if (usersSize < 3 || room.getImpostors() > usersSize / 2) {
            throw new CustomBadRequestException("Impostores devem ser minoria");
        }

        if (room.getImpostors() < 1 || room.getImpostors() > 3) {
            throw new CustomBadRequestException("Quantidade inválida de impostores");
        }
    }

    @Override
    public List<Game> listGames(String roomId) {
        var room = roomService.findById(roomId);
        var games = repository.findByRoomIdOrderByCreatedAt(room.getId());
        return games;
    }
}