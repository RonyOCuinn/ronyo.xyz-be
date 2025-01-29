package org.ronyo.ronyo.xyz_be.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    @GetMapping
    public String index() {
        return "Hello World!";
    }

}
