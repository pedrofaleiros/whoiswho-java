package dev.pedrofaleiros.whoiswho_api.service;

import java.util.List;
import dev.pedrofaleiros.whoiswho_api.entity.Game;
import dev.pedrofaleiros.whoiswho_api.entity.GamePlayer;
import dev.pedrofaleiros.whoiswho_api.entity.PlayerRole;
import dev.pedrofaleiros.whoiswho_api.entity.UserEntity;

public interface GameService {
    
    Game createGame(String roomId, String username);

    GamePlayer createGamePlayer(Game game, UserEntity user, PlayerRole playerRole);

    List<Game> listGames(String roomId);
}
