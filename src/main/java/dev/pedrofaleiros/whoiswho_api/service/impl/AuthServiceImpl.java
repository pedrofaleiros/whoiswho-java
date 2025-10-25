package dev.pedrofaleiros.whoiswho_api.service.impl;

import java.util.Optional;
import dev.pedrofaleiros.whoiswho_api.dto.request.UpdateUsernameDto;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import dev.pedrofaleiros.whoiswho_api.config.security.TokenService;
import dev.pedrofaleiros.whoiswho_api.dto.request.LoginRequestDTO;
import dev.pedrofaleiros.whoiswho_api.dto.request.SignupRequestDTO;
import dev.pedrofaleiros.whoiswho_api.dto.response.AuthResponseDTO;
import dev.pedrofaleiros.whoiswho_api.entity.UserEntity;
import dev.pedrofaleiros.whoiswho_api.exception.LoginException;
import dev.pedrofaleiros.whoiswho_api.exception.bad_request.UsernameAlreadyExistsException;
import dev.pedrofaleiros.whoiswho_api.repository.UserRepository;
import dev.pedrofaleiros.whoiswho_api.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import java.util.Collections;
import java.util.Map;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private UserRepository userRepository;
    private TokenService tokenService;
    private PasswordEncoder passwordEncoder;

    @Override
    public AuthResponseDTO signup(SignupRequestDTO data) {
        var user = userRepository.findByUsername(data.getUsername());
        if (user.isPresent()) {
            throw new UsernameAlreadyExistsException();
        }
        var encodedPassword = passwordEncoder.encode(data.getPassword());
        UserEntity newUser = UserEntity.builder()
                            .username(data.getUsername())
                            .password(encodedPassword)
                            .role("USER")
                            .build();

        var savedUser = userRepository.save(newUser);
        var token = tokenService.generateToken(savedUser);

        return AuthResponseDTO.builder()
                .id(savedUser.getId())
                .username(savedUser.getUsername())
                .token(token).build();
    }

    @Override
    public AuthResponseDTO login(LoginRequestDTO data) {
        Optional<UserEntity> user = userRepository.findByUsername(data.getUsername());
        if (user.isEmpty()) {
            throw new LoginException();
        }
        
        if(user.get().isGuest()){
            throw new LoginException("Não é possivel fazer login com esse usuario");
        }

        boolean passwordMatches = passwordEncoder.matches(data.getPassword(), user.get().getPassword());
        if (!passwordMatches) {
            throw new LoginException();
        }
        var token = tokenService.generateToken(user.get());

        return AuthResponseDTO.builder()
                .id(user.get().getId())
                .username(user.get().getUsername())
                .token(token).build();
    }

    @Override
    public AuthResponseDTO updateUsername(UpdateUsernameDto data) {
        var findUser = userRepository.findByUsername(data.getUsername());
        if(findUser.isPresent() && !findUser.get().getUsername().equals(data.getOldUsername())){
            throw new UsernameAlreadyExistsException();
        }

        if(findUser.get().isGuest()){
            throw new LoginException("Não é possivel alterar os dados desse usuario");
        }

        UserEntity user = userRepository.findByUsername(data.getOldUsername()).orElseThrow(()->new LoginException());
        user.setUsername(data.getUsername());
        var savedUser = userRepository.save(user);
        var token = tokenService.generateToken(savedUser);
        return AuthResponseDTO.builder()
                .id(savedUser.getId())
                .username(savedUser.getUsername())
                .token(token).build();
    }

    @Override
    public AuthResponseDTO loginGuest(String username) {
        if(userRepository.existsByUsername(username)){
            throw new UsernameAlreadyExistsException();
        }
        
        UserEntity newUser = UserEntity.builder()
                            .username(username)
                            .password(passwordEncoder.encode("guest"))
                            .role("USER")
                            .isGuest(true)
                            .build();

        var savedUser = userRepository.save(newUser);
        var token = tokenService.generateToken(savedUser);

        return AuthResponseDTO.builder()
                .id(savedUser.getId())
                .username(savedUser.getUsername())
                .token(token).build();
    }

    @Override
    public AuthResponseDTO loginWithGithub(String code) {
        try {
            System.out.println("[GitHub OAuth] loginWithGithub started");
            String maskedCode = code == null ? "null" : (code.length() <= 6 ? code : code.substring(0, 6) + "...");
            System.out.println("[GitHub OAuth] Received code: " + maskedCode);
            System.out.println("[GitHub OAuth] Using clientId: " + tokenService.getGithubClientId());

            RestTemplate rest = new RestTemplate();

            HttpHeaders tokenHeaders = new HttpHeaders();
            tokenHeaders.setContentType(MediaType.APPLICATION_JSON);
            tokenHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

            Map<String, String> tokenBody = Map.of(
                "client_id", tokenService.getGithubClientId(),
                "client_secret", tokenService.getGithubClientSecret(),
                "code", code
            );

            HttpEntity<Map<String, String>> tokenRequest = new HttpEntity<>(tokenBody, tokenHeaders);
            System.out.println("[GitHub OAuth] Exchanging code for access_token...");
            ResponseEntity<Map> tokenResponse = rest.postForEntity(
                "https://github.com/login/oauth/access_token",
                tokenRequest,
                Map.class
            );

            System.out.println("[GitHub OAuth] Token response status: " + tokenResponse.getStatusCode());
            if (tokenResponse.getBody() != null) {
                System.out.println("[GitHub OAuth] Token response keys: " + tokenResponse.getBody().keySet());
                Object err = tokenResponse.getBody().get("error");
                if (err != null) {
                    System.out.println("[GitHub OAuth] Token error: " + err + ", desc=" + tokenResponse.getBody().get("error_description"));
                }
            }

            if (!tokenResponse.getStatusCode().is2xxSuccessful() || tokenResponse.getBody() == null) {
                throw new LoginException("Falha ao obter token do GitHub");
            }

            Object accessTokenObj = tokenResponse.getBody().get("access_token");
            if (accessTokenObj == null) {
                throw new LoginException("Token do GitHub não retornado");
            }
            String accessToken = accessTokenObj.toString();
            String maskedToken = accessToken.length() <= 8 ? "***" : accessToken.substring(0, 4) + "..." + accessToken.substring(accessToken.length() - 4);
            System.out.println("[GitHub OAuth] Received access_token: " + maskedToken);

            HttpHeaders userHeaders = new HttpHeaders();
            userHeaders.setBearerAuth(accessToken);
            userHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

            HttpEntity<Void> userRequest = new HttpEntity<>(userHeaders);
            System.out.println("[GitHub OAuth] Fetching /user from GitHub API...");
            ResponseEntity<Map> userResponse = rest.exchange(
                "https://api.github.com/user",
                HttpMethod.GET,
                userRequest,
                Map.class
            );

            System.out.println("[GitHub OAuth] User response status: " + userResponse.getStatusCode());
            if (!userResponse.getStatusCode().is2xxSuccessful() || userResponse.getBody() == null) {
                throw new LoginException("Falha ao obter usuário do GitHub");
            }

            String githubLogin = String.valueOf(userResponse.getBody().get("login"));
            if (githubLogin == null || githubLogin.equals("null") || githubLogin.isBlank()) {
                throw new LoginException("Login do GitHub inválido");
            }
            System.out.println("[GitHub OAuth] GitHub login: " + githubLogin);

            String username = "github-" + githubLogin;
            System.out.println("[GitHub OAuth] Local username: " + username);

            var existing = userRepository.findByUsername(username);
            UserEntity user = existing.orElseGet(() -> {
                System.out.println("[GitHub OAuth] Creating new local user record...");
                UserEntity u = UserEntity.builder()
                    .username(username)
                    .password(passwordEncoder.encode("github_oauth"))
                    .role("USER")
                    .isGuest(false)
                    .build();
                return userRepository.save(u);
            });
            if (existing.isPresent()) {
                System.out.println("[GitHub OAuth] Using existing local user record");
            }

            var jwt = tokenService.generateToken(user);
            System.out.println("[GitHub OAuth] Generated JWT for user id=" + user.getId());
            return AuthResponseDTO.builder()
                    .id(user.getId())
                    .username(user.getUsername())
                    .token(jwt)
                    .build();
        } catch (LoginException e) {
            System.out.println("[GitHub OAuth] LoginException: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.out.println("[GitHub OAuth] Unexpected error: " + e.getMessage());
            throw new LoginException("Erro ao autenticar com o GitHub");
        }
    }
}
