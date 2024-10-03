package com.example.demo.controller;

import com.example.demo.model.Person;
import com.example.demo.service.DataLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class CommunityEmailController {
    @Autowired
    private DataLoader dataLoader;

    @GetMapping("/communityEmail")
    public List<String> getEmailsByCity(@RequestParam String city) {
        // Filtrer les personnes qui habitent dans la ville spécifiée et récupérer les emails
        return dataLoader.getPersons().stream()
                .filter(person -> person.getCity().equalsIgnoreCase(city))  // Filtrer par ville
                .map(Person::getEmail)  // Récupérer les adresses e-mail
                .distinct()  // Éviter les doublons
                .collect(Collectors.toList());
    }
}
