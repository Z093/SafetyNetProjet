package com.example.demo.service;

import com.example.demo.modelResponse.ChildAlertResponse;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;

public interface ChildAlertService {
     List<ChildAlertResponse> getChildrenAtAddress(String address);
}
