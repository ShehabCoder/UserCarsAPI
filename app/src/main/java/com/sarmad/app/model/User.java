package com.sarmad.app.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "USERS")
public class User {
    @Id
    private String id;
    @Indexed(unique = true)
    private Integer userId;
    private String firstName;
    private String secondName;
    private String loginId;
    private String password;
}