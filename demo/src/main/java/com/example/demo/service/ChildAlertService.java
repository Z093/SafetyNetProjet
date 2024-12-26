package com.example.demo.service;

import com.example.demo.modelResponse.ChildAlertResponse;

import java.util.List;

public interface ChildAlertService {
     List<ChildAlertResponse> getChildrenAtAddress(String address);
}
