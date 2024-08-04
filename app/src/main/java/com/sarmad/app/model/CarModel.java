package com.sarmad.app.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Data
@Document(collection = "CAR_MODELS")
public class CarModel implements Serializable {
    @Id
    private String id;
    private Integer carModelId;
    private String modelName;
    private String type;
    private Integer manufacturerYear;
}


