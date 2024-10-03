package com.example.demo.controller;


import com.example.demo.model.MedicalRecord;
import com.example.demo.model.Person;
import com.example.demo.modelResponse.ChildAlertResponse;
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
public class ChildAlertController {
    @Autowired
    private DataLoader dataLoader;

    @GetMapping("/childAlert")
    public List<ChildAlertResponse> getChildrenAtAddress(@RequestParam String address) {
        // Filtrer les personnes vivant à cette adresse
        List<Person> personsAtAddress = dataLoader.getPersons().stream()
                .filter(person -> person.getAddress().equalsIgnoreCase(address))
                .toList();

        // Séparer les enfants (<= 18 ans) et les autres membres du foyer
        List<ChildAlertResponse> children = personsAtAddress.stream()
                .filter(person -> getAge(person) <= 18)
                .map(child -> {
                    List<String> otherMembers = personsAtAddress.stream()
                            .filter(p -> !p.equals(child)) // Exclure l'enfant lui-même
                            .map(p -> p.getFirstName() + " " + p.getLastName())
                            .collect(Collectors.toList());
                    return new ChildAlertResponse(child.getFirstName(), child.getLastName(), getAge(child), otherMembers);
                })
                .collect(Collectors.toList());

        // Retourner une chaîne vide s'il n'y a pas d'enfants
        return children.isEmpty() ? List.of() : children;
    }

    // Méthode pour calculer l'âge à partir de la date de naissance
    private int getAge(Person person) {
        MedicalRecord medicalRecord = dataLoader.getMedicalRecords().stream()
                .filter(record -> record.getFirstName().equals(person.getFirstName()) &&
                        record.getLastName().equals(person.getLastName()))
                .findFirst()
                .orElse(null);

        if (medicalRecord == null) {
            return 0; // Si pas de dossier médical, âge inconnu
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate birthDate = LocalDate.parse(medicalRecord.getBirthdate(), formatter);
        return (int) ChronoUnit.YEARS.between(birthDate, LocalDate.now());
    }
}

/*@Data
@ToString
class ChildAlertResponse {
    private String firstName;
    private String lastName;
    private int age;
    private List<String> otherMembers;

    public ChildAlertResponse(String firstName, String lastName, int age, List<String> otherMembers) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.otherMembers = otherMembers;
    }

    // Getters et setters
}*/
