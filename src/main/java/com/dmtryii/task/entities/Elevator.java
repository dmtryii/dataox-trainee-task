package com.dmtryii.task.entities;

import com.dmtryii.task.enums.Direction;

import java.util.Arrays;

public class Elevator {
    private static final int MAX_CAPACITY = 5;
    private int currentCapacity;
    private final int[] comingPeople;
    private Direction direction;

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

    public Direction getDirection() {
        return this.direction;
    }

    public int getMaxCapacity() {
        return MAX_CAPACITY;
    }

    public void setCurrentCapacity(int currentCapacity) {
        this.currentCapacity = currentCapacity;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }
}
