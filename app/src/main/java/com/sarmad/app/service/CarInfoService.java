package com.sarmad.app.service;

import com.sarmad.app.model.CarInfo;
import com.sarmad.app.model.CarModel;
import com.sarmad.app.model.User;
import com.sarmad.app.model.UserCar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CarInfoService {
    private final UserService userService;
    private final CarService carService;
    private final Logger logger = LoggerFactory.getLogger(CarInfoService.class);

    @Autowired
    public CarInfoService(UserService userService, CarService carService) {
        this.userService = userService;
        this.carService = carService;
    }



    @Cacheable(value = "carInfoCache", key = "#firstName + '_' + #secondName + '_' + #carPlateNumber", unless = "#result.isEmpty()")
    public List<CarInfo> getCarInfoList(String firstName, String secondName, String carPlateNumber) {
        logger.info("Cache miss. Fetching car info for: {} {} {}", firstName, secondName, carPlateNumber);

        List<User> users = userService.findByName(firstName, secondName);
        List<CarInfo> carInfoList = new ArrayList<>();

        for (User user : users) {
            List<UserCar> userCars = carService.findUserCars(user.getUserId(), carPlateNumber);
            for (UserCar userCar : userCars) {
                CarModel carModel = carService.getCarModel(userCar.getCarModelId());
                carInfoList.add(new CarInfo(user, userCar, carModel));
            }
        }

        return carInfoList;
    }
}