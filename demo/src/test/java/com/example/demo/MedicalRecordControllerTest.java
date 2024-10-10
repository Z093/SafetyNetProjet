package com.example.demo;

import com.example.demo.controller.MedicalRecordController;
import com.example.demo.model.MedicalRecord;
import com.example.demo.service.DataLoader;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.RequestMatcher;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;


import static org.mockito.Mockito.when;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@WebMvcTest(MedicalRecordController.class)
public class MedicalRecordControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DataLoader dataLoader;

    private static MedicalRecord record1;
    private static MedicalRecord record2;

    @BeforeAll
    public static void setUp() throws Exception {
        record1 = new MedicalRecord("John", "Doe", "01/01/1980",
                Arrays.asList("Aspirin:100mg"), Arrays.asList("Peanut"));
        record2 = new MedicalRecord("Jane", "Doe", "02/02/1990",
                Arrays.asList("Ibuprofen:200mg"), Arrays.asList("Seafood"));
    }

    // Test POST - Ajouter un dossier médical
    @Test
    public void testAddMedicalRecord() throws Exception {
        when(dataLoader.getMedicalRecords()).thenReturn(Arrays.asList(record1));

        String newRecordJson = "{ \"firstName\": \"Michael\", \"lastName\": \"Smith\", \"birthdate\": \"03/03/2000\", \"medications\": [\"Tylenol:500mg\"], \"allergies\": [\"Dust\"] }";

        mockMvc.perform(post("/medicalRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newRecordJson))
                .andExpect(status().isCreated())
                .andExpect((ResultMatcher) content().string("Medical record added successfully"));
    }

    // Test PUT - Mettre à jour un dossier médical existant
    @Test
    public void testUpdateMedicalRecord() throws Exception {
        when(dataLoader.getMedicalRecords()).thenReturn(Arrays.asList(record1, record2));

        String updatedRecordJson = "{ \"firstName\": \"John\", \"lastName\": \"Doe\", \"birthdate\": \"01/01/1981\", \"medications\": [\"Tylenol:1000mg\"], \"allergies\": [\"Dust\"] }";

        mockMvc.perform(put("/medicalRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedRecordJson))
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) content().string("Medical record updated successfully"));
    }

    // Test DELETE - Supprimer un dossier médical
    @Test
    public void testDeleteMedicalRecord() throws Exception {
        when(dataLoader.getMedicalRecords()).thenReturn(Arrays.asList(record1, record2));
        Mockito.doAnswer(invocation -> {
            dataLoader.getMedicalRecords().remove(record1);
            return null;
        }).when(dataLoader).getMedicalRecords();

        mockMvc.perform(delete("/medicalRecord")
                        .param("firstName", "John")
                        .param("lastName", "Doe"))
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) content().string("Medical record deleted successfully"));
    }
}
