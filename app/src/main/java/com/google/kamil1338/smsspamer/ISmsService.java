package com.google.kamil1338.smsspamer;

import com.google.kamil1338.smsspamer.interactor.enums.ServiceWorkingState;

/**
 * Created by pierudzki on 2016-05-05.
 */
public interface ISmsService {
    void initialize();
    void uninitialize();

    /**
     * <p>
     *     Wysłanie wiadomości do wszystkich numerów.
     * </p>
     * @param message Traść wiadomości.
     * */
    void sendToAll(String message);

    /**
     * <p>
     *     Resetowanie ustawień aplikacji
     * </p>
     * */
    void resetApp();

    /**
     * <p>
     *     Słuchacz odpowiedzi.
     * </p>
     * */
    interface ISmsServiceListener {

        /**
         * @param remained Ile numerów telefonów pozostało. Jeśli -1 to nastąpił błąd podczas czytania danych z pliku.
         * */
        void onRemainPhoneNumbers(int remained);

        /**
         * @param success Jeśli TRUE to poprawnie załadowano plik z wiadomościami.
         * */
        void onReadMessagesResult(boolean success);

        /**
         * <p>
         *     Wywołane w momencie gdy zakończono wysyłanie wiadomości.
         * </p>
         * */
        void onCompleted();

        /**
         * <p>
         *     Wywołane w momencie gdy usługa wysyła wiadomości.
         * </p>
         * */
        void onServiceWorking(ServiceWorkingState state);

        /**
         * <p>
         *     Gdy nastąpiła zmiana w liczbie wysłanych wiadomości.
         * </p>
         * */
        void onSendedMessagesChanged(int sendedMessages, int phoneNumbersCount);
    }
}
