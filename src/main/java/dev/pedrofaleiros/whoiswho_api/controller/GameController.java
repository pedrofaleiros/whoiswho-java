package dev.pedrofaleiros.whoiswho_api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import dev.pedrofaleiros.whoiswho_api.dto.request.CreateLocalGameDTO;
import dev.pedrofaleiros.whoiswho_api.dto.response.LocalGameResponseDTO;
import dev.pedrofaleiros.whoiswho_api.service.LocalGameService;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("game")
public class GameController {

    @Autowired
    private LocalGameService service;

    @PostMapping
    public ResponseEntity<LocalGameResponseDTO> getMethodName(@RequestBody CreateLocalGameDTO data,
            Principal principal) {
        data.setUsername(principal.getName());

        var response = service.createGame(data);

        return ResponseEntity.ok().body(response);
    }

}
