package com.example.demo.controller;

import com.example.demo.model.MedicalRecord;
import com.example.demo.model.Person;
import com.example.demo.modelResponse.PersonResponse;
import com.example.demo.modelResponse.FireStationResponse;
import com.example.demo.model.FireStation;
import com.example.demo.service.DataLoader;
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
}
/*
@Data
@ToString
class PersonResponse {
    private String firstName;
    private String lastName;
    private String address;
    private String phone;

    public PersonResponse(String firstName, String lastName, String address, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phone = phone;
    }

    // Getters et setters
}
@Data
@ToString
class FireStationResponse {
    private List<PersonResponse> persons;
    private long numberOfAdults;
    private long numberOfChildren;

    public FireStationResponse(List<PersonResponse> persons, long numberOfAdults, long numberOfChildren) {
        this.persons = persons;
        this.numberOfAdults = numberOfAdults;
        this.numberOfChildren = numberOfChildren;
    }*/

    // Getters et setters

