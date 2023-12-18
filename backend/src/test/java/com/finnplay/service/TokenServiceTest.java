package com.finnplay.service;

import com.finnplay.entity.Token;
import com.finnplay.repo.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.Optional;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TokenServiceTest {

    @Autowired
    private TokenService tokenService;

    @MockBean
    private TokenRepository tokenRepository;

    @MockBean
    private JwtTokenService jwtTokenService;

    @Test
    public void testDeleteToken() {
        String token = "exampleToken";
        Token tokenEntity = Token.builder().token(token).build();
        when(tokenRepository.findByToken(token)).thenReturn(Optional.of(tokenEntity));
        tokenService.deleteToken(token);
        verify(tokenRepository).delete(tokenEntity);
    }

    @Test
    public void testResolveToken() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer exampleToken");
        String resolvedToken = tokenService.resolveToken(request);
        assertEquals("exampleToken", resolvedToken);
    }

    @Test
    public void testGenerateToken() {
        UserDetails userDetails = mock(UserDetails.class);
        when(jwtTokenService.generateToken(any(), eq(userDetails))).thenReturn("generatedToken");
        String generatedToken = tokenService.generateToken(userDetails);
        verify(tokenRepository).save(any(Token.class));
        assertEquals("generatedToken", generatedToken);
    }

    @Test
    public void testValidateToken() {
        String token = "exampleToken";
        Token tokenEntity = Token.builder().token(token).lastSeeAt(LocalDateTime.now()).build();
        when(tokenRepository.findByToken(token)).thenReturn(Optional.of(tokenEntity));

        Token validatedToken = tokenService.validateToken(token);
        assertNotNull(validatedToken);
    }
}
