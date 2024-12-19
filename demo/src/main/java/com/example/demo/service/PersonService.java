package com.example.demo.service;

import com.example.demo.model.Person;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface PersonService {
    ResponseEntity<String> addPerson(Person newPerson);
    ResponseEntity<String> updatePerson(Person person);
    ResponseEntity<String> deletePerson(String firstName, String lastName);
}
