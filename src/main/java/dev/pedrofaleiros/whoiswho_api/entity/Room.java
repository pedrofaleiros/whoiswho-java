package dev.pedrofaleiros.whoiswho_api.entity;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "rooms")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Room {

    @Id
    private String id;

    @ManyToOne(optional = false)
    private UserEntity owner;

    // @JsonIgnore
    // @ManyToMany
    // @JoinTable(
    //     name = "room_users",
    //     joinColumns = @JoinColumn(name = "room_id", referencedColumnName = "id"),
    //     inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id")
    // )
    // private Set<UserEntity> users;

    @JsonIgnore
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RoomUser> roomUsers;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoomStatus status;

    @JsonIgnore
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private List<Game> games;

    private int impostors;
    private boolean includeDefaultGameEnvs;
    private boolean includeUserGameEnvs;
}
