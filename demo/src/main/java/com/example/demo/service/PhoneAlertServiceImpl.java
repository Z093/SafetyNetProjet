package com.example.demo.service;

import com.example.demo.Utils.DataLoader;
import com.example.demo.model.FireStation;
import com.example.demo.model.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class PhoneAlertServiceImpl implements PhoneAlertService {

    @Autowired
    private DataLoader dataLoader;
    private static final Logger logger = LoggerFactory.getLogger(PhoneAlertServiceImpl.class);

    @Override
    public List<String> getPhoneNumbersByFirestation(String firestation){
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

