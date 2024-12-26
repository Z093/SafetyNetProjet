package com.example.demo.controller;

import com.example.demo.model.MedicalRecord;
import com.example.demo.service.MedicalRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/medicalRecord")
public class MedicalRecordController {

    private static final Logger logger = LoggerFactory.getLogger(MedicalRecordController.class);

    @Autowired
    private MedicalRecordService medicalRecordService; // Service simulant la base de données

    // Ajouter un nouveau dossier médical
    @PostMapping("/add")
    public ResponseEntity<String> addMedicalRecord(@RequestBody MedicalRecord newMedicalRecord) {
        logger.info("Attempting to add a new medical record for {} {}", newMedicalRecord.getFirstName(), newMedicalRecord.getLastName());
        boolean resultat = medicalRecordService.addMedicalRecord(newMedicalRecord);
        if (resultat) {
            return ResponseEntity.ok("Medical record added successfully");
        }
        else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Medical Record not added");
        }
    }

    // Mettre à jour un dossier médical existant
    @PutMapping
    public ResponseEntity<String> updateMedicalRecord(@RequestBody MedicalRecord updatedMedicalRecord) {
        logger.info("Attempting to update medical record for {} {}", updatedMedicalRecord.getFirstName(), updatedMedicalRecord.getLastName());

        boolean resultat = medicalRecordService.updateMedicalRecord(updatedMedicalRecord);
        if (resultat) {
            return ResponseEntity.ok("Medical record updated successfully");
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Medical Record not updated");
        }
        }


    // Supprimer un dossier médical
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteMedicalRecord(@RequestParam String firstName, @RequestParam String lastName)  {
        logger.info("Attempting to delete medical record for {} {}", firstName, lastName);
        boolean resultat =  medicalRecordService.deleteMedicalRecord(firstName, lastName);

        if (resultat) {
            return ResponseEntity.ok("Medical record deleted successfully");
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Medical Record not deleted");
        }
}

        }

