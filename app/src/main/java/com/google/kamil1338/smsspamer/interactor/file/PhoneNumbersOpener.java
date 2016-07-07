package com.google.kamil1338.smsspamer.interactor.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import com.google.kamil1338.smsspamer.ISmsService;
import com.google.kamil1338.smsspamer.interactor.service.SmsService;
import com.google.kamil1338.smsspamer.utils.App;
import com.google.kamil1338.smsspamer.utils.preferences.Pref;
import com.google.kamil1338.smsspamer.utils.preferences.PrefType;

/**
 * Created by pierudzki on 2016-06-01.
 */
public class PhoneNumbersOpener implements IFileOpenable {

    private List<String> numbers;
    private int remained;

    public PhoneNumbersOpener() {
        File directory = App.getContext().getExternalFilesDir(null);
        directory.mkdirs();

        File file = new File(directory, SmsService.FILE_NAME_NUMBERS);
        if (!file.exists()) {
            DefaultFileProvider.provideDefaultFile(SmsService.FILE_NAME_NUMBERS, directory);
        }
        numbers = new ArrayList<>();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            numbers = new ArrayList<>();
            for (String line; (line = bufferedReader.readLine()) != null; ) {
                numbers.add(line.trim());
            }
            bufferedReader.close();
            remained = numbers.size();
        } catch (IOException e) {
            remained = -1;
        }
    }

    @Override
    public void getResponse(ISmsService.ISmsServiceListener listener) {
        if (remained == -1) {
            listener.onRemainPhoneNumbers(remained);
            return;
        }
        /** Sprawdzenie czy używano już tengo pliku z numerami*/
        if (Pref.getString(PrefType.LAST_PHONE_LIST_CONTROLL_SUM).equals(String.valueOf(numbers.hashCode()))) {
            /** Pominięcie numerów, które otrzymały już wiadomość.*/
            remained = numbers.size() - Pref.getInt(PrefType.LAST_PHONE_NUMBER_INDEX);
        } else {
            /** Zapisanie nowej sumy kontrolnej*/
            Pref.setString(PrefType.LAST_PHONE_LIST_CONTROLL_SUM, String.valueOf(numbers.hashCode()));
            Pref.setInt(PrefType.LAST_PHONE_NUMBER_INDEX, 0);
            remained = numbers.size();
        }
        listener.onRemainPhoneNumbers(remained);
    }

    @Override
    public List<String> getReadedData() {
        return numbers;
    }

    @Override
    public ListIterator<String> getReadedDataIterator() {
        return numbers.listIterator(Pref.getInt(PrefType.LAST_PHONE_NUMBER_INDEX));
    }

    @Override
    public int getReadedDataSize() {
        return numbers.size() - Pref.getInt(PrefType.LAST_PHONE_NUMBER_INDEX);
    }
}
