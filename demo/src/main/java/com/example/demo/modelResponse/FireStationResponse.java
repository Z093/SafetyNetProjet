package com.example.demo.modelResponse;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class FireStationResponse {
    private List<PersonResponse> persons;
    private long numberOfAdults;
    private long numberOfChildren;

    public FireStationResponse(List<PersonResponse> persons, long numberOfAdults, long numberOfChildren) {
        this.persons = persons;
        this.numberOfAdults = numberOfAdults;
        this.numberOfChildren = numberOfChildren;
    }

    public long getNumberOfChildren() {
        return numberOfChildren;
    }

    public void setNumberOfChildren(long numberOfChildren) {
        this.numberOfChildren = numberOfChildren;
    }

    public long getNumberOfAdults() {
        return numberOfAdults;
    }

    public void setNumberOfAdults(long numberOfAdults) {
        this.numberOfAdults = numberOfAdults;
    }

    public List<PersonResponse> getPersons() {
        return persons;
    }

    public void setPersons(List<PersonResponse> persons) {
        this.persons = persons;
    }

}
