package com.example.demo.controller;

import com.example.demo.model.Person;
import com.example.demo.model.MedicalRecord;
import com.example.demo.service.DataLoader;
import com.example.demo.modelResponse.PersonInfoResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@RestController
public class PersonInfoController {
    @Autowired
    private DataLoader dataLoader;

    @GetMapping("/personInfo")
    public List<PersonInfoResponse> getPersonInfoByLastName(@RequestParam String lastName) {
        // Filtrer les personnes par nom de famille
        // Récupérer les informations médicales
        // Créer l'objet PersonInfo avec toutes les informations requises
        List<PersonInfoResponse> list = new ArrayList<>();
        for (Person person1 : dataLoader.getPersons()) {
            if (person1.getLastName().equalsIgnoreCase(lastName)) {
// Récupérer les informations médicales
                MedicalRecord medicalRecord = dataLoader.getMedicalRecords().stream()
                        .filter(record -> record.getFirstName().equals(person1.getFirstName()) &&
                                record.getLastName().equals(person1.getLastName()))
                        .findFirst()
                        .orElse(null);

                int age = medicalRecord != null ? calculateAge(medicalRecord.getBirthdate()) : 0;
                List<String> medications = medicalRecord != null ? medicalRecord.getMedications() : List.of();
                List<String> allergies = medicalRecord != null ? medicalRecord.getAllergies() : List.of();

                // Créer l'objet PersonInfo avec toutes les informations requises
                PersonInfoResponse apply = new PersonInfoResponse(person1.getFirstName(), person1.getLastName(), person1.getAddress(),
                        person1.getEmail(), age, medications, allergies);
                list.add(apply);
            }
        }
        return list;
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
class PersonInfoM {
    private String firstName;
    private String lastName;
    private String address;
    private String email;
    private int age;
    private List<String> medications;
    private List<String> allergies;

    public PersonInfoM(String firstName, String lastName, String address, String email, int age,
                      List<String> medications, List<String> allergies) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.email = email;
        this.age = age;
        this.medications = medications;
        this.allergies = allergies;
    }

    // Getters et setters
}*/
