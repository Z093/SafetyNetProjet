package com.example.demo.controller;

import com.example.demo.model.MedicalRecord;
import com.example.demo.modelResponse.PersonInfo;
import com.example.demo.Utils.DataLoader;
import com.example.demo.model.FireStation;
import com.example.demo.modelResponse.FireResponse;
import com.example.demo.service.FireService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class FireController {

    private static final Logger logger = LoggerFactory.getLogger(FireController.class);

    @Autowired
    private FireService fireService;

    @GetMapping("/fire")
    public FireResponse getPersonsAtAddress(@RequestParam String address) {
        logger.info("Processing /fire request for address: {}", address);

        return fireService.getPersonsAtAddress(address);
    }
}
