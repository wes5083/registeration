package com.finnplay.service;

import com.finnplay.entity.Token;
import com.finnplay.repo.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TokenService {

    @Value("${application.authentication.session-expiry-interval}")
    private Duration sessionExpiryInterval;
    @Value("${application.authentication.session-update-interval}")
    private Duration sessionUpdateInterval;

    private final TokenRepository tokenRepository;

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
        var key = KeyGenerators.secureRandom(32).generateKey();
        var token = new String(Hex.encode(key));
        var saveToken = Token.builder()
                .token(token)
                .email(userDetails.getUsername())
                .lastSeeAt(LocalDateTime.now())
                .build();
        tokenRepository.save(saveToken);
        return token;
    }

    public Token validateToken(String token) {
        if (token == null) return null;
        var dbToken = tokenRepository.findByToken(token).orElseThrow(() -> new UsernameNotFoundException("Token not found"));
        var now = LocalDateTime.now();
        var lastSeenAt = dbToken.getLastSeeAt();
        if (lastSeenAt.plus(sessionExpiryInterval).isBefore(now)) {
            return null;
        } else if (lastSeenAt.plus(sessionUpdateInterval).isBefore(now)) {
            dbToken.setLastSeeAt(lastSeenAt);
            tokenRepository.save(dbToken);
        }
        return dbToken;
    }

}
