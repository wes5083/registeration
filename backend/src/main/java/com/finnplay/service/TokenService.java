package com.finnplay.service;

import com.finnplay.entity.Token;
import com.finnplay.repo.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TokenService {

    @Value("${application.authentication.session-expiry-interval}")
    private Duration sessionExpiryInterval;
    @Value("${application.authentication.session-update-interval}")
    private Duration sessionUpdateInterval;

    private final TokenRepository tokenRepository;
    private final JwtTokenService jwtTokenService;

    public void deleteToken(String token) {
        tokenRepository.delete(Token.builder()
                .token(token)
                .build());
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public String generateToken(UserDetails userDetails) {
        var token = jwtTokenService.generateToken(Map.of(), userDetails);
        var saveToken = Token.builder()
                .token(token)
                .email(userDetails.getUsername())
                .lastSeeAt(LocalDateTime.now())
                .build();
        tokenRepository.save(saveToken);
        return token;
    }

    public Token validateToken(String token) {
        return Optional.ofNullable(token)
                .map(t -> tokenRepository.findByToken(t).orElse(null))
                .map(dbToken -> {
                    var now = LocalDateTime.now();
                    var lastSeenAt = dbToken.getLastSeeAt();
                    if (lastSeenAt.plus(sessionExpiryInterval).isBefore(now)) {
                        return null;
                    } else if (lastSeenAt.plus(sessionUpdateInterval).isBefore(now)) {
                        dbToken.setLastSeeAt(now);
                        tokenRepository.save(dbToken);
                    }
                    return dbToken;
                })
                .orElse(null);
    }


}
