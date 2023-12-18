package com.finnplay.service;

import com.finnplay.entity.User;
import com.finnplay.exception.BussinessException;
import com.finnplay.repo.UserRepository;
import com.finnplay.vo.AccessToken;
import com.finnplay.vo.UserLoginRequest;
import com.finnplay.vo.UserRegisterRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final UserAuthenticateService userAuthenticateService;

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new BussinessException("email not exist"));
    }

    public User update(String email, UserRegisterRequest request) {
        var userDb = findByEmail(email);
        var password = request.getPassword() != null ? passwordEncoder.encode(request.getPassword()) : userDb.getPassword();
        var user = User.builder()
                .id(userDb.getId())
                .email(email)
                .password(password)
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .birthDay(request.getBirthDay())
                .build();
        return userRepository.save(user);
    }

    public User register(UserRegisterRequest request) {
        userRepository.findByEmail(request.getEmail())
                .ifPresent(dbUser -> {
                    throw new BussinessException("email already exist");
                });

        return userRepository.save(User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .birthDay(request.getBirthDay())
                .build());
    }

    public AccessToken login(UserLoginRequest request) {
        User user = userAuthenticateService.authenticateUser(request.getEmail(), request.getPassword());
        String token = tokenService.generateToken(user);
        return AccessToken.builder().token(token).build();
    }

    public boolean logout(HttpServletRequest request) {
        String token = tokenService.resolveToken(request);
        if (token != null) {
            tokenService.deleteToken(token);
            SecurityContextHolder.getContext().setAuthentication(null);
        }
        return true;
    }

}
