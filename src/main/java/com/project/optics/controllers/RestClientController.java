package com.project.optics.controllers;

import com.project.optics.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/clients")
public class RestClientController {

    @Autowired
    private ClientService clientService;

    @GetMapping("/check-client-name")
    public ResponseEntity<Boolean> checkClientName(
            @RequestParam("firstName") String firstName,
            @RequestParam("secondName") String secondName,
            @RequestParam("thirdName") String thirdName,
            @RequestParam("lastName") String lastName) {

        boolean exists = clientService.clientExists(firstName, secondName, thirdName, lastName);
        return ResponseEntity.ok(exists);
    }
}