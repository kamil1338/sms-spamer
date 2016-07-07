package com.google.kamil1338.smsspamer.interactor.validator;

import android.support.annotation.Nullable;

/**
 * Created by pierudzki on 2016-05-05.
 */
public abstract class PhoneNumberValidator {

    protected PhoneNumberValidator validator;

    protected PhoneNumberValidator(@Nullable final PhoneNumberValidator validator) {
        this.validator = validator;
    }

    public abstract boolean validate(String number);
}
