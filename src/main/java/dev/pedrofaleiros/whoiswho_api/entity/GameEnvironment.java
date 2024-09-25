package dev.pedrofaleiros.whoiswho_api.entity;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "game_environments")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GameEnvironment {

    public GameEnvironment(String name, List<PlayerRole> list) {
        this.name = name;
        this.playerRoles = list;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "gameEnvironment", cascade = CascadeType.ALL)
    private List<PlayerRole> playerRoles;

    @JsonIgnore
    @ManyToOne(optional = true)
    @JoinColumn(name = "game_category_id", referencedColumnName = "id")
    private GameCategory gameCategory;

    @JsonIgnore
    @ManyToOne(optional = true)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity user;

    @JsonIgnore
    @OneToMany(mappedBy = "gameEnvironment", cascade = CascadeType.ALL)
    private List<Game> games;
}
