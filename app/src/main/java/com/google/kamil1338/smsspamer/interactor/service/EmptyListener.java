package com.google.kamil1338.smsspamer.interactor.service;

import com.google.kamil1338.smsspamer.ISmsService;
import com.google.kamil1338.smsspamer.interactor.enums.ServiceWorkingState;

/**
 * <p>
 *     Pusty listener.
 *
 *     Created by pierudzki on 2016-06-03.
 * </p>
 */
public class EmptyListener implements ISmsService.ISmsServiceListener {
    @Override
    public void onRemainPhoneNumbers(int remained) {
        /** Not implemented*/
    }

    @Override
    public void onReadMessagesResult(boolean success) {
        /** Not implemented*/
    }

    @Override
    public void onCompleted() {
        /** Not implemented*/
    }

    @Override
    public void onServiceWorking(ServiceWorkingState state) {
        /** Not implemented*/
    }

    @Override
    public void onSendedMessagesChanged(int sendedMessages, int phoneNumbersCount) {
        /** Not implemented*/
    }
}
