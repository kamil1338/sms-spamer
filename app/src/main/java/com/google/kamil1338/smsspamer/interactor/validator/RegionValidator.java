package com.google.kamil1338.smsspamer.interactor.validator;

import android.support.annotation.Nullable;

/**
 * Created by pierudzki on 2016-05-05.
 */
public class RegionValidator extends PhoneNumberValidator {

    public RegionValidator(@Nullable PhoneNumberValidator validator) {
        super(validator);
    }

    @Override
    public boolean validate(String number) {
        return number.trim().startsWith("48") && (validator == null || validator.validate(number));
    }
}
