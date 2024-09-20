package com.sarmad.app.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sarmad.app.exception.InvalidCredentialsException;
import com.sarmad.app.exception.UserErrorMessage;
import com.sarmad.app.exception.UserNotFoundException;
import com.sarmad.app.model.User;
import com.sarmad.app.service.UserService;
import com.sarmad.app.util.JwtTokenUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

//@Component

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtTokenUtil jwtTokenUtil;
    private final UserService userService;
    private final TokenBlacklistService tokenBlacklistService;

    @Autowired
    public JwtAuthenticationFilter(JwtTokenUtil jwtTokenUtil, @Lazy UserService userService, TokenBlacklistService tokenBlacklistService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userService = userService;
        this.tokenBlacklistService = tokenBlacklistService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            final String authorizationHeader = request.getHeader("Authorization");
            logger.info("Authorization Header: {}", authorizationHeader);

            String loginId = null;
            String jwt = null;

            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                jwt = authorizationHeader.substring(7);
                loginId = jwtTokenUtil.getLoginIdFromToken(jwt);
                logger.info("JWT: {}", jwt);
                logger.info("Login ID: {}", loginId);
            }

            if (loginId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                if (tokenBlacklistService.isTokenBlacklisted(jwt)) {
                    logger.info("Token has been blacklisted, please login again");
                    throw new InvalidCredentialsException("Token has been blacklisted");
                }

                Optional<User> userOptional = userService.findByLoginId(loginId);

                if (userOptional.isPresent()) {
                    User user = userOptional.get();

                    if (jwtTokenUtil.validateToken(jwt, user)) {
                        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                                user, null, Collections.emptyList());
                        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                        logger.info("User authenticated: {}", user.getLoginId());
                    } else {
                        logger.info("Invalid token for user: {}", user.getLoginId());
                        throw new InvalidCredentialsException("Invalid token");
                    }
                } else {
                    logger.info("User not found with login ID: {}", loginId);
                    throw new UserNotFoundException("User not found with login ID: " + loginId);
                }
            }
            filterChain.doFilter(request, response);
        } catch (InvalidCredentialsException | UserNotFoundException e) {
            logger.error("Authentication error: ", e);
            sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error during authentication: ", e);
            sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An unexpected error occurred");
        }
    }

    private void sendErrorResponse(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(new ObjectMapper().writeValueAsString(new UserErrorMessage(
                status,
                message,
                System.currentTimeMillis()
        )));
    }
}

//@Component
//public class JwtAuthenticationFilter extends OncePerRequestFilter {
//
//    private final JwtTokenUtil jwtTokenUtil;
//    private final UserService userService;
//
//    @Autowired
//    public JwtAuthenticationFilter(JwtTokenUtil jwtTokenUtil, @Lazy UserService userService) {
//        this.jwtTokenUtil = jwtTokenUtil;
//        this.userService = userService;
//    }
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//            throws ServletException, IOException {
//
//        final String authorizationHeader = request.getHeader("Authorization");
//        logger.info("Authorization Header: " + authorizationHeader);
//
//        String loginId = null;
//        String jwt = null;
//
//        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
//            jwt = authorizationHeader.substring(7);
//            loginId = jwtTokenUtil.getLoginIdFromToken(jwt);
//            logger.info("JWT: " + jwt);
//            logger.info("Login ID: " + loginId);
//        }
//
//        if (loginId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//            Optional<User> userOptional = userService.findByLoginId(loginId);
//
//            if (userOptional.isPresent()) {
//                User user = userOptional.get();
//
//                if (jwtTokenUtil.validateToken(jwt, user)) {
//                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
//                            user, null, Collections.emptyList());
//                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
//                    logger.info("User authenticated: " + user.getLoginId());
//                } else {
//                    logger.info("Invalid token for user: " + user.getLoginId());
//                }
//            } else {
//                logger.info("User not found with login ID: " + loginId);
//            }
//        }
//        filterChain.doFilter(request, response);
//    }
//}
