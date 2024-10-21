package dev.pedrofaleiros.whoiswho_api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class LoginRequestDTO {
    @NotBlank(message = "Username inválido")
    @Size(max = 32, message = "Nome deve conter no máximo 32 caracteres")
    String username;

    @NotBlank(message = "Senha inválida")
    @Size(max = 32, message = "Senha pode conter no máximo 32 caracteres")
    String password;

}