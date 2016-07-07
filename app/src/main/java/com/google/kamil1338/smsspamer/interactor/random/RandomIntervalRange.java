package com.google.kamil1338.smsspamer.interactor.random;

import com.google.kamil1338.smsspamer.view.fragment.model.ConfigurationModel;

/**
 * <p>
 *     Klasa losuje wartość opóźnienia między wiadomościami z przedziału.
 * </p>
 * Created by pierudzki on 2016-06-07.
 */
public class RandomIntervalRange extends RandomNumber {

    /**
     * Model z konfiguracją.
     * */
    ConfigurationModel model;

    public RandomIntervalRange() {
        super();
    }

    public void setModel(ConfigurationModel model) {
        this.model = model;
    }

    /**
     * <p>
     *     @return Zwraca wylosowany czas w sekundach
     * </p>
     * */
    @Override
    public int getRandomValue() {
        return super.getRandomValue((model.getIntervalTo() - model.getIntervalFrom()) + 1) + model.getIntervalFrom();
    }
}
