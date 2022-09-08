package com.dmtryii.task.utils;

import java.util.Random;

public class Generator {
    public int range(int max, int min) {
        return new Random().nextInt(max - min) + min;
    }
}
