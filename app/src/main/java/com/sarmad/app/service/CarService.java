package com.sarmad.app.service;
import com.sarmad.app.model.CarModel;
import com.sarmad.app.model.UserCar;
import com.sarmad.app.repository.CarModelRepository;
import com.sarmad.app.repository.UserCarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarService {
    @Autowired
    private UserCarRepository userCarRepository;
    @Autowired
    private CarModelRepository carModelRepository;




    public List<UserCar> findUserCars(Integer userId, String carPlateNumber) {
        if (carPlateNumber != null) {
            return userCarRepository.findByCarPlateNumber(carPlateNumber);
        }
        return userCarRepository.findByUserId(userId);
    }





    public CarModel getCarModel(Integer carModelId) {
        return carModelRepository.findByCarModelId(carModelId);
    }
}