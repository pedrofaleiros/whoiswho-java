package dev.pedrofaleiros.whoiswho_api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;
import dev.pedrofaleiros.whoiswho_api.entity.GameEnvironment;
import dev.pedrofaleiros.whoiswho_api.entity.PlayerRole;
import dev.pedrofaleiros.whoiswho_api.entity.Room;
import dev.pedrofaleiros.whoiswho_api.entity.RoomStatus;
import dev.pedrofaleiros.whoiswho_api.entity.UserEntity;
import dev.pedrofaleiros.whoiswho_api.repository.GameEnvRepository;
import dev.pedrofaleiros.whoiswho_api.repository.PlayerRoleRepository;
import dev.pedrofaleiros.whoiswho_api.repository.RoomRepository;
import dev.pedrofaleiros.whoiswho_api.repository.UserRepository;
import lombok.AllArgsConstructor;

@SpringBootApplication
@AllArgsConstructor
public class WhoiswhoApiApplication implements CommandLineRunner {

  private PasswordEncoder passwordEncoder;
  private UserRepository userRepository;
  private GameEnvRepository gameEnvRepository;
  private PlayerRoleRepository playerRoleRepository;
  private RoomRepository roomRepository;

  public static void main(String[] args) {
    SpringApplication.run(WhoiswhoApiApplication.class, args);
  }

