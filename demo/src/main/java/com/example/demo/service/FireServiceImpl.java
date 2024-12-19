package com.example.demo.service;

import com.example.demo.Utils.DataLoader;
import com.example.demo.controller.FireController;
import com.example.demo.model.FireStation;
import com.example.demo.model.MedicalRecord;
import com.example.demo.modelResponse.FireResponse;
import com.example.demo.modelResponse.PersonInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FireServiceImpl implements FireService {

    @Autowired
    private DataLoader dataLoader;

    private static final Logger logger = LoggerFactory.getLogger(FireController.class);

    @Override
    public FireResponse getPersonsAtAddress(String address) {

    try {
        // Récupérer la caserne de pompiers desservant l'adresse donnée
        String fireStationNumber = dataLoader.getFireStations().stream()
                .filter(fireStation -> fireStation.getAddress().equalsIgnoreCase(address))
                .map(FireStation::getStation)
                .findFirst()
                .orElse(null);

        if (fireStationNumber == null) {
            logger.info("No fire station found for address: {}", address);
        } else {
            logger.info("Fire station serving address {}: {}", address, fireStationNumber);
        }

        // Récupérer les personnes vivant à cette adresse
        List<PersonInfo> residents = dataLoader.getPersons().stream()
                .filter(person -> person.getAddress().equalsIgnoreCase(address))
                .map(person -> {
                    // Récupérer l'âge et les informations médicales
                    MedicalRecord medicalRecord = dataLoader.getMedicalRecords().stream()
                            .filter(record -> record.getFirstName().equals(person.getFirstName()) &&
                                    record.getLastName().equals(person.getLastName()))
                            .findFirst()
                            .orElse(null);

                    int age = 0;
                    List<String> medications = List.of();
                    List<String> allergies = List.of();

                    if (medicalRecord != null) {
                        age = calculateAge(medicalRecord.getBirthdate());
                        medications = medicalRecord.getMedications();
                        allergies = medicalRecord.getAllergies();
                    } else {
                        logger.error("No medical record found for person: {} {}", person.getFirstName(), person.getLastName());
                    }

                    // Créer l'objet PersonInfo
                    return new PersonInfo(person.getFirstName(), person.getLastName(), person.getPhone(), person.getEmail(), age, medications, allergies);
                })
                .collect(Collectors.toList());

        logger.info("Found {} residents at address {}", residents.size(), address);

        // Retourner la réponse avec les résidents et le numéro de la caserne de pompiers
        return new FireResponse(fireStationNumber, residents);
    } catch (Exception e) {
        logger.error("Error occurred while processing /fire request for address {}: {}", address, e.getMessage(), e);
        return new FireResponse(null, List.of()); // Retourner une réponse vide en cas d'erreur
    }
}

// Méthode pour calculer l'âge à partir de la date de naissance
private int calculateAge(String birthdate) {
    try {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate birthDate = LocalDate.parse(birthdate, formatter);
        int age = (int) ChronoUnit.YEARS.between(birthDate, LocalDate.now());
        logger.info("Calculated age: {}", age);
        return age;
    } catch (Exception e) {
        logger.error("Error occurred while calculating age for birthdate {}: {}", birthdate, e.getMessage(), e);
        return 0;
    }
}
}
