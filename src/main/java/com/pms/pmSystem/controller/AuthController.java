package com.pms.pmSystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.pms.pmSystem.model.Users;
import com.pms.pmSystem.service.authentication.UserService;


@RestController
public class AuthController {
    @Autowired
    UserService service;

    @PostMapping("/register")
    public Users register(@RequestBody Users users){
        return service.register(users);
    }

    @PostMapping("/login")
    public String login(@RequestBody Users users) {
        System.out.println(users);
        return service.verify(users);
    }
    
}
