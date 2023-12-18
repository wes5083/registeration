package com.finnplay.vo;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Date;

@Data
public class UserRegisterRequest {
    @NotBlank(message = "email is null")
    @Email(message = "email format is incorrect")
    private String email;

    @NotBlank(message = "password is null")
    private String password;

    @Length(max = 100, message = "firsName max length is 100")
    private String firstName;

    @Length(max = 100, message = "lastName max length is 100")
    private String lastName;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Past(message="birthDay should before today")
    private Date birthDay;
}
