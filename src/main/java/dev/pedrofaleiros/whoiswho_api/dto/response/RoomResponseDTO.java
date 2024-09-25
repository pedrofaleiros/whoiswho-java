package dev.pedrofaleiros.whoiswho_api.dto.response;

import java.util.List;
import dev.pedrofaleiros.whoiswho_api.entity.Game;
import dev.pedrofaleiros.whoiswho_api.entity.RoomStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RoomResponseDTO {

    private String id;

    private String ownerId;

    private List<UserResponseDTO> users;

    private RoomStatus status;

    private List<Game> games;

    private int impostors;
    private boolean includeDefaultGameEnvs;
    private boolean includeUserGameEnvs;
}
