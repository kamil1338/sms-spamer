package com.google.kamil1338.smsspamer.view.fragment.verification;

import android.widget.EditText;

import com.google.kamil1338.smsspamer.R;
import com.google.kamil1338.smsspamer.utils.App;
import com.google.kamil1338.smsspamer.view.fragment.model.ConfigurationModel;

/**
 * Created by pierudzki on 2016-06-15.
 */
public class AvailableNumberLessVerificator extends FieldVerificator {

    public AvailableNumberLessVerificator(ConfigurationModel model, EditText fieldToVerify) {
        super(model, fieldToVerify, App.getContext().getString(R.string.field_cannot_be_greater_than_remained));
    }

    @Override
    public void verify() {
        if (model.getPhoneNumbersCount() > model.getRemainedNumbers()) {
            setError();
            model.setModelCorrect(false);
        }
    }
}
