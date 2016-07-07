package com.google.kamil1338.smsspamer.interactor.enums;

/**
 * <p>
 *     Stan pracy usługi.
 *
 *     Created by pierudzki on 2016-06-01.
 * </p>
 */
public enum ServiceWorkingState {

    WORKING("WORKING"),
    NOT_WORKING("NOT_WORKING");

    private final String state;

    ServiceWorkingState(final String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }

    public static ServiceWorkingState fromString(final String state) {
        for (ServiceWorkingState workingState : values()) {
            if (workingState.getState() == state) {
                return workingState;
            }
        }
        return null;
    }
}
