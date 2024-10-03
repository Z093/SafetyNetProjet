package com.example.demo.model;

import lombok.ToString;

import java.util.List;

@lombok.Data
@ToString
public class Data {
    private List<Person> persons;
    private List<FireStation> firestations;
    private List<MedicalRecord> medicalrecords;
}
