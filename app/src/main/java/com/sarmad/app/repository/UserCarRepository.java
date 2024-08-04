package com.sarmad.app.repository;

import com.sarmad.app.model.UserCar;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserCarRepository extends MongoRepository<UserCar, Integer> {

    @Query( "{'userId': ?0 }")
    List<UserCar> findByUserId(Integer userId);

    @Query( "{'carPlateNumber': ?0 }")
    List<UserCar> findByCarPlateNumber(String carPlateNumber);
}