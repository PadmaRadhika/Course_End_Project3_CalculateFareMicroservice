package com.example.CalculateFareMicroService.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.CalculateFareMicroService.service.CalculateFareService;

public class CalculateFareControllerTest {
	private MockMvc mockMvc;

    @Mock
    private CalculateFareService calculateFareService;

    @InjectMocks
    private CalculateFareController calculateFareController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(calculateFareController).build();
    }

    @Test
    void testCalculateFare() throws Exception {
        // Arrange
        Map<String, String> distanceMap = new HashMap<>();
        distanceMap.put("distance", "10.0"); // Assume 10.0 km
        distanceMap.put("duration", "15 minutes");

        when(calculateFareService.calculateDistance("San Ramon", "San Francisco"))
                .thenReturn(distanceMap);
        when(calculateFareService.calculateEconomyCabFare(10.0)).thenReturn(100.0);

        // Act & Assert
        mockMvc.perform(get("/calculateFare")
                        .param("fromLocation", "San Ramon")
                        .param("toLocation", "San Francisco")
                        .param("cabType", "Economy"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.distance", is("10.0")))
                .andExpect(jsonPath("$.duration", is("15 minutes")))
                .andExpect(jsonPath("$.fare", is("100.0")));

        verify(calculateFareService, times(1)).calculateDistance("San Ramon", "San Francisco");
        verify(calculateFareService, times(1)).calculateEconomyCabFare(10.0);
    }

    @Test
    void testCalculateFareForPremiumCab() throws Exception {
        // Arrange
        Map<String, String> distanceMap = new HashMap<>();
        distanceMap.put("distance", "20.0"); // Assume 20.0 km
        distanceMap.put("duration", "30 minutes");

        when(calculateFareService.calculateDistance("Oakland", "San Jose"))
                .thenReturn(distanceMap);
        when(calculateFareService.calculatePremiumCabFare(20.0)).thenReturn(200.0);

        // Act & Assert
        mockMvc.perform(get("/calculateFare")
                        .param("fromLocation", "Oakland")
                        .param("toLocation", "San Jose")
                        .param("cabType", "Premium"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.distance", is("20.0")))
                .andExpect(jsonPath("$.duration", is("30 minutes")))
                .andExpect(jsonPath("$.fare", is("200.0")));

        verify(calculateFareService, times(1)).calculateDistance("Oakland", "San Jose");
        verify(calculateFareService, times(1)).calculatePremiumCabFare(20.0);
    }

    @Test
    void testDisplayAllCabsFare() throws Exception {
        // Arrange
        Map<String, String> distanceMap = new HashMap<>();
        distanceMap.put("distance", "15.0"); // Assume 15.0 km
        distanceMap.put("duration", "25 minutes");

        when(calculateFareService.calculateDistance("Milpitas", "Fremont"))
                .thenReturn(distanceMap);
        when(calculateFareService.calculateEconomyCabFare(15.0)).thenReturn(150.0);
        when(calculateFareService.calculatePremiumCabFare(15.0)).thenReturn(250.0);
        when(calculateFareService.calculateLuxuryCabFare(15.0)).thenReturn(350.0);

        // Act & Assert
        mockMvc.perform(post("/displayFare")
                        .param("fromLocation", "Milpitas")
                        .param("toLocation", "Fremont"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.distance", is("15.0")))
                .andExpect(jsonPath("$.Economy", is("150.0")))
                .andExpect(jsonPath("$.Premium", is("250.0")))
                .andExpect(jsonPath("$.Luxury", is("350.0")));

        verify(calculateFareService, times(1)).calculateDistance("Milpitas", "Fremont");
        verify(calculateFareService, times(1)).calculateEconomyCabFare(15.0);
        verify(calculateFareService, times(1)).calculatePremiumCabFare(15.0);
        verify(calculateFareService, times(1)).calculateLuxuryCabFare(15.0);
    }
}
