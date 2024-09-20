//package com.sarmad.app.config;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.stereotype.Service;
//
//import java.time.Duration;
//
//@Service
//public class TokenBlacklistService {
//
//    @Autowired
//    private StringRedisTemplate redisTemplate;
//
//    private static final String BLACKLIST_KEY = "blacklisted-tokens";
//    private static final String USER_TOKEN_KEY_PREFIX = "user:token:";
//
//    public void addTokenToBlacklist(String token) {
//
//        redisTemplate.opsForSet().add(BLACKLIST_KEY, token);
//    }
//    // Store or update the current token for a user in Redis
//    public void storeCurrentToken(String loginId, String token) {
//        redisTemplate.opsForValue().set(USER_TOKEN_KEY_PREFIX + loginId, token);
//    }
//
//    // Retrieve the current token for a user from Redis
//    public String getCurrentToken(String loginId) {
//        return redisTemplate.opsForValue().get(USER_TOKEN_KEY_PREFIX + loginId);
//    }
//
//    // Check if the token is the current token for the user
//    public boolean isTokenValid(String loginId, String token) {
//        String currentToken = getCurrentToken(loginId);
//        return token.equals(currentToken);
//    }
//
//    // Optionally, handle token expiry
//    public void setTokenExpiry(String loginId, long expiryInSeconds) {
//        redisTemplate.expire(USER_TOKEN_KEY_PREFIX + loginId, Duration.ofSeconds(expiryInSeconds));
//    }
//
//    public boolean isTokenBlacklisted(String token) {
//        return redisTemplate.opsForSet().isMember(BLACKLIST_KEY, token);
//    }
//}




package com.sarmad.app.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class TokenBlacklistService {

    private static final Logger logger = LoggerFactory.getLogger(TokenBlacklistService.class);

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String BLACKLIST_KEY = "blacklisted-tokens";
    private static final String USER_TOKEN_KEY_PREFIX = "user:token:";

    @Retryable(value = RuntimeException.class, maxAttempts = 1, backoff = @Backoff(delay = 1000))
    public void addTokenToBlacklist(String token) {
        try {
            redisTemplate.opsForSet().add(BLACKLIST_KEY, token);
        } catch (RuntimeException e) {
            logger.error("Failed to add token to blacklist: {}", e.getMessage());
            throw new RuntimeException(e.getMessage()); // Rethrow to trigger retry
        }
    }

    @Retryable(value = RuntimeException.class, maxAttempts = 1, backoff = @Backoff(delay = 1000))
    public void storeCurrentToken(String loginId, String token) {
        try {
            redisTemplate.opsForValue().set(USER_TOKEN_KEY_PREFIX + loginId, token);
        } catch (RuntimeException e) {
            logger.error("Failed to store current token: {}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Retryable(value = RuntimeException.class, maxAttempts = 1, backoff = @Backoff(delay = 1000))
    public String getCurrentToken(String loginId) {
        try {
            return redisTemplate.opsForValue().get(USER_TOKEN_KEY_PREFIX + loginId);
        } catch (RuntimeException e) {
            logger.error("Failed to get current token: {}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public boolean isTokenValid(String loginId, String token) {
        try {
            String currentToken = getCurrentToken(loginId);
            return token.equals(currentToken);
        } catch (RuntimeException e) {
            logger.error("Failed to check token validity: {}", e.getMessage());
            throw new RuntimeException(e.getMessage());
//            return false; // Consider the token invalid if we can't verify
        }
    }

    @Retryable(value = RedisConnectionFailureException.class, maxAttempts = 1, backoff = @Backoff(delay = 1000))
    public void setTokenExpiry(String loginId, long expiryInSeconds) {
        try {
            redisTemplate.expire(USER_TOKEN_KEY_PREFIX + loginId, Duration.ofSeconds(expiryInSeconds));
        } catch (RuntimeException e) {
            logger.error("Failed to set token expiry: {}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Retryable(value = RedisConnectionFailureException.class, maxAttempts = 1, backoff = @Backoff(delay = 1000))
    public boolean isTokenBlacklisted(String token) {
        try {
            return redisTemplate.opsForSet().isMember(BLACKLIST_KEY, token);
        } catch (RuntimeException e) {
            logger.error("Failed to check if token is blacklisted: {}", e.getMessage());
            return false; // Consider the token not blacklisted if we can't verify
        }
    }
}
