package com.example.demo.controller;

import com.example.demo.model.MedicalRecord;
import com.example.demo.model.Person;
import com.example.demo.modelResponse.PersonResponse;
import com.example.demo.modelResponse.FireStationResponse;
import com.example.demo.model.FireStation;
import com.example.demo.service.DataLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(FireStationController.class);

    @Autowired
    private DataLoader dataLoader;

    @GetMapping("/firestation")
    public FireStationResponse getPersonsByStation(@RequestParam String stationNumber) {
        logger.info("Received request for fire station: {}", stationNumber);

        try {
            // Filtrer les adresses couvertes par cette station de pompiers
            List<String> coveredAddresses = dataLoader.getFireStations().stream()
                    .filter(fireStation -> fireStation.getStation().equals(stationNumber))
                    .map(FireStation::getAddress)
                    .collect(Collectors.toList());

            logger.info("Addresses covered by station {}: {}", stationNumber, coveredAddresses);

            // Filtrer les personnes vivant à ces adresses
            List<Person> coveredPersons = dataLoader.getPersons().stream()
                    .filter(person -> coveredAddresses.contains(person.getAddress()))
                    .collect(Collectors.toList());

            long numberOfAdults = coveredPersons.stream()
                    .filter(person -> getAge(person) > 18)
                    .count();

            long numberOfChildren = coveredPersons.size() - numberOfAdults;

            logger.info("Found {} adults and {} children for station {}", numberOfAdults, numberOfChildren, stationNumber);

            // Créer la liste des réponses
            List<PersonResponse> personResponses = coveredPersons.stream()
                    .map(person -> new PersonResponse(
                            person.getFirstName(),
                            person.getLastName(),
                            person.getAddress(),
                            person.getPhone()))
                    .collect(Collectors.toList());

            return new FireStationResponse(personResponses, numberOfAdults, numberOfChildren);

        } catch (Exception e) {
            logger.error("Error processing request for fire station {}: {}", stationNumber, e.getMessage(), e);
            return new FireStationResponse(List.of(), 0, 0); // Réponse vide en cas d'erreur
        }
    }

    private int getAge(Person person) {
        try {
            MedicalRecord medicalRecord = dataLoader.getMedicalRecords().stream()
                    .filter(record -> record.getFirstName().equals(person.getFirstName()) &&
                            record.getLastName().equals(person.getLastName()))
                    .findFirst()
                    .orElse(null);

            if (medicalRecord == null) {
                logger.error("No medical record found for person: {} {}", person.getFirstName(), person.getLastName());
                return 0;
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            LocalDate birthDate = LocalDate.parse(medicalRecord.getBirthdate(), formatter);
            int age = (int) ChronoUnit.YEARS.between(birthDate, LocalDate.now());
            logger.info("Calculated age for person {} {}: {}", person.getFirstName(), person.getLastName(), age);
            return age;

        } catch (Exception e) {
            logger.error("Error calculating age for person {} {}: {}", person.getFirstName(), person.getLastName(), e.getMessage(), e);
            return 0;
        }
    }

    @PostMapping
    public ResponseEntity<String> addFireStationMapping(@RequestBody FireStation newMapping) {
        logger.info("Received request to add fire station mapping: {}", newMapping);

        Optional<FireStation> existingMapping = dataLoader.getFireStations().stream()
                .filter(fs -> fs.getAddress().equalsIgnoreCase(newMapping.getAddress()))
                .findFirst();

        if (existingMapping.isPresent()) {
            logger.error("Mapping already exists for address: {}", newMapping.getAddress());
            return new ResponseEntity<>("Mapping already exists for this address", HttpStatus.CONFLICT);
        }

        dataLoader.getFireStations().add(newMapping);
        logger.info("Fire station mapping added successfully for address: {}", newMapping.getAddress());
        return new ResponseEntity<>("Fire station mapping added successfully", HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<String> updateFireStationMapping(@RequestBody FireStation updatedMapping) {
        logger.info("Received request to update fire station mapping: {}", updatedMapping);

        Optional<FireStation> existingMapping = dataLoader.getFireStations().stream()
                .filter(fs -> fs.getAddress().equalsIgnoreCase(updatedMapping.getAddress()))
                .findFirst();

        if (existingMapping.isPresent()) {
            FireStation fireStationToUpdate = existingMapping.get();
            fireStationToUpdate.setStation(updatedMapping.getStation());
            logger.info("Fire station mapping updated for address: {}", updatedMapping.getAddress());
            return new ResponseEntity<>("Fire station mapping updated successfully", HttpStatus.OK);
        } else {
            logger.error("No mapping found for address: {}", updatedMapping.getAddress());
            return new ResponseEntity<>("Mapping not found for this address", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping
    public ResponseEntity<String> deleteFireStationMapping(@RequestParam String address) {
        logger.info("Received request to delete fire station mapping for address: {}", address);

        Optional<FireStation> existingMapping = dataLoader.getFireStations().stream()
                .filter(fs -> fs.getAddress().equalsIgnoreCase(address))
                .findFirst();

        if (existingMapping.isPresent()) {
            dataLoader.getFireStations().remove(existingMapping.get());
            logger.info("Fire station mapping deleted successfully for address: {}", address);
            return new ResponseEntity<>("Fire station mapping deleted successfully", HttpStatus.OK);
        } else {
            logger.error("No mapping found for address: {}", address);
            return new ResponseEntity<>("Mapping not found for this address", HttpStatus.NOT_FOUND);
        }
    }
}
