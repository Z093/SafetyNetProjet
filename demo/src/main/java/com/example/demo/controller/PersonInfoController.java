package com.example.demo.controller;


import com.example.demo.modelResponse.PersonInfoResponse;
import com.example.demo.service.PersonInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;

@RestController
public class PersonInfoController {

    private static final Logger logger = LoggerFactory.getLogger(PersonInfoController.class);

    @Autowired
    private PersonInfoService personInfoService;

    @GetMapping("/personInfo")
    public List<PersonInfoResponse> getPersonInfoByLastName(@RequestParam String lastName) {
        logger.info("Fetching person info for last name: {}", lastName);
        return personInfoService.getPersonInfoByLastName(lastName);
}
}
