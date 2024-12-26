package com.example.demo.service;

import com.example.demo.Utils.DataLoader;
import com.example.demo.model.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CommunityEmailServiceImplTest {

    @InjectMocks
    private CommunityEmailServiceImpl communityEmailServiceImpl;

    @Mock
    private DataLoader dataLoader;

    private List<Person> mockPersonList;

    @BeforeEach
    void setUp() {
        // Initialisation des données de test
        mockPersonList = Arrays.asList(
                  new Person("John", "Doe", "123 Main St", "City1", "00000", "123-456-7890", "john.doe@example.com", 25, null),
                  new Person("Jane", "Doe", "456 Oak St", "City1", "00000", "987-654-3210", "jane.doe@example.com", 30, null),
                  new Person("Bob", "Smith", "789 Pine St", "City2", "00000", "111-222-3333", "bob.smith@example.com", 40, null),
                new Person("Alice", "Johnson", "456 Oak St", "City1", "00000", "333-222-4444", "alice.johnson@example.com", 45, null)

        );
    }

    @Test
    void testGetEmailsByCity_ReturnsEmailsForExistingCity() {
        // Arrange
        String city = "City1";
        when(dataLoader.getPersons()).thenReturn(mockPersonList);

        // Act
        List<String> emails = communityEmailServiceImpl.getEmailsByCity(city);

        // Assert
        assertEquals(3, emails.size()); // John, Jane, Alice (John est dupliqué mais distinct() le retire)
        assertTrue(emails.contains("john.doe@example.com"));
        assertTrue(emails.contains("jane.doe@example.com"));
        assertTrue(emails.contains("alice.johnson@example.com"));
    }

    @Test
    void testGetEmailsByCity_ReturnsEmptyListForNonExistingCity() {
        // Arrange
        String city = "City3";
        when(dataLoader.getPersons()).thenReturn(mockPersonList);

        // Act
        List<String> emails = communityEmailServiceImpl.getEmailsByCity(city);

        // Assert
        assertTrue(emails.isEmpty());
    }

    @Test
    void testGetEmailsByCity_ReturnsEmptyListWhenNoPersonsAvailable() {
        // Arrange
        String city = "City1";
        when(dataLoader.getPersons()).thenReturn(Collections.emptyList());

        // Act
        List<String> emails = communityEmailServiceImpl.getEmailsByCity(city);

        // Assert
        assertTrue(emails.isEmpty());
    }

    @Test
    void testGetEmailsByCity_HandlesExceptionGracefully() {
        // Arrange
        String city = "City1";
        when(dataLoader.getPersons()).thenThrow(new RuntimeException("Database error"));

        // Act
        List<String> emails = communityEmailServiceImpl.getEmailsByCity(city);

        // Assert
        assertTrue(emails.isEmpty()); // On vérifie que la liste retournée est vide en cas d'exception
    }
}
