package com.example.demo.modelResponse;

import lombok.Data;
import lombok.ToString;

import java.util.List;

// Classe pour représenter les informations des résidents

public class PersonInfoResponse {

        private String firstName;
        private String lastName;
        private String address;
        private String email;
        private int age;
        private List<String> medications;
        private List<String> allergies;

        public PersonInfoResponse(String firstName, String lastName, String address, String email, int age,
                           List<String> medications, List<String> allergies) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.address = address;
            this.email = email;
            this.age = age;
            this.medications = medications;
            this.allergies = allergies;
        }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public List<String> getMedications() {
        return medications;
    }

    public void setMedications(List<String> medications) {
        this.medications = medications;
    }

    public List<String> getAllergies() {
        return allergies;
    }

    public void setAllergies(List<String> allergies) {
        this.allergies = allergies;
    }

    // Getters et setters
    }

