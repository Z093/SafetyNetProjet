package com.example.demo;


import com.example.demo.controller.PersonInfoController;
import com.example.demo.model.MedicalRecord;
import com.example.demo.model.Person;
import com.example.demo.service.DataLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.Arrays;
import java.util.List;

import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(PersonInfoController.class)
public class PersonInfoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DataLoader dataLoader;

    private Person person1;
    private Person person2;
    private MedicalRecord medicalRecord1;
    private MedicalRecord medicalRecord2;

    @BeforeEach
    public void setUp() {
        // Création de deux personnes avec le même nom de famille mais des informations différentes
        medicalRecord1 = new MedicalRecord("John", "Doe","01/01/2000", List.of("aspirin"), List.of("penicillin"));
        medicalRecord2 = new MedicalRecord("Jane", "Doe","01/01/2000", List.of("ibuprofen"), List.of("gluten"));

        person1 = new Person("John", "Doe", "123 Main St", "City", "00000", "123-456-7890", "john.doe@example.com", 25, medicalRecord1);
        person2 = new Person("Jane", "Doe", "456 Oak St", "City", "00000", "987-654-3210", "jane.doe@example.com", 30, medicalRecord2);
    }

    // Test 1: Retourne les informations pour les personnes ayant le nom "Doe"
    @Test
    public void testGetPersonInfoByLastName_withValidLastName() throws Exception {
        when(dataLoader.getPersons()).thenReturn(List.of(person1, person2));
        when(dataLoader.getMedicalRecords()).thenReturn(List.of(medicalRecord1, medicalRecord2));


        mockMvc.perform(get("/personInfo?lastName=Doe")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("John")))
                .andExpect(content().string(containsString("Jane")))
                .andExpect(content().string(containsString("123 Main St")))
                .andExpect(content().string(containsString("456 Oak St")))
                .andExpect(content().string(containsString("aspirin")))
                .andExpect(content().string(containsString("ibuprofen")))
                .andExpect(content().string(containsString("penicillin")))
                .andExpect(content().string(containsString("gluten")));



    }

    // Test 2: Retourne une réponse vide si aucun nom ne correspond
    @Test
    public void testGetPersonInfoByLastName_withInvalidLastName() throws Exception {
        when(dataLoader.getPersons()).thenReturn(Arrays.asList(person1, person2));

        mockMvc.perform(get("/personInfo?lastName=toto")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }

    // Test 3: Retourne 400 Bad Request si le paramètre lastName est manquant
    @Test
    public void testGetPersonInfoByLastName_missingLastName() throws Exception {
        mockMvc.perform(get("/personInfo")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}