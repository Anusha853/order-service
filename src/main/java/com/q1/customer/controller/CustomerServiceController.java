package com.q1.customer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.
        bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class CustomerServiceController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/customer/1")
    public String getCustomer() {
        String profileServiceUrl = "http://localhost:8081/profile/1";
        String profileResponse = restTemplate.getForObject(profileServiceUrl, String.class);
        return "Customer service working. Profile service response: " + profileResponse;
    }
}
