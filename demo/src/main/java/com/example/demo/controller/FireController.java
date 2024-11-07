package com.example.demo.controller;

import com.example.demo.model.MedicalRecord;
import com.example.demo.modelResponse.PersonInfo;
import com.example.demo.service.DataLoader;
import com.example.demo.model.FireStation;
import com.example.demo.modelResponse.FireResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class FireController {
    @Autowired
    private DataLoader dataLoader;

    @GetMapping("/fire")
    public FireResponse getPersonsAtAddress(@RequestParam String address) {
        // Récupérer la caserne de pompiers desservant l'adresse donnée
        String fireStationNumber = dataLoader.getFireStations().stream()
                .filter(fireStation -> fireStation.getAddress().equalsIgnoreCase(address))
                .map(FireStation::getStation)
                .findFirst()
                .orElse(null);

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

                    int age = medicalRecord != null ? calculateAge(medicalRecord.getBirthdate()) : 0;
                    List<String> medications = medicalRecord != null ? medicalRecord.getMedications() : List.of();
                    List<String> allergies = medicalRecord != null ? medicalRecord.getAllergies() : List.of();

                    // Créer l'objet PersonInfo
                    return new PersonInfo(person.getFirstName(), person.getLastName(), person.getPhone(), person.getEmail(), age, medications, allergies);
                })
                .collect(Collectors.toList());

        // Retourner la réponse avec les résidents et le numéro de la caserne de pompiers
        return new FireResponse(fireStationNumber, residents);
    }

    // Méthode pour calculer l'âge à partir de la date de naissance
    private int calculateAge(String birthdate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate birthDate = LocalDate.parse(birthdate, formatter);
        return (int) ChronoUnit.YEARS.between(birthDate, LocalDate.now());
    }
}

