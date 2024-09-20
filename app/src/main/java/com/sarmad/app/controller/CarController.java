package com.sarmad.app.controller;

import ch.qos.logback.classic.Logger;
import com.sarmad.app.config.TokenBlacklistService;
import com.sarmad.app.exception.InvalidCredentialsException;
import com.sarmad.app.exception.UserErrorMessage;
import com.sarmad.app.exception.UserNotFoundException;
import com.sarmad.app.model.CarInfo;
import com.sarmad.app.model.CarModel;
import com.sarmad.app.model.User;
import com.sarmad.app.model.UserCar;
import com.sarmad.app.service.CarInfoService;
import com.sarmad.app.service.CarService;
import com.sarmad.app.service.UserService;
import com.sarmad.app.util.JwtTokenUtil;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;







//@RestController
//@RequestMapping("/api")
//public class CarController {
//    private final UserService userService;
//    private final CarInfoService carInfoService;
//
//    private final TokenBlacklistService  tokenBlacklistService ;
//    private  final JwtTokenUtil  jwtTokenUtil ;
//
//
//    private static final Logger logger = (Logger) LoggerFactory.getLogger(CarController.class);
//
//
//    @Autowired
//    public CarController(UserService userService, CarInfoService carInfoService,TokenBlacklistService tokenBlacklistService,JwtTokenUtil jwtTokenUtil) {
//        this.userService = userService;
//        this.carInfoService = carInfoService;
//        this.tokenBlacklistService = tokenBlacklistService;
//        this.jwtTokenUtil = jwtTokenUtil;
//    }
//
//    @GetMapping("/search")
//    public ResponseEntity<?> searchUserCars(@RequestBody Map<String, String> searchParams,@RequestHeader("Authorization") String authorizationHeader) {
//
//        logger.info("Searching for cars: {}", searchParams);
//
//    try {
//        // Extract token from Authorization header
//        String token = authorizationHeader.replace("Bearer ", "");
//
//        // Extract loginId from token (you might need to implement this method)
////        String currenTloginIdFromRedis = tokenBlacklistService.getCurrentToken(token);
//
//        // Check if the token is valid
//        if (tokenBlacklistService.isTokenBlacklisted(token) || jwtTokenUtil.isTokenExpired(token)) {
//            // Token is invalid, throw an exception
//            logger.error("Invalid token: {}", token);
//            throw new InvalidCredentialsException("Invalid or expired token");
//        }
//
//
//
//
//
//        String firstName = searchParams.get("firstName");
//        String secondName = searchParams.get("secondName");
//        String carPlateNumber = searchParams.get("carPlateNumber");
//
//
//        if ((firstName == null || firstName.isEmpty()) && (secondName == null || secondName.isEmpty())) {
//                throw new IllegalArgumentException("firstName and secondName cannot be empty");
//        }
//        logger.info("Please Dont forget update token");
//
//        logger.info("Searching for cars: firstName={}, secondName={}, carPlateNumber={}", firstName, secondName, carPlateNumber);
//
//        List<User> users = userService.findByName(firstName, secondName);
//        logger.info("Found users: {}", users);
//
//
//        List<CarInfo> carInfoList = carInfoService.getCarInfoList(firstName, secondName, carPlateNumber);
//        return ResponseEntity.ok(carInfoList);
//    }
//    catch (Exception e) {
//        logger.error("Error occurred during search: ", e);
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred during the search");
//    }
//    }
//}













@RestController
@RequestMapping("/api")
public class CarController {
    private final UserService userService;
    private final CarInfoService carInfoService;
    private final TokenBlacklistService tokenBlacklistService;
    private final JwtTokenUtil jwtTokenUtil;

    private static final Logger logger = (Logger) LoggerFactory.getLogger(CarController.class);

    @Autowired
    public CarController(UserService userService, CarInfoService carInfoService, TokenBlacklistService tokenBlacklistService, JwtTokenUtil jwtTokenUtil) {
        this.userService = userService;
        this.carInfoService = carInfoService;
        this.tokenBlacklistService = tokenBlacklistService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchUserCars(@RequestBody Map<String, String> searchParams, @RequestHeader("Authorization") String authorizationHeader) {
        logger.info("Searching for cars: {}", searchParams);

        try {
            // Extract token from Authorization header
            String token = authorizationHeader.replace("Bearer ", "");

            // Validate token
            if (tokenBlacklistService.isTokenBlacklisted(token) || jwtTokenUtil.isTokenExpired(token)) {
                logger.error("Invalid or expired token: {}", token);
                throw new InvalidCredentialsException("Invalid or expired token");
            }

            String firstName = searchParams.get("firstName");
            String secondName = searchParams.get("secondName");
            String carPlateNumber = searchParams.get("carPlateNumber");

            if ((firstName == null || firstName.isEmpty()) && (secondName == null || secondName.isEmpty())) {
                logger.error("Invalid search parameters: firstName and secondName are empty");
                throw new IllegalArgumentException("firstName and secondName cannot be empty");
            }

            logger.info("Searching for cars: firstName={}, secondName={}, carPlateNumber={}", firstName, secondName, carPlateNumber);

            List<CarInfo> carInfoList = carInfoService.getCarInfoList(firstName, secondName, carPlateNumber);
            return ResponseEntity.ok(carInfoList);
        } catch (IllegalArgumentException e) {
            logger.error("Invalid search parameters: ", e);
            return ResponseEntity.badRequest()
                    .body(new UserErrorMessage(HttpStatus.BAD_REQUEST.value(), e.getMessage(), System.currentTimeMillis()));
        } catch (UserNotFoundException | InvalidCredentialsException e) {
            logger.error("Authentication error: ", e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new UserErrorMessage(HttpStatus.UNAUTHORIZED.value(), e.getMessage(), System.currentTimeMillis()));
        } catch (Exception e) {
            logger.error("Unexpected error occurred during search: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new UserErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error occurred during the search", System.currentTimeMillis()));
        }
    }
}