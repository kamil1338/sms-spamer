package com.google.kamil1338.smsspamer.interactor.service;

import android.content.Intent;

import com.google.kamil1338.smsspamer.utils.App;
import com.google.kamil1338.smsspamer.view.fragment.model.ConfigurationModel;

/**
 * Created by pierudzki on 2016-06-01.
 */
public class Sms {

    /**
     * <p>
     *     Uruchomienie usługi.
     *     @param configurationModel Konfiguracja wysyłania wiadomości.
     * </p>
     * */
    public static void startWorking(ConfigurationModel configurationModel) {
        Intent intent = new Intent(App.getContext(), SmsService.class);
        intent.setAction(SmsService.START_WORKING);
        intent.putExtra(SmsService.CONFIGURATION_MODEL, configurationModel);
        App.getContext().startService(intent);
    }

    /**
     * <p>
     *     Bezpieczne zatrzymanie usługi.
     * </p>
     * */
    public static void stopWorking() {
        Intent intent = new Intent(App.getContext(), SmsService.class);
        intent.setAction(SmsService.STOP_WORKING);
        App.getContext().startService(intent);
    }
}
