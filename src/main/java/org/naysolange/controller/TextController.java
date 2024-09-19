package org.naysolange.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TextController {

    @GetMapping
    String isAlive() {
        return "It's alive!";
    }
}
