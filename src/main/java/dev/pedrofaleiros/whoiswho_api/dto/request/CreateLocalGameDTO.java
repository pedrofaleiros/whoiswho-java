package dev.pedrofaleiros.whoiswho_api.dto.request;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateLocalGameDTO {
    List<String> players;
    int impostors;
    boolean includeUserGameEnvs;
    boolean includeDefaultGameEnvs;
    String username;
}
