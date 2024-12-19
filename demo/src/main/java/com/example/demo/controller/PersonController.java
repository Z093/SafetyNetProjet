package com.example.demo.controller;

import com.example.demo.model.Person;
import com.example.demo.Utils.DataLoader;
import com.example.demo.service.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/person")
public class PersonController {

    private static final Logger logger = LoggerFactory.getLogger(PersonController.class);

    @Autowired
    private PersonService personService; // Service simulant la base de données (ou une vraie base)

    // Ajouter une nouvelle personne
    @PostMapping
    public ResponseEntity<String> addPerson(@RequestBody Person newPerson) {
        logger.info("Attempting to add a new person: {} {}", newPerson.getFirstName(), newPerson.getLastName());

       return personService.addPerson(newPerson);
    }

    // Mettre à jour une personne existante
    @PutMapping
    public ResponseEntity<String> updatePerson(@RequestBody Person updatedPerson) {
        logger.info("Attempting to update person: {} {}", updatedPerson.getFirstName(), updatedPerson.getLastName());

       return personService.updatePerson(updatedPerson);
    }

    // Supprimer une personne
    @DeleteMapping
    public ResponseEntity<String> deletePerson(@RequestParam String firstName, @RequestParam String lastName) {
        logger.info("Attempting to delete person: {} {}", firstName, lastName);
        return personService.deletePerson(firstName, lastName);
    }
}
