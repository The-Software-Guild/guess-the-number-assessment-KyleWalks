/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.guessnumber.data;

import com.sg.guessnumber.models.GuessNumber;
import com.sg.guessnumber.models.Round;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.junit4.SpringRunner;

/**
 *
 * @author Cosmos
 */
@Profile("test")
@RunWith(SpringRunner.class)
@SpringBootTest
public class GuessNumberDatabaseDaoTest {
    
    @Autowired
    private GuessNumberDao testDao;    
    
    @BeforeEach
    public void setUp() {
        List<GuessNumber> games = testDao.getAllGames();
        games.forEach(game -> {
            testDao.deleteGameById(game.getId());
        });
    }
    
    @Test
    public void testAddGetGame() {
        GuessNumber game = new GuessNumber(1);
        game.setAnswer(7628);
        game.setFinished(false);
        
        game = testDao.add(game);
        GuessNumber fromDao = testDao.findById(game.getId());
        
        assertEquals(game, fromDao);
    }
    
    @Test
    public void testAddGetRound() {
        GuessNumber game = new GuessNumber(1);
        game.setAnswer(7628);
        game.setFinished(false);
        
        game = testDao.add(game);
        
        Round round = new Round(1, 7628);
        round.setResult(String.format("e:%dp:%d", 4, 0));
        round.setRoundTime(LocalDateTime.now());
        round.setGuessNumberId(game.getId());
        
        round = testDao.add(round);
        
        Round fromDao = testDao.getAllRounds(game.getId()).get(0);
        
        assertEquals(round, fromDao);
    }
    
    @Test
    public void testGetAllGames() {
        GuessNumber game = new GuessNumber(1);
        game.setAnswer(7628);
        game.setFinished(false);
        testDao.add(game);
        
        GuessNumber game2 = new GuessNumber(1);
        game2.setAnswer(7628);
        game2.setFinished(false);
        testDao.add(game2);
        
        List<GuessNumber> games = testDao.getAllGames();
        
        assertEquals(2, games.size());
        assertTrue(games.contains(game));
        assertTrue(games.contains(game2));
    }
    
    @Test
    public void testUpdateGame() {
        GuessNumber game = new GuessNumber(1);
        game.setAnswer(7628);
        game.setFinished(false);
        testDao.add(game);
        
        GuessNumber fromDao = testDao.findById(game.getId());
        
        assertEquals(game, fromDao);
        
        game.setFinished(true);
        
        assertNotEquals(game, fromDao);
        
        fromDao = testDao.findById(game.getId());
        
        assertEquals(game, fromDao);
    }
    
    @Test
    public void testDeleteGame() {
        GuessNumber game = new GuessNumber(1);
        game.setAnswer(7628);
        game.setFinished(false);
        game = testDao.add(game);
        
        testDao.deleteGameById(game.getId());
        
        GuessNumber fromDao = testDao.findById(game.getId());
        assertNull(fromDao);
    }
    
    @Test
    public void testDeleteRound() {
        GuessNumber game = new GuessNumber(1);
        game.setAnswer(7628);
        game.setFinished(false);
        
        game = testDao.add(game);
        
        Round round = new Round(1, 7628);
        round.setResult(String.format("e:%dp:%d", 4, 0));
        round.setRoundTime(LocalDateTime.now());
        round.setGuessNumberId(game.getId());
        
        testDao.add(round);
        
        testDao.deleteRoundsByGame(game);
        
        Round fromDao = testDao.getAllRounds(game.getId()).get(0);
        
        assertNull(fromDao);
    }
}
