package dev.pedrofaleiros.whoiswho_api.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PlayerRoleRequestDTO {

    @NotBlank(message = "Nome não pode ser nulo")
    private String name;

    private String gameEnvId;

    private String username;
}