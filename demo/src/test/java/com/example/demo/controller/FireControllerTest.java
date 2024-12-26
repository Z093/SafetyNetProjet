package com.example.demo.controller;

import com.example.demo.modelResponse.FireResponse;
import com.example.demo.modelResponse.PersonInfo;
import com.example.demo.service.FireService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.mockito.ArgumentMatchers.anyString;

import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class FireControllerTest {

    private MockMvc mockMvc;

    @Mock
    private FireService fireService;

    @InjectMocks
    private FireController fireController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(fireController).build();
    }

    @Test
    public void testGetPersonsAtAddress() throws Exception {
        // Arrange
        FireResponse fireResponse = new FireResponse("1", Arrays.asList(
                new PersonInfo("John", "Doe", "123-456-789", "john.doe@example.com",30,List.of("med1"),List.of("allergies1")),
                new PersonInfo("Jane", "Smith", "987-654-321", "jane.smith@example.com",35,List.of("med2"),List.of("allergies2"))
                ));
        when(fireService.getPersonsAtAddress(anyString())).thenReturn(fireResponse);

        // Act & Assert
        mockMvc.perform(get("/fire")
                        .param("address", "123 Test Street"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fireStationNumber").value("1"))
                .andExpect(jsonPath("$.residents[0].firstName").value("John"))
                .andExpect(jsonPath("$.residents[0].lastName").value("Doe"))
                .andExpect(jsonPath("$.residents[1].firstName").value("Jane"))
                .andExpect(jsonPath("$.residents[1].lastName").value("Smith"));
    }
}


