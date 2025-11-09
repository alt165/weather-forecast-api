package com.weather.forecast.service;

import com.weather.forecast.dto.AuthResponse;
import com.weather.forecast.dto.LoginRequest;
import com.weather.forecast.model.Administrator;
import com.weather.forecast.repository.AdministratorRepository;
import com.weather.forecast.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AdministratorRepository administratorRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Transactional
    public AuthResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);

        Administrator admin = administratorRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return new AuthResponse(jwt, admin.getUsername(), admin.getEmail());
    }

    @Transactional
    public Administrator registerAdmin(String username, String password, String email) {
        if (administratorRepository.existsByUsername(username)) {
            throw new RuntimeException("El nombre de usuario ya existe");
        }

        if (administratorRepository.existsByEmail(email)) {
            throw new RuntimeException("El email ya existe");
        }

        Administrator admin = new Administrator();
        admin.setUsername(username);
        admin.setPassword(passwordEncoder.encode(password));
        admin.setEmail(email);
        admin.setEnabled(true);

        return administratorRepository.save(admin);
    }

    public Administrator getCurrentAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return administratorRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }
}