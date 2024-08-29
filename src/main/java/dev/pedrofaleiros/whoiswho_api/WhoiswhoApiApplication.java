package dev.pedrofaleiros.whoiswho_api;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;
import dev.pedrofaleiros.whoiswho_api.entity.GameEnvironment;
import dev.pedrofaleiros.whoiswho_api.entity.PlayerRole;
import dev.pedrofaleiros.whoiswho_api.entity.UserEntity;
import dev.pedrofaleiros.whoiswho_api.repository.GameEnvRepository;
import dev.pedrofaleiros.whoiswho_api.repository.PlayerRoleRepository;
import dev.pedrofaleiros.whoiswho_api.repository.UserRepository;
import lombok.AllArgsConstructor;

@SpringBootApplication
@AllArgsConstructor
public class WhoiswhoApiApplication implements CommandLineRunner {

	private PasswordEncoder passwordEncoder;
	private UserRepository userRepository;
	private GameEnvRepository gameEnvRepository;
	private PlayerRoleRepository playerRoleRepository;

	public static void main(String[] args) {
		SpringApplication.run(WhoiswhoApiApplication.class, args);
	}

	// TODO: dev only
	@Override
	public void run(String... args) throws Exception {
		var user1 = userRepository.save(UserEntity.builder()
				.username("pedro")
				.password(passwordEncoder.encode("pedro123"))
				.role("ADMIN").build());
		
		userRepository.save(UserEntity.builder()
				.username("dani")
				.password(passwordEncoder.encode("dani123"))
				.role("USER").build());

		var gameEnv1 = gameEnvRepository.save(new GameEnvironment(null, "Aeroporto", null, null, user1));
		var gameEnv2 = gameEnvRepository.save(new GameEnvironment(null, "Restaurante", null, null, user1));
		gameEnvRepository.save(new GameEnvironment(null, "Escola", null, null, null));
		gameEnvRepository.save(new GameEnvironment(null, "Faculdade", null, null, null));

		playerRoleRepository.save(new PlayerRole(null, "Piloto", gameEnv1));
		playerRoleRepository.save(new PlayerRole(null, "Aeromoça", gameEnv1));
		playerRoleRepository.save(new PlayerRole(null, "Turista", gameEnv1));
		
		playerRoleRepository.save(new PlayerRole(null, "Chefe", gameEnv2));
		playerRoleRepository.save(new PlayerRole(null, "Cozinheiro", gameEnv2));
		playerRoleRepository.save(new PlayerRole(null, "Cliente", gameEnv2));

	}
}
