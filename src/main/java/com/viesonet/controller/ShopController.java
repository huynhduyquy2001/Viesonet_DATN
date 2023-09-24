package com.viesonet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.viesonet.entity.Users;
import com.viesonet.service.UsersService;

@RestController
public class ShopController {

    @Autowired
    private UsersService usersService;

}
