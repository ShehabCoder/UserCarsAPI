package com.sarmad.app.util;


import lombok.Data;

import java.io.Serializable;

@Data
public class JwtResponse implements Serializable {
    private String token;
    public JwtResponse(String token) {
        this.token = token;
    }
    // Getter
}