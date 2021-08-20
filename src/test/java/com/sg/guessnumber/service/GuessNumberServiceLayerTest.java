/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.guessnumber.service;

import com.sg.guessnumber.models.GuessNumber;
import com.sg.guessnumber.models.Round;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit4.SpringRunner;

/**
 *
 * @author Cosmos
 */
@Profile("test")
@RunWith(SpringRunner.class)
@SpringBootTest
public class GuessNumberServiceLayerTest {
    
    @Autowired
    private GuessNumberService service;
    
    @Test
    public void testAddGame() {
        GuessNumber game = new GuessNumber(1);
        game.setAnswer(7628);
        game.setFinished(false);
        
        try {
            service.add(game);
        } catch (Exception ex) {
            fail("Exception was thrown.");
        }
    }
    
    @Test
    public void testAddRound() {
        GuessNumber game = new GuessNumber(1);
        game.setAnswer(7628);
        game.setFinished(false);
        
        Round round = new Round(1, 2051);
        round.setGuessNumberId(1);
        round.setRoundTime(LocalDateTime.now());
        
        try {
            service.add(round);
        } catch (EmptyResultDataAccessException ed) {
        } catch (Exception ex) {
            fail("Exception was thrown.");
        }
    }
    
    @Test
    public void testGetAllGames() {
        try {
            service.getAllGames();
        } catch (Exception ex) {
            fail("Exception was thrown.");
        }
    }
    
    @Test
    public void testGetAllRounds() {
        try {
            service.getAllRounds(1);
        } catch (Exception ex) {
            fail("Exception was thrown.");
        }
    }
    
    @Test
    public void testFindById() {
        try {
            service.findById(1);
        } catch (EmptyResultDataAccessException ed) {
        } catch (Exception ex) {
            fail("Exception was thrown. " + ex.getMessage());
        }
    }
    
    @Test
    public void testUpdate() {
        try {
            service.update(null);
        } catch (EmptyResultDataAccessException ed) {
        } catch (Exception ex) {
            fail("Exception was thrown.");
        }
    }
    
    @Test
    public void deleteById() {
        try {
            service.deleteById(1);
        } catch (EmptyResultDataAccessException ed) {
        } catch (Exception ex) {
            fail("Exception was thrown. "  + ex.getMessage());
        }
    }
}
