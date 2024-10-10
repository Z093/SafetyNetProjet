package com.example.demo.controller;

import com.example.demo.model.Person;
import com.example.demo.service.DataLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/person")
public class PersonController {
    @Autowired
    private DataLoader dataLoader; // Service simulant la base de données (ou une vraie base)

    // Ajouter une nouvelle personne
    @PostMapping
    public ResponseEntity<String> addPerson(@RequestBody Person newPerson) {
        // Vérifier si la personne existe déjà
        Optional<Person> existingPerson = dataLoader.getPersons().stream()
                .filter(person -> person.getFirstName().equalsIgnoreCase(newPerson.getFirstName()) &&
                        person.getLastName().equalsIgnoreCase(newPerson.getLastName()))
                .findFirst();

        if (existingPerson.isPresent()) {
            return new ResponseEntity<>("Person already exists", HttpStatus.CONFLICT);
        }

        dataLoader.getPersons().add(newPerson);  // Ajouter la nouvelle personne
        return new ResponseEntity<>("Person added successfully", HttpStatus.CREATED);
    }

    // Mettre à jour une personne existante
    @PutMapping("/person")
    public ResponseEntity<String> updatePerson(@RequestBody Person updatedPerson) {
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

            return new ResponseEntity<>("Person updated successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Person not found", HttpStatus.NOT_FOUND);
        }
    }

    // Supprimer une personne
    @DeleteMapping("/person")
    public ResponseEntity<String> deletePerson(@RequestParam String firstName, @RequestParam String lastName) {
        Optional<Person> existingPerson = dataLoader.getPersons().stream()
                .filter(person -> person.getFirstName().equalsIgnoreCase(firstName) &&
                        person.getLastName().equalsIgnoreCase(lastName))
                .findFirst();

        if (existingPerson.isPresent()) {
            dataLoader.getPersons().remove(existingPerson.get());  // Supprimer la personne
            return new ResponseEntity<>("Person deleted successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Person not found", HttpStatus.NOT_FOUND);
        }
    }
}


