package com.sarmad.app.util;


import lombok.Data;

import java.io.Serializable;

@Data
public class LoginRequest implements Serializable {
    private String loginId;
    private String password;

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
// Getters and setters
}