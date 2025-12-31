package com.PavitraSoft.FoodApplication.auth;

import com.PavitraSoft.FoodApplication.enums.Role;
import com.PavitraSoft.FoodApplication.model.User;
import com.PavitraSoft.FoodApplication.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthTokenGen authTokenGen;

    public AuthService(AuthenticationManager authenticationManager, UserRepository userRepository, PasswordEncoder passwordEncoder, AuthTokenGen authTokenGen) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authTokenGen = authTokenGen;
    }

        public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDto.getEmail(), loginRequestDto.getPassword())

        );

        User user = (User) authentication.getPrincipal();

        String token = authTokenGen.generateAccessToken(user);

        return new LoginResponseDto(token, user.getId());
    }

    public RegisterResponseDto register(RegisterRequestDto registerRequestDto) throws IOException {
        if(userRepository.findByEmail(registerRequestDto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("User already exists");
        }

        User user = User.builder()
                .name(registerRequestDto.getName())
                .email(registerRequestDto.getEmail())
                .password(passwordEncoder.encode(registerRequestDto.getPassword()))
                .phone(registerRequestDto.getPhone())
                .role(Role.USER)
                .active(true)
                .createdAt(LocalDateTime.now())
                .build();

        userRepository.save(user);
        return RegisterResponseDto.builder()
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

}
