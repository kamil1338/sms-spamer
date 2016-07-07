package com.google.kamil1338.smsspamer.utils.thread;

import android.os.Handler;
import android.os.Looper;

/**
 * <p>
 *     Klasa odpowiada za wykonanie zadania na wątku interfejsu graficznego (wątku głównym).
 *
 *     Created by pierudzki on 2016-06-02.
 * </p>
 */
public class GuiThread {

    private static final Handler mainHandler = new Handler(Looper.getMainLooper());

    /**
     * <p>
     *     Metoda odpowiada za uruchomieniu podanego zadania na wątku głównym aplikacji. W kolejce FIFO.
     * </p>
     * @param runnable Zadanie do wykonania.
     * */
    public static void runOnUiThread(final Runnable runnable) {
        if(Looper.myLooper() == Looper.getMainLooper())
            runnable.run();
        else
            mainHandler.post(runnable);
    }
}