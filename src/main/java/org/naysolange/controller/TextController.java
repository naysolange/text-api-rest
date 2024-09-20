package org.naysolange.controller;

import org.naysolange.entity.Text;
import org.naysolange.repository.TextRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TextController {

    @Autowired
    TextRepository repository;

    @GetMapping
    String isAlive() {
        return "It's alive!";
    }

    @GetMapping("/{amount}")
    List<Text> get(@PathVariable Integer amount) {
        return repository.findLimited(amount);
    }
}
