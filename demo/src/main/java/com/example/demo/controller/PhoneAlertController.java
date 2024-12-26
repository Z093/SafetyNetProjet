package com.example.demo.controller;

import com.example.demo.service.PhoneAlertService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class PhoneAlertController {

    private static final Logger logger = LoggerFactory.getLogger(PhoneAlertController.class);

    @Autowired
    private PhoneAlertService phoneAlertService;

    @GetMapping("/phoneAlert")
    public List<String> getPhoneNumbersByFirestation(@RequestParam String firestation) {
        logger.info("Starting phone number search for fire station: {}", firestation);
        return phoneAlertService.getPhoneNumbersByFirestation(firestation);
    }
}
