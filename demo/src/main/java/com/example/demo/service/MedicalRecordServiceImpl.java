package com.example.demo.service;

import com.example.demo.Utils.DataLoader;
import com.example.demo.model.MedicalRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MedicalRecordServiceImpl implements MedicalRecordService {

    private static final Logger logger = LoggerFactory.getLogger(MedicalRecordService.class);
    @Autowired
    private DataLoader dataLoader;

    @Override
    public boolean addMedicalRecord(MedicalRecord newMedicalRecord) {
        logger.info("Attempting to add a new medical record for {} {}", newMedicalRecord.getFirstName(), newMedicalRecord.getLastName());

        // Vérifier si un dossier médical existe déjà pour cette personne
        Optional<MedicalRecord> existingRecord = dataLoader.getMedicalRecords().stream()
                .filter(mr -> mr.getFirstName().equalsIgnoreCase(newMedicalRecord.getFirstName()) &&
                        mr.getLastName().equalsIgnoreCase(newMedicalRecord.getLastName()))
                .findFirst();

        if (existingRecord.isPresent()) {
            logger.error("Medical record already exists for {} {}", newMedicalRecord.getFirstName(), newMedicalRecord.getLastName());
            return false;
        }

        dataLoader.getMedicalRecords().add(newMedicalRecord);  // Ajouter le dossier médical
        logger.info("Medical record successfully added for {} {}", newMedicalRecord.getFirstName(), newMedicalRecord.getLastName());
        return true;
    }

    // Mettre à jour un dossier médical existant

    public boolean updateMedicalRecord(MedicalRecord updatedMedicalRecord) {
        logger.info("Attempting to update medical record for {} {}", updatedMedicalRecord.getFirstName(), updatedMedicalRecord.getLastName());

        Optional<MedicalRecord> existingRecord = dataLoader.getMedicalRecords().stream()
                .filter(mr -> mr.getFirstName().equalsIgnoreCase(updatedMedicalRecord.getFirstName()) &&
                        mr.getLastName().equalsIgnoreCase(updatedMedicalRecord.getLastName()))
                .findFirst();

        if (existingRecord.isPresent()) {
            MedicalRecord medicalRecordToUpdate = existingRecord.get();
            medicalRecordToUpdate.setBirthdate(updatedMedicalRecord.getBirthdate());
            medicalRecordToUpdate.setMedications(updatedMedicalRecord.getMedications());
            medicalRecordToUpdate.setAllergies(updatedMedicalRecord.getAllergies());

            logger.info("Medical record successfully updated for {} {}", updatedMedicalRecord.getFirstName(), updatedMedicalRecord.getLastName());
            return true;
        } else {
            logger.error("Medical record not found for {} {}", updatedMedicalRecord.getFirstName(), updatedMedicalRecord.getLastName());
            return false;
        }
    }

    // Supprimer un dossier médical

    public boolean deleteMedicalRecord(String firstName, String lastName)  {
        logger.info("Attempting to delete medical record for {} {}", firstName, lastName);

        Optional<MedicalRecord> existingRecord = dataLoader.getMedicalRecords().stream()
                .filter(mr -> mr.getFirstName().equalsIgnoreCase(firstName) &&
                        mr.getLastName().equalsIgnoreCase(lastName))
                .findFirst();

        if (existingRecord.isPresent()) {
            dataLoader.getMedicalRecords().remove(existingRecord.get());  // Supprimer le dossier médical
            logger.info("Medical record successfully deleted for {} {}", firstName, lastName);
            return true;
        } else {
            logger.error("Medical record not found for {} {}", firstName, lastName);
            return false;
        }
    }
}
