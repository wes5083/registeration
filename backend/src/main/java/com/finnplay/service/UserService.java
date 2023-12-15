package com.finnplay.service;

import com.finnplay.entity.User;
import com.finnplay.repo.UserRepository;
import com.finnplay.vo.AccessTokenResponse;
import com.finnplay.vo.UserLoginRequest;
import com.finnplay.vo.UserRegisterRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
    private final AuthenticationManager authenticationManager;

    public void update(String email, UserRegisterRequest request) {
        var userDb = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        var password = request.getPassword() != null ? passwordEncoder.encode(request.getPassword()) : userDb.getPassword();
        var user = User.builder()
                .id(userDb.getId())
                .email(email)
                .password(password)
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .birthDay(request.getBirthDay())
                .build();
        userRepository.save(user);
    }

    public void register(UserRegisterRequest request) {
        var dbUser = userRepository.findByEmail(request.getEmail());
        if (dbUser.isPresent()) {
            throw new RuntimeException("email already exist!");
        }
        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .birthDay(request.getBirthDay())
                .build();
        userRepository.save(user);
    }

    public AccessTokenResponse login(UserLoginRequest request) {
        var authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        var token = tokenService.generateToken((User) authentication.getPrincipal());

        return AccessTokenResponse.builder().token(token).build();
    }


    public void logout(HttpServletRequest request) {
        String token = tokenService.resolveToken(request);
        if (token != null) {
            tokenService.deleteToken(token);
            SecurityContextHolder.getContext().setAuthentication(null);
        }
    }


}
