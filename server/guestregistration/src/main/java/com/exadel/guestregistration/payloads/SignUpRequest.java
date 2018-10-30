package com.exadel.guestregistration.payloads;
import javax.validation.constraints.*;

/**
 * used to create signUp object from information retrieved form request
 */
public class SignUpRequest {
    @NotBlank
    @Size(min = 3, max = 15)
    private String username;


    @NotBlank
    @Size(min = 6, max = 20)
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