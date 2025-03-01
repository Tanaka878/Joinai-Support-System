package com.springAI.SupprtTicket;

import com.springAI.DTO.AdminLoginRequest;
import com.springAI.DTO.AuthenticationResponse;
import com.springAI.User.User;
import com.springAI.User.UserRepository;
import com.springAI.springSecurity.config.JWTService;
import lombok.RequiredArgsConstructor;


import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;


    public AuthenticationResponse authenticate(AdminLoginRequest authenticationRequest) {
        // Correct the order of parameters: username first, password second
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword())
        );

        var user = userRepository.findByEmail(authenticationRequest.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String token = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(token)
                .build();
    }

   /* public AuthenticationResponse register(RegisterRequest registerRequest) {

        Optional<User> user1 = userRepository.findByEmail(registerRequest.getEmail());
        if(user1.isPresent()) {
            throw new IllegalArgumentException("User already exists with the provided email."); // Or a custom exception
        }

        var user = User.builder()
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .username(registerRequest.getEmail())
                .nickname(registerRequest.getNickname())
                .role(Role.USER)
                .localDate(LocalDate.now())
                .build();
        userRepository.save(user);
        String token = jwtService.generateToken(user);
        return AutheticationResponse.builder()
                .token(token).build();
    }*/
}