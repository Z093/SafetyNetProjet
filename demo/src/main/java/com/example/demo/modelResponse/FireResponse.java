package com.example.demo.modelResponse;



import java.util.List;


    public class FireResponse {
        private String fireStationNumber;
        private List<PersonInfo> residents;

        public FireResponse(String fireStationNumber, List<PersonInfo> residents) {
            this.fireStationNumber = fireStationNumber;
            this.residents = residents;
        }

        public String getFireStationNumber() {
            return fireStationNumber;
        }

        public void setFireStationNumber(String fireStationNumber) {
            this.fireStationNumber = fireStationNumber;
        }

        public List<PersonInfo> getResidents() {
            return residents;
        }

        public void setResidents(List<PersonInfo> residents) {
            this.residents = residents;
        }



        // Getters et setters
    }

