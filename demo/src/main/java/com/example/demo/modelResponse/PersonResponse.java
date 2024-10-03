package com.example.demo.modelResponse;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class PersonResponse {
    private String firstName;
    private String lastName;
    private String address;
    private String phone;

    public PersonResponse(String firstName, String lastName, String address, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phone = phone;
    }

    // Getters et setters
}
