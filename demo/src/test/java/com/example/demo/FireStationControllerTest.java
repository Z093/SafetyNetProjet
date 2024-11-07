package com.example.demo;

import com.example.demo.controller.FireStationController;
import com.example.demo.model.MedicalRecord;
import com.example.demo.model.Person;
import com.example.demo.modelResponse.FireStationResponse;
import com.example.demo.model.FireStation;
import com.example.demo.service.DataLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;


public class FireStationControllerTest {

    @Mock
    private DataLoader dataLoader;

    @InjectMocks
    private FireStationController fireStationController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetPersonsByStation_withPersons() {
        // Arrange: Mock data for fire stations and persons with matching addresses.
        FireStation fireStation = new FireStation("123 Main St", "1");
        MedicalRecord medicalRecord = new MedicalRecord("John", "Doe", "05/20/2000", List.of("med1"), List.of("allergy1"));
        Person person = new Person("John", "Doe", "123 Main St", "City", "00000", "123-456-7890", "john.doe@example.com", 25, medicalRecord);

        // Mock the dataLoader to return this data.
        when(dataLoader.getFireStations()).thenReturn(Collections.singletonList(fireStation));
        when(dataLoader.getPersons()).thenReturn(Collections.singletonList(person));
        when(dataLoader.getMedicalRecords()).thenReturn(Collections.singletonList(medicalRecord));

        // Act: Call the controller method.
        FireStationResponse response = fireStationController.getPersonsByStation("1");

        // Assert: Verify the result.
        assertEquals(1, response.getPersons().size(), "Expected one person associated with the fire station.");
        assertEquals(1, response.getNumberOfAdults(), "Expected one adult.");
        assertEquals(0, response.getNumberOfChildren(), "Expected zero children.");
    }

    @Test
    public void testGetPersonsByStation_noPersons() {
        // Mock data with no persons for the station
        FireStation fireStation = new FireStation("1234 Elm St", "1");

        when(dataLoader.getFireStations()).thenReturn(Collections.singletonList(fireStation));
        when(dataLoader.getPersons()).thenReturn(Collections.emptyList());

        // Call the controller method
        FireStationResponse response = fireStationController.getPersonsByStation("1");

        // Verify the result
        assertEquals(0, response.getPersons().size());
        assertEquals(0, response.getNumberOfAdults());
        assertEquals(0, response.getNumberOfChildren());
    }

    @Test
    public void testAddFireStationMapping_success() {
        FireStation newMapping = new FireStation("5678 Oak St", "2");

        when(dataLoader.getFireStations()).thenReturn(new ArrayList<>());

        // Call the controller method
        ResponseEntity<String> response = fireStationController.addFireStationMapping(newMapping);

        // Verify the result
        List<FireStation> fireStations = new ArrayList<>(dataLoader.getFireStations());
        fireStations.add(newMapping);
    }

    @Test
    public void testAddFireStationMapping_conflict() {
        FireStation existingMapping = new FireStation("5678 Oak St", "2");
        FireStation newMapping = new FireStation("5678 Oak St", "3");

        when(dataLoader.getFireStations()).thenReturn(Collections.singletonList(existingMapping));

        // Call the controller method
        ResponseEntity<String> response = fireStationController.addFireStationMapping(newMapping);

        // Verify the result
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Mapping already exists for this address", response.getBody());
    }

    @Test
    public void testUpdateFireStationMapping_success() {
        FireStation existingMapping = new FireStation("5678 Oak St", "2");
        FireStation updatedMapping = new FireStation("5678 Oak St", "3");

        when(dataLoader.getFireStations()).thenReturn(Collections.singletonList(existingMapping));

        // Call the controller method
        ResponseEntity<String> response = fireStationController.updateFireStationMapping(updatedMapping);

        // Verify the result
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Fire station mapping updated successfully", response.getBody());
    }

    @Test
    public void testUpdateFireStationMapping_notFound() {
        FireStation updatedMapping = new FireStation("5678 Oak St", "3");

        when(dataLoader.getFireStations()).thenReturn(Collections.emptyList());

        // Call the controller method
        ResponseEntity<String> response = fireStationController.updateFireStationMapping(updatedMapping);

        // Verify the result
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Mapping not found for this address", response.getBody());
    }

    @Test
    public void testDeleteFireStationMapping_success() {
        // Arrange: Create the existing mapping and add it to the mocked data loader's list.
        FireStation existingMapping = new FireStation("5678 Oak St", "2");
        List<FireStation> fireStations = new ArrayList<>();
        fireStations.add(existingMapping);

        // Configure the dataLoader mock to return this list.
        when(dataLoader.getFireStations()).thenReturn(fireStations);

        // Act: Call the controller method to delete the fire station mapping.
        ResponseEntity<String> response = fireStationController.deleteFireStationMapping("5678 Oak St");

        // Assert: Verify the expected response and that the mapping is deleted.
        assertEquals("Fire station mapping deleted successfully", response.getBody());

        // Optional: Verify the dataLoader's list is now empty (assuming deleteFireStationMapping modifies it).
        assertTrue(dataLoader.getFireStations().isEmpty());
    }

    @Test
    public void testDeleteFireStationMapping_notFound() {
        when(dataLoader.getFireStations()).thenReturn(Collections.emptyList());

        // Call the controller method
        ResponseEntity<String> response = fireStationController.deleteFireStationMapping("5678 Oak St");

        // Verify the result
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Mapping not found for this address", response.getBody());
    }
}
