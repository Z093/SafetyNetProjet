package com.example.demo.controller;

import com.example.demo.model.MedicalRecord;
import com.example.demo.model.Person;
import com.example.demo.model.FireStation;
import com.example.demo.modelResponse.FloodResponse;
import com.example.demo.service.DataLoader;
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

    @Autowired
    private DataLoader dataLoader;

    @GetMapping("/flood/stations")
    public Map<String, List<FloodResponse>> getHouseholdsByStations(@RequestParam List<String> stations) {
        // Filtrer les adresses desservies par les casernes de pompiers spécifiées
        List<String> coveredAddresses = dataLoader.getFireStations().stream()
                .filter(fireStation -> stations.contains(fireStation.getStation()))
                .map(FireStation::getAddress)
                .collect(Collectors.toList());

        // Regrouper les habitants par adresse
        Map<String, List<FloodResponse>> householdsByAddress = dataLoader.getPersons().stream()
                .filter(person -> coveredAddresses.contains(person.getAddress()))
                .collect(Collectors.groupingBy(Person::getAddress, Collectors.mapping(person -> {
                    // Récupérer les informations médicales pour chaque personne
                    MedicalRecord medicalRecord = dataLoader.getMedicalRecords().stream()
                            .filter(record -> record.getFirstName().equals(person.getFirstName()) &&
                                    record.getLastName().equals(person.getLastName()))
                            .findFirst()
                            .orElse(null);

                    int age = medicalRecord != null ? calculateAge(medicalRecord.getBirthdate()) : 0;
                    List<String> medications = medicalRecord != null ? medicalRecord.getMedications() : List.of();
                    List<String> allergies = medicalRecord != null ? medicalRecord.getAllergies() : List.of();

                    // Créer l'objet PersonInfo
                    return new FloodResponse(person.getFirstName(), person.getLastName(), person.getPhone(), age, medications, allergies);
                }, Collectors.toList())));

        return householdsByAddress;
    }

    // Méthode pour calculer l'âge à partir de la date de naissance
    private int calculateAge(String birthdate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate birthDate = LocalDate.parse(birthdate, formatter);
        return (int) ChronoUnit.YEARS.between(birthDate, LocalDate.now());
    }
}

// Classe pour représenter les informations des résidents
/*@Data
@ToString
class
PersonInfoFlood {
    private String firstName;
    private String lastName;
    private String phone;
    private int age;
    private List<String> medications;
    private List<String> allergies;

    public PersonInfoFlood(String firstName, String lastName, String phone, int age, List<String> medications, List<String> allergies) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.age = age;
        this.medications = medications;
        this.allergies = allergies;
    }

    // Getters et setters
}*/
