/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.guessnumber.models;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 *
 * @author Cosmos
 */
public class Round {
    
    private int id;
    private int guess;
    private String result;
    private LocalDateTime roundTime;
    private int guessNumberId;

    public Round() {
    }

    public Round(int id, int guess) {
        this.id = id;
        this.guess = guess;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public int getGuess() {
        return guess;
    }

    public void setGuess(int guess) {
        this.guess = guess;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public LocalDateTime getRoundTime() {
        return roundTime;
    }

    public void setRoundTime(LocalDateTime roundTime) {
        this.roundTime = roundTime;
    }
    
    public int getGuessNumberId() {
        return guessNumberId;
    }

    public void setGuessNumberId(int guessNumberId) {
        this.guessNumberId = guessNumberId;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 19 * hash + this.id;
        hash = 19 * hash + this.guess;
        hash = 19 * hash + Objects.hashCode(this.result);
        hash = 19 * hash + Objects.hashCode(this.roundTime);
        hash = 19 * hash + this.guessNumberId;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Round other = (Round) obj;
        if (this.id != other.id) {
            return false;
        }
        if (this.guess != other.guess) {
            return false;
        }
        if (this.guessNumberId != other.guessNumberId) {
            return false;
        }
        if (!Objects.equals(this.result, other.result)) {
            return false;
        }
        return Objects.equals(this.roundTime, other.roundTime);
    }
    
    
}
