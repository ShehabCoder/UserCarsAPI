package com.sarmad.app.repository;

import com.sarmad.app.model.CarModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarModelRepository extends MongoRepository<CarModel, Integer> {

    @Query("{ 'carModelId': ?0 }")
    CarModel findByCarModelId(Integer carModelId);

    @Query("{ 'modelName': ?0 }")
    List<CarModel> findByModelName(Integer modelName);

    @Query("{ 'type': ?0 }")
    List<CarModel> findByType(String type);

    @Query("{ 'manufacturerYear': ?0 }")
    List<CarModel> findByManufacturerYear(Integer manufacturerYear);

    @Query("{ 'carPlateNumber': ?0 }")
    List<CarModel> findByCarPlateNumber(String carPlateNumber);
    @Query("{ 'carModelId': ?0 }")
    boolean existsByCarModelId(Integer carModelId);
    @Query("{ 'carModelId': ?0 }")
    void deleteByCarModelId(Integer carModelId);
}