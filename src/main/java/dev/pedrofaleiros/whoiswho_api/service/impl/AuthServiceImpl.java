package dev.pedrofaleiros.whoiswho_api.service.impl;

import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import dev.pedrofaleiros.whoiswho_api.config.security.TokenService;
import dev.pedrofaleiros.whoiswho_api.dto.request.LoginRequestDTO;
import dev.pedrofaleiros.whoiswho_api.dto.request.SignupRequestDTO;
import dev.pedrofaleiros.whoiswho_api.dto.response.AuthResponseDTO;
import dev.pedrofaleiros.whoiswho_api.entity.UserEntity;
import dev.pedrofaleiros.whoiswho_api.exception.bad_request.LoginException;
import dev.pedrofaleiros.whoiswho_api.exception.bad_request.UsernameAlreadyExistsException;
import dev.pedrofaleiros.whoiswho_api.repository.UserRepository;
import dev.pedrofaleiros.whoiswho_api.service.AuthService;
import lombok.AllArgsConstructor;

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
        if (!user.isPresent()) {
            throw new LoginException();
        }
        var passwordMatches = passwordEncoder.matches(data.getPassword(), user.get().getPassword());
        if (!passwordMatches) {
            throw new LoginException();
        }
        var token = tokenService.generateToken(user.get());

        return AuthResponseDTO.builder()
                .id(user.get().getId())
                .username(user.get().getUsername())
                .token(token).build();
    }
}
