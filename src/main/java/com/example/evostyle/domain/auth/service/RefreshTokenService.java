package com.example.evostyle.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RedisTemplate<String, String> redisTemplate;

    private final String REDIS_PREFIX = "refresh:";

    public void save(Long memberId, String refreshToken, long expirationMillis) {
        String key = REDIS_PREFIX + memberId;

        redisTemplate.opsForValue().set(key, refreshToken, Duration.ofMillis(expirationMillis));
    }

    public String get(Long memberId) {
        return redisTemplate.opsForValue().get(REDIS_PREFIX + memberId);
    }

    public void delete(Long memberId) {
        redisTemplate.delete(REDIS_PREFIX + memberId);
    }
}
