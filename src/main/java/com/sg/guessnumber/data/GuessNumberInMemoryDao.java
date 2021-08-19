/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.guessnumber.data;

import com.sg.guessnumber.models.GuessNumber;
import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Cosmos
 */
@Repository
@Profile("memory")
public class GuessNumberInMemoryDao implements GuessNumberDao{
    private static final List<GuessNumber> guessNumbers = new ArrayList<>();

    @Override
    public GuessNumber add(GuessNumber guessNumber) {

        int nextId = guessNumbers.stream()
                .mapToInt(i -> i.getId())
                .max()
                .orElse(0) + 1;

        guessNumber.setId(nextId);
        guessNumbers.add(guessNumber);
        return guessNumber;
    }

    @Override
    public List<GuessNumber> getAll() {
        return new ArrayList<>(guessNumbers);
    }

    @Override
    public GuessNumber findById(int id) {
        return guessNumbers.stream()
                .filter(i -> i.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public boolean update(GuessNumber todo) {

        int index = 0;
        while (index < guessNumbers.size()
                && guessNumbers.get(index).getId() != todo.getId()) {
            index++;
        }

        if (index < guessNumbers.size()) {
            guessNumbers.set(index, todo);
        }
        return index < guessNumbers.size();
    }

    @Override
    public boolean deleteById(int id) {
        return guessNumbers.removeIf(i -> i.getId() == id);
    }
}
