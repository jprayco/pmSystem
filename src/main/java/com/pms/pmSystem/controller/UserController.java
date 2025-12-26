package com.pms.pmSystem.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pms.pmSystem.projection.UserLoginProjection;
import com.pms.pmSystem.service.authentication.UserService;


@RestController
public class UserController {
    @Autowired
    UserService service;

    @GetMapping("/users")
    public List<UserLoginProjection> getUser() {
        return service.getAllUser();
    }
    

}
