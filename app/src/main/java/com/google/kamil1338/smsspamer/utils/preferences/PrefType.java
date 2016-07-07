package com.google.kamil1338.smsspamer.utils.preferences;

/**
 * Created by pierudzki on 2016-05-31.
 */

public enum  PrefType {
    TEST(""),
    LAST_PHONE_NUMBER_INDEX(0),
    LAST_PHONE_LIST_CONTROLL_SUM("");

    private boolean defaultBooleanValue;
    private int defaultIntValue;
    private long defaultLongValue;
    private String defaultStringValue;
    private boolean isBoolUsed;
    private boolean isIntUsed;
    private boolean isLongUsed;
    private boolean isStringUsed;

    PrefType(boolean defaultValue) {
        this.defaultBooleanValue = defaultValue;
        this.isBoolUsed = true;
    }

    PrefType(int defaultValue) {
        this.defaultIntValue = defaultValue;
        this.isIntUsed = true;
    }

    PrefType(long defaultValue) {
        this.defaultLongValue = defaultValue;
        this.isLongUsed = true;
    }

    PrefType(String defaultValue) {
        this.defaultStringValue = defaultValue;
        this.isStringUsed = true;
    }

    public boolean getDefaultBooleanValue() {
        return defaultBooleanValue;
    }

    public int getDefaultIntValue() {
        return defaultIntValue;
    }

    public long getDefaultLongValue() {
        return defaultLongValue;
    }

    public String getDefaultStringValue() {
        return defaultStringValue;
    }

    public boolean isBoolUsed() {
        return isBoolUsed;
    }

    public boolean isIntUsed() {
        return isIntUsed;
    }

    public boolean isLongUsed() {
        return isLongUsed;
    }

    public boolean isStringUsed() {
        return isStringUsed;
    }
}

