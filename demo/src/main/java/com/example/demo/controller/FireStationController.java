package com.example.demo.controller;

import com.example.demo.model.MedicalRecord;
import com.example.demo.model.Person;
import com.example.demo.modelResponse.PersonResponse;
import com.example.demo.modelResponse.FireStationResponse;
import com.example.demo.model.FireStation;
import com.example.demo.Utils.DataLoader;
import com.example.demo.service.FireStationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class FireStationController {

    private static final Logger logger = LoggerFactory.getLogger(FireStationController.class);

    @Autowired
    private FireStationService fireStationService;

    @GetMapping("/firestation")
    public FireStationResponse getPersonsByStation(@RequestParam String stationNumber) {
        logger.info("Received request for fire station: {}", stationNumber);
        return fireStationService.getPersonsByStation(stationNumber);
    }

}
