package dev.pedrofaleiros.whoiswho_api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;
import dev.pedrofaleiros.whoiswho_api.entity.GameEnvironment;
import dev.pedrofaleiros.whoiswho_api.entity.UserEntity;
import dev.pedrofaleiros.whoiswho_api.repository.GameEnvRepository;
import dev.pedrofaleiros.whoiswho_api.repository.UserRepository;

@SpringBootApplication
public class WhoiswhoApiApplication implements CommandLineRunner {

	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private GameEnvRepository gameEnvRepository;

	public static void main(String[] args) {
		SpringApplication.run(WhoiswhoApiApplication.class, args);
	}

	// TODO: dev only
	@Override
	public void run(String... args) throws Exception {
		try {
			userRepository.save(UserEntity.builder()
					.username("pedro")
					.password(passwordEncoder.encode("pedro123"))
					.role("ADMIN").build());
			
			userRepository.save(UserEntity.builder()
					.username("dani")
					.password(passwordEncoder.encode("dani123"))
					.role("USER").build());
		} catch (Exception e) {
		}

		gameEnvRepository.save(new GameEnvironment(null, "Aeroporto", null, null, null));
		gameEnvRepository.save(new GameEnvironment(null, "Restaurante", null, null, null));
		gameEnvRepository.save(new GameEnvironment(null, "Escola", null, null, null));
		gameEnvRepository.save(new GameEnvironment(null, "Faculdade", null, null, null));
	}

}
