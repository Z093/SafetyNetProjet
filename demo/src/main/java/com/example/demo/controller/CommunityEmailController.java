package com.example.demo.controller;

import com.example.demo.model.Person;
import com.example.demo.service.DataLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class CommunityEmailController {

    private static final Logger logger = LoggerFactory.getLogger(CommunityEmailController.class);

    @Autowired
    private DataLoader dataLoader;

    @GetMapping("/communityEmail")
    public List<String> getEmailsByCity(@RequestParam String city) {
        logger.info("Processing /communityEmail request for city: {}", city);

        try {
            // Filtrer les personnes qui habitent dans la ville spécifiée et récupérer les emails
            List<String> emails = dataLoader.getPersons().stream()
                    .filter(person -> person.getCity().equalsIgnoreCase(city)) // Filtrer par ville
                    .map(Person::getEmail)  // Récupérer les adresses e-mail
                    .distinct()  // Éviter les doublons
                    .collect(Collectors.toList());

            if (emails.isEmpty()) {
                logger.info("No emails found for city: {}", city);
            } else {
                logger.info("Found {} unique emails for city: {}", emails.size(), city);
            }

            return emails;
        } catch (Exception e) {
            logger.error("Error occurred while processing /communityEmail request for city {}: {}", city, e.getMessage(), e);
            return List.of(); // Retourner une liste vide en cas d'erreur
        }
    }
}
