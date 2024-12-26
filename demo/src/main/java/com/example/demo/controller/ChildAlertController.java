package com.example.demo.controller;


import com.example.demo.modelResponse.ChildAlertResponse;
import com.example.demo.service.ChildAlertService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;


@RestController
public class ChildAlertController {

    private static final Logger logger = LoggerFactory.getLogger(ChildAlertController.class);

    @Autowired
    private ChildAlertService childAlertService;

    @GetMapping("/childAlert")
    public List<ChildAlertResponse> getChildrenAtAddress(@RequestParam String address) {
        logger.info("Processing /childAlert request for address: {}", address);
        return childAlertService.getChildrenAtAddress(address);

}
}
