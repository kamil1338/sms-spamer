package com.google.kamil1338.smsspamer.interactor.random;

import java.util.Random;

/**
 * Created by pierudzki on 2016-06-07.
 */
public abstract class RandomNumber {

    private Random generator = new Random();

    protected int getRandomValue(int n) {
        return generator.nextInt(n);
    }

    protected int getRandomValue() {
        return generator.nextInt();
    }
}
