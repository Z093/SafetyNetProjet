package com.example.demo.service;

import com.example.demo.model.MedicalRecord;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface MedicalRecordService {
    ResponseEntity<String> addMedicalRecord(MedicalRecord newMedicalRecord);
    ResponseEntity<String> updateMedicalRecord(MedicalRecord updatedMedicalRecord);
    ResponseEntity<String> deleteMedicalRecord(String firstName, String lastName);

}
