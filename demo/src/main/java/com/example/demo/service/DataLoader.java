package com.example.demo.service;


import com.example.demo.model.FireStation;
import com.example.demo.model.MedicalRecord;
import com.example.demo.model.Person;
import com.example.demo.model.Data;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class DataLoader {
    private List<Person> persons;
    //private List<Person> persons = new ArrayList<>();
    private List<FireStation> fireStations;
    private List<MedicalRecord> medicalRecords;

    @PostConstruct
    public void loadData() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        // Charger le fichier JSON
        File file = new File("/Users/ibenhamouda/git/demo/src/main/resources/static/data.json");
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


/*@lombok.Data
@ToString
class Data {
    private List<Person> persons;
    private List<FireStation> firestations;
    private List<MedicalRecord> medicalrecords;

    // Getters et setters
}*/
