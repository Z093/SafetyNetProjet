package com.example.demo.modelResponse;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class ChildAlertResponse {

        private String firstName;
        private String lastName;
        private int age;
        private List<String> otherMembers;

        public ChildAlertResponse(String firstName, String lastName, int age, List<String> otherMembers) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.age = age;
            this.otherMembers = otherMembers;
        }
}
