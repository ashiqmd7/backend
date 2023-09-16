package com.G2T5203.wingit.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class HomeController {

    @GetMapping("/")
    public String home(Principal principal) {
        // principal prints out the username of the currently authenticated user
        return "Hello, " + principal.getName();
    }

}
