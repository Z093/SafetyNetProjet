package com.example.demo.modelResponse;



import java.util.List;


public class ChildAlertResponse {

        private String firstName;
        private String lastName;
        private int age;
        private List<String> otherMembers;

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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public List<String> getOtherMembers() {
        return otherMembers;
    }

    public void setOtherMembers(List<String> otherMembers) {
        this.otherMembers = otherMembers;
    }

    public ChildAlertResponse(String firstName, String lastName, int age, List<String> otherMembers) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.age = age;
            this.otherMembers = otherMembers;
        }
}
