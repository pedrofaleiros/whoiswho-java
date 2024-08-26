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
import dev.pedrofaleiros.whoiswho_api.dto.request.GameEnvRequestDTO;
import dev.pedrofaleiros.whoiswho_api.entity.GameEnvironment;
import dev.pedrofaleiros.whoiswho_api.service.GameEnvService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("gameEnv")
public class GameEnvController {

    @Autowired
    private GameEnvService service;

    @PostMapping
    public ResponseEntity<GameEnvironment> create(Principal principal,
            @Valid @RequestBody GameEnvRequestDTO data,
            @RequestParam(required = false) String categoryId) {
        var response = service.create(data, principal.getName(), categoryId);
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
    public ResponseEntity<GameEnvironment> update(@Valid @RequestBody GameEnvRequestDTO data,
            @PathVariable String gameEnvId, Principal principal) {
        var response = service.update(data, gameEnvId, principal.getName());
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(Principal principal, @PathVariable String id) {
        service.delete(id, principal.getName());
        return ResponseEntity.noContent().build();
    }

}
