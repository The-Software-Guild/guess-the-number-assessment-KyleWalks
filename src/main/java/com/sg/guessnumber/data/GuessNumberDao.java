/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.guessnumber.data;

import com.sg.guessnumber.models.GuessNumber;
import java.util.List;

/**
 *
 * @author Cosmos
 */
public interface GuessNumberDao {
    GuessNumber add(GuessNumber todo);

    List<GuessNumber> getAll();

    GuessNumber findById(int id);

    // true if item exists and is updated
    boolean update(GuessNumber todo);

    // true if item exists and is deleted
    boolean deleteById(int id);
}
