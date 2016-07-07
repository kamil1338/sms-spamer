package com.google.kamil1338.smsspamer.view.fragment.verification;

import android.widget.EditText;

import com.google.kamil1338.smsspamer.R;
import com.google.kamil1338.smsspamer.utils.App;
import com.google.kamil1338.smsspamer.view.fragment.model.ConfigurationModel;

/**
 * Created by pierudzki on 2016-06-15.
 */
public class EmptyAvailableNumbersVerificator extends FieldVerificator {

    public EmptyAvailableNumbersVerificator(ConfigurationModel model, EditText field) {
        super(model, field, App.getContext().getString(R.string.field_cannot_be_empty));
    }

    @Override
    public void verify() {
        if (field.getText().toString().isEmpty()) {
            model.setModelCorrect(false);
            setError();
        } else {
            model.setPhoneNumbersCount(Integer.valueOf(field.getText().toString()));
        }
    }
}
