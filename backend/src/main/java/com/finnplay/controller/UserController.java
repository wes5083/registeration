package com.finnplay.controller;

import com.finnplay.service.UserService;
import com.finnplay.vo.AccessTokenResponse;
import com.finnplay.vo.UserLoginRequest;
import com.finnplay.vo.UserRegisterRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/api/users/{email}")
    public void update(@PathVariable String email, @RequestBody UserRegisterRequest request) {
        userService.update(email, request);
    }

    @PostMapping("/api/register")
    public void register(@RequestBody UserRegisterRequest request) {
        userService.register(request);
    }

    @PostMapping("/api/login")
    public ResponseEntity<AccessTokenResponse> login(@RequestBody UserLoginRequest request) {
        return ResponseEntity.ok(userService.login(request));
    }

    @PostMapping("/api/logout")
    public void logout(HttpServletRequest request) {
        userService.logout(request);
    }


}
