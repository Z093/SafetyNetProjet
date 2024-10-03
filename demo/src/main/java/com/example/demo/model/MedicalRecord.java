package com.example.demo.model;

import lombok.Data;
import lombok.ToString;

import java.util.List;


@Data
@ToString
public class MedicalRecord {
    private String firstName;
    private String lastName;
    private String birthdate;
    private List<String> medications;
    private List<String> allergies;

    // Getters et setters
}
