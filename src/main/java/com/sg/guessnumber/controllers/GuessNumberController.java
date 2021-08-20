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
import java.util.ArrayList;
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
        List<GuessNumber> games = service.getAllGames();
        List<GuessNumber> result = new ArrayList<>();
        
        if (games == null || games.isEmpty())
            return new ResponseEntity(null, HttpStatus.NOT_FOUND);
        
        games.stream().map(gn -> new GuessNumber(gn)).map(last -> {
            if (!last.isFinished())
                last.setAnswer(0);
            return last;
        }).forEachOrdered(last -> {
            result.add(last);
        });
        
        if (result.isEmpty())
            return new ResponseEntity(null, HttpStatus.NOT_FOUND);
                
        return new ResponseEntity(result, HttpStatus.OK);
    }
    
    @GetMapping("/game/{id}")
    public ResponseEntity<GuessNumber> findById(@PathVariable int id) {
        GuessNumber result = service.findById(id);
        if (result == null) {
            return new ResponseEntity(null, HttpStatus.NOT_FOUND);
        }
        GuessNumber copy = new GuessNumber(result);
        if (!copy.isFinished())
            copy.setAnswer(0);
        
        return new ResponseEntity(copy, HttpStatus.FOUND);
    }
    
    @GetMapping("/rounds/{id}")
    public ResponseEntity<Round> getAll(@PathVariable int id) {
        List<Round> result = service.getAllRounds(id);
        
        if (result == null)
            return new ResponseEntity(null, HttpStatus.NOT_FOUND);
        
        return new ResponseEntity(result, HttpStatus.OK);
    }
}
