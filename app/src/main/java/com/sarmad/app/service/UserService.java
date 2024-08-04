package com.sarmad.app.service;

import com.sarmad.app.config.TokenBlacklistService;
import com.sarmad.app.exception.InvalidCredentialsException;
import com.sarmad.app.exception.UserAlreadyExistsException;
import com.sarmad.app.exception.UserNotFoundException;
import com.sarmad.app.model.User;
import com.sarmad.app.repository.UserRepository;
import com.sarmad.app.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenBlacklistService tokenBlacklistService;
    @Autowired
    private IdGeneratorService idGeneratorService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    public User registerUser(User user) throws UserAlreadyExistsException {
        if (userRepository.findByLoginId(user.getLoginId()).isPresent()) {
            throw new UserAlreadyExistsException( "User already exists with loginId: " + user.getLoginId());
        }

        user.setUserId(idGeneratorService.getNextId());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }



    public String loginUser(String loginId, String password) throws UserNotFoundException, InvalidCredentialsException {
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new UserNotFoundException("User not found with loginId: " + loginId));

        if (passwordEncoder.matches(password, user.getPassword())) {
            // Blacklist old token if it exists
            String oldToken = tokenBlacklistService.getCurrentToken(loginId);
            if (oldToken != null) {
                tokenBlacklistService.addTokenToBlacklist(oldToken);
            }

            // Generate new token
            String newToken = jwtTokenUtil.generateToken(user);

            // Store new token in Redis
            tokenBlacklistService.storeCurrentToken(loginId, newToken);

            // Optionally, set token expiry
            tokenBlacklistService.setTokenExpiry(loginId, jwtTokenUtil.getExpirationInSeconds());

            return newToken;
        } else {
            throw new InvalidCredentialsException("Invalid credentials for loginId: " + loginId);
        }
    }


    public Optional<User> findByLoginId(String loginId) {
        return userRepository.findByLoginId(loginId);
    }

    public List<User> findByName(String firstName, String secondName) {
        if (firstName != null && secondName != null) {
            return userRepository.findByFirstNameAndSecondName(firstName, secondName);
        } else if (firstName != null) {
            return userRepository.findByFirstName(firstName);
        } else if (secondName != null) {
            return userRepository.findBySecondName(secondName);
        }
        return new ArrayList<>();
    }
}