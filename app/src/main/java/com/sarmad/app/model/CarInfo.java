package com.sarmad.app.model;

import lombok.Data;

import java.io.Serializable;


@Data
public class CarInfo implements Serializable {
    private Integer userId;
    private String firstName;
    private String lastName;
    private String carPlateNumber;
    private String color;
    private String modelName;
    private String type;
    private Integer manufacturerYear;

    public CarInfo(User user, UserCar userCar, CarModel carModel) {
        this.userId = user.getUserId();
        this.firstName = user.getFirstName();
        this.lastName = user.getSecondName();
        this.carPlateNumber = userCar.getCarPlateNumber();
        this.color = userCar.getColor();
        this.modelName = carModel.getModelName();
        this.type = carModel.getType();
        this.manufacturerYear = carModel.getManufacturerYear();
    }

    // Getters and setters
    // You can use Lombok's @Data annotation to automatically generate these if you prefer

    // toString method for easy printing/logging
    @Override
    public String toString() {
        return "CarInfo{" +
                "userId='" + userId + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", carPlateNumber='" + carPlateNumber + '\'' +
                ", color='" + color + '\'' +
                ", modelName='" + modelName + '\'' +
                ", type='" + type + '\'' +
                ", manufacturerYear=" + manufacturerYear +
                '}';
    }
}