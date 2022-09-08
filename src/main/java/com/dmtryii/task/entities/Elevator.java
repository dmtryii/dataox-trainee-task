package com.dmtryii.task.entities;

import java.util.Arrays;

public class Elevator {
    private static final int MAX_CAPACITY = 5;
    private int state = 0; // up = 1, waiting = 0, down = -1
    private int currentCapacity;
    private final int[] comingPeople;

    public Elevator() {
        this.comingPeople = new int[MAX_CAPACITY];
    }

    public int getCurrentCapacity() {
        return this.currentCapacity;
    }

    public void addToComingPeople(int[] peopleArr) {

        for (int k = 0; k < MAX_CAPACITY; k++) {
            if (comingPeople[k] == 0) {
                comingPeople[k] = peopleArr[k];
            }
        }
        Arrays.sort(comingPeople);
    }

    public int[] getComingPeople() {
        return this.comingPeople;
    }

    public int getState() {
        return this.state;
    }

    public int getMaxCapacity() {
        return MAX_CAPACITY;
    }

    public void setCurrentCapacity(int currentCapacity) {
        this.currentCapacity = currentCapacity;
    }

    public void setState(int state) {
        this.state = state;
    }
}
