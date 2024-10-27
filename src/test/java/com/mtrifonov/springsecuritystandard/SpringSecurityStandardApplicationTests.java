package com.mtrifonov.springsecuritystandard;


import java.util.Map;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
class SpringSecurityStandardApplicationTests {
    
    private final RestTemplate restTemplate;

    @Autowired
    public SpringSecurityStandardApplicationTests(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
      
    @Test
    void contextLoads() {
    }
    
    /*@Test
    void registrationSuccessTest() {
        Map<String, Object> data = Map.of("username", "h.simpson", "password", "springfield", "roles", new String[]{"USER"});
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(data);
        ResponseEntity<Void> response = restTemplate.postForEntity("http://localhost:8081/registration", request, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
    }*/

}
