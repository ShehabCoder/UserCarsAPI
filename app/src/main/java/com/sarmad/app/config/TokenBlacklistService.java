package com.sarmad.app.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class TokenBlacklistService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String BLACKLIST_KEY = "blacklisted-tokens";
    private static final String USER_TOKEN_KEY_PREFIX = "user:token:";

    public void addTokenToBlacklist(String token) {
        redisTemplate.opsForSet().add(BLACKLIST_KEY, token);
    }
    // Store or update the current token for a user in Redis
    public void storeCurrentToken(String loginId, String token) {
        redisTemplate.opsForValue().set(USER_TOKEN_KEY_PREFIX + loginId, token);
    }

    // Retrieve the current token for a user from Redis
    public String getCurrentToken(String loginId) {
        return redisTemplate.opsForValue().get(USER_TOKEN_KEY_PREFIX + loginId);
    }

    // Check if the token is the current token for the user
    public boolean isTokenValid(String loginId, String token) {
        String currentToken = getCurrentToken(loginId);
        return token.equals(currentToken);
    }

    // Optionally, handle token expiry
    public void setTokenExpiry(String loginId, long expiryInSeconds) {
        redisTemplate.expire(USER_TOKEN_KEY_PREFIX + loginId, Duration.ofSeconds(expiryInSeconds));
    }

    public boolean isTokenBlacklisted(String token) {
        return redisTemplate.opsForSet().isMember(BLACKLIST_KEY, token);
    }
}
