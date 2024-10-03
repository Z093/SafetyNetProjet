package com.example.demo.model;


import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Person {
    private String firstName;
    private String lastName;
    private String address;
    private String city;
    private String zip;
    private String phone;
    private String email;

    // Getters et setters
}

