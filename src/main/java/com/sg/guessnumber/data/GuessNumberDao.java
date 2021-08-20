/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.guessnumber.data;

import com.sg.guessnumber.models.GuessNumber;
import com.sg.guessnumber.models.Round;
import java.util.List;

/**
 *
 * @author Cosmos
 */
public interface GuessNumberDao {
    GuessNumber add(GuessNumber guessNumber);

    Round add(Round round);
    
    List<GuessNumber> getAllGames();
    
    List<Round> getAllRounds(int id);
    
    GuessNumber findById(int id);

    // true if item exists and is updated
    boolean update(GuessNumber guessNumber);

    // true if item exists and is deleted
    boolean deleteGameById(int id);
    
    boolean deleteRoundsByGame(GuessNumber game);
}
