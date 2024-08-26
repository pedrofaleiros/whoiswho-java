package dev.pedrofaleiros.whoiswho_api.service;

import dev.pedrofaleiros.whoiswho_api.dto.request.LoginRequestDTO;
import dev.pedrofaleiros.whoiswho_api.dto.request.SignupRequestDTO;
import dev.pedrofaleiros.whoiswho_api.dto.response.AuthResponseDTO;

public interface AuthService {
    AuthResponseDTO signup(SignupRequestDTO data);
    AuthResponseDTO login(LoginRequestDTO data);
}
