package com.example.demo.controller;

import com.example.demo.model.MedicalRecord;
import com.example.demo.service.MedicalRecordService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.ResponseEntity.*;

class MedicalRecordControllerTest {

    @Mock
    private MedicalRecordService medicalRecordService;

    @InjectMocks
    private MedicalRecordController medicalRecordController;

    public MedicalRecordControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddMedicalRecord() {
        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setFirstName("John");
        medicalRecord.setLastName("Doe");

        when(medicalRecordService.addMedicalRecord(medicalRecord)).thenReturn(ok("Medical record added successfully").hasBody());

        ResponseEntity<String> response = medicalRecordController.addMedicalRecord(medicalRecord);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Medical record added successfully", response.getBody());
        verify(medicalRecordService, times(1)).addMedicalRecord(medicalRecord);
    }

    @Test
    void testUpdateMedicalRecord() {
        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setFirstName("Jane");
        medicalRecord.setLastName("Doe");

        when(medicalRecordService.updateMedicalRecord(medicalRecord)).thenReturn(ok("Medical record updated successfully").hasBody());

        ResponseEntity<String> response = medicalRecordController.updateMedicalRecord(medicalRecord);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Medical record updated successfully", response.getBody());
        verify(medicalRecordService, times(1)).updateMedicalRecord(medicalRecord);
    }

    @Test
    void testDeleteMedicalRecord() {
        String firstName = "John";
        String lastName = "Doe";

        when(medicalRecordService.deleteMedicalRecord(firstName, lastName)).thenReturn(ok("Medical record deleted successfully").hasBody());

        ResponseEntity<String> response = medicalRecordController.deleteMedicalRecord(firstName, lastName);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Medical record deleted successfully", response.getBody());
        verify(medicalRecordService, times(1)).deleteMedicalRecord(firstName, lastName);
    }
}
