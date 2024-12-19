package com.example.demo;

import com.example.demo.controller.FloodController;
import com.example.demo.model.MedicalRecord;
import com.example.demo.model.Person;
import com.example.demo.model.FireStation;
import com.example.demo.modelResponse.FloodResponse;
import com.example.demo.Utils.DataLoader;
import com.example.demo.service.FloodServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class FloodControllerTest {

    @Mock
    private DataLoader dataLoader;

    @InjectMocks
    //private FloodController floodController;
    private FloodServiceImpl floodServiceImpl;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetHouseholdsByStations() {
        // Mock fire station and address associations
        FireStation station1 = new FireStation("123 Main St","1");
        FireStation station2 = new FireStation("789 Pine St","2");

        when(dataLoader.getFireStations()).thenReturn(List.of(station1, station2));

        // Mock medical records
        MedicalRecord record1 = new MedicalRecord("John", "Doe", "01/01/1980", List.of("med1"), List.of("allergy1"));
        MedicalRecord record2 = new MedicalRecord("Jane", "Doe", "02/02/1990", List.of("med2"), List.of("allergy2"));
        MedicalRecord record3 = new MedicalRecord("Bob", "Smith", "03/03/1975", List.of("med3"), List.of("allergy3"));

        when(dataLoader.getMedicalRecords()).thenReturn(List.of(record1, record2, record3));

        // Mock persons data
        Person person1 = new Person("John", "Doe", "123 Main St", "City", "00000", "123-456-7890", "john.doe@example.com", 25, record1);
        Person person2 = new Person("Jane", "Doe", "123 Main St", "City", "00000", "987-654-3210", "jane.doe@example.com", 30, record2);
        Person person3 = new Person("Bob", "Smith", "789 Pine St", "City", "00000", "111-222-3333", "bob.smith@example.com", 40, record3);

        when(dataLoader.getPersons()).thenReturn(List.of(person1, person2, person3));

        // Call the method with specific station IDs
        List<String> stations = List.of("1", "2");
        Map<String, List<FloodResponse>> result = floodServiceImpl.getHouseholdsByStations(stations);

        // Validate the result
        assertEquals(2, result.size()); // Expecting 2 addresses
        assertEquals(2, result.get("123 Main St").size()); // 2 people at "123 Main St"
        assertEquals(1, result.get("789 Pine St").size()); // 1 person at "789 Pine St"

        // Verify details of one entry
        FloodResponse johnResponse = result.get("123 Main St").get(0);
        assertEquals("John", johnResponse.getFirstName());
        assertEquals("Doe", johnResponse.getLastName());
        assertEquals("123-456-7890", johnResponse.getPhone());
        assertEquals(44, johnResponse.getAge()); // Adjust this based on current date and mock birthdate
        assertEquals(List.of("med1"), johnResponse.getMedications());
        assertEquals(List.of("allergy1"), johnResponse.getAllergies());
    }
}
