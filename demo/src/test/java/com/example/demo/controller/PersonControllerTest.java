package com.example.demo.controller;

import com.example.demo.model.Person;
import com.example.demo.service.PersonService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class PersonControllerTest {

    @Mock
    private PersonService personService;

    @InjectMocks
    private PersonController personController;

    public PersonControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddPerson() {
        Person person = new Person();
        person.setFirstName("John");
        person.setLastName("Doe");

        when(personService.addPerson(person)).thenReturn(ResponseEntity.ok("Person added successfully").hasBody());

        ResponseEntity<String> response = personController.addPerson(person);

        //assertTrue(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Person added successfully", response.getBody());
        verify(personService, times(1)).addPerson(person);
    }

    @Test
    void testUpdatePerson() {
        Person person = new Person();
        person.setFirstName("Jane");
        person.setLastName("Doe");

        when(personService.updatePerson(person)).thenReturn(ResponseEntity.ok("Person updated successfully").hasBody());

        ResponseEntity<String> response = personController.updatePerson(person);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Person updated successfully", response.getBody());
        verify(personService, times(1)).updatePerson(person);
    }

    @Test
    void testDeletePerson() {
        String firstName = "John";
        String lastName = "Doe";

        when(personService.deletePerson(firstName, lastName)).thenReturn(ResponseEntity.ok("Person deleted successfully").hasBody());

        ResponseEntity<String> response = personController.deletePerson(firstName, lastName);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Person deleted successfully", response.getBody());
        verify(personService, times(1)).deletePerson(firstName, lastName);
    }
}

