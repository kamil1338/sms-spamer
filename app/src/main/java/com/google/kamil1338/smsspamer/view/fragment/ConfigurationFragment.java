package com.google.kamil1338.smsspamer.view.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import com.google.kamil1338.smsspamer.R;
import com.google.kamil1338.smsspamer.view.fragment.model.ConfigurationModel;
import com.google.kamil1338.smsspamer.view.fragment.verification.AvailableNumberLessVerificator;
import com.google.kamil1338.smsspamer.view.fragment.verification.EmptyAvailableNumbersVerificator;
import com.google.kamil1338.smsspamer.view.fragment.verification.EmptyWaitTimeVerificator;
import com.google.kamil1338.smsspamer.view.fragment.verification.FieldVerificator;
import com.google.kamil1338.smsspamer.view.fragment.verification.EmptyIntervalFromVerificator;
import com.google.kamil1338.smsspamer.view.fragment.verification.EmptyIntervalToVerificator;
import com.google.kamil1338.smsspamer.view.fragment.verification.IntervalRangeVerificator;
import com.google.kamil1338.smsspamer.view.fragment.verification.RemainedPhoneNumberVerificator;
import com.google.kamil1338.smsspamer.view.fragment.verification.ToLittleWaitTimeVerificator;

/**
 * Created by pierudzki on 2016-05-31.
 */
public class ConfigurationFragment extends Fragment implements ISynchronizable {

    private EditText availablePhoneNumbers;
    private EditText remainedPhoneNumbers;
    private EditText intervalBetweenMessagesFrom;
    private EditText intervalBetweenMessagesTo;
    private EditText waitTime;
    private Object configSynchObj = new Object();
    private boolean isViewCreated = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.configuration_layout, container, false);
        availablePhoneNumbers = (EditText) view.findViewById(R.id.available_phone_numbers);
        remainedPhoneNumbers = (EditText) view.findViewById(R.id.remained_phone_numbers);
        intervalBetweenMessagesFrom = (EditText) view.findViewById(R.id.interval_between_messages_from);
        intervalBetweenMessagesTo = (EditText) view.findViewById(R.id.interval_between_messages_to);
        waitTime = (EditText) view.findViewById(R.id.wait_time_field);
        isViewCreated = true;
        synchronized (configSynchObj) {
            configSynchObj.notifyAll();
        }
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isViewCreated = false;
    }

    /**
     * <p>
     *     Ustawienie wartości w polu z ilością pozostałych numerów.
     * </p>
     * */
    public void setRemainPhoneNumbersCount(final int count) {
        if (count > 0) {
            remainedPhoneNumbers.setText(String.valueOf(count));
            availablePhoneNumbers.setText("");
            availablePhoneNumbers.setEnabled(true);
            intervalBetweenMessagesFrom.setText("");
            intervalBetweenMessagesFrom.setEnabled(true);
            intervalBetweenMessagesTo.setText("");
            intervalBetweenMessagesTo.setEnabled(true);
            waitTime.setText("");
            waitTime.setEnabled(true);
            intervalBetweenMessagesFrom.setEnabled(true);
        } else {
            remainedPhoneNumbers.setText(getString(R.string.empty));
            availablePhoneNumbers.setText(getString(R.string.field_blocked));
            availablePhoneNumbers.setEnabled(false);
            intervalBetweenMessagesFrom.setText(getString(R.string.field_blocked));
            intervalBetweenMessagesFrom.setEnabled(false);
            intervalBetweenMessagesTo.setText(getString(R.string.field_blocked));
            intervalBetweenMessagesTo.setEnabled(false);
            waitTime.setText(getString(R.string.field_blocked));
            waitTime.setEnabled(false);
        }
    }

    private void cleanAllFieldsErrorState() {
        availablePhoneNumbers.setError(null);
        remainedPhoneNumbers.setError(null);
        intervalBetweenMessagesFrom.setError(null);
        intervalBetweenMessagesTo.setError(null);
        waitTime.setError(null);
    }

    /**
     * <p>
     *     Pobranie konfiguracji z fragmentu.
     * </p>
     * */
    public ConfigurationModel getConfigModel() {
        ConfigurationModel model = new ConfigurationModel();
        cleanAllFieldsErrorState();
        List<FieldVerificator> verificators = new ArrayList<>();
        verificators.add(new RemainedPhoneNumberVerificator(model, remainedPhoneNumbers));
        verificators.add(new EmptyAvailableNumbersVerificator(model, availablePhoneNumbers));
        verificators.add(new AvailableNumberLessVerificator(model, availablePhoneNumbers));
        verificators.add(new EmptyIntervalFromVerificator(model, intervalBetweenMessagesFrom));
        verificators.add(new EmptyIntervalToVerificator(model, intervalBetweenMessagesTo));
        verificators.add(new IntervalRangeVerificator(model, intervalBetweenMessagesFrom));
        verificators.add(new EmptyWaitTimeVerificator(model, waitTime));
        verificators.add(new ToLittleWaitTimeVerificator(model, waitTime));

        for (FieldVerificator verificator : verificators) {
            verificator.verify();
        }
        return model;
    }

    @Override
    public void setSyncObject(Object syncObject) {
        this.configSynchObj = syncObject;
    }

    @Override
    public boolean isViewCreated() {
        return isViewCreated;
    }
}
