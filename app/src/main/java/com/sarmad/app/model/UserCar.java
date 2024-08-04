package com.sarmad.app.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "USER_CARS")
public class UserCar {
    @Id
    private String id;
    private Integer userId;
    private Integer carModelId;
    private String carPlateNumber;
    private String color;


}