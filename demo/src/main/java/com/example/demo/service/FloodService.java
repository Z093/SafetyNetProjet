package com.example.demo.service;

import com.example.demo.modelResponse.FloodResponse;

import java.util.List;
import java.util.Map;

public interface FloodService {
    Map<String, List<FloodResponse>> getHouseholdsByStations(List<String> stations);
}
