package com.example.demo.controller;

import com.example.demo.service.DataLoader;
import com.example.demo.model.Person;
import com.example.demo.model.FireStation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class PhoneAlertController {
    @Autowired
    private DataLoader dataLoader;

    @GetMapping("/phoneAlert")
    public List<String> getPhoneNumbersByFirestation(@RequestParam String firestation) {
        // Filtrer les adresses couvertes par la caserne spécifiée
        List<String> coveredAddresses = dataLoader.getFireStations().stream()
                .filter(fireStation -> fireStation.getStation().equals(firestation))
                .map(FireStation::getAddress)
                .collect(Collectors.toList());

        // Filtrer les personnes vivant à ces adresses et récupérer leurs numéros de téléphone
        List<String> phoneNumbers = dataLoader.getPersons().stream()
                .filter(person -> coveredAddresses.contains(person.getAddress()))
                .map(Person::getPhone)
                .distinct()  // Éliminer les doublons éventuels
                .collect(Collectors.toList());

        return phoneNumbers;
    }
}
