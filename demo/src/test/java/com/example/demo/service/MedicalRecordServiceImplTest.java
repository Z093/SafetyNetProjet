package com.example.demo.service;

import com.example.demo.Utils.DataLoader;
import com.example.demo.model.MedicalRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MedicalRecordServiceImplTest {

    @InjectMocks
    private MedicalRecordServiceImpl medicalRecordService;

    @Mock
    private DataLoader dataLoader;

    private List<MedicalRecord> medicalRecords;

    @BeforeEach
    void setUp() {
        medicalRecords = new ArrayList<>();
        when(dataLoader.getMedicalRecords()).thenReturn(medicalRecords);
    }

    @Test
    void testAddMedicalRecord_Success() {
        MedicalRecord newRecord = new MedicalRecord("John", "Doe", "01/01/1990", List.of("med1"), List.of("allergy1"));

        boolean response = medicalRecordService.addMedicalRecord(newRecord);

        assertTrue(response);
        //assertEquals("Medical record added successfully", response.getBody());
        //assertTrue(medicalRecords.contains(newRecord));
    }

    @Test
    void testAddMedicalRecord_AlreadyExists() {
        MedicalRecord existingRecord = new MedicalRecord("Jane", "Doe", "02/02/1985", List.of("med2"), List.of("allergy2"));
        medicalRecords.add(existingRecord);

       boolean response = medicalRecordService.addMedicalRecord(existingRecord);

        assertFalse(response);
        //assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        //assertEquals("Medical record already exists for this person", response.getBody());
    }

    @Test
    void testUpdateMedicalRecord_Success() {
        MedicalRecord existingRecord = new MedicalRecord("Jane", "Doe", "02/02/1985", List.of("med2"), List.of("allergy2"));
        medicalRecords.add(existingRecord);

        MedicalRecord updatedRecord = new MedicalRecord("Jane", "Doe", "02/02/1985", List.of("newMed"), List.of("newAllergy"));

        boolean response = medicalRecordService.updateMedicalRecord(updatedRecord);

        assertTrue(response);
        //assertEquals(HttpStatus.OK, response.getStatusCode());
        //assertEquals("Medical record updated successfully", response.getBody());
        assertEquals(updatedRecord.getMedications(), existingRecord.getMedications());
        assertEquals(updatedRecord.getAllergies(), existingRecord.getAllergies());
    }

    @Test
    void testUpdateMedicalRecord_NotFound() {
        MedicalRecord updatedRecord = new MedicalRecord("NonExistent", "Person", "01/01/2000", List.of("med3"), List.of("allergy3"));

        boolean response = medicalRecordService.updateMedicalRecord(updatedRecord);

        assertFalse(response);

    }

    @Test
    void testDeleteMedicalRecord_Success() {
        MedicalRecord existingRecord = new MedicalRecord("John", "Smith", "03/03/1995", List.of("med4"), List.of("allergy4"));
        medicalRecords.add(existingRecord);

        boolean response = medicalRecordService.deleteMedicalRecord("John", "Smith");

        assertTrue(response);
        assertFalse(medicalRecords.contains(existingRecord));
    }

    @Test
    void testDeleteMedicalRecord_NotFound() {
        boolean response = medicalRecordService.deleteMedicalRecord("NonExistent", "Person");

        assertFalse(response);
    }
}
