package com.example.demo.model;

import lombok.Data;
import lombok.ToString;

public class FireStation {
    private String address;
    private String station;

    public FireStation() {}

    public FireStation(String adress, String station) {
        this.address = adress;
        this.station = station;
    }


    // Getters et setters

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }
}
