package com.example.demo.controller;


import com.example.demo.modelResponse.FloodResponse;

import com.example.demo.service.FloodService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;
import java.util.Map;


@RestController
public class FloodController {

    private static final Logger logger = LoggerFactory.getLogger(FloodController.class);

    @Autowired
    private FloodService floodService;

    @GetMapping("/flood/stations")
    public Map<String, List<FloodResponse>> getHouseholdsByStations(@RequestParam List<String> stations) {
        logger.info("Received request for households covered by fire stations: {}", stations);
        return floodService.getHouseholdsByStations(stations);
    }
}
