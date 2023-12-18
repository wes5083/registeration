package com.finnplay.controller;

import com.finnplay.vo.ResponseResult;
import com.finnplay.vo.UserLoginRequest;
import com.finnplay.vo.UserRegisterRequest;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    private String baseUrl;

    @Before
    public void setUp() {
        this.baseUrl = "http://localhost:8088/api/users";
    }

    @Test
    @Ignore
    public void testRegister() {
        UserRegisterRequest request = new UserRegisterRequest();
        request.setEmail("wes.zhang@163.com");
        request.setPassword("123456");

        ResponseEntity<ResponseResult> responseEntity = testRestTemplate.postForEntity(baseUrl + "/register", request, ResponseResult.class);
        System.out.println(responseEntity.getBody());


        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        assertThat(responseEntity.getBody().getData()).isNotNull();
    }

    @Test
    @Ignore
    public void testLogin() {
        UserLoginRequest request = new UserLoginRequest();
        request.setEmail("wes.zhang@163.com");
        request.setPassword("123456");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<UserLoginRequest> requestEntity = new HttpEntity<>(request, headers);

        ResponseEntity<ResponseResult> responseEntity = testRestTemplate.postForEntity(baseUrl + "/login", requestEntity, ResponseResult.class);

        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        System.out.println(responseEntity.getBody());
        assertThat(responseEntity.getBody()).isNotNull();
    }

}
