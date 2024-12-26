package com.example.demo.controller;


import com.example.demo.modelResponse.FireStationResponse;
import com.example.demo.service.FireStationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;



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
