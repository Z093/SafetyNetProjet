package com.example.demo.service;

import com.example.demo.Utils.DataLoader;
import com.example.demo.model.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PersonServiceImpl implements PersonService {

    @Autowired
    private DataLoader dataLoader;
    private static final Logger logger = LoggerFactory.getLogger(PersonServiceImpl.class);

    @Override
    public boolean addPerson(Person newPerson){
        logger.info("Attempting to add a new person: {} {}", newPerson.getFirstName(), newPerson.getLastName());

        // Vérifier si la personne existe déjà
        Optional<Person> existingPerson = dataLoader.getPersons().stream()
                .filter(person -> person.getFirstName().equalsIgnoreCase(newPerson.getFirstName()) &&
                        person.getLastName().equalsIgnoreCase(newPerson.getLastName()))
                .findFirst();

        if (existingPerson.isPresent()) {
            logger.error("Person already exists: {} {}", newPerson.getFirstName(), newPerson.getLastName());
            return false;
        }

        dataLoader.getPersons().add(newPerson);  // Ajouter la nouvelle personne
        logger.info("Person successfully added: {} {}", newPerson.getFirstName(), newPerson.getLastName());
        return true;

    }

    public boolean updatePerson(Person updatedPerson){
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
            return true;
        } else {
            logger.error("Person not found for update: {} {}", updatedPerson.getFirstName(), updatedPerson.getLastName());
            return false;
        }

    }

    public boolean deletePerson(String firstName, String lastName){
        logger.info("Attempting to delete person: {} {}", firstName, lastName);

        Optional<Person> existingPerson = dataLoader.getPersons().stream()
                .filter(person -> person.getFirstName().equalsIgnoreCase(firstName) &&
                        person.getLastName().equalsIgnoreCase(lastName))
                .findFirst();

        if (existingPerson.isPresent()) {
            dataLoader.getPersons().remove(existingPerson.get());  // Supprimer la personne
            logger.info("Person successfully deleted: {} {}", firstName, lastName);
            return true;
        } else {
            logger.error("Person not found for deletion: {} {}", firstName, lastName);
            return false;
        }
    }
}
