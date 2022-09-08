package com.dmtryii.task.entities;

import com.dmtryii.task.utils.Generator;

public class Passenger {
    public int getNeededFloor() {
        Generator generator = new Generator();
        return generator.range(Dispatcher.getFloorQuantity(), 1);
    }
}
