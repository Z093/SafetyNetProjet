package com.example.demo.service;

import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface PhoneAlertService {
    List<String> getPhoneNumbersByFirestation(String firestation);
}
