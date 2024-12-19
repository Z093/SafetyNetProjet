package com.example.demo.service;

import com.example.demo.Utils.DataLoader;
import com.example.demo.controller.PersonInfoController;
import com.example.demo.model.MedicalRecord;
import com.example.demo.model.Person;
import com.example.demo.modelResponse.PersonInfo;
import com.example.demo.modelResponse.PersonInfoResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class PersonInfoServiceImpl implements PersonInfoService {

    @Autowired
    private DataLoader dataLoader;
    private static final Logger logger = LoggerFactory.getLogger(PersonServiceImpl.class);

    @Override
    public List<PersonInfoResponse> getPersonInfoByLastName(String lastName) {
        logger.info("Fetching person info for last name: {}", lastName);

        List<PersonInfoResponse> list = new ArrayList<>();
        boolean personFound = false;

        for (Person person : dataLoader.getPersons()) {
            if (person.getLastName().equalsIgnoreCase(lastName)) {
                personFound = true;
                logger.info("Processing person: {} {}", person.getFirstName(), person.getLastName());

                // Récupérer les informations médicales
                MedicalRecord medicalRecord = dataLoader.getMedicalRecords().stream()
                        .filter(record -> record.getFirstName().equals(person.getFirstName()) &&
                                record.getLastName().equals(person.getLastName()))
                        .findFirst()
                        .orElse(null);

                int age = medicalRecord != null ? calculateAge(medicalRecord.getBirthdate()) : 0;
                List<String> medications = medicalRecord != null ? medicalRecord.getMedications() : List.of();
                List<String> allergies = medicalRecord != null ? medicalRecord.getAllergies() : List.of();

                // Créer l'objet PersonInfo avec toutes les informations requises
                PersonInfoResponse personInfo = new PersonInfoResponse(
                        person.getFirstName(),
                        person.getLastName(),
                        person.getAddress(),
                        person.getEmail(),
                        age,
                        medications,
                        allergies
                );
                list.add(personInfo);
                logger.info("PersonInfo created for: {} {}", person.getFirstName(), person.getLastName());
            }
        }

        if (!personFound) {
            logger.error("No person found with last name: {}", lastName);
        }

        return list;
    }

    // Méthode pour calculer l'âge à partir de la date de naissance
    int calculateAge(String birthdate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate birthDate = LocalDate.parse(birthdate, formatter);
        return (int) ChronoUnit.YEARS.between(birthDate, LocalDate.now());
    }
    }


