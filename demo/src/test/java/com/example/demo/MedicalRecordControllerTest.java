package com.example.demo.controller;

import com.example.demo.model.MedicalRecord;
import com.example.demo.service.DataLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MedicalRecordController.class)
public class MedicalRecordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DataLoader dataLoader;

    private List<MedicalRecord> medicalRecords;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize a sample list of medical records
        medicalRecords = new ArrayList<>();
        MedicalRecord record1 = new MedicalRecord("John", "Doe", "1990-01-01", List.of("Med1"), List.of("Allergy1"));
        MedicalRecord record2 = new MedicalRecord("Jane", "Smith", "1985-05-15", List.of("Med2"), List.of("Allergy2"));
        medicalRecords.add(record1);
        medicalRecords.add(record2);

        // Mock the dataLoader to return our sample list
        when(dataLoader.getMedicalRecords()).thenReturn(medicalRecords);
    }

    @Test
    public void testAddMedicalRecord_success() throws Exception {
        MedicalRecord newRecord = new MedicalRecord("Alice", "Johnson", "1992-07-20", List.of("Med3"), List.of("Allergy3"));

        mockMvc.perform(post("/medicalRecord/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\": \"Alice\", \"lastName\": \"Johnson\", \"birthdate\": \"1992-07-20\", \"medications\": [\"Med3\"], \"allergies\": [\"Allergy3\"]}"))
                .andExpect(status().isCreated())
                .andExpect(content().string("Medical record added successfully"));

        // Expect `getMedicalRecords()` to be called twice: once to check existence, once to add
        verify(dataLoader, times(2)).getMedicalRecords();
    }


    @Test
    public void testAddMedicalRecord_conflict() throws Exception {
        // Adding an existing medical record
        mockMvc.perform(post("/medicalRecord/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\": \"John\", \"lastName\": \"Doe\", \"birthdate\": \"1990-01-01\", \"medications\": [\"Med1\"], \"allergies\": [\"Allergy1\"]}"))
                .andExpect(status().isConflict())
                .andExpect(content().string("Medical record already exists for this person"));
    }

    @Test
    public void testUpdateMedicalRecord_success() throws Exception {
        // Updating an existing record
        mockMvc.perform(put("/medicalRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\": \"John\", \"lastName\": \"Doe\", \"birthdate\": \"1990-01-01\", \"medications\": [\"UpdatedMed\"], \"allergies\": [\"UpdatedAllergy\"]}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Medical record updated successfully"));
    }

    @Test
    public void testUpdateMedicalRecord_notFound() throws Exception {
        // Attempt to update a non-existing record
        mockMvc.perform(put("/medicalRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\": \"Nonexistent\", \"lastName\": \"Person\", \"birthdate\": \"2000-01-01\", \"medications\": [\"Med4\"], \"allergies\": [\"Allergy4\"]}"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Medical record not found"));
    }

    @Test
    public void testDeleteMedicalRecord_success() throws Exception {
        // Delete an existing record
        mockMvc.perform(delete("/medicalRecord/delete")
                        .param("firstName", "Jane")
                        .param("lastName", "Smith"))
                .andExpect(status().isOk())
                .andExpect(content().string("Medical record deleted successfully"));
    }

    @Test
    public void testDeleteMedicalRecord_notFound() throws Exception {
        // Attempt to delete a non-existing record
        mockMvc.perform(delete("/medicalRecord/delete")
                        .param("firstName", "Nonexistent")
                        .param("lastName", "Person"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Medical record not found"));
    }
}
