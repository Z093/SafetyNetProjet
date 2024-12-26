package com.example.demo.controller;


import com.example.demo.service.CommunityEmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CommunityEmailController {

    private static final Logger logger = LoggerFactory.getLogger(CommunityEmailController.class);

    @Autowired
    private CommunityEmailService communityEmailService;

    @GetMapping("/communityEmail")
    public List<String> getEmailsByCity(@RequestParam String city) {
        logger.info("Processing /communityEmail request for city: {}", city);

        return communityEmailService.getEmailsByCity(city);
    }
}
