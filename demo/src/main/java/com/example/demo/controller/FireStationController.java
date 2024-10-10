package com.example.demo.controller;

import com.example.demo.model.MedicalRecord;
import com.example.demo.model.Person;
import com.example.demo.modelResponse.PersonResponse;
import com.example.demo.modelResponse.FireStationResponse;
import com.example.demo.model.FireStation;
import com.example.demo.service.DataLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class FireStationController {
    @Autowired
    private DataLoader dataLoader;

    @GetMapping("/firestation")
    public FireStationResponse getPersonsByStation(@RequestParam String stationNumber) {
        // Filtrer les adresses couvertes par cette station de pompiers
        List<String> coveredAddresses = dataLoader.getFireStations().stream()
                .filter(fireStation -> fireStation.getStation().equals(stationNumber))
                .map(FireStation::getAddress)
                .collect(Collectors.toList());

        // Filtrer les personnes vivant à ces adresses
        List<Person> coveredPersons = dataLoader.getPersons().stream()
                .filter(person -> coveredAddresses.contains(person.getAddress()))
                .collect(Collectors.toList());

        // Calcul du nombre d'adultes et d'enfants
        long numberOfAdults = coveredPersons.stream()
                .filter(person -> getAge(person) > 18)
                .count();

        long numberOfChildren = coveredPersons.size() - numberOfAdults;

        // Créer la liste des réponses
        List<PersonResponse> personResponses = coveredPersons.stream()
                .map(person -> new PersonResponse(
                        person.getFirstName(),
                        person.getLastName(),
                        person.getAddress(),
                        person.getPhone()))
                .collect(Collectors.toList());

        return new FireStationResponse(personResponses, numberOfAdults, numberOfChildren);
    }

    // Méthode pour calculer l'âge d'une personne à partir de sa date de naissance
    private int getAge(Person person) {
        MedicalRecord medicalRecord = dataLoader.getMedicalRecords().stream()
                .filter(record -> record.getFirstName().equals(person.getFirstName()) && record.getLastName().equals(person.getLastName()))
                .findFirst()
                .orElse(null);

        if (medicalRecord == null) {
            return 0;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate birthDate = LocalDate.parse(medicalRecord.getBirthdate(), formatter);
        return (int) ChronoUnit.YEARS.between(birthDate, LocalDate.now());
    }

    // Ajouter un nouveau mapping caserne/adresse
    @PostMapping("/firestation")
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
    @PutMapping("/firestation")
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
    @DeleteMapping("/firestation")
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


}


