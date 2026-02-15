package com.vibe.employee.controller;

import com.vibe.employee.dto.LoginRequest;
import com.vibe.employee.dto.LoginResponse;
import com.vibe.employee.dto.TokenRefreshRequest;
import com.vibe.employee.dto.TokenRefreshResponse;
import com.vibe.employee.model.RefreshToken;
import com.vibe.employee.repository.ManagerRepository;
import com.vibe.employee.security.JwtUtils;
import com.vibe.employee.service.RefreshTokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final ManagerRepository managerRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }

        return managerRepository.findByUsername(authentication.getName())
                .map(manager -> {
                    String jwtToken = jwtUtils.generateToken(
                            (org.springframework.security.core.userdetails.UserDetails) authentication.getPrincipal());

                    RefreshToken refreshToken = refreshTokenService.createRefreshToken(manager.getUsername());

                    return ResponseEntity.ok(LoginResponse.builder()
                            .username(manager.getUsername())
                            .name(manager.getName())
                            .role(manager.getRole())
                            .token(jwtToken)
                            .refreshToken(refreshToken.getToken())
                            .build());
                })
                .orElse(ResponseEntity.status(401).build());
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(refreshToken -> {
                    String token = jwtUtils.generateTokenByUsername(refreshToken.getUsername());
                    return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
                })
                .orElseThrow(() -> new RuntimeException("Refresh token is not in database!"));
    }
}
