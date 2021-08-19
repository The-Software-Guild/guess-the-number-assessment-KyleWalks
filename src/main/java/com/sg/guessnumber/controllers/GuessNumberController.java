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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.sg.guessnumber.data.GuessNumberDao;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author Cosmos
 */
@RestController
@RequestMapping("/api/guessnumber")
public class GuessNumberController {
    private final GuessNumberDao dao;

    public GuessNumberController(GuessNumberDao dao) {
        this.dao = dao;
    }

    @GetMapping
    public List<GuessNumber> all() {
        return dao.getAll();
    }
    
    @PostMapping("/begin")
    public ResponseEntity<Integer> begin() {
        // Generate answer
        int ansInt = ThreadLocalRandom.current().nextInt(1000, 9999);
        
        String ans = Integer.toString(ansInt);
        // Check for duplicates
        Character[] ansAsChar = ans.chars().mapToObj(c -> (char)c).toArray(Character[]::new);
        Set<Character> ansSet = new HashSet<>(Arrays.asList(ansAsChar));
        
        // Generate 4 digit int with no duplicate integers
        while (ansSet.size() != 4) {
            ansInt = ThreadLocalRandom.current().nextInt(1000, 9999);
        
            ans = Integer.toString(ansInt);
        
            // Check for duplicates
            ansAsChar = ans.chars().mapToObj(c -> (char)c).toArray(Character[]::new);
            ansSet = new HashSet<>(Arrays.asList(ansAsChar));
        }
        GuessNumber guessNumber = new GuessNumber(ansInt);
        // Return HttpStatus.CREATED and the gameId
        return new ResponseEntity(dao.add(guessNumber), HttpStatus.CREATED);
    }
    
    @PostMapping("/guess")
    public ResponseEntity<GuessNumber> guess(@RequestBody GuessNumber guessNumber) {
        return new ResponseEntity(guessNumber, HttpStatus.OK);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<GuessNumber> findById(@PathVariable int id) {
        GuessNumber result = dao.findById(id);
        if (result == null) {
            return new ResponseEntity(null, HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(result);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable int id, @RequestBody GuessNumber guessNumber) {
        ResponseEntity response = new ResponseEntity(HttpStatus.NOT_FOUND);
        if (id != guessNumber.getId()) {
            response = new ResponseEntity(HttpStatus.UNPROCESSABLE_ENTITY);
        } else if (dao.update(guessNumber)) {
            response = new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        return response;
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable int id) {
        if (dao.deleteById(id)) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }
}
