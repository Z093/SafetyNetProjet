package com.example.demo.modelResponse;

import lombok.Data;
import lombok.ToString;

import java.util.List;

    @Data
    @ToString
    public class FireResponse {
        private String fireStationNumber;
        private List<PersonInfo> residents;

        public FireResponse(String fireStationNumber, List<PersonInfo> residents) {
            this.fireStationNumber = fireStationNumber;
            this.residents = residents;
        }

        // Getters et setters
    }

