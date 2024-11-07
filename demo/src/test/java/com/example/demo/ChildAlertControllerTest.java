package com.example.demo;

import com.example.demo.controller.ChildAlertController;
import com.example.demo.model.MedicalRecord;
import com.example.demo.model.Person;
import com.example.demo.modelResponse.ChildAlertResponse;
import com.example.demo.service.DataLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class ChildAlertControllerTest {

    @Mock
    private DataLoader dataLoader;

    @InjectMocks
    private ChildAlertController childAlertController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetChildrenAtAddress_withChildrenAndAdults() {
        // Define persons for the given address
        Person child = new Person("John", "Doe", "123 Main St", "City", "00000", "123-456-7890", "john.doe@example.com", 10, null);
        Person adult = new Person("Jane", "Doe", "123 Main St", "City", "00000", "123-456-7891", "jane.doe@example.com", 15, null);

        // Define medical records for ages
        MedicalRecord childMedicalRecord = new MedicalRecord("John", "Doe", "12/31/2010", List.of(), List.of());
        MedicalRecord adultMedicalRecord = new MedicalRecord("Jane", "Doe", "01/01/1980", List.of(), List.of());

        // Configure mock DataLoader behavior
        when(dataLoader.getPersons()).thenReturn(Arrays.asList(child, adult));
        when(dataLoader.getMedicalRecords()).thenReturn(Arrays.asList(childMedicalRecord, adultMedicalRecord));

        // Call the method with matching address
        List<ChildAlertResponse> response = childAlertController.getChildrenAtAddress("123 Main St");

        // Verify results
        assertEquals(1, response.size());
        assertEquals("John", response.get(0).getFirstName());
        assertEquals("Doe", response.get(0).getLastName());
        assertEquals(13, response.get(0).getAge());
        assertEquals(List.of("Jane Doe"), response.get(0).getOtherMembers());
    }

    @Test
    void testGetChildrenAtAddress_noChildrenAtAddress() {
        // Définir un adulte sans enfant à l'adresse
        Person adult = new Person("Jane", "Doe", "123 Main St", "City", "00000", "123-456-7891", "jane.doe@example.com", 15,null);
        MedicalRecord adultMedicalRecord = new MedicalRecord("Jane", "Doe", "01/01/1980", List.of(), List.of());

        // Configurer le comportement du mock DataLoader
        when(dataLoader.getPersons()).thenReturn(Collections.singletonList(adult));
        when(dataLoader.getMedicalRecords()).thenReturn(Collections.singletonList(adultMedicalRecord));

        // Appeler la méthode testée
        List<ChildAlertResponse> response = childAlertController.getChildrenAtAddress("1234 Elm Street");

        // Vérifier que la réponse est vide (pas d'enfants)
        assertEquals(0, response.size());
    }

    @Test
    void testGetChildrenAtAddress_noPersonsAtAddress() {
        // Configurer le DataLoader pour qu'il retourne une liste vide
        when(dataLoader.getPersons()).thenReturn(Collections.emptyList());

        // Appeler la méthode testée
        List<ChildAlertResponse> response = childAlertController.getChildrenAtAddress("1234 Elm Street");

        // Vérifier que la réponse est vide
        assertEquals(0, response.size());
    }
}
