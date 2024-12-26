package com.example.demo.controller;

import com.example.demo.service.PhoneAlertService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PhoneAlertControllerTest {

    @Mock
    private PhoneAlertService phoneAlertService;

    @InjectMocks
    private PhoneAlertController phoneAlertController;

    public PhoneAlertControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetPhoneNumbersByFirestation() {
        String firestation = "1";

        List<String> mockPhoneNumbers = Arrays.asList("555-1234", "555-5678");

        when(phoneAlertService.getPhoneNumbersByFirestation(firestation)).thenReturn(mockPhoneNumbers);

        List<String> response = phoneAlertController.getPhoneNumbersByFirestation(firestation);

        assertNotNull(response);
        assertEquals(2, response.size());
        assertEquals("555-1234", response.get(0));
        assertEquals("555-5678", response.get(1));

        verify(phoneAlertService, times(1)).getPhoneNumbersByFirestation(firestation);
    }
}

