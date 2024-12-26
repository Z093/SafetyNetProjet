package com.example.demo.service;

import com.example.demo.Utils.DataLoader;
import com.example.demo.model.MedicalRecord;
import com.example.demo.model.Person;
import com.example.demo.modelResponse.PersonInfoResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PersonInfoServiceImplTest {

    @InjectMocks
    private PersonInfoServiceImpl personInfoServiceImpl;

    @Mock
    private DataLoader dataLoader;

    private List<Person> mockPersons;
    private List<MedicalRecord> mockMedicalRecords;

    @BeforeEach
    void setUp() {
        // Mock des personnes
        mockPersons = Arrays.asList(
                new Person("John", "Doe", "123 Main St", "City", "00000", "123-456-7890", "john.doe@example.com", 25, null),
                new Person("Jane", "Doe", "456 Oak St", "City", "00000", "987-654-3210", "jane.doe@example.com", 30, null)

        );



        // Mock des dossiers médicaux
        mockMedicalRecords = Arrays.asList(
                new MedicalRecord("John", "Doe", "01/15/1985", Arrays.asList("Aspirin"), Arrays.asList("Peanut")),
                new MedicalRecord("Jane", "Doe", "06/22/1990", Arrays.asList("Ibuprofen"), Arrays.asList("Pollen"))
        );
    }

    @Test
    void testGetPersonInfoByLastName_PersonFound() {
        // Configuration du comportement du mock DataLoader
        when(dataLoader.getPersons()).thenReturn(mockPersons);
        when(dataLoader.getMedicalRecords()).thenReturn(mockMedicalRecords);

        // Appel de la méthode de service
        List<PersonInfoResponse> result = personInfoServiceImpl.getPersonInfoByLastName("Doe");

        // Assertions
        assertEquals(2, result.size());

        PersonInfoResponse personInfoJohn = result.get(0);
        assertEquals("John", personInfoJohn.getFirstName());
        assertEquals("Doe", personInfoJohn.getLastName());
        assertEquals("123 Main St", personInfoJohn.getAddress());
        assertEquals("john.doe@example.com", personInfoJohn.getEmail());
        assertTrue(personInfoJohn.getAge() > 0); // On vérifie que l'âge est calculé
        assertEquals(1, personInfoJohn.getMedications().size());
        assertTrue(personInfoJohn.getMedications().contains("Aspirin"));
        assertEquals(1, personInfoJohn.getAllergies().size());
        assertTrue(personInfoJohn.getAllergies().contains("Peanut"));

        PersonInfoResponse personInfoJane = result.get(1);
        assertEquals("Jane", personInfoJane.getFirstName());
        assertEquals("Doe", personInfoJane.getLastName());
        assertEquals("456 Oak St", personInfoJane.getAddress());
        assertEquals("jane.doe@example.com", personInfoJane.getEmail());
        assertTrue(personInfoJane.getAge() > 0); // On vérifie que l'âge est calculé
        assertEquals(1, personInfoJane.getMedications().size());
        assertTrue(personInfoJane.getMedications().contains("Ibuprofen"));
        assertEquals(1, personInfoJane.getAllergies().size());
        assertTrue(personInfoJane.getAllergies().contains("Pollen"));
    }

    @Test
    void testGetPersonInfoByLastName_PersonNotFound() {
        // Configuration du comportement du mock DataLoader
        when(dataLoader.getPersons()).thenReturn(mockPersons);

        // Appel de la méthode de service avec un nom de famille inexistant
        List<PersonInfoResponse> result = personInfoServiceImpl.getPersonInfoByLastName("Smith");

        // Assertions
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetPersonInfoByLastName_NoMedicalRecord() {
        // Supposons qu'une personne existe sans dossier médical
        mockPersons = Arrays.asList(
                new Person("John", "Smith", "789 Maple Ave", "City", "00000", "333-456-7890", "john.smith@email.com", 25, null)
        );

        // Configuration du comportement du mock DataLoader
        when(dataLoader.getPersons()).thenReturn(mockPersons);
        when(dataLoader.getMedicalRecords()).thenReturn(Collections.emptyList()); // Pas de dossier médical

        // Appel de la méthode de service
        List<PersonInfoResponse> result = personInfoServiceImpl.getPersonInfoByLastName("Smith");

        // Assertions
        assertEquals(1, result.size());
        PersonInfoResponse personInfo = result.get(0);
        assertEquals("John", personInfo.getFirstName());
        assertEquals("Smith", personInfo.getLastName());
        assertEquals("789 Maple Ave", personInfo.getAddress());
        assertEquals("john.smith@email.com", personInfo.getEmail());
        assertEquals(0, personInfo.getAge()); // Pas de dossier médical, donc âge = 0
        assertTrue(personInfo.getMedications().isEmpty());
        assertTrue(personInfo.getAllergies().isEmpty());
    }

    @Test
    void testCalculateAge() {
        // Test de la méthode de calcul de l'âge
        int age = personInfoServiceImpl.calculateAge("01/01/2000");
        assertTrue(age > 0);
    }
}
