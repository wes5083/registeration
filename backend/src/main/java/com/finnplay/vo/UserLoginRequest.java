package com.finnplay.vo;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserLoginRequest {
    @NotBlank(message = "email is null")
    private String email;
    @NotBlank(message = "password is null")
    private String password;
}
