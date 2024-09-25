package org.naysolange.controller;

import org.naysolange.entity.Text;
import org.naysolange.repository.TextRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    ResponseEntity<List<Text>> get(@PathVariable Integer amount) {
        List<Text> texts = repository.findLimited(amount);
        if(texts.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.ok(texts);
        }
    }

    @PostMapping
    ResponseEntity<Text> createText(@RequestBody Text text) {
        Text savedText = repository.save(text);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedText);
    }
}
