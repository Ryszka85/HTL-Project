package com.ryszka.imageRestApi.controller.readController;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "testing")
public class HandyTestController {

    @GetMapping
    public String fooTest() {
        return "Welcome, you ve made it to foo";
    }
}
