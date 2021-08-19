/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.guessnumber.controllers;

import com.sg.guessnumber.models.GuessNumber;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.sg.guessnumber.models.Round;
import com.sg.guessnumber.service.GuessNumberService;
import com.sg.guessnumber.service.GuessNumberServiceLayer;
import org.springframework.web.bind.annotation.*;


/**
 *
 * @author Cosmos
 */
@RestController
@RequestMapping("/api/guessnumber")
public class GuessNumberController {
    private final GuessNumberService service;

    public GuessNumberController(GuessNumberServiceLayer service) {
        this.service = service;
    }

    @GetMapping
    public List<GuessNumber> all() {
        return service.getAllGames();
    }
    
    @PostMapping("/begin")
    public ResponseEntity<Integer> begin() {
        return new ResponseEntity(service.add(new GuessNumber()).getId(), HttpStatus.CREATED);
    }
    
    @PostMapping("/guess")
    public ResponseEntity<Round> guess(@RequestBody Round roundGuess) {
        roundGuess.setGuessNumberId(roundGuess.getId());
        Round result = service.add(roundGuess);
        if (result == null)
            return new ResponseEntity(null, HttpStatus.NOT_FOUND);
        
        return new ResponseEntity(result, HttpStatus.OK);
    }
    
    @GetMapping("/game")
    public ResponseEntity<GuessNumber> getAll() {
        List<GuessNumber> result = service.getAllGames();
        
        if (result == null)
            return new ResponseEntity(null, HttpStatus.NOT_FOUND);
        
        return new ResponseEntity(service.getAllGames(), HttpStatus.OK);
    }
    
    @GetMapping("/game/{id}")
    public ResponseEntity<GuessNumber> findById(@PathVariable int id) {
        GuessNumber result = service.findById(id);
        if (result == null) {
            return new ResponseEntity(null, HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(result);
    }
    
    @GetMapping("/rounds/{id}")
    public ResponseEntity<Round> getAll(@PathVariable int id) {
        List<Round> result = service.getAllRounds(id);
        
        if (result == null)
            return new ResponseEntity(null, HttpStatus.NOT_FOUND);
        
        return new ResponseEntity(result, HttpStatus.OK);
    }
}
