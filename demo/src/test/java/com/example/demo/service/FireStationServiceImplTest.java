package com.example.demo.service;

import com.example.demo.Utils.DataLoader;
import com.example.demo.model.FireStation;
import com.example.demo.model.MedicalRecord;
import com.example.demo.model.Person;
import com.example.demo.modelResponse.FireStationResponse;
import com.example.demo.modelResponse.PersonResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FireStationServiceImplTest {

    @InjectMocks
    private FireStationServiceImpl fireStationServiceImpl;

    @Mock
    private DataLoader dataLoader;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetPersonsByStation_withPersons() {
        // Données de test
        FireStation fireStation1 = new FireStation("123 Main St", "1");
        FireStation fireStation2 = new FireStation("456 Oak St", "2");

        MedicalRecord medicalRecord1 = new MedicalRecord("John", "Doe", "05/20/2000", Arrays.asList("med1"), Arrays.asList("allergy1"));
        MedicalRecord medicalRecord2 = new MedicalRecord("Jane", "Doe", "09/15/2015", Arrays.asList("med2"), Arrays.asList("allergy2"));

        Person person1 = new Person("John", "Doe", "123 Main St", "City", "00000", "123-456-7890", "john.doe@example.com", 25, medicalRecord1);
        Person person2 = new Person("Jane", "Doe", "123 Main St", "City", "00000", "987-654-3210", "jane.doe@example.com", 8, medicalRecord2);

        // Mock des méthodes de DataLoader
        when(dataLoader.getFireStations()).thenReturn(Arrays.asList(fireStation1, fireStation2));
        when(dataLoader.getPersons()).thenReturn(Arrays.asList(person1, person2));
        when(dataLoader.getMedicalRecords()).thenReturn(Arrays.asList(medicalRecord1, medicalRecord2));

        // Appel de la méthode à tester
        FireStationResponse response = fireStationServiceImpl.getPersonsByStation("1");

        // Vérifications
        assertEquals(2, response.getPersons().size()); // On doit retrouver 2 personnes
        assertEquals(1, response.getNumberOfAdults()); // John est un adulte (25 ans)
        assertEquals(1, response.getNumberOfChildren()); // Jane est un enfant (8 ans)

        // Vérification des informations de la personne
        PersonResponse personResponse1 = response.getPersons().get(0);
        assertEquals("John", personResponse1.getFirstName());
        assertEquals("Doe", personResponse1.getLastName());
        assertEquals("123 Main St", personResponse1.getAddress());
        assertEquals("123-456-7890", personResponse1.getPhone());
    }

    @Test
    public void testGetPersonsByStation_noPersons() {
        // Mock des méthodes de DataLoader avec des données vides
        when(dataLoader.getFireStations()).thenReturn(Collections.emptyList());
        when(dataLoader.getPersons()).thenReturn(Collections.emptyList());

        // Appel de la méthode à tester
        FireStationResponse response = fireStationServiceImpl.getPersonsByStation("1");

        // Vérifications
        assertEquals(0, response.getPersons().size()); // Aucune personne n'est retournée
        assertEquals(0, response.getNumberOfAdults()); // Aucun adulte
        assertEquals(0, response.getNumberOfChildren()); // Aucun enfant
    }

    @Test
    public void testGetPersonsByStation_noMedicalRecord() {
        // Données de test
        FireStation fireStation1 = new FireStation("123 Main St", "1");
        Person person1 = new Person("John", "Doe", "123 Main St", "City", "00000", "123-456-7890", "john.doe@example.com", 25, null);

        // Mock des méthodes de DataLoader
        when(dataLoader.getFireStations()).thenReturn(Collections.singletonList(fireStation1));
        when(dataLoader.getPersons()).thenReturn(Collections.singletonList(person1));
        when(dataLoader.getMedicalRecords()).thenReturn(Collections.emptyList()); // Aucun dossier médical

        // Appel de la méthode à tester
        FireStationResponse response = fireStationServiceImpl.getPersonsByStation("1");

        // Vérifications
        assertEquals(1, response.getPersons().size()); // 1 personne retournée
        assertEquals(0, response.getNumberOfAdults()); // L'âge ne peut pas être calculé sans le dossier médical
        assertEquals(1, response.getNumberOfChildren()); // La personne est considérée comme enfant par défaut
    }
}
