package com.example.demo.controller;

import com.example.demo.model.Person;
import com.example.demo.service.DataLoader;
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
    private DataLoader dataLoader; // Service simulant la base de données (ou une vraie base)

    // Ajouter une nouvelle personne
    @PostMapping
    public ResponseEntity<String> addPerson(@RequestBody Person newPerson) {
        logger.info("Attempting to add a new person: {} {}", newPerson.getFirstName(), newPerson.getLastName());

        // Vérifier si la personne existe déjà
        Optional<Person> existingPerson = dataLoader.getPersons().stream()
                .filter(person -> person.getFirstName().equalsIgnoreCase(newPerson.getFirstName()) &&
                        person.getLastName().equalsIgnoreCase(newPerson.getLastName()))
                .findFirst();

        if (existingPerson.isPresent()) {
            logger.error("Person already exists: {} {}", newPerson.getFirstName(), newPerson.getLastName());
            return new ResponseEntity<>("Person already exists", HttpStatus.CONFLICT);
        }

        dataLoader.getPersons().add(newPerson);  // Ajouter la nouvelle personne
        logger.info("Person successfully added: {} {}", newPerson.getFirstName(), newPerson.getLastName());
        return new ResponseEntity<>("Person added successfully", HttpStatus.CREATED);
    }

    // Mettre à jour une personne existante
    @PutMapping
    public ResponseEntity<String> updatePerson(@RequestBody Person updatedPerson) {
        logger.info("Attempting to update person: {} {}", updatedPerson.getFirstName(), updatedPerson.getLastName());

        Optional<Person> existingPerson = dataLoader.getPersons().stream()
                .filter(person -> person.getFirstName().equalsIgnoreCase(updatedPerson.getFirstName()) &&
                        person.getLastName().equalsIgnoreCase(updatedPerson.getLastName()))
                .findFirst();

        if (existingPerson.isPresent()) {
            Person personToUpdate = existingPerson.get();
            // Mettre à jour les champs
            personToUpdate.setAddress(updatedPerson.getAddress());
            personToUpdate.setCity(updatedPerson.getCity());
            personToUpdate.setZip(updatedPerson.getZip());
            personToUpdate.setPhone(updatedPerson.getPhone());
            personToUpdate.setEmail(updatedPerson.getEmail());

            logger.info("Person successfully updated: {} {}", updatedPerson.getFirstName(), updatedPerson.getLastName());
            return new ResponseEntity<>("Person updated successfully", HttpStatus.OK);
        } else {
            logger.error("Person not found for update: {} {}", updatedPerson.getFirstName(), updatedPerson.getLastName());
            return new ResponseEntity<>("Person not found", HttpStatus.NOT_FOUND);
        }
    }

    // Supprimer une personne
    @DeleteMapping
    public ResponseEntity<String> deletePerson(@RequestParam String firstName, @RequestParam String lastName) {
        logger.info("Attempting to delete person: {} {}", firstName, lastName);

        Optional<Person> existingPerson = dataLoader.getPersons().stream()
                .filter(person -> person.getFirstName().equalsIgnoreCase(firstName) &&
                        person.getLastName().equalsIgnoreCase(lastName))
                .findFirst();

        if (existingPerson.isPresent()) {
            dataLoader.getPersons().remove(existingPerson.get());  // Supprimer la personne
            logger.info("Person successfully deleted: {} {}", firstName, lastName);
            return new ResponseEntity<>("Person deleted successfully", HttpStatus.OK);
        } else {
            logger.error("Person not found for deletion: {} {}", firstName, lastName);
            return new ResponseEntity<>("Person not found", HttpStatus.NOT_FOUND);
        }
    }
}
