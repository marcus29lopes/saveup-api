package com.saveup.api.user.service;

import com.saveup.api.shared.exception.EmailAlreadyInUseException;
import com.saveup.api.user.dto.LoginRequest;
import com.saveup.api.user.dto.LoginResponse;
import com.saveup.api.user.dto.RegisterRequest;
import com.saveup.api.user.dto.RegisterResponse;
import com.saveup.api.user.entity.ApiUser;
import com.saveup.api.user.entity.Role;
import com.saveup.api.user.repository.ApiUserRepository;
import com.saveup.api.user.repository.RoleRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class AuthService {
    private final ApiUserRepository apiUserRepository;
    private final RoleRepository roleRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    public AuthService(ApiUserRepository apiUserRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.apiUserRepository = apiUserRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public LoginResponse login(LoginRequest request) {

        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(
                        request.email().trim().toLowerCase(Locale.ROOT),
                        request.password()
                );

        Authentication authentication =
                authenticationManager.authenticate(token);

        String jwt = jwtService.generateToken(authentication);

        return new LoginResponse(jwt, "Bearer");
    }

    @Transactional
    public RegisterResponse register(RegisterRequest request) {
        Role roleUser = roleRepository.findByName("ROLE_USER").orElseThrow(() ->
                new IllegalStateException("Default role ROLE_USER not configured"));
        String email = request.email()
                .trim()
                .toLowerCase(Locale.ROOT);
        if (apiUserRepository.existsByEmail(email)) {
            throw new EmailAlreadyInUseException("Email already registered");
        }
        String passwordHash = passwordEncoder.encode(request.password());

        ApiUser user = new ApiUser(request.name(), email, passwordHash);
        user.addRole(roleUser);

        ApiUser savedUser = apiUserRepository.save(user);

        return new RegisterResponse(savedUser.getId(), savedUser.getName(), savedUser.getEmail());


    }
}
