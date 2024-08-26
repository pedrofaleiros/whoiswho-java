package dev.pedrofaleiros.whoiswho_api.controller;

import java.security.Principal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import dev.pedrofaleiros.whoiswho_api.dto.request.CreateGameEnvDTO;
import dev.pedrofaleiros.whoiswho_api.dto.request.UpdateGameEnvDTO;
import dev.pedrofaleiros.whoiswho_api.entity.GameEnvironment;
import dev.pedrofaleiros.whoiswho_api.service.GameEnvService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("gameEnv")
public class GameEnvController {

    @Autowired
    private GameEnvService service;

    @PostMapping
    public ResponseEntity<GameEnvironment> create(
        Principal principal,
        @Valid @RequestBody CreateGameEnvDTO data,
        @RequestParam(required = false) String categoryId
    ) {
        data.setUsername(principal.getName());
        data.setGameCategoryId(categoryId);
        var response = service.create(data);
        return ResponseEntity.created(null).body(response);
    }

    @GetMapping("all")
    public ResponseEntity<List<GameEnvironment>> findAll() {
        return ResponseEntity.ok().body(service.findAll());
    }

    @GetMapping
    public ResponseEntity<List<GameEnvironment>> findUserGameEnvs(Principal principal) {
        return ResponseEntity.ok().body(service.findByUser(principal.getName()));
    }

    @PutMapping("{gameEnvId}")
    public ResponseEntity<GameEnvironment> update(
        Principal principal,
        @Valid @RequestBody UpdateGameEnvDTO data,
        @PathVariable String gameEnvId 
    ) {
        data.setGameEnvId(gameEnvId);
        data.setUsername(principal.getName());
        var response = service.update(data);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(Principal principal, @PathVariable String id) {
        service.delete(id, principal.getName());
        return ResponseEntity.noContent().build();
    }

}
