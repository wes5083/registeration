package com.finnplay.service;

import com.finnplay.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.TestCase.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JwtTokenServiceTest {

    @Autowired
    private JwtTokenService jwtTokenService;

    @Test
    public void testGenerateToken() {
        UserDetails userDetails = User.builder().email("wes.zhang@163.com").password("123456").build();
        Map<String, Object> extraClaims = new HashMap<>();
        String token = jwtTokenService.generateToken(extraClaims, userDetails);
        assertNotNull(token);
    }

    @Test
    public void testIsTokenValid() {
        UserDetails userDetails = User.builder().email("wes.zhang@163.com").password("123456").build();
        Map<String, Object> extraClaims = new HashMap<>();
        String token = jwtTokenService.generateToken(extraClaims, userDetails);
        assertTrue(jwtTokenService.isTokenValid(token, userDetails.getUsername()));
    }

}
