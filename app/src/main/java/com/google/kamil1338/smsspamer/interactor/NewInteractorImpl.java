package com.google.kamil1338.smsspamer.interactor;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.google.kamil1338.smsspamer.ISmsService;
import com.google.kamil1338.smsspamer.interactor.service.SmsService;
import com.google.kamil1338.smsspamer.utils.App;

/**
 * Created by pierudzki on 2016-06-01.
 */
public class NewInteractorImpl implements ISmsService, ServiceConnection {

    private ISmsServiceListener listener;
    private SmsService service;


    public NewInteractorImpl(ISmsServiceListener listener) {
        this.listener = listener;
    }

    @Override
    public void initialize() {
        App.getContext().bindService(new Intent(App.getContext(), SmsService.class), this, Context.BIND_AUTO_CREATE);

    }

    @Override
    public void uninitialize() {
        if (service != null) {
            service.setSmsListener(null);
        }
        App.getContext().unbindService(this);
    }

    @Override
    public void sendToAll(String message) {

    }

    @Override
    public void resetApp() {
        uninitialize();
        initialize();
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        this.service = ((SmsService.SmsBinder) service).getService();
        this.service.setSmsListener(listener);
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        service = null;
    }
}
