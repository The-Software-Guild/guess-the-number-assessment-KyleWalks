/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.guessnumber.models;

/**
 *
 * @author Cosmos
 */
public class GuessNumber {
    private int id;
    private int answer;
    private boolean finished;

    public GuessNumber() {
    }
    
    public GuessNumber(int answer) {
        this.answer = answer;
    }
    
    public GuessNumber(GuessNumber gn) {
        this.id = gn.getId();
        this.answer = gn.getAnswer();
        this.finished = gn.isFinished();
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAnswer() {
        return answer;
    }

    public void setAnswer(int answer) {
        this.answer = answer;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 73 * hash + this.id;
        hash = 73 * hash + this.answer;
        hash = 73 * hash + (this.finished ? 1 : 0);
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
        final GuessNumber other = (GuessNumber) obj;
        if (this.id != other.id) {
            return false;
        }
        if (this.answer != other.answer) {
            return false;
        }
        return this.finished == other.finished;
    }
}
