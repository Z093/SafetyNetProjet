package com.example.demo.controllerEndPoints;

import com.example.demo.model.FireStation;
import com.example.demo.service.DataLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/*@RestController
@RequestMapping("/firestationEP")
public class FireStationEPController {
    @Autowired
    private DataLoader dataLoader; // Service simulant la base de données

    // Ajouter un nouveau mapping caserne/adresse
    @PostMapping
    public ResponseEntity<String> addFireStationMapping(@RequestBody FireStation newMapping) {
        Optional<FireStation> existingMapping = dataLoader.getFireStations().stream()
                .filter(fs -> fs.getAddress().equalsIgnoreCase(newMapping.getAddress()))
                .findFirst();

        if (existingMapping.isPresent()) {
            return new ResponseEntity<>("Mapping already exists for this address", HttpStatus.CONFLICT);
        }

        dataLoader.getFireStations().add(newMapping);
        return new ResponseEntity<>("Fire station mapping added successfully", HttpStatus.CREATED);
    }

    // Mettre à jour le numéro de la caserne de pompiers pour une adresse existante
    @PutMapping
    public ResponseEntity<String> updateFireStationMapping(@RequestBody FireStation updatedMapping) {
        Optional<FireStation> existingMapping = dataLoader.getFireStations().stream()
                .filter(fs -> fs.getAddress().equalsIgnoreCase(updatedMapping.getAddress()))
                .findFirst();

        if (existingMapping.isPresent()) {
            FireStation fireStationToUpdate = existingMapping.get();
            fireStationToUpdate.setStation(updatedMapping.getStation());
            return new ResponseEntity<>("Fire station mapping updated successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Mapping not found for this address", HttpStatus.NOT_FOUND);
        }
    }

    // Supprimer un mapping caserne/adresse
    @DeleteMapping
    public ResponseEntity<String> deleteFireStationMapping(@RequestParam String address) {
        Optional<FireStation> existingMapping = dataLoader.getFireStations().stream()
                .filter(fs -> fs.getAddress().equalsIgnoreCase(address))
                .findFirst();

        if (existingMapping.isPresent()) {
            dataLoader.getFireStations().remove(existingMapping.get());
            return new ResponseEntity<>("Fire station mapping deleted successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Mapping not found for this address", HttpStatus.NOT_FOUND);
        }
    }
}*/


