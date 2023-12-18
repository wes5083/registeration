package com.finnplay.controller;

import com.finnplay.service.UserService;
import com.finnplay.vo.ResponseResult;
import com.finnplay.vo.UserLoginRequest;
import com.finnplay.vo.UserRegisterRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @GetMapping("/{email}")
    public ResponseResult get(@PathVariable String email) {
        return ResponseResult.success(userService.findByEmail(email));
    }

    @PutMapping("/{email}")
    public ResponseResult update(@PathVariable String email, @RequestBody @Valid UserRegisterRequest request) {
        return ResponseResult.success(userService.update(email, request));
    }


    @PostMapping("/register")
    public ResponseResult register(@RequestBody @Valid UserRegisterRequest request) {
        return ResponseResult.success(userService.register(request));
    }

    @PostMapping("/login")
    public ResponseResult login(@RequestBody @Valid UserLoginRequest request) {
        return ResponseResult.success(userService.login(request));
    }

    @PostMapping("/logout")
    public ResponseResult logout(HttpServletRequest request) {
        return ResponseResult.success(userService.logout(request));
    }


}
