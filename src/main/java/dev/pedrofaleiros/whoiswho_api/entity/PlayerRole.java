package dev.pedrofaleiros.whoiswho_api.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "player_roles")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlayerRole {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;

    @ManyToOne(optional = false)
    @JoinColumn(name = "game_environment_id", referencedColumnName = "id")
    private GameEnvironment gameEnvironment;
}
