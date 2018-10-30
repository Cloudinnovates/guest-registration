package com.exadel.guestregistration.payloads;

import javax.validation.constraints.NotBlank;

/**
 * used to create login object from information
 * retrieved from login request
 */
public class LoginRequest {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
