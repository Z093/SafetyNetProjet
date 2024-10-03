package com.example.demo.modelResponse;

import lombok.Data;
import lombok.ToString;

import java.util.List;

// Classe pour représenter les informations des résidents
@Data
@ToString
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

        // Getters et setters
    }

