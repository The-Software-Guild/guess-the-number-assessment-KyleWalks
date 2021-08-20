/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.guessnumber.service;

import com.sg.guessnumber.data.GuessNumberDao;
import com.sg.guessnumber.data.GuessNumberDatabaseDao;
import com.sg.guessnumber.models.GuessNumber;
import com.sg.guessnumber.models.Round;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.stereotype.Service;

/**
 *
 * @author Cosmos
 */
@Service
@Profile({"service", "prod"})
public class GuessNumberServiceLayer implements GuessNumberService {
    
    private final GuessNumberDao dao;

    @Autowired
    public GuessNumberServiceLayer(GuessNumberDatabaseDao dao) {
        this.dao = dao;
    }
    
    @Override
    public GuessNumber add(GuessNumber guessNumber) {
        
        if (guessNumber.getAnswer() == 0) {
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
            guessNumber = new GuessNumber(ansInt);
        }
        
        return dao.add(guessNumber);
    }

    @Override
    public Round add(Round round) {
        if (dao.getAllGames().size() < round.getGuessNumberId()) {
            return null;
        }
        // Set foreign key linking round to game
        GuessNumber ans = findById(round.getGuessNumberId());
                
        String ansAsString = Integer.toString(ans.getAnswer());
        String guessAsString = Integer.toString(round.getGuess());
        
        // Check for exact and partial matches.
        int correct = 0;
        int partial = 0;
        for (char c : ansAsString.toCharArray()) {
            if (guessAsString.indexOf(c) > -1) {
                if (ansAsString.indexOf(c) == guessAsString.indexOf(c))
                    correct += 1;
                else
                    partial += 1;
            }
        }
        // Check if all correct
        if (correct == 4) {
            ans.setFinished(true);
            update(ans);
        }
        
        // Save result and timestamp
        round.setResult(String.format("e:%dp:%d", correct, partial));
        round.setRoundTime(LocalDateTime.now());
                
        return dao.add(round);
    }

    @Override
    public List<GuessNumber> getAllGames() {
        
        List<GuessNumber> result = dao.getAllGames();
        
        if (result.size() < 1)
            return null;
                
        return result;
    }

    @Override
    public List<Round> getAllRounds(int id) {
        
        List<GuessNumber> result = dao.getAllGames();
        
        if (result.size() < id || id == 0) {
            return null;
        }
        
        return dao.getAllRounds(id);
    }

    @Override
    public GuessNumber findById(int id) {
        
        if (dao.getAllGames().size() < id || id == 0) {
            return null;
        }
        GuessNumber gn = dao.findById(id);
        
        return gn;
    }

    @Override
    public boolean update(GuessNumber guessNumber) {
        
        if (guessNumber == null)
            return false;
        
        return dao.update(guessNumber);
    }

    @Override
    public boolean deleteById(int id) {
        List<GuessNumber> games = dao.getAllGames();
        if (games.size() < id || id == 0) {
            return false;
        }
        
        return dao.deleteGameById(id);
    }
    
}
