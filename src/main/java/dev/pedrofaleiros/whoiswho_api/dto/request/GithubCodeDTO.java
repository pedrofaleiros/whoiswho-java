package dev.pedrofaleiros.whoiswho_api.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GithubCodeDTO {
    @NotBlank
    private String code;
}

