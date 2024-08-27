package dev.pedrofaleiros.whoiswho_api.service;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import dev.pedrofaleiros.whoiswho_api.dto.request.CreateGameEnvDTO;
import dev.pedrofaleiros.whoiswho_api.entity.GameCategory;
import dev.pedrofaleiros.whoiswho_api.entity.GameEnvironment;
import dev.pedrofaleiros.whoiswho_api.entity.UserEntity;
import dev.pedrofaleiros.whoiswho_api.exception.NotAuthException;
import dev.pedrofaleiros.whoiswho_api.exception.not_found.CustomEntityNotFoundException;
import dev.pedrofaleiros.whoiswho_api.exception.not_found.GameCategoryNotFoundException;
import dev.pedrofaleiros.whoiswho_api.exception.not_found.UserNotFoundException;
import dev.pedrofaleiros.whoiswho_api.repository.GameEnvRepository;
import dev.pedrofaleiros.whoiswho_api.service.impl.GameEnvServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class GameEnvServiceTest {

    @Mock
    private UserService userService;
    @Mock
    private GameCategoryService categoryService;
    @Mock
    private GameEnvRepository repository;
    @InjectMocks
    private GameEnvServiceImpl service;

    private CreateGameEnvDTO data;
    private UserEntity user;
    private GameEnvironment gameEnv;
    private GameCategory category;

    @Before
    public void setup() {
        user = UserEntity.builder().id("1").username("username").build();

        category = new GameCategory();
        category.setName("category");
        category.setId("1");

        data = new CreateGameEnvDTO("gameEnvName", user.getUsername(), category.getId());

        gameEnv = GameEnvironment.builder().user(user).name(data.getName()).id("1")
                .gameCategory(category).build();
    }

    @Test
    public void create_Success() {
        when(userService.findByUsername(user.getUsername())).thenReturn(user);
        when(repository.save(any(GameEnvironment.class))).thenReturn(gameEnv);
        when(categoryService.findById(category.getId())).thenReturn(category);

        GameEnvironment result = service.create(data);

        verify(userService, times(1)).findByUsername(anyString());
        verify(categoryService, times(1)).findById(anyString());
        verify(repository, times(1)).save(any(GameEnvironment.class));
        assertNotNull(result);
        assertEquals(gameEnv.getId(), result.getId());
    }

    @Test
    public void createCategoryNull_Success() {
        when(userService.findByUsername(user.getUsername())).thenReturn(user);
        when(repository.save(any(GameEnvironment.class))).thenReturn(gameEnv);

        data.setGameCategoryId(null);
        GameEnvironment result = service.create(data);

        verify(userService, times(1)).findByUsername(anyString());
        verify(categoryService, times(0)).findById(anyString());
        verify(repository, times(1)).save(any(GameEnvironment.class));
        assertNotNull(result);
        assertEquals(gameEnv.getId(), result.getId());
    }

    @Test
    public void createCategoryNotFound_Error() {
        when(userService.findByUsername(user.getUsername())).thenReturn(user);
        when(categoryService.findById(category.getId()))
                .thenThrow(new GameCategoryNotFoundException());

        assertThrows(CustomEntityNotFoundException.class, () -> {
            service.create(data);
        });

        verify(userService, times(1)).findByUsername(anyString());
        verify(categoryService, times(1)).findById(anyString());
        verify(repository, times(0)).save(any(GameEnvironment.class));
    }

    @Test
    public void createUserNotFound_Error() {
        when(userService.findByUsername(user.getUsername())).thenThrow(new UserNotFoundException());

        assertThrows(UserNotFoundException.class, () -> {
            service.create(data);
        });
        verify(userService, times(1)).findByUsername(anyString());
        verify(categoryService, times(0)).findById(anyString());
        verify(repository, times(0)).save(any(GameEnvironment.class));
    }

    @Test
    public void delete_Success() {
        when(repository.findById(gameEnv.getId())).thenReturn(Optional.of(gameEnv));
        when(userService.findByUsername(user.getUsername())).thenReturn(user);

        service.delete(gameEnv.getId(), user.getUsername());

        verify(repository, times(1)).findById(anyString());
        verify(userService, times(1)).findByUsername(anyString());
        verify(repository, times(1)).delete(gameEnv);
    }

    @Test
    public void deleteOtherUser_Error() {
        var user2 = UserEntity.builder().id("id2").username("user2").build();
        when(repository.findById(gameEnv.getId())).thenReturn(Optional.of(gameEnv));
        when(userService.findByUsername(user2.getUsername())).thenReturn(user2);

        assertThrows(NotAuthException.class, () -> {
            service.delete(gameEnv.getId(), user2.getUsername());
        });

        verify(repository, times(1)).findById(anyString());
        verify(userService, times(1)).findByUsername(anyString());
        verify(repository, times(0)).delete(gameEnv);
    }

    @Test
    public void getGameEnvFromUser_OtherUser() {
        var user2 = UserEntity.builder().id("id2").username("user2").build();
        when(repository.findById(gameEnv.getId())).thenReturn(Optional.of(gameEnv));
        when(userService.findByUsername(user2.getUsername())).thenReturn(user2);

        assertThrows(NotAuthException.class, () -> {
            service.getGameEnvFromUser(gameEnv.getId(), user2.getUsername());
        });

        verify(repository, times(1)).findById(anyString());
        verify(userService, times(1)).findByUsername(anyString());
    }
    
    @Test
    public void getGameEnvFromUser_UserNull() {
        gameEnv.setUser(null);
        when(repository.findById(gameEnv.getId())).thenReturn(Optional.of(gameEnv));

        assertThrows(NotAuthException.class, () -> {
            service.getGameEnvFromUser(gameEnv.getId(), user.getUsername());
        });

        verify(repository, times(1)).findById(anyString());
        verify(userService, times(0)).findByUsername(anyString());
    }
}
