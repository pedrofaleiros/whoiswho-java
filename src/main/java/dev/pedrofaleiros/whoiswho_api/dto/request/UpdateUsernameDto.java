package dev.pedrofaleiros.whoiswho_api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUsernameDto {
    @NotBlank(message = "Username inválido")
    @Size(max = 32, message = "Nome deve conter no máximo 32 caracteres")
    String username;

    String oldUsername;
}
