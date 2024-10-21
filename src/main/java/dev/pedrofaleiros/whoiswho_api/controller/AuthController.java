package dev.pedrofaleiros.whoiswho_api.controller;

import dev.pedrofaleiros.whoiswho_api.dto.request.UpdateUsernameDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import dev.pedrofaleiros.whoiswho_api.dto.request.LoginGuestDTO;
import dev.pedrofaleiros.whoiswho_api.dto.request.LoginRequestDTO;
import dev.pedrofaleiros.whoiswho_api.dto.request.SignupRequestDTO;
import dev.pedrofaleiros.whoiswho_api.dto.response.AuthResponseDTO;
import dev.pedrofaleiros.whoiswho_api.service.AuthService;
import jakarta.validation.Valid;

import java.security.Principal;

@RestController
@RequestMapping("auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("signup")
    public ResponseEntity<AuthResponseDTO> signup(@Valid @RequestBody SignupRequestDTO data) {
        var response = authService.signup(data);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginRequestDTO data) {
        var response = authService.login(data);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("update")
    public ResponseEntity<AuthResponseDTO> updateUsername(@Valid @RequestBody UpdateUsernameDto data, Principal principal){
        data.setOldUsername(principal.getName());
        var response = authService.updateUsername(data);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("guest")
    public ResponseEntity<AuthResponseDTO> loginGuest(@Valid @RequestBody LoginGuestDTO data) {
        var response = authService.loginGuest(data.getUsername());
        return ResponseEntity.ok().body(response);
    }
}
