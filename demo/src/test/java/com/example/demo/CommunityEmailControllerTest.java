package com.example.demo;

import com.example.demo.controller.CommunityEmailController;
import com.example.demo.model.Person;
import com.example.demo.service.DataLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommunityEmailController.class)
public class CommunityEmailControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DataLoader dataLoader;

    private Person person1;
    private Person person2;
    private Person person3;

    @BeforeEach
    public void setUp() {
        // Création de quelques personnes avec des villes et emails différents
        person1 = new Person("John", "Doe", "123 Main St", "City1", "00000", "123-456-7890", "john.doe@example.com", 25,null);
        person2 = new Person("Jane", "Doe", "456 Oak St", "City1", "00000", "987-654-3210", "jane.doe@example.com", 30,null);
        person3 = new Person("Bob", "Smith", "789 Pine St", "City2", "00000", "111-222-3333", "bob.smith@example.com", 40,null);
    }

    // Test 1: Retourne les emails pour une ville valide
    @Test
    public void testGetEmailsByCity_withValidCity() throws Exception {
        when(dataLoader.getPersons()).thenReturn(Arrays.asList(person1, person2, person3));

        mockMvc.perform(get("/communityEmail?city=City1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("[\"john.doe@example.com\",\"jane.doe@example.com\"]"));
    }

    // Test 2: Retourne une réponse vide si aucune personne n'est trouvée dans la ville
    @Test
    public void testGetEmailsByCity_withNoPersonsInCity() throws Exception {
        when(dataLoader.getPersons()).thenReturn(Arrays.asList(person1, person2, person3));

        mockMvc.perform(get("/communityEmail?city=City3")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }

    // Test 3: Retourne 400 Bad Request si le paramètre city est manquant
    @Test
    public void testGetEmailsByCity_missingCityParam() throws Exception {
        mockMvc.perform(get("/communityEmail")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
