package dev.pedrofaleiros.whoiswho_api.dto.response;

import lombok.Builder;

@Builder
public record AuthResponseDTO(String id, String username, String token) {

}
