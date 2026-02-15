package com.vibe.employee.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RedisHash(value = "RefreshToken")
public class RefreshToken {

    @Id
    private String token;

    @Indexed
    private String username;

    private Instant expiryDate;
}
