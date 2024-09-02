package dev.pedrofaleiros.whoiswho_api.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import dev.pedrofaleiros.whoiswho_api.dto.request.PlayerRoleRequestDTO;
import dev.pedrofaleiros.whoiswho_api.entity.GameEnvironment;
import dev.pedrofaleiros.whoiswho_api.entity.PlayerRole;
import dev.pedrofaleiros.whoiswho_api.entity.UserEntity;
import dev.pedrofaleiros.whoiswho_api.exception.NotAuthException;
import dev.pedrofaleiros.whoiswho_api.repository.PlayerRoleRepository;
import dev.pedrofaleiros.whoiswho_api.service.impl.PlayerRoleServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class PlayerRoleServiceTest {

    @Mock
    private GameEnvService gameEnvService;
    @Mock
    private PlayerRoleRepository playerRoleRepository;

    @InjectMocks
    private PlayerRoleServiceImpl service;

    private UserEntity user;
    private GameEnvironment gameEnv;
    private PlayerRole playerRole;

    @Before
    public void setup() {
        user = UserEntity.builder().id("1").username("user1").build();
        gameEnv = GameEnvironment.builder().user(user).name("gameEnv1").id("1").build();

        playerRole =
                PlayerRole.builder().id("1").name("playerRole1").gameEnvironment(gameEnv).build();
    }

    @Test
    public void create_Success() {
        when(gameEnvService.findFromUserById(gameEnv.getId(), user.getUsername())).thenReturn(gameEnv);
        when(playerRoleRepository.save(any(PlayerRole.class))).thenReturn(playerRole);

        var data =
                new PlayerRoleRequestDTO(playerRole.getName(), gameEnv.getId(), user.getUsername());

        var result = service.create(data);

        verify(gameEnvService, times(1)).findFromUserById(anyString(), anyString());
        verify(playerRoleRepository, times(1)).save(any(PlayerRole.class));

        assertEquals(data.getName(), result.getName());
        assertNotNull(result);
    }

    @Test
    public void createOtherUserGameEnv() {
        var user2 = UserEntity.builder().id("2").username("user2").build();

        when(gameEnvService.findFromUserById(gameEnv.getId(), user2.getUsername()))
                .thenThrow(new NotAuthException());

        var data = new PlayerRoleRequestDTO(playerRole.getName(), gameEnv.getId(),
                user2.getUsername());

        assertThrows(NotAuthException.class, () -> {
            service.create(data);
        });

        verify(gameEnvService, times(1)).findFromUserById(anyString(), anyString());
        verify(playerRoleRepository, times(0)).save(any(PlayerRole.class));
    }
}
