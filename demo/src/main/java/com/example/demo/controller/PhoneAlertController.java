package com.example.demo.controller;

import com.example.demo.service.DataLoader;
import com.example.demo.model.Person;
import com.example.demo.model.FireStation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class PhoneAlertController {

    private static final Logger logger = LoggerFactory.getLogger(PhoneAlertController.class);

    @Autowired
    private DataLoader dataLoader;

    @GetMapping("/phoneAlert")
    public List<String> getPhoneNumbersByFirestation(@RequestParam String firestation) {
        logger.info("Starting phone number search for fire station: {}", firestation);

        try {
            // Filtrer les adresses couvertes par la caserne spécifiée
            List<String> coveredAddresses = dataLoader.getFireStations().stream()
                    .filter(fireStation -> fireStation.getStation().equals(firestation))
                    .map(FireStation::getAddress)
                    .collect(Collectors.toList());

            if (coveredAddresses.isEmpty()) {
                logger.warn("No addresses found for fire station: {}", firestation);
                return List.of(); // Retourne une liste vide si aucune adresse n'est trouvée
            }

            logger.info("Found {} addresses for fire station: {}", coveredAddresses.size(), firestation);

            // Filtrer les personnes vivant à ces adresses et récupérer leurs numéros de téléphone
            List<String> phoneNumbers = dataLoader.getPersons().stream()
                    .filter(person -> coveredAddresses.contains(person.getAddress()))
                    .map(Person::getPhone)
                    .distinct() // Éliminer les doublons éventuels
                    .collect(Collectors.toList());

            logger.info("Found {} unique phone numbers for fire station: {}", phoneNumbers.size(), firestation);

            return phoneNumbers;
        } catch (Exception e) {
            logger.error("An error occurred while fetching phone numbers for fire station: {}", firestation, e);
            return List.of(); // Retourne une liste vide en cas d'erreur
        }
    }
}
