package com.google.kamil1338.smsspamer.interactor.random;

import java.util.List;

/**
 * <p>
 *     Klasa losuje wiadomość.
 *
 *     Created by pierudzki on 2016-06-07.
 * </p>
 */
public class RandomMessage extends RandomNumber {

    /**
     * Lista z wiadomościami pobranymi z dysku.
     * */
    private List<String> messages;


    public RandomMessage(List<String> messages) {
        this.messages = messages;
    }

    /**
     * <p>
     *     @return Zwraca wylosowaną wiadomość.
     * </p>
     * */
    public String getRandomMessage() {
        return messages.get(getRandomValue(messages.size()));
    }
}
