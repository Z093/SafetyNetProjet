package com.example.demo.controller;

import com.example.demo.model.MedicalRecord;
import com.example.demo.service.MedicalRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
        return medicalRecordService.addMedicalRecord(newMedicalRecord);
    }

    // Mettre à jour un dossier médical existant
    @PutMapping
    public ResponseEntity<String> updateMedicalRecord(@RequestBody MedicalRecord updatedMedicalRecord) {
        logger.info("Attempting to update medical record for {} {}", updatedMedicalRecord.getFirstName(), updatedMedicalRecord.getLastName());

       return medicalRecordService.updateMedicalRecord(updatedMedicalRecord);
        }


    // Supprimer un dossier médical
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteMedicalRecord(@RequestParam String firstName, @RequestParam String lastName)  {
        logger.info("Attempting to delete medical record for {} {}", firstName, lastName);
        return  medicalRecordService.deleteMedicalRecord(firstName, lastName);
}

        }


