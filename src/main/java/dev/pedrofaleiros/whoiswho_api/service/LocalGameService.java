package dev.pedrofaleiros.whoiswho_api.service;

import dev.pedrofaleiros.whoiswho_api.dto.request.CreateLocalGameDTO;
import dev.pedrofaleiros.whoiswho_api.dto.response.LocalGameResponseDTO;

public interface LocalGameService {
    LocalGameResponseDTO createGame(CreateLocalGameDTO data);
}