  // TODO: dev only
  @Override
  public void run(String... args) throws Exception {
    var user1 = userRepository.save(UserEntity.builder().username("pedro")
        .password(passwordEncoder.encode("pedro123")).role("ADMIN").build());

    userRepository.save(UserEntity.builder().username("dani")
        .password(passwordEncoder.encode("dani123")).role("USER").build());
    userRepository.save(UserEntity.builder().username("pedrofaleiros")
        .password(passwordEncoder.encode("pedro123")).role("USER").build());

    roomRepository.save(new Room("1234", user1, null, RoomStatus.IDLE, null, 1, true, true));
    roomRepository.save(new Room("1111", user1, null, RoomStatus.IDLE, null, 3, true, false));

    var gameEnv1 =
        gameEnvRepository.save(new GameEnvironment(null, "Aeroporto", null, null, user1, null));
    var gameEnv2 =
        gameEnvRepository.save(new GameEnvironment(null, "Restaurante", null, null, user1, null));
    gameEnvRepository.save(new GameEnvironment(null, "Biblioteca", null, null, user1, null));
    gameEnvRepository.save(new GameEnvironment(null, "Condomínio", null, null, user1, null));

    playerRoleRepository.save(new PlayerRole(null, "Piloto", gameEnv1, null));
    playerRoleRepository.save(new PlayerRole(null, "Aeromoça", gameEnv1, null));
    playerRoleRepository.save(new PlayerRole(null, "Turista", gameEnv1, null));

    playerRoleRepository.save(new PlayerRole(null, "Chefe", gameEnv2, null));
    playerRoleRepository.save(new PlayerRole(null, "Cozinheiro", gameEnv2, null));
    playerRoleRepository.save(new PlayerRole(null, "Cliente", gameEnv2, null));

    List<GameEnvironment> places = new ArrayList<>(Arrays.asList(
        new GameEnvironment("Escritório",
            Arrays.asList(new PlayerRole("Chefe"), new PlayerRole("Gerente"),
                new PlayerRole("Secretária"), new PlayerRole("Copeiro"),
                new PlayerRole("Cara da TI"), new PlayerRole("Faxineira"))),
        new GameEnvironment("Jogo de futebol",
            Arrays.asList(new PlayerRole("Jogador"), new PlayerRole("Torcedor"),
                new PlayerRole("Vendedor"), new PlayerRole("Gandula"), new PlayerRole("Repórter"),
                new PlayerRole("Técnico"), new PlayerRole("Narrador"), new PlayerRole("Juiz"))),
        new GameEnvironment("Praia",
            Arrays.asList(new PlayerRole("Pai de família"), new PlayerRole("Turistão gringo"),
                new PlayerRole("Vendedor ambulante"), new PlayerRole("Salva-vidas"),
                new PlayerRole("Surfista"), new PlayerRole("Vermelhão de sol"),
                new PlayerRole("Cachorro praiano"))),
        new GameEnvironment("Avião",
            Arrays.asList(new PlayerRole("Piloto"), new PlayerRole("Criança berrando"),
                new PlayerRole("Passageiro"), new PlayerRole("Aeromoça"),
                new PlayerRole("Homem Bomba"), new PlayerRole("Co-piloto"))),
        new GameEnvironment("Escola",
            Arrays.asList(new PlayerRole("Diretora"), new PlayerRole("Aluno"),
                new PlayerRole("Professor"), new PlayerRole("Coordenadora"),
                new PlayerRole("Tia da cantina"), new PlayerRole("Filho da diretora"),
                new PlayerRole("Nerd"))),
        new GameEnvironment("Encontro de família",
            Arrays.asList(new PlayerRole("Avós"), new PlayerRole("Tio do pavê"),
                new PlayerRole("Crianças"), new PlayerRole("Cachorro simpático"),
                new PlayerRole("Dono da casa"))),
        new GameEnvironment("Oficina mecânica",
            Arrays.asList(new PlayerRole("Borracheiro"), new PlayerRole("Motorista"),
                new PlayerRole("Mecânico"), new PlayerRole("Dono"), new PlayerRole("Cliente"),
                new PlayerRole("Lavador de carro"))),
        new GameEnvironment("Restaurante",
            Arrays.asList(new PlayerRole("Garçom"), new PlayerRole("Cliente"),
                new PlayerRole("Cozinheiro"), new PlayerRole("Chef de cozinha"),
                new PlayerRole("Recepcionista"), new PlayerRole("Dono"))),
        new GameEnvironment("Hospital",
            Arrays.asList(new PlayerRole("Médico"), new PlayerRole("Enfermeira"),
                new PlayerRole("Paciente"), new PlayerRole("Parente do paciente"),
                new PlayerRole("Cirurgião"), new PlayerRole("Residente"))),
        new GameEnvironment("Balada",
            Arrays.asList(new PlayerRole("Heterotop"), new PlayerRole("Barman"),
                new PlayerRole("Segurança"), new PlayerRole("Bombado"), new PlayerRole("DJ"),
                new PlayerRole("Cara sofrendo pela ex"))),
        new GameEnvironment("Casamento", Arrays.asList(new PlayerRole("Noivo/Noiva"),
            new PlayerRole("Padre"), new PlayerRole("Porta-Alianças"), new PlayerRole("Fotógrafo"),
            new PlayerRole("Pai da noiva"), new PlayerRole("Penetra"), new PlayerRole("Parente"))),
        new GameEnvironment("Metrô",
            Arrays.asList(new PlayerRole("Turista"), new PlayerRole("Trabalhador"),
                new PlayerRole("Vendedor ambulante"), new PlayerRole("Operador de trem"),
                new PlayerRole("Grávida"), new PlayerRole("Homem fedendo"))),
        new GameEnvironment("Asilo",
            Arrays.asList(new PlayerRole("Parente"), new PlayerRole("Idoso"),
                new PlayerRole("Médico"), new PlayerRole("Enfermeira"), new PlayerRole("Zelador"),
                new PlayerRole("Cozinheiro"))),
        new GameEnvironment("Clube de Jazz",
            Arrays.asList(new PlayerRole("Baterista"), new PlayerRole("Saxofonista"),
                new PlayerRole("Cantor"), new PlayerRole("Barman"), new PlayerRole("Dançarina"),
                new PlayerRole("Fã"))),
        new GameEnvironment("Teatro",
            Arrays.asList(new PlayerRole("Espectador"), new PlayerRole("Diretor"),
                new PlayerRole("Ator"), new PlayerRole("Músico"), new PlayerRole("Figurante"),
                new PlayerRole("Assistente de palco"))),
        new GameEnvironment("Cassino",
            Arrays.asList(new PlayerRole("Segurança"), new PlayerRole("Gerente"),
                new PlayerRole("Apostador"), new PlayerRole("Malandro"),
                new PlayerRole("Gerente"))),
        new GameEnvironment("Circo", Arrays.asList(new PlayerRole("Mágico"),
            new PlayerRole("Palhaço"), new PlayerRole("Espectador"), new PlayerRole("Malabrista"),
            new PlayerRole("Acrobata"), new PlayerRole("Contorcionista"), new PlayerRole("Ator"))),
        new GameEnvironment("Delegacia de polícia",
            Arrays.asList(new PlayerRole("Delegado"), new PlayerRole("Policial"),
                new PlayerRole("Bandido"), new PlayerRole("Advogado"), new PlayerRole("Detetive"),
                new PlayerRole("Jornalista"))),
        new GameEnvironment("Supermercado",
            Arrays.asList(new PlayerRole("Cliente"), new PlayerRole("Caixa"),
                new PlayerRole("Açougueiro"), new PlayerRole("Faxineiro"),
                new PlayerRole("Segurança"), new PlayerRole("Repositor"))),
        new GameEnvironment("Obra (Construção)",
            Arrays.asList(new PlayerRole("Dono do terreno"), new PlayerRole("Engenheiro"),
                new PlayerRole("Arquitero"), new PlayerRole("Pedreiro"),
                new PlayerRole("Eletricista"), new PlayerRole("Invasor"))),
        new GameEnvironment("Prisão",
            Arrays.asList(new PlayerRole("Guarda"), new PlayerRole("Preso"),
                new PlayerRole("Visitante"), new PlayerRole("Advogado"),
                new PlayerRole("Traficante"), new PlayerRole("Cozinheiro"))),
        new GameEnvironment("Zoológico",
            Arrays.asList(new PlayerRole("Visitante"), new PlayerRole("Veterinário"),
                new PlayerRole("Leão"), new PlayerRole("Elefante"), new PlayerRole("Girafa"),
                new PlayerRole("Vendedor de pipoca"))),
        new GameEnvironment("Carnaval",
            Arrays.asList(new PlayerRole("Gays"), new PlayerRole("Mlk querendo mulher"),
                new PlayerRole("Gummy duvidoso"), new PlayerRole("Travesti"),
                new PlayerRole("Policial"), new PlayerRole("Bandido"), new PlayerRole("Bêbado"))),
        new GameEnvironment("Motel",
            Arrays.asList(new PlayerRole("Camareira"), new PlayerRole("Puta"),
                new PlayerRole("Amante"), new PlayerRole("Infiel"), new PlayerRole("Recepcionista"),
                new PlayerRole("Casal"))),
        new GameEnvironment("Academia",
            Arrays.asList(new PlayerRole("Personal safado"), new PlayerRole("Bombado"),
                new PlayerRole("Frango"), new PlayerRole("Idosa fazendo zumba"),
                new PlayerRole("Mãe solteira siliconada"), new PlayerRole("Gay da smart fit"))),
        new GameEnvironment("Buteco de Esquina",
            Arrays.asList(new PlayerRole("Cachorro caramelo"), new PlayerRole("Zé da pinga"),
                new PlayerRole("Veio com cirrose"), new PlayerRole("Mendigo"),
                new PlayerRole("Pai de família"), new PlayerRole("Desempregado"))),
        new GameEnvironment("Cruzeiro",
            Arrays.asList(new PlayerRole("Capitão"), new PlayerRole("Chefe de Cozinha"),
                new PlayerRole("Camareira"), new PlayerRole("Animador de Festa"),
                new PlayerRole("Tripulante"), new PlayerRole("Fotógrafo"), new PlayerRole("Garçom"),
                new PlayerRole("Salva Vidas"))),
        new GameEnvironment("Navio Pirata",
            Arrays.asList(new PlayerRole("Capitão"), new PlayerRole("Marujo"),
                new PlayerRole("Prisioneiro"), new PlayerRole("Papagaio"),
                new PlayerRole("Marinheiro"), new PlayerRole("Escravo"))),
        new GameEnvironment("Faculdade",
            Arrays.asList(new PlayerRole("Reitor"), new PlayerRole("Professor"),
                new PlayerRole("Calouro"), new PlayerRole("Veterano"),
                new PlayerRole("Lider da Atlética"))),
        new GameEnvironment("Banco",
            Arrays.asList(new PlayerRole("Gerente"), new PlayerRole("Consultor Financeiro"),
                new PlayerRole("Cliente"), new PlayerRole("Segurança"),
                new PlayerRole("Assaltante"), new PlayerRole("Caixa"))),
        new GameEnvironment("Parque",
            Arrays.asList(new PlayerRole("Vendedor de pipoca"), new PlayerRole("Patinho"),
                new PlayerRole("Cara correndo"), new PlayerRole("Ciclista"),
                new PlayerRole("Casal com cachorro"), new PlayerRole("Amigas no piquenique"))),
        new GameEnvironment("Museu de Arte",
            Arrays.asList(new PlayerRole("Estudante"), new PlayerRole("Turista"),
                new PlayerRole("Segurança"), new PlayerRole("Pintor"), new PlayerRole("Fotógrafo"),
                new PlayerRole("Artista"), new PlayerRole("Caixa"))),
        new GameEnvironment("Cemitério",
            Arrays.asList(new PlayerRole("Coveiro"), new PlayerRole("Padre"),
                new PlayerRole("Morto"), new PlayerRole("Parente de luto"),
                new PlayerRole("Ladrão de covas"))),
        new GameEnvironment("Posto de Gasolina",
            Arrays.asList(new PlayerRole("Frentista"), new PlayerRole("Cliente"),
                new PlayerRole("Lavador de carro"), new PlayerRole("Gerente"),
                new PlayerRole("Caixa da lojinha"))),
        new GameEnvironment("Show de Rock",
            Arrays.asList(new PlayerRole("Vocalista"), new PlayerRole("Guitarrista"),
                new PlayerRole("Baterista"), new PlayerRole("Fã"), new PlayerRole("Técnico de som"),
                new PlayerRole("Vendedor de cerveja"), new PlayerRole("Baixista")))));

    for (var i = 0; i < places.size(); i++) {
      var game = places.get(i);
      var created =
          gameEnvRepository.save(new GameEnvironment(null, game.getName(), null, null, null, null));

      for (var j = 0; j < game.getPlayerRoles().size(); j++) {
        var role = game.getPlayerRoles().get(j);
        playerRoleRepository.save(new PlayerRole(null, role.getName(), created, null));
      }
    }

  }
}
