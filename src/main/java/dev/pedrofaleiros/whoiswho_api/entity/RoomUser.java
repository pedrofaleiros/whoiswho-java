package dev.pedrofaleiros.whoiswho_api.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "room_user")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoomUser {
    
    @Id
    private String sessionId;

    
    private Room room;
    
    private UserEntity user;

}
