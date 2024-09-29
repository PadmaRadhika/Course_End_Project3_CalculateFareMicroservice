package com.example.CalculateFareMicroService;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;

import com.example.CalculateFareMicroService.service.CalculateFareService;

@SpringBootTest
class CalculateFareMicroServiceApplicationTests {
	
	@Autowired
    private ApplicationContext applicationContext;
	 @MockBean
	    private CalculateFareService calculateFareService;
	@Test
	void contextLoads() {
        // Check if the context loads successfully
        assertThat(applicationContext).isNotNull();
    }

}
