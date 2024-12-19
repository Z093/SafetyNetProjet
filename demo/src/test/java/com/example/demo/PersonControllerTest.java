package com.example.demo;

import com.example.demo.controller.PersonController;
import com.example.demo.model.Person;
import com.example.demo.Utils.DataLoader;
import com.example.demo.service.PersonServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class PersonControllerTest {

    @Mock
    private DataLoader dataLoader;

    @InjectMocks
    //private PersonController personController;
    private PersonServiceImpl personServiceImpl;

    private List<Person> personList;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize a sample list of persons for testing
        personList = new ArrayList<>();
        Person person1 = new Person("John", "Doe", "123 Main St", "New York", "10001", "123-456-7890", "john.doe@example.com", 25, null);
        personList.add(person1);

        // Mock the getPersons method to return the test data
        when(dataLoader.getPersons()).thenReturn(personList);
    }

    @Test
    void testAddPerson_Success() {
        // Arrange
        Person newPerson = new Person("Jane", "Smith", "456 Maple St", "Los Angeles", "90001", "987-654-3210", "jane.doe@example.com", 30, null);

        // Act
        ResponseEntity<String> response = personServiceImpl.addPerson(newPerson);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Person added successfully", response.getBody());
        verify(dataLoader, times(2)).getPersons(); // Ensure called once
    }

    @Test
    void testAddPerson_Conflict() {
        Person duplicatePerson = new Person("John", "Doe", "789 Elm St", "Chicago", "60601", "555-123-4567", "johndoe@example.com", 30, null);

        ResponseEntity<String> response = personServiceImpl.addPerson(duplicatePerson);

        // Verify
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Person already exists", response.getBody());
        verify(dataLoader, times(1)).getPersons();
    }

    @Test
    void testUpdatePerson_Success() {
        Person updatedPerson = new Person("John", "Doe", "789 Elm St", "Chicago", "60601", "555-123-4567", "johndoe@example.com", 30, null);

        ResponseEntity<String> response = personServiceImpl.updatePerson(updatedPerson);

        // Verify
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Person updated successfully", response.getBody());
        verify(dataLoader, times(1)).getPersons();
    }

    @Test
    void testUpdatePerson_NotFound() {
        Person nonExistentPerson = new Person("Jane", "Doe", "456 Maple St", "San Francisco", "94101", "987-654-3210", "jane.doe@example.com", 30, null);

        ResponseEntity<String> response = personServiceImpl.updatePerson(nonExistentPerson);

        // Verify
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Person not found", response.getBody());
        verify(dataLoader, times(1)).getPersons();
    }

    @Test
    void testDeletePerson_Success() {
        ResponseEntity<String> response = personServiceImpl.deletePerson("John", "Doe");

        // Verify
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Person deleted successfully", response.getBody());
        verify(dataLoader, times(2)).getPersons();
    }

    @Test
    void testDeletePerson_NotFound() {
        ResponseEntity<String> response = personServiceImpl.deletePerson("Jane", "Doe");

        // Verify
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Person not found", response.getBody());
        verify(dataLoader, times(1)).getPersons();
    }
}
