package com.example.demo.service;

import com.example.demo.model.Person;

public interface PersonService {
    boolean addPerson(Person newPerson);
    boolean updatePerson(Person person);
    boolean deletePerson(String firstName, String lastName);
}
