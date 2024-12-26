package com.example.demo.controller;

import com.example.demo.modelResponse.ChildAlertResponse;
import com.example.demo.service.ChildAlertService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ChildAlertControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ChildAlertService childAlertService;

    @InjectMocks
    private ChildAlertController childAlertController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(childAlertController).build();
    }

    @Test
    void testGetChildrenAtAddress() throws Exception {
        // Given
        String address = "123 Main St";
        ChildAlertResponse response1 = new ChildAlertResponse(
                "John", "Doe", 10, Arrays.asList("Parent1 Doe", "Parent2 Doe")
        );
        ChildAlertResponse response2 = new ChildAlertResponse(
                "Jane", "Doe", 8, Arrays.asList("Parent1 Doe", "Parent2 Doe")
        );
        List<ChildAlertResponse> mockResponses = Arrays.asList(response1, response2);

        // Mock the service to return the correct type
        when(childAlertService.getChildrenAtAddress(address)).thenReturn(mockResponses);

        // When & Then
        mockMvc.perform(get("/childAlert")
                        .param("address", address)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].lastName").value("Doe"))
                .andExpect(jsonPath("$[0].age").value(10))
                .andExpect(jsonPath("$[1].firstName").value("Jane"))
                .andExpect(jsonPath("$[1].lastName").value("Doe"))
                .andExpect(jsonPath("$[1].age").value(8));
    }
}

