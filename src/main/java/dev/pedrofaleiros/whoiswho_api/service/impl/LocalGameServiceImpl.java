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
            throw new CustomBadRequestException("Jogo invalido 1");
        }

        var len = data.getPlayers().size();
        if (len < 3 || data.getImpostors() > len / 2) {
            throw new CustomBadRequestException("Jogo invalido 2");
        }

        if (data.getImpostors() < 1 || data.getImpostors() > 3) {
            throw new CustomBadRequestException("Jogo invalido 3");
        }

        var min = len - data.getImpostors();

        List<GameEnvironment> gameEnvs;
        if (data.isIncludeDefaultGameEnvs()) {
            if (data.isIncludeUserGameEnvs()) {
                gameEnvs = mergeLists(data.getUsername(), min);
            } else {
                gameEnvs = getDefaultGameEnvs(min);
            }
        } else {
            gameEnvs = getUserGameEnvs(data.getUsername(), min);
        }
        if (gameEnvs.isEmpty()) {
            throw new CustomBadRequestException("Jogo invalido 4");
        }

        var rand = new Random();
        var index = rand.nextInt(gameEnvs.size());
        var selectedGameEnv = gameEnvs.get(index);

        var gameResponse = new LocalGameResponseDTO();

        gameResponse.setGameEnv(selectedGameEnv.getName());
        gameResponse.setPlayerRoles(getPlayerRoles(selectedGameEnv.getPlayerRoles(),
                data.getPlayers(), min, data.getImpostors()));

        return gameResponse;
    }

    private List<GamePlayerRole> getPlayerRoles(List<PlayerRole> playerRoles, List<String> players,
            int min, int impostors) {
        if (playerRoles.size() < players.size() - impostors) {
            throw new CustomBadRequestException("Jogo invalido 5");
        }

        Collections.shuffle(playerRoles);

        var list = new ArrayList<GamePlayerRole>();
        for (var i = 0; i < players.size(); i++) {
            list.add(new GamePlayerRole(players.get(i), ""));
        }

        int total = 0;

        while (total < impostors) {
            var rand = new Random().nextInt(players.size());

            if (list.get(rand).getProfession() != null) {
                list.get(rand).setProfession(null);
                total++;
            }
        }

        int j = 0;
        for (var i = 0; i < list.size(); i++) {
            if (list.get(i).getProfession() != null) {
                list.get(i).setProfession(playerRoles.get(j++).getName());
            }
        }

        return list;
    }

    private List<GameEnvironment> mergeLists(String username, int min) {
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
