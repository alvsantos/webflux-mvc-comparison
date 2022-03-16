package com.example.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class PersonController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("person")
    public Person get() {
        return restTemplate.getForEntity("http://localhost:3004/person", Person.class).getBody();
    }
}
