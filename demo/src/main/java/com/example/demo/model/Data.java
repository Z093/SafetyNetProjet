package com.example.demo.model;




import java.util.List;


public class Data {
    private List<Person> persons;
    private List<FireStation> firestations;
    private List<MedicalRecord> medicalrecords;


    public Data() {}

    public Data(List<Person> persons, List<MedicalRecord> medicalrecords, List<FireStation> firestations) {
        this.persons = persons;
        this.medicalrecords = medicalrecords;
        this.firestations = firestations;

    }
    public List<Person> getPersons() {
        return persons;
    }

    public void setPersons(List<Person> persons) {
        this.persons = persons;
    }

    public List<FireStation> getFirestations() {
        return firestations;
    }

    public void setFirestations(List<FireStation> firestations) {
        this.firestations = firestations;
    }

    public List<MedicalRecord> getMedicalrecords() {
        return medicalrecords;
    }

    public void setMedicalrecords(List<MedicalRecord> medicalrecords) {
        this.medicalrecords = medicalrecords;
    }
}

