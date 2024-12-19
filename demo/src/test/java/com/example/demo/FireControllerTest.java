package com.example.demo;

import com.example.demo.controller.FireController;
import com.example.demo.model.MedicalRecord;
import com.example.demo.model.Person;
import com.example.demo.model.FireStation;
import com.example.demo.modelResponse.FireResponse;
import com.example.demo.modelResponse.PersonInfo;
import com.example.demo.Utils.DataLoader;
import com.example.demo.service.FireServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class FireControllerTest {

    @Mock
    private DataLoader dataLoader;

    @InjectMocks
    private FireServiceImpl fireServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetPersonsAtAddress() {
        // Mock data
        String address = "123 Main St";

        // Mock fire station data
        FireStation fireStation = new FireStation(address, "1");
        when(dataLoader.getFireStations()).thenReturn(List.of(fireStation));


        // Mock medical record data
        MedicalRecord medicalRecord = new MedicalRecord("John", "Doe", "01/01/1980", List.of("aspirin"), List.of("peanuts"));
        when(dataLoader.getMedicalRecords()).thenReturn(List.of(medicalRecord));

        // Mock person data
        Person person = new Person("John", "Doe", "123 Main St", "City", "00000", "123-456-7890", "john.doe@example.com", 44, medicalRecord);
        when(dataLoader.getPersons()).thenReturn(List.of(person));

        // Call the controller method
        FireResponse response = fireServiceImpl.getPersonsAtAddress(address);

        // Validate the response
        assertEquals("1", response.getFireStationNumber());
        assertEquals(1, response.getResidents().size());

        // Validate the details of the resident
        PersonInfo residentInfo = response.getResidents().get(0);
        assertEquals("John", residentInfo.getFirstName());
        assertEquals("Doe", residentInfo.getLastName());
        assertEquals("123-456-7890", residentInfo.getPhone());
        assertEquals("john.doe@example.com", residentInfo.getEmail());
        assertEquals(44, residentInfo.getAge());  // Adjust the age based on current date and birthdate.
        assertEquals(List.of("aspirin"), residentInfo.getMedications());
        assertEquals(List.of("peanuts"), residentInfo.getAllergies());
    }
}

