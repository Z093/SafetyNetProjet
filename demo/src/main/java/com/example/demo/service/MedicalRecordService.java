package com.example.demo.service;

import com.example.demo.model.MedicalRecord;


public interface MedicalRecordService {
    boolean addMedicalRecord(MedicalRecord newMedicalRecord);
    boolean updateMedicalRecord(MedicalRecord updatedMedicalRecord);
    boolean deleteMedicalRecord(String firstName, String lastName);

}
