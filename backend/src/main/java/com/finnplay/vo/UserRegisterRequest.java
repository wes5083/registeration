package com.finnplay.vo;

import lombok.Data;

import java.sql.Date;

@Data
public class UserRegisterRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Date birthDay;
}
