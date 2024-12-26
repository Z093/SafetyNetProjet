package com.example.demo.controller;

import com.example.demo.modelResponse.FireStationResponse;
import com.example.demo.modelResponse.PersonResponse;
import com.example.demo.service.FireStationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FireStationController.class)
class FireStationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FireStationService fireStationService;

    private FireStationResponse fireStationResponse;

    @BeforeEach
    void setUp() {
        // Mock sample PersonResponse objects
        List<PersonResponse> persons = Arrays.asList(
                new PersonResponse("John", "Doe", "123 Main St", "111-111-1111"),
                new PersonResponse("Jane", "Doe", "123 Main St", "222-222-2222")
        );

        // Create a sample FireStationResponse
        fireStationResponse = new FireStationResponse(persons, 1, 1);
    }

    @Test
    void testGetPersonsByStation() throws Exception {
        // Mock the behavior of fireStationService
        when(fireStationService.getPersonsByStation("1")).thenReturn(fireStationResponse);

        // Perform GET request and verify the response
        mockMvc.perform(get("/firestation")
                        .param("stationNumber", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numberOfAdults").value(1))
                .andExpect(jsonPath("$.numberOfChildren").value(1))
                .andExpect(jsonPath("$.persons[0].firstName").value("John"))
                .andExpect(jsonPath("$.persons[0].lastName").value("Doe"))
                .andExpect(jsonPath("$.persons[1].firstName").value("Jane"))
                .andExpect(jsonPath("$.persons[1].lastName").value("Doe"));
    }

}
