package dev.pedrofaleiros.whoiswho_api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SignupRequestDTO {
    @NotBlank(message = "Username não pode ser nulo")
    String username;

    @NotBlank(message = "Senha inválida")
    @Size(min = 6, message = "A senha deve conter no mínimo 6 caracteres")
    String password;

}
