package com.example.demo.service;

import com.example.demo.Utils.DataLoader;
import com.example.demo.model.FireStation;
import com.example.demo.model.MedicalRecord;
import com.example.demo.model.Person;
import com.example.demo.modelResponse.FireStationResponse;
import com.example.demo.modelResponse.PersonResponse;
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
public class FireStationServiceImpl implements FireStationService {

    @Autowired
    private DataLoader dataLoader;

    private static final Logger logger = LoggerFactory.getLogger(FireStationServiceImpl.class);

    @Override
    public FireStationResponse getPersonsByStation(String stationNumber){

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
}
