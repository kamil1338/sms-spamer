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
import com.google.kamil1338.smsspamer.utils.CharactersUtil;

/**
 * Created by pierudzki on 2016-06-01.
 */
public class MessagesOpener implements IFileOpenable {

    private static final int MESSAGE_CHARACTER_LIMIT = 140;

    private List<String> messages;
    private boolean status = false;

    public MessagesOpener() {
        File directory = App.getContext().getExternalFilesDir(null);
        directory.mkdirs();

        File file = new File(directory, SmsService.FILE_NAME_MESSAGES);
        if (!file.exists()) {
            DefaultFileProvider.provideDefaultFile(SmsService.FILE_NAME_MESSAGES, directory);
        }
        messages = new ArrayList<>();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            messages = new ArrayList<>();
            for (String line; (line = bufferedReader.readLine()) != null; ) {
                messages.add(CharactersUtil.transformToAscii(line.substring(0, (line.length() > MESSAGE_CHARACTER_LIMIT ? MESSAGE_CHARACTER_LIMIT : line.length()))));
            }
            bufferedReader.close();
            status = true;
        } catch (IOException e) {
            status = false;
        }
    }

    @Override
    public void getResponse(ISmsService.ISmsServiceListener listener) {
        listener.onReadMessagesResult(status);
    }

    @Override
    public List<String> getReadedData() {
        return messages;
    }

    @Override
    public ListIterator<String> getReadedDataIterator() {
        return messages.listIterator();
    }

    @Override
    public int getReadedDataSize() {
        return messages.size();
    }
}
