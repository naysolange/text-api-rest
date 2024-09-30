package org.naysolange.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.naysolange.entity.Text;
import org.naysolange.repository.TextRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Text", description = "the text API")
@RestController
public class TextController {

    @Autowired
    TextRepository repository;

    @Operation(
            summary = "It's alive endpoint",
            description = "Validates that the api is running")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation")
    })
    @GetMapping
    String isAlive() {
        return "It's alive!";
    }

    @GetMapping("/{amount}")
    @Operation(
            summary = "Fetch a specific amount of random texts",
            description = "fetches a specific amount of random texts from data source")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation"),
            @ApiResponse(responseCode = "204", description = "no content")
    })
    ResponseEntity<List<Text>> getTexts(@PathVariable Integer amount) {
        List<Text> texts = repository.findLimited(amount);
        if(texts.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.ok(texts);
        }
    }

    @PostMapping
    @Operation(
            summary = "Save a text",
            description = "creates a text into the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "successful operation"),
            @ApiResponse(responseCode = "400", description = "bad request")
    })
    ResponseEntity<Text> createText(@RequestBody Text text) {
        if(text.getContent() == null || text.getContent().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else {
            Text savedText = repository.save(text);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedText);
        }
    }
}
