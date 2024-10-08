package dev.pedrofaleiros.whoiswho_api.controller;

import java.security.Principal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import dev.pedrofaleiros.whoiswho_api.dto.request.PlayerRoleRequestDTO;
import dev.pedrofaleiros.whoiswho_api.entity.PlayerRole;
import dev.pedrofaleiros.whoiswho_api.service.PlayerRoleService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("playerRole")
public class PlayerRoleController {
    
    @Autowired
    private PlayerRoleService service;

    @PostMapping("gameEnv/{gameEnvId}")
    public ResponseEntity<PlayerRole> create(
        Principal principal,
        @Valid @RequestBody PlayerRoleRequestDTO data,
        @PathVariable String gameEnvId
    ){
        data.setUsername(principal.getName());
        data.setGameEnvId(gameEnvId);
        var response = service.create(data);
        return ResponseEntity.created(null).body(response);
    }

    @GetMapping("gameEnv/{gameEnvId}")
    public ResponseEntity<List<PlayerRole>> findByGameEnv(
        Principal principal,
        @PathVariable String gameEnvId
    ){
        var response = service.listByGameEnv(gameEnvId, principal.getName());
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(Principal principal, @PathVariable String id){
        service.delete(id, principal.getName());
        return ResponseEntity.noContent().build();
    }
}
