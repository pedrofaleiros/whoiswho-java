package dev.pedrofaleiros.whoiswho_api.entity;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "game_categories")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GameCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "gameCategory", cascade = CascadeType.PERSIST)
    private List<GameEnvironment> gameEnvironments;
}
