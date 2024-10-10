package com.example.demo.controller;


import com.example.demo.model.MedicalRecord;
import com.example.demo.service.DataLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/medicalRecord")
public class MedicalRecordController {
    @Autowired
    private DataLoader dataLoader; // Service simulant la base de données

    // Ajouter un nouveau dossier médical
    @PostMapping
    public ResponseEntity<String> addMedicalRecord(@RequestBody MedicalRecord newMedicalRecord) {
        // Vérifier si un dossier médical existe déjà pour cette personne
        Optional<MedicalRecord> existingRecord = dataLoader.getMedicalRecords().stream()
                .filter(mr -> mr.getFirstName().equalsIgnoreCase(newMedicalRecord.getFirstName()) &&
                        mr.getLastName().equalsIgnoreCase(newMedicalRecord.getLastName()))
                .findFirst();

        if (existingRecord.isPresent()) {
            return new ResponseEntity<>("Medical record already exists for this person", HttpStatus.CONFLICT);
        }

        dataLoader.getMedicalRecords().add(newMedicalRecord);  // Ajouter le dossier médical
        return new ResponseEntity<>("Medical record added successfully", HttpStatus.CREATED);
    }

    // Mettre à jour un dossier médical existant
    @PutMapping
    public ResponseEntity<String> updateMedicalRecord(@RequestBody MedicalRecord updatedMedicalRecord) {
        Optional<MedicalRecord> existingRecord = dataLoader.getMedicalRecords().stream()
                .filter(mr -> mr.getFirstName().equalsIgnoreCase(updatedMedicalRecord.getFirstName()) &&
                        mr.getLastName().equalsIgnoreCase(updatedMedicalRecord.getLastName()))
                .findFirst();

        if (existingRecord.isPresent()) {
            MedicalRecord medicalRecordToUpdate = existingRecord.get();
            medicalRecordToUpdate.setBirthdate(updatedMedicalRecord.getBirthdate());
            medicalRecordToUpdate.setMedications(updatedMedicalRecord.getMedications());
            medicalRecordToUpdate.setAllergies(updatedMedicalRecord.getAllergies());

            return new ResponseEntity<>("Medical record updated successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Medical record not found", HttpStatus.NOT_FOUND);
        }
    }

    // Supprimer un dossier médical
    @DeleteMapping
    public ResponseEntity<String> deleteMedicalRecord(@RequestParam String firstName, @RequestParam String lastName) {
        Optional<MedicalRecord> existingRecord = dataLoader.getMedicalRecords().stream()
                .filter(mr -> mr.getFirstName().equalsIgnoreCase(firstName) &&
                        mr.getLastName().equalsIgnoreCase(lastName))
                .findFirst();

        if (existingRecord.isPresent()) {
            dataLoader.getMedicalRecords().remove(existingRecord.get());  // Supprimer le dossier médical
            return new ResponseEntity<>("Medical record deleted successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Medical record not found", HttpStatus.NOT_FOUND);
        }
    }
}
