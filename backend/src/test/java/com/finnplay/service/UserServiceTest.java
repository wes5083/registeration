package com.finnplay.service;

import com.finnplay.exception.BussinessException;
import com.finnplay.repo.UserRepository;
import com.finnplay.vo.UserLoginRequest;
import com.finnplay.vo.UserRegisterRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Order;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.TestCase.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @Before
    public void setUp() {
        var request = buildRegister();

        var exist = true;
        try {
            userService.findByEmail(request.getEmail());
        } catch (Exception e) {
            exist = false;
        }

        if (!exist) {
            userService.register(request);
        }
    }


    @Test
    @Order(1)
    public void testRegister() {
        var request = buildRegister();
        var result = userService.findByEmail(request.getEmail());
        assertNotNull(result);
        assertEquals("wes.zhang@163.com", result.getEmail());
    }

    @Test
    @Order(2)
    public void testRegisterEmailExists() {
        var request = buildRegister();
        var second = new UserRegisterRequest();
        second.setEmail(request.getEmail());
        second.setPassword(request.getPassword());
        assertThrows(BussinessException.class, () -> userService.register(second));
    }

    @Test
    @Order(3)
    public void testLogin() {
        var request = buildRegister();

        var login = new UserLoginRequest();
        login.setEmail(request.getEmail());
        login.setPassword(request.getPassword());
        var result = userService.login(login);

        assertNotNull(result);
        assertNotNull(result.getToken());
    }

    @Test
    @Order(4)
    public void testFindByEmail() {
        var request = buildRegister();

        var result = userService.findByEmail(request.getEmail());
        assertNotNull(result);
    }

    @Test
    @Order(5)
    public void testFindByEmailNotFound() {
        var email = "nonexistent@163.com";
        assertThrows(BussinessException.class, () -> userService.findByEmail(email));
    }

    @Test
    @Order(6)
    public void testUpdate() {
        var request = buildRegister();
        var email = request.getEmail();
        var oldUser = userService.findByEmail(email);
        assertEquals("wes", oldUser.getFirstName());
        assertEquals("zhang", oldUser.getLastName());

        request.setFirstName("wes1");
        request.setLastName("zhang1");
        var newUser = userService.update(email, request);

        assertNotNull(newUser);
        assertEquals("wes1", newUser.getFirstName());
        assertEquals("zhang1", newUser.getLastName());
    }

    @Test
    @Order(7)
    public void testLogout() {
        try{
            TimeUnit.SECONDS.sleep(5);
        }catch (InterruptedException e){}
        var register = buildRegister();

        var login = new UserLoginRequest();
        login.setEmail(register.getEmail());
        login.setPassword(register.getPassword());
        var token = userService.login(login).getToken();

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + token);

        boolean result = userService.logout(request);
        assertTrue(result);
        assertNull(SecurityContextHolder.getContext().getAuthentication());

    }

    private UserRegisterRequest buildRegister() {
        UserRegisterRequest request = new UserRegisterRequest();
        request.setEmail("wes.zhang@163.com");
        request.setPassword("123456");
        request.setFirstName("wes");
        request.setLastName("zhang");
        return request;
    }
}


