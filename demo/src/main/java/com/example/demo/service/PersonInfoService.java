package com.example.demo.service;

import com.example.demo.modelResponse.PersonInfoResponse;

import java.util.List;

public interface PersonInfoService {
    List<PersonInfoResponse> getPersonInfoByLastName(String lastName);
}
