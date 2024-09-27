package dev.pedrofaleiros.whoiswho_api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateRoomDTO {

    private String room;
    private String username;
    
    private int impostors;
    private boolean includeDefaultGameEnvs;
    private boolean includeUserGameEnvs;
}
