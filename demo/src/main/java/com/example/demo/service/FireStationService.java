package com.example.demo.service;

import com.example.demo.modelResponse.FireStationResponse;


public interface FireStationService {
     FireStationResponse getPersonsByStation(String stationNumber);
}
