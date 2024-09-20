package com.sarmad.app.util;

import ch.qos.logback.classic.Logger;
import com.sarmad.app.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;



@Component
public class JwtTokenUtil implements Serializable {

    private static final long serialVersionUID = 1L;

    Logger logger = (Logger) LoggerFactory.getLogger(JwtTokenUtil.class);

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    private Key getSigningKey() {
        byte[] keyBytes = secret.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", user.getLoginId());
        claims.put("created", new Date().getTime());
        String token = Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getLoginId())
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();

        logger.info("Generated Token: " + token);
        return token;
    }

    public String getLoginIdFromToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            logger.info("Parsed Claims: " + claims);
            String loginId = claims.getSubject();
            logger.info("Extracted Login ID: " + loginId);
            return loginId;
        } catch (RuntimeException e) {
            logger.error("Error extracting login ID from token", e);
            throw new RuntimeException(e);
        }
    }

    public boolean validateToken(String token, User user) {
        String loginId = getLoginIdFromToken(token);
        logger.info("Validating token for login ID: {}", loginId);
        return (loginId.equals(user.getLoginId()) && !isTokenExpired(token));
    }

    public boolean isTokenExpired(String token) {
        Date expiration = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        return expiration.before(new Date());
    }

    public Long getExpirationInSeconds() {
        return expiration;
    }
}











//
//@Component
//public class JwtTokenUtil implements Serializable {
//    Logger logger = (Logger) LoggerFactory.getLogger(JwtTokenUtil.class);
//    @Value("${jwt.secret}")
//    private String secret;
//
//    @Value("${jwt.expiration}")
//    private Long expiration;
//
//    private Key getSigningKey() {
//        byte[] keyBytes = secret.getBytes();
//        return Keys.hmacShaKeyFor(keyBytes);
//    }
//
//    public String generateToken(User user) {
//        Map<String, Object> claims = new HashMap<>();
//        claims.put("username", user.getLoginId());
//        claims.put("created", new Date().getTime());
//        String token = Jwts.builder()
//                .setClaims(claims)
//                .setSubject(user.getLoginId())
//                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
//                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
//                .compact();
//
//        logger.info("Generated Token: " + token);
//        return token;
//    }
//
//
//    public String getLoginIdFromToken(String token) {
//        try {
//            Claims claims = Jwts.parserBuilder()
//                    .setSigningKey(getSigningKey())
//                    .build()
//                    .parseClaimsJws(token)
//                    .getBody();
//            logger.info("Parsed Claims: " + claims);
//            String loginId = claims.getSubject();
//            logger.info("Extracted Login ID: " + loginId);
//            return loginId;
//        } catch (RuntimeException e) {
//            logger.error("Error extracting login ID from token", e);
//            throw new RuntimeException(e);
//        }
//    }
//
//
//    public boolean validateToken(String token, User user) {
//        String loginId = getLoginIdFromToken(token);
//        System.out.println(loginId + "----{from jwt Util} ---- " + user.getLoginId());
//        return (loginId.equals(user.getLoginId()) && !isTokenExpired(token));
//    }
//
//    public boolean isTokenExpired(String token) {
//        Date expiration = Jwts.parserBuilder()
//                .setSigningKey(getSigningKey())
//                .build()
//                .parseClaimsJws(token)
//                .getBody()
//                .getExpiration();
//        return expiration.before(new Date());
//    }
//
//    public Long getExpirationInSeconds() {
//        return expiration;
//    }
//
//
//}