package com.google.kamil1338.smsspamer.interactor.validator;

import android.support.annotation.Nullable;

/**
 * Created by pierudzki on 2016-05-05.
 */
public class LengthValidator extends PhoneNumberValidator {

    public LengthValidator(@Nullable PhoneNumberValidator validator) {
        super(validator);
    }

    @Override
    public boolean validate(String number) {
        return number.trim().length() == 11 && (validator == null || validator.validate(number));
    }
}
