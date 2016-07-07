package com.google.kamil1338.smsspamer.interactor.service;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.telephony.SmsManager;
import android.util.Log;

import com.google.kamil1338.smsspamer.ISmsService;
import com.google.kamil1338.smsspamer.interactor.enums.ServiceWorkingState;
import com.google.kamil1338.smsspamer.interactor.file.IFileOpenable;
import com.google.kamil1338.smsspamer.interactor.file.MessagesOpener;
import com.google.kamil1338.smsspamer.interactor.file.PhoneNumbersOpener;
import com.google.kamil1338.smsspamer.interactor.random.RandomIntervalRange;
import com.google.kamil1338.smsspamer.interactor.random.RandomMessage;
import com.google.kamil1338.smsspamer.interactor.validator.LengthValidator;
import com.google.kamil1338.smsspamer.interactor.validator.PhoneNumberValidator;
import com.google.kamil1338.smsspamer.interactor.validator.RegionValidator;
import com.google.kamil1338.smsspamer.utils.App;
import com.google.kamil1338.smsspamer.utils.Conf;
import com.google.kamil1338.smsspamer.utils.preferences.Pref;
import com.google.kamil1338.smsspamer.utils.preferences.PrefType;
import com.google.kamil1338.smsspamer.view.fragment.model.ConfigurationModel;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ListIterator;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class SmsService extends Service {

    public static final String CONFIGURATION_MODEL = "CONFIGURATION_MODEL";

    //Nazwy plików
    public static final String FILE_NAME_MESSAGES = "messages.txt";
    public static final String FILE_NAME_NUMBERS = "numbers.txt";
    public static final String FILE_NAME_FAILS = "fails.txt";
    public static final String FILE_NAME_OK = "correct_sent.txt";

    //Akcje
    private static final String ACTION_SENT = "ACTION_SMS_SENT";
    private static final String ACTION_DELIVERED = "ACTION_SMS_DELIVERED";
    public static final String START_WORKING = "START_WORKING";
    public static final String STOP_WORKING = "STOP_WORKING";
    public static final String EXTRA_HASH = "EXTRA_HASH";

    private final SmsBinder smsBinder = new SmsBinder();

    private ServiceWorkingState workingState = ServiceWorkingState.NOT_WORKING;

    private ISmsService.ISmsServiceListener listener;
    private ListIterator<String> phoneNumberIterator;

    //tymczasowe obiekty
    private String tmpNumber = "";
    private String tmpMessage = "";
    private int tmpHashCode = 0;
    private int sentMessages = 0;

    //Timery
    private Handler waitHandler;
    private Handler delayedHandler;

    private IFileOpenable messagesOpener;
    private IFileOpenable phoneNumbersOpener;

    private ConfigurationModel configurationModel;
    private SmsManager smsManager;
    private RandomIntervalRange randomIntervalRange;
    private RandomMessage randomMessage;

    private PendingIntent sentPI;
    private PendingIntent deliveredPI;

    private PhoneNumberValidator phoneNumberValidator;

    private boolean isWorkingStopped = false;

    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss", new Locale("PL"));

    public SmsService() {
        messagesOpener = new MessagesOpener();
        phoneNumbersOpener = new PhoneNumbersOpener();
        smsManager = SmsManager.getDefault();

        sentPI = PendingIntent.getBroadcast(App.getContext(), 0, new Intent(ACTION_SENT), 0);
        deliveredPI = PendingIntent.getBroadcast(App.getContext(), 0, new Intent(ACTION_DELIVERED), 0);

        phoneNumberValidator = new LengthValidator(new RegionValidator(null));
        waitHandler = new Handler(Looper.getMainLooper());
        delayedHandler = new Handler();
        randomIntervalRange = new RandomIntervalRange();
        randomMessage = new RandomMessage(messagesOpener.getReadedData());
        /** Utworzenie katalogów*/
        App.getContext().getExternalFilesDir(null);
    }

    /**
     * <p>
     *     Klasa bindera.
     * </p>
     * */
    public class SmsBinder extends Binder {

        public SmsService getService() {
            return SmsService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return smsBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final String action = intent.getAction();
        if (action != null) {
            if (START_WORKING.equals(action)) {
                workingState = ServiceWorkingState.WORKING;
                configurationModel = intent.getParcelableExtra(CONFIGURATION_MODEL);
                randomIntervalRange.setModel(configurationModel);
                isWorkingStopped = false;
                /** Przygotanie iteratora z numerami telefonów*/
                phoneNumberIterator = phoneNumbersOpener.getReadedDataIterator();
                listener.onServiceWorking(workingState);
                startWaitHandler(0);
            } else if (STOP_WORKING.equals(action)) {
                stopWorking();
                stopSelf();
            }
        }
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        registerReceivers(false);
    }

    /**
     * <p>
     *     Ustawienie słuchacza.
     *     @param listener Obiekt obserwujący.
     * </p>
     * */
    public void setSmsListener(final ISmsService.ISmsServiceListener listener) {
        if (listener != null) {
            this.listener = listener;
            messagesOpener.getResponse(listener);
            phoneNumbersOpener.getResponse(listener);
            listener.onServiceWorking(workingState);
        } else {
            this.listener = new EmptyListener();
        }
    }

    /**
     * <p>
     *     Wysłanie sms'a na aktualny numer.
     *
     *     @param delay opóźnienie wysłania wiadomości.
     * </p>
     * */
    private void send(final long delay) {
        Log.d(Conf.TAG, "SmsService - send(" + delay + ")");
        delayedHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                incrementPrefAndSentMessages();
                listener.onSendedMessagesChanged(sentMessages, configurationModel.getPhoneNumbersCount());
                if (phoneNumberValidator.validate(tmpNumber)) {
                    startWaitHandler(configurationModel.getWaitTime());
                    registerReceivers(true);
                    smsManager.sendTextMessage("+" + tmpNumber, null, tmpMessage, sentPI, deliveredPI);
                } else {
                    logResultToLocalStorage(false, "Niewłaściwy format numeru");
                    checkNextNumber();
                }
            }
        }, delay);
    }

    /**
     * <p>
     *     Rozpoczęcie odliczania maksymalnego czasu oczekiwania.
     *     @param waitTime Czas oczekiwania w milisekundach.
     * </p>
     * */
    private void startWaitHandler(final long waitTime) {
        waitHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (waitTime > 0) {
                    Log.d(Conf.TAG, "SmsService - TIMEOUT");
                    logResultToLocalStorage(false, "TIMEOUT");
                }
                checkNextNumber();
            }
        }, waitTime);
    }

    /**
     * <p>
     *     Zatrzymanie licznika.
     * </p>
     * */
    private void stopWaitHandler() {
        waitHandler.removeCallbacksAndMessages(null);
    }

    /**
     * <p>
     *     Zatrzymanie licznika opóźnienia.
     * </p>
     * */
    private void stopDelayedHandler() {
        delayedHandler.removeCallbacksAndMessages(null);
    }

    /**
     * <p>
     *     Przygotowanie danych i wysłanie wiadomości.
     * </p>
     * */
    private void checkNextNumber() {
        /** Sprawdzenie czy przerwano wysyłanie*/
        if (isWorkingStopped) {
            stopWorking();
            return;
        }

        registerReceivers(false);

        tmpMessage = randomMessage.getRandomMessage();
        Log.d(Conf.TAG, "SmsService - checkNextNumber() - tmpMessage[" + tmpMessage + "]");

        /** Jeśli jest jeszcze jakiś numer do wysłania i zadana ilość SMS'ów nie została przekroczona*/
        if (phoneNumberIterator.hasNext() && configurationModel.getPhoneNumbersCount() > sentMessages) {
            tmpNumber = phoneNumberIterator.next();
            send(TimeUnit.SECONDS.toMillis(randomIntervalRange.getRandomValue()));
        } else {
            stopWorking();
        }
    }

    /**
     * <p>
     *     Zarządzenie rejestracją odbiorników.
     *
     *     @param register Jeśli TRUE to zarejestruj.
     * </p>
     * */
    private void registerReceivers(boolean register) {
        if (register) {
            App.getContext().registerReceiver(deliveredSmsReceiver, new IntentFilter(ACTION_DELIVERED));
            App.getContext().registerReceiver(sentSmsReceiver, new IntentFilter(ACTION_SENT));
        } else {
            try {
                App.getContext().unregisterReceiver(deliveredSmsReceiver);
            } catch (IllegalArgumentException e) {}

            try {
                App.getContext().unregisterReceiver(sentSmsReceiver);
            } catch (IllegalArgumentException e) {}
        }
    }

    private void stopWorking() {
        Log.d(Conf.TAG, "SmsService - stopWorking()");
        registerReceivers(false);
        stopWaitHandler();
        stopDelayedHandler();
        sentMessages = 0;
        workingState = ServiceWorkingState.NOT_WORKING;
        listener.onServiceWorking(workingState);
        listener.onCompleted();
        phoneNumbersOpener.getResponse(listener);
        stopSelf();
    }

    /**
     * <p>
     *     Ujednolicone zapisywanie logów do pamięci telefonu.
     *
     *     @param positive Jeśli TRUE to zapisuje loga do pliku z numerami, do których udało
     *                     się wysłać poprawną wiadomość. Jeśli FALSE to odwrotnie.
     *     @param reason Powód, dla którego wystąpiło niepowodzenie podczas wysłania wiadomości.
     *                   Wymagane jeśli {positive == FALSE}.
     * </p>
     * */
    private void logResultToLocalStorage(boolean positive, @Nullable  String reason) {
        try {
            if (positive) {
                FileWriter positiveFile =
                        new FileWriter(new File(App.getContext().getExternalFilesDir(null), FILE_NAME_OK), true);
                positiveFile.append(simpleDateFormat.format(new Date(System.currentTimeMillis()))).append(", ")
                        .append(tmpNumber).append(", ")
                        .append(tmpMessage).append("\n");
                positiveFile.close();
            } else {
                FileWriter negativeFile =
                        new FileWriter(new File(App.getContext().getExternalFilesDir(null), FILE_NAME_FAILS), true);
                negativeFile.append(simpleDateFormat.format(new Date(System.currentTimeMillis()))).append(", ")
                        .append(tmpNumber).append(", ")
                        .append((reason != null ? reason : "NULL")).append(", ")
                        .append(tmpMessage).append("\n");
                Log.d(Conf.TAG, tmpNumber + ", " + reason);
                negativeFile.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * <p>
     *     Odbiornik komunikatów, które są odbierane podczas odbierania raportu doręczenia.
     * </p>
     * */
    private final BroadcastReceiver deliveredSmsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            stopWaitHandler();
            Log.d(Conf.TAG, "SmsServices - deliveredSmsReceiver - getResultCode(" + getResultCode() + ")");
            if (getResultCode() == Activity.RESULT_CANCELED) {
                Log.d(Conf.TAG, "SmsService - deliveredSmsReceiver - SMS not delivered");
                logResultToLocalStorage(false, "SMS not delivered");
            } else {
                Log.d(Conf.TAG, "SmsService - deliveredSmsReceiver - SMS delivered [" + tmpNumber + "]");
                logResultToLocalStorage(true, null);
            }

            checkNextNumber();
        }
    };

    /**
     * <p>
     *     Inkrementacja prefsa
     * </p>
     * */
    private void incrementPrefAndSentMessages() {
        sentMessages++;
        int index = Pref.getInt(PrefType.LAST_PHONE_NUMBER_INDEX);
        Pref.setInt(PrefType.LAST_PHONE_NUMBER_INDEX, ++index);
    }

    /**
     * <p>
     *     Odbiornik komunikatów, które są odbierane podczas wysyłania wiadomości.
     * </p>
     * */
    private final BroadcastReceiver sentSmsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(Conf.TAG, "SmsService - sentSmsReceiver - getResult(" + getResultCode() + ")");
            switch (getResultCode()) {
                case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                    Log.d(Conf.TAG, "SmsService - sentSmsReceiver - Generic failure");
                    logResultToLocalStorage(false, "Generic failure");
                    break;
                case SmsManager.RESULT_ERROR_NO_SERVICE:
                    Log.d(Conf.TAG, "SmsService - sentSmsReceiver - No service");
                    logResultToLocalStorage(false, "No service");
                    break;
                case SmsManager.RESULT_ERROR_NULL_PDU:
                    Log.d(Conf.TAG, "SmsService - sentSmsReceiver - Null PDU");
                    logResultToLocalStorage(false, "Null PDU");
                    break;
                case SmsManager.RESULT_ERROR_RADIO_OFF:
                    Log.d(Conf.TAG, "SmsService - sentSmsReceiver - Radio off");
                    logResultToLocalStorage(false, "Radio off");
                    break;
            }
        }
    };
}
