package com.example.demo.service;

import com.example.demo.Utils.DataLoader;
import com.example.demo.model.Person;
import com.example.demo.model.FireStation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class PhoneAlertServiceImplTest {

    @Mock
    private DataLoader dataLoader;

    @InjectMocks
    //private PhoneAlertController phoneAlertController;
    private PhoneAlertServiceImpl phoneAlertServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetPhoneNumbersByFirestation() {
        // Mock data for FireStations
        FireStation station1 = new FireStation("123 Main St", "1");
        FireStation station2 = new FireStation("456 Elm St", "2");
        FireStation station3 = new FireStation("789 Oak St", "1");

        // Mock data for Persons
        Person person1 = new Person("John", "Doe", "123 Main St", "City", "00000", "123-456-7890", "john.doe@example.com", 25, null);
        Person person2 = new Person("Jane", "Doe", "456 Elm St", "City", "00000", "234-567-8901", "jane.doe@example.com", 30, null);
        Person person3 = new Person("Jim", "Beam", "789 Oak St", "City", "00000", "345-678-9012", "bob.smith@example.com", 40, null);

        // Set up the mock responses for DataLoader
        when(dataLoader.getFireStations()).thenReturn(Arrays.asList(station1, station2, station3));
        when(dataLoader.getPersons()).thenReturn(Arrays.asList(person1, person2, person3));

        // Expected result for firestation "1"
        List<String> expectedPhoneNumbers = Arrays.asList("123-456-7890", "345-678-9012");

        // Call the method with firestation "1"
        List<String> actualPhoneNumbers = phoneAlertServiceImpl.getPhoneNumbersByFirestation("1");

        // Assert that the result matches the expected list
        assertEquals(expectedPhoneNumbers, actualPhoneNumbers);
    }

    @Test
    void testGetPhoneNumbersByFirestation_NoMatchingFirestation() {
        // Mock data for FireStations and Persons, but no matching firestation number
        when(dataLoader.getFireStations()).thenReturn(Collections.emptyList());
        when(dataLoader.getPersons()).thenReturn(Collections.emptyList());

        // Expected result for a firestation with no matches
        List<String> expectedPhoneNumbers = Collections.emptyList();

        // Call the method with a firestation number that has no matches
        List<String> actualPhoneNumbers = phoneAlertServiceImpl.getPhoneNumbersByFirestation("3");

        // Assert that the result is an empty list
        assertEquals(expectedPhoneNumbers, actualPhoneNumbers);
    }
}
