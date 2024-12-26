package com.example.demo.controller;

import com.example.demo.modelResponse.PersonInfoResponse;
import com.example.demo.service.PersonInfoService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PersonInfoControllerTest {

    @Mock
    private PersonInfoService personInfoService;

    @InjectMocks
    private PersonInfoController personInfoController;

    public PersonInfoControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetPersonInfoByLastName() {
        String lastName = "Doe";

        List<PersonInfoResponse> mockResponse = Arrays.asList(
                new PersonInfoResponse("John", "Doe", "123 Main St", "john.doe@example.com",30,List.of("med1"),List.of("allergies1")),
                new PersonInfoResponse("Jane", "Smith", "456 Elm St", "jane.smith@example.com",35,List.of("med2"),List.of("allergies2"))
        );

        when(personInfoService.getPersonInfoByLastName(lastName)).thenReturn(mockResponse);

        List<PersonInfoResponse> response = personInfoController.getPersonInfoByLastName(lastName);

        assertNotNull(response);
        assertEquals(2, response.size());
        assertEquals("John", response.get(0).getFirstName());
        assertEquals("Jane", response.get(1).getFirstName());

        verify(personInfoService, times(1)).getPersonInfoByLastName(lastName);
    }
}
