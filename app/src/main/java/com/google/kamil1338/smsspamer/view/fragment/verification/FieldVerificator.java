package com.google.kamil1338.smsspamer.view.fragment.verification;

import android.widget.EditText;

import com.google.kamil1338.smsspamer.view.fragment.model.ConfigurationModel;

/**
 * Created by pierudzki on 2016-06-15.
 */
public abstract class FieldVerificator {

    protected ConfigurationModel model;
    protected EditText field;
    protected String errorMessage;

    protected FieldVerificator(ConfigurationModel model, EditText fieldToVerify, String errorMessage) {
        this.model = model;
        this.field = fieldToVerify;
        this.errorMessage = errorMessage;
    }

    public void setError() {
        if (field.getError() == null) {
            field.setError(errorMessage);
        }
    }

    public abstract void verify();
}
