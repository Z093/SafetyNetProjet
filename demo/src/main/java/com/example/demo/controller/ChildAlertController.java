package com.example.demo.controller;

import com.example.demo.model.MedicalRecord;
import com.example.demo.model.Person;
import com.example.demo.modelResponse.ChildAlertResponse;
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
import java.util.stream.Collectors;

@RestController
public class ChildAlertController {

    private static final Logger logger = LoggerFactory.getLogger(ChildAlertController.class);

    @Autowired
    private DataLoader dataLoader;

    @GetMapping("/childAlert")
    public List<ChildAlertResponse> getChildrenAtAddress(@RequestParam String address) {
        logger.info("Processing /childAlert request for address: {}", address);

        try {
            // Filtrer les personnes vivant à cette adresse
            List<Person> personsAtAddress = dataLoader.getPersons().stream()
                    .filter(person -> person.getAddress().equalsIgnoreCase(address))
                    .toList();
            logger.info("Found {} persons at address {}", personsAtAddress.size(), address);

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

            if (children.isEmpty()) {
                logger.info("No children found at address: {}", address);
            } else {
                logger.info("Found {} children at address: {}", children.size(), address);
            }

            return children;
        } catch (Exception e) {
            logger.error("Error occurred while processing /childAlert request for address {}: {}", address, e.getMessage(), e);
            return List.of(); // Retourner une liste vide en cas d'erreur
        }
    }

    // Méthode pour calculer l'âge à partir de la date de naissance
    private int getAge(Person person) {
        try {
            MedicalRecord medicalRecord = dataLoader.getMedicalRecords().stream()
                    .filter(record -> record.getFirstName().equals(person.getFirstName()) &&
                            record.getLastName().equals(person.getLastName()))
                    .findFirst()
                    .orElse(null);

            if (medicalRecord == null) {
                logger.error("No medical record found for person: {} {}", person.getFirstName(), person.getLastName());
                return 0; // Si pas de dossier médical, âge inconnu
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            LocalDate birthDate = LocalDate.parse(medicalRecord.getBirthdate(), formatter);
            int age = (int) ChronoUnit.YEARS.between(birthDate, LocalDate.now());
            logger.info("Calculated age for {} {}: {}", person.getFirstName(), person.getLastName(), age);
            return age;
        } catch (Exception e) {
            logger.error("Error occurred while calculating age for person {} {}: {}",
                    person.getFirstName(), person.getLastName(), e.getMessage(), e);
            return 0;
        }
    }
}
