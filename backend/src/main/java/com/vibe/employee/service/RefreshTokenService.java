package com.vibe.employee.service;

import com.vibe.employee.model.RefreshToken;
import com.vibe.employee.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    @Value("${app.jwt.refreshExpiration:604800000}") // 7 days
    private Long refreshTokenDurationMs;

    private final RefreshTokenRepository refreshTokenRepository;

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findById(token);
    }

    public RefreshToken createRefreshToken(String username) {
        // Option: Delete existing tokens for this username in Redis
        List<RefreshToken> existingTokens = refreshTokenRepository.findAllByUsername(username);
        refreshTokenRepository.deleteAll(existingTokens);

        RefreshToken refreshToken = RefreshToken.builder()
                .username(username)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(refreshTokenDurationMs))
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException("Refresh token was expired. Please make a new signin request");
        }
        return token;
    }

    public void deleteByUsername(String username) {
        List<RefreshToken> existingTokens = refreshTokenRepository.findAllByUsername(username);
        refreshTokenRepository.deleteAll(existingTokens);
    }
}
