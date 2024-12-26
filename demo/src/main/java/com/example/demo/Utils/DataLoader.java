package com.example.demo.Utils;

import com.example.demo.model.FireStation;
import com.example.demo.model.MedicalRecord;
import com.example.demo.model.Person;
import com.example.demo.model.Data;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class DataLoader {
    private List<Person> persons;
    private List<FireStation> fireStations;
    private List<MedicalRecord> medicalRecords;
@Value("${DataUrl}")
private String DataUrl;
    @PostConstruct
    public void loadData() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        // Charger le fichier JSON
        File file = new File(DataUrl);
        Data data = objectMapper.readValue(file, Data.class);

        this.persons = data.getPersons();
        this.fireStations = data.getFirestations();
        this.medicalRecords = data.getMedicalrecords();
    }

    public List<Person> getPersons() {
        return persons;
    }

    public List<FireStation> getFireStations() {
        return fireStations;
    }

    public List<MedicalRecord> getMedicalRecords() {
        return medicalRecords;
    }
}



