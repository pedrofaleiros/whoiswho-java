package dev.pedrofaleiros.whoiswho_api.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import dev.pedrofaleiros.whoiswho_api.dto.request.CreateLocalGameDTO;
import dev.pedrofaleiros.whoiswho_api.dto.response.LocalGameResponseDTO;
import dev.pedrofaleiros.whoiswho_api.dto.response.LocalGameResponseDTO.GamePlayerRole;
import dev.pedrofaleiros.whoiswho_api.entity.GameEnvironment;
import dev.pedrofaleiros.whoiswho_api.entity.PlayerRole;
import dev.pedrofaleiros.whoiswho_api.exception.bad_request.CustomBadRequestException;
import dev.pedrofaleiros.whoiswho_api.service.GameEnvService;
import dev.pedrofaleiros.whoiswho_api.service.LocalGameService;

@Service
public class LocalGameServiceImpl implements LocalGameService {

    @Autowired
    private GameEnvService gameEnvService;

    @Override
    public LocalGameResponseDTO createGame(CreateLocalGameDTO data) {

        if (!data.isIncludeDefaultGameEnvs() && !data.isIncludeUserGameEnvs()) {
            throw new CustomBadRequestException("Nenhum local encontrado");
        }

        var len = data.getPlayers().size();
        var min = len - data.getImpostors();

        validateImpostors(data, len);

        List<GameEnvironment> gameEnvs = getGameEnvs(data, min);

        var randomIndex = new Random().nextInt(gameEnvs.size());
        var selectedGameEnv = gameEnvs.get(randomIndex);

        var gameResponse = new LocalGameResponseDTO();
        gameResponse.setGameEnv(selectedGameEnv.getName());
        gameResponse.setPlayerRoles(
            getPlayerRoles(selectedGameEnv.getPlayerRoles(), data.getPlayers(), data.getImpostors())
        );

        return gameResponse;
    }

    private List<GameEnvironment> getGameEnvs(CreateLocalGameDTO data, int min) {
        List<GameEnvironment> gameEnvs;
        if (data.isIncludeDefaultGameEnvs()) {
            if (data.isIncludeUserGameEnvs()) {
                gameEnvs = getAllGameEnvs(data.getUsername(), min);
            } else {
                gameEnvs = getDefaultGameEnvs(min);
            }
        } else {
            gameEnvs = getUserGameEnvs(data.getUsername(), min);
        }
        if (gameEnvs.isEmpty()) {
            throw new CustomBadRequestException(
                    "Nenhum local encontrado para essa quantidade de jogadores");
        }
        return gameEnvs;
    }

    private void validateImpostors(CreateLocalGameDTO data, int len) {
        if (len < 3 || data.getImpostors() > len / 2) {
            throw new CustomBadRequestException("Impostores devem ser minoria");
        }

        if (data.getImpostors() < 1 || data.getImpostors() > 3) {
            throw new CustomBadRequestException("Quantidade inválida de impostores");
        }
    }

    private List<GamePlayerRole> getPlayerRoles(List<PlayerRole> playerRoles, List<String> players, int impostors) {
        if (playerRoles.size() < players.size() - impostors) {
            throw new CustomBadRequestException(
                    "Nenhum local encontrado para essa quantidade de jogadores.");
        }

        Collections.shuffle(playerRoles);

        //Preencha lista
        var list = new ArrayList<GamePlayerRole>();
        for (var i = 0; i < players.size(); i++) {
            list.add(new GamePlayerRole(players.get(i), ""));
        }
        
        //Marca impostores
        int total = 0;
        while (total < impostors) {
            var rand = new Random().nextInt(players.size());
            
            if (list.get(rand).getProfession() != null) {
                list.get(rand).setProfession(null);
                total++;
            }
        }
        
        //Marca papeis
        int j = 0;
        for (var i = 0; i < list.size(); i++) {
            if (list.get(i).getProfession() != null) {
                list.get(i).setProfession(playerRoles.get(j++).getName());
            }
        }

        return list;
    }

    private List<GameEnvironment> getAllGameEnvs(String username, int min) {
        var l1 = getDefaultGameEnvs(min);
        var l2 = getUserGameEnvs(username, min);
        var list = new ArrayList<GameEnvironment>();

        for (var i = 0; i < l1.size(); i++) {
            list.add(l1.get(i));
        }
        for (var i = 0; i < l2.size(); i++) {
            list.add(l2.get(i));
        }
        return list;
    }

    private List<GameEnvironment> getDefaultGameEnvs(int min) {
        var aux = gameEnvService.listAll();
        var list = new ArrayList<GameEnvironment>();

        for (var i = 0; i < aux.size(); i++) {
            var el = aux.get(i);
            if (el.getPlayerRoles().size() >= min)
                list.add(el);
        }
        return list;
    }

    private List<GameEnvironment> getUserGameEnvs(String username, int min) {
        var aux = gameEnvService.listByUser(username);
        var list = new ArrayList<GameEnvironment>();

        for (var i = 0; i < aux.size(); i++) {
            var el = aux.get(i);
            if (el.getPlayerRoles().size() >= min)
                list.add(el);
        }
        return list;
    }

}
