package com.google.kamil1338.smsspamer.utils;

import android.app.Application;
import android.content.Context;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

/**
 * Created by pierudzki on 2016-05-05.
 */
public class App extends Application {

    private final static String EXCEPTIONS_FILE_NAME = "exceptions.txt";
    private static App instance;
    private Context context;

    /**
     * Domyślne zdarzenie dla nieprzechwyconych wyjątków.
     * */
    private Thread.UncaughtExceptionHandler defaultExceptionHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initialize();
    }

    private void initialize() {
        instance.context = super.getApplicationContext();
        defaultExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(uncaughtExceptionHandler);
    }

    public static Context getContext() {
        return instance.context;
    }

    private final Thread.UncaughtExceptionHandler uncaughtExceptionHandler = new Thread.UncaughtExceptionHandler() {
        @Override
        public void uncaughtException(Thread thread, Throwable ex) {
            File file = new File(context.getExternalFilesDir(null), EXCEPTIONS_FILE_NAME);
            try {
                PrintStream printStream = new PrintStream(new FileOutputStream(file, true), true, "UTF-8");
                ex.printStackTrace(printStream);
                printStream.close();
            } catch (FileNotFoundException | UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            if (defaultExceptionHandler != null) {
                defaultExceptionHandler.uncaughtException(thread, ex);
            }
        }
    };
}
