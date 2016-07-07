package com.google.kamil1338.smsspamer.presenter;

import com.google.kamil1338.smsspamer.ISmsService;
import com.google.kamil1338.smsspamer.interactor.NewInteractorImpl;
import com.google.kamil1338.smsspamer.interactor.enums.ServiceWorkingState;

/**
 * Created by pierudzki on 2016-05-05.
 */
public class PresenterImpl implements ISmsService, ISmsService.ISmsServiceListener {

    private ISmsService interactor;
    private ISmsService.ISmsServiceListener view;

    public PresenterImpl(ISmsService.ISmsServiceListener view) {
        this.view = view;
        interactor = new NewInteractorImpl(this);
    }

    @Override
    public void initialize() {
        interactor.initialize();
    }

    @Override
    public void uninitialize() {
        interactor.uninitialize();
    }

    @Override
    public void sendToAll(final String message) {
        interactor.sendToAll(message);
    }

    @Override
    public void resetApp() {
        interactor.resetApp();
    }

    @Override
    public void onRemainPhoneNumbers(final int remained) {
        view.onRemainPhoneNumbers(remained);
    }

    @Override
    public void onReadMessagesResult(final boolean success) {
        view.onReadMessagesResult(success);
    }

    @Override
    public void onCompleted() {
        view.onCompleted();
    }

    @Override
    public void onServiceWorking(final ServiceWorkingState state) {
        view.onServiceWorking(state);
    }

    @Override
    public void onSendedMessagesChanged(final int sendedMessages, final int phoneNumbersCount) {
        view.onSendedMessagesChanged(sendedMessages, phoneNumbersCount);
    }
}
