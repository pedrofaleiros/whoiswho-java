package dev.pedrofaleiros.whoiswho_api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateGameEnvDTO {

    @NotBlank(message = "Nome não pode ser nulo")
    @Size(max = 32, message = "Nome deve ter no máximo 32 caracteres")
    private String name;

    private String username;
    private String gameCategoryId;
}
