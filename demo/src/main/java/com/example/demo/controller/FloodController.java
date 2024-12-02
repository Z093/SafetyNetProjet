package com.example.demo.controller;

import com.example.demo.model.MedicalRecord;
import com.example.demo.model.Person;
import com.example.demo.model.FireStation;
import com.example.demo.modelResponse.FloodResponse;
import com.example.demo.service.DataLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class FloodController {

    private static final Logger logger = LoggerFactory.getLogger(FloodController.class);

    @Autowired
    private DataLoader dataLoader;

    @GetMapping("/flood/stations")
    public Map<String, List<FloodResponse>> getHouseholdsByStations(@RequestParam List<String> stations) {
        logger.info("Received request for households covered by fire stations: {}", stations);

        try {
            // Filtrer les adresses desservies par les casernes de pompiers spécifiées
            List<String> coveredAddresses = dataLoader.getFireStations().stream()
                    .filter(fireStation -> stations.contains(fireStation.getStation()))
                    .map(FireStation::getAddress)
                    .collect(Collectors.toList());

            logger.info("Addresses covered by stations {}: {}", stations, coveredAddresses);

            // Regrouper les habitants par adresse
            Map<String, List<FloodResponse>> householdsByAddress = dataLoader.getPersons().stream()
                    .filter(person -> coveredAddresses.contains(person.getAddress()))
                    .collect(Collectors.groupingBy(Person::getAddress, Collectors.mapping(person -> {
                        try {
                            // Récupérer les informations médicales pour chaque personne
                            MedicalRecord medicalRecord = dataLoader.getMedicalRecords().stream()
                                    .filter(record -> record.getFirstName().equals(person.getFirstName()) &&
                                            record.getLastName().equals(person.getLastName()))
                                    .findFirst()
                                    .orElse(null);

                            if (medicalRecord == null) {
                                logger.error("No medical record found for person: {} {}", person.getFirstName(), person.getLastName());
                                return new FloodResponse(person.getFirstName(), person.getLastName(), person.getPhone(), 0, List.of(), List.of());
                            }

                            int age = calculateAge(medicalRecord.getBirthdate());
                            List<String> medications = medicalRecord.getMedications();
                            List<String> allergies = medicalRecord.getAllergies();

                            logger.info("Successfully retrieved medical record for person: {} {}, Age: {}", person.getFirstName(), person.getLastName(), age);

                            return new FloodResponse(person.getFirstName(), person.getLastName(), person.getPhone(), age, medications, allergies);

                        } catch (Exception e) {
                            logger.error("Error processing person: {} {}, Details: {}", person.getFirstName(), person.getLastName(), e.getMessage(), e);
                            return null;
                        }
                    }, Collectors.toList())));

            logger.info("Successfully retrieved households by stations.");
            return householdsByAddress;

        } catch (Exception e) {
            logger.error("Error retrieving households for stations {}: {}", stations, e.getMessage(), e);
            return Map.of(); // Retourne une réponse vide en cas d'erreur
        }
    }

    // Méthode pour calculer l'âge à partir de la date de naissance
    private int calculateAge(String birthdate) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            LocalDate birthDate = LocalDate.parse(birthdate, formatter);
            int age = (int) ChronoUnit.YEARS.between(birthDate, LocalDate.now());
            logger.info("Calculated age for birthdate {}: {}", birthdate, age);
            return age;
        } catch (Exception e) {
            logger.error("Error calculating age from birthdate {}: {}", birthdate, e.getMessage(), e);
            return 0;
        }
    }
}
