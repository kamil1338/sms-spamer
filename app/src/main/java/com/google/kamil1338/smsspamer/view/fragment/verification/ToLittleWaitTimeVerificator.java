package com.google.kamil1338.smsspamer.view.fragment.verification;

import android.widget.EditText;

import com.google.kamil1338.smsspamer.R;
import com.google.kamil1338.smsspamer.utils.App;
import com.google.kamil1338.smsspamer.view.fragment.model.ConfigurationModel;

import java.util.concurrent.TimeUnit;

/**
 * Created by pierudzki on 2016-06-15.
 */
public class ToLittleWaitTimeVerificator extends FieldVerificator {

    public ToLittleWaitTimeVerificator(ConfigurationModel model, EditText fieldToVerify) {
        super(model, fieldToVerify, App.getContext().getString(R.string.field_wait_time_between_messages_error));
    }

    @Override
    public void verify() {
        if (model.getWaitTime() < TimeUnit.SECONDS.toMillis(5)) {
            setError();
            model.setModelCorrect(false);
        }
    }
}
