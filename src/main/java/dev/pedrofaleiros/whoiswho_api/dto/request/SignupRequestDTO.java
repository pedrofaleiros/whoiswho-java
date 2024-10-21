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
    @Size(min = 5, message = "A senha deve conter no mínimo 5 caracteres")
    String password;

}
