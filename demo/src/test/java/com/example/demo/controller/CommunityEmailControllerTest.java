package com.example.demo.controller;

import com.example.demo.service.CommunityEmailService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CommunityEmailControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CommunityEmailService communityEmailService;

    @InjectMocks
    private CommunityEmailController communityEmailController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(communityEmailController).build();
    }

    @Test
    void testGetEmailsByCity() throws Exception {
        // Given
        String city = "123 Main St";
        List<String> emails = Arrays.asList("john.doe@example.com", "jane.doe@example.com");
        when(communityEmailService.getEmailsByCity(city)).thenReturn(emails);

        // When & Then
        mockMvc.perform(get("/communityEmail")
                        .param("city", city)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[\"john.doe@example.com\", \"jane.doe@example.com\"]"));
    }
}
