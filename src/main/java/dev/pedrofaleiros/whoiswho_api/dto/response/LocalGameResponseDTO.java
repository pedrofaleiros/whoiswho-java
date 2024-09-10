package dev.pedrofaleiros.whoiswho_api.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LocalGameResponseDTO {
    String gameEnv;
    List<GamePlayerRole> playerRoles;

    @AllArgsConstructor
    @Getter
    @Setter
    public static class GamePlayerRole {
        String name;
        String profession;
    }
}
