package com.example.demo.controller;

import com.example.demo.modelResponse.FloodResponse;
import com.example.demo.service.FloodService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class FloodControllerTest {

    private MockMvc mockMvc;

    @Mock
    private FloodService floodService;

    @InjectMocks
    private FloodController floodController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(floodController).build();
    }

    @Test
    void testGetHouseholdsByStations() throws Exception {
        // Arrange
        List<String> stations = List.of("1", "2");
        FloodResponse floodResponse1 = new FloodResponse("John", "Doe", "123-456-7890", 30, List.of("med1"),List.of("allergies1"));
        FloodResponse floodResponse2 = new FloodResponse("Jane", "Smith", "987-654-3210",35, List.of("med2"), List.of("allergies2"));

        Map<String, List<FloodResponse>> mockResponse = Map.of(
                "1", List.of(floodResponse1),
                "2", List.of(floodResponse2)
        );

        when(floodService.getHouseholdsByStations(stations)).thenReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(get("/flood/stations")
                        .param("stations", "1", "2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$['1'][0].firstName").value("John"))
                .andExpect(jsonPath("$['1'][0].lastName").value("Doe"))
                .andExpect(jsonPath("$['1'][0].phone").value("123-456-7890"))
                .andExpect(jsonPath("$['1'][0].age").value(30))
                .andExpect(jsonPath("$['1'][0].medications[0]").value("med1"))
                .andExpect(jsonPath("$['1'][0].allergies[0]").value("allergies1"))
                .andExpect(jsonPath("$['2'][0].firstName").value("Jane"))
                .andExpect(jsonPath("$['2'][0].lastName").value("Smith"))
                .andExpect(jsonPath("$['2'][0].phone").value("987-654-3210"))
                .andExpect(jsonPath("$['2'][0].age").value(35))
                .andExpect(jsonPath("$['2'][0].medications[0]").value("med2"))
                .andExpect(jsonPath("$['2'][0].allergies[0]").value("allergies2"));

        verify(floodService, times(1)).getHouseholdsByStations(stations);
    }
}
