package com.google.kamil1338.smsspamer.view.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Locale;

import com.google.kamil1338.smsspamer.R;

/**
 * Created by pierudzki on 2016-05-31.
 */
public class ProgressFragment extends Fragment implements ISynchronizable {

    private ProgressBar progressBar;
    private TextView counterLabel;
    private Object progressSyncObj;
    private boolean isViewCreated = false;
    private TextView waitingForStop;
    private ProgressBar spinningBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.progress_layout, container, false);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        counterLabel = (TextView) view.findViewById(R.id.counter);
        waitingForStop = (TextView) view.findViewById(R.id.waiting_for_stop);
        spinningBar = (ProgressBar) view.findViewById(R.id.progressBar2);
        isViewCreated = true;
        synchronized (progressSyncObj) {
            progressSyncObj.notifyAll();
        }
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isViewCreated = false;
    }

    /**
     * <p>
     *     Aktualizacja stanu paska postępu.
     * </p>
     * */
    public void changeProgress(int sendedMessages, int phoneNumbersCount) {
        progressBar.setMax(phoneNumbersCount);
        progressBar.setProgress(sendedMessages);
        counterLabel.setText(String.format(new Locale("pl"), "%d/%d", sendedMessages, phoneNumbersCount));
        spinningBar.setVisibility(View.GONE);
    }

    /**
     * <p>
     *     Pokazanie informacji, że trwa zatrzymywanie wysyłania
     * </p>
     * */
    public void showWaitinginfo(boolean show) {
        if (show) {
            waitingForStop.setVisibility(View.VISIBLE);
        } else {
            waitingForStop.setVisibility(View.GONE);
        }
    }

    @Override
    public void setSyncObject(final Object syncObject) {
        this.progressSyncObj = syncObject;
    }

    @Override
    public boolean isViewCreated() {
        return isViewCreated;
    }
}
