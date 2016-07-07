package com.google.kamil1338.smsspamer.view.activity;

import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Process;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;

import com.google.kamil1338.smsspamer.ISmsService;
import com.google.kamil1338.smsspamer.R;
import com.google.kamil1338.smsspamer.interactor.enums.ServiceWorkingState;
import com.google.kamil1338.smsspamer.interactor.service.Sms;
import com.google.kamil1338.smsspamer.presenter.PresenterImpl;
import com.google.kamil1338.smsspamer.utils.preferences.Pref;
import com.google.kamil1338.smsspamer.utils.preferences.PrefType;
import com.google.kamil1338.smsspamer.utils.thread.GuiThread;
import com.google.kamil1338.smsspamer.view.fragment.ConfigurationFragment;
import com.google.kamil1338.smsspamer.view.fragment.ProgressFragment;
import com.google.kamil1338.smsspamer.view.fragment.model.ConfigurationModel;

public class MainActivity extends AppCompatActivity implements ISmsService.ISmsServiceListener {

    private CoordinatorLayout rootView;

    private PresenterImpl presenter;

    /** Fragments*/
    private ConfigurationFragment configurationFragment = new ConfigurationFragment();
    private ProgressFragment progressFragment = new ProgressFragment();

    /** FAB's*/
    private FloatingActionButton notWorkingMainFAB;
    private FloatingActionButton workingMainFAB;

    private CheckBox messagesCheckBox;
    private CheckBox contactsCheckBox;

    //Obiekty synchronizujące fragmenty
    private final Object progressFragmentSyncObject = new Object();
    private final Object configurationFragmentSyncObject = new Object();

    private boolean isServiceWorking = false;

    private Handler asyncHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Obiekt synchronizujący
        progressFragment.setSyncObject(progressFragmentSyncObject);
        configurationFragment.setSyncObject(configurationFragmentSyncObject);

        rootView = (CoordinatorLayout) findViewById(R.id.root_view);

        presenter = new PresenterImpl(this);

        messagesCheckBox = (CheckBox) findViewById(R.id.messages_checkbox);
        contactsCheckBox = (CheckBox) findViewById(R.id.contacts_checkbox);

        notWorkingMainFAB = (FloatingActionButton) findViewById(R.id.fab_main_not_working);
        workingMainFAB = (FloatingActionButton) findViewById(R.id.fab_main_working);

        workingMainFAB.setOnClickListener(stopFABListener);

        notWorkingMainFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfigurationModel model = configurationFragment.getConfigModel();
                if (model.isModelCorrect()) {
                    Sms.startWorking(model);
                } else {
                    Snackbar.make(rootView, R.string.configuration_incorrect, Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        HandlerThread handlerThread = new HandlerThread("AsyncUIChange", Process.THREAD_PRIORITY_DISPLAY);
        handlerThread.start();
        asyncHandler = new Handler(handlerThread.getLooper());

        if (savedInstanceState == null) {
            replaceFragment(configurationFragment, R.id.frame_layout);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        asyncHandler.getLooper().quit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.initialize();
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.uninitialize();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (isServiceWorking) {
            menu.getItem(2).setEnabled(false);
        } else {
            menu.getItem(2).setEnabled(true);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Snackbar.make(rootView, "¯\\_(ツ)_/¯", Snackbar.LENGTH_SHORT).show();
            return true;
        }

        if (id == R.id.action_info) {
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setMessage(R.string.help)
                    .setNegativeButton(R.string.ok_button, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create();
            dialog.show();
            return true;
        }

        if (id == R.id.action_refresh) {
            Pref.setInt(PrefType.LAST_PHONE_NUMBER_INDEX, 0);
            Pref.setString(PrefType.LAST_PHONE_LIST_CONTROLL_SUM, "");
            presenter.resetApp();
            Snackbar.make(rootView, R.string.app_resetted, Snackbar.LENGTH_SHORT).show();
        }

        if (id == R.id.about) {
            new AlertDialog.Builder(this)
                    .setTitle("")
                    .setMessage(R.string.icon_license)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    })
                    .show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRemainPhoneNumbers(final int remained) {
        if (remained > 0) {
            contactsCheckBox.setChecked(true);
        } else {
            contactsCheckBox.setChecked(false);
        }
        if (configurationFragment.isViewCreated()) {
            configurationFragment.setRemainPhoneNumbersCount(remained);
        } else {
            /** Uruchomienie zadania asynchroniczne ponieważ może dojść do zatrzymania GUI.*/
            asyncHandler.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        synchronized (configurationFragmentSyncObject) {
                            /** Czekanie do momentu utworzenia widoku*/
                            configurationFragmentSyncObject.wait();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    /** Modyfikowanie GUI z głównego wątku*/
                    GuiThread.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            configurationFragment.setRemainPhoneNumbersCount(remained);
                        }
                    });
                }
            });
        }
    }

    @Override
    public void onReadMessagesResult(boolean success) {
        messagesCheckBox.setChecked(success);
    }

    @Override
    public void onCompleted() {
        Snackbar.make(rootView, getString(R.string.sent_all), Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onServiceWorking(ServiceWorkingState state) {
        switch (state) {
            case WORKING:
                replaceFragment(progressFragment, R.id.frame_layout);
                notWorkingMainFAB.hide();
                workingMainFAB.show();
                isServiceWorking = true;
                break;
            case NOT_WORKING:
                replaceFragment(configurationFragment, R.id.frame_layout);
                notWorkingMainFAB.show();
                workingMainFAB.hide();
                isServiceWorking = false;
                break;
        }
    }

    @Override
    public void onSendedMessagesChanged(final int sendedMessages, final int phoneNumbersCount) {
        if (progressFragment.isViewCreated()) {
            progressFragment.changeProgress(sendedMessages, phoneNumbersCount);
        } else {
            /** Uruchomienie zadania asynchroniczne ponieważ może dojść do zatrzymania GUI.*/
            asyncHandler.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        synchronized (progressFragmentSyncObject) {
                            /** Czekanie do momentu utworzenia widoku*/
                            progressFragmentSyncObject.wait();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    GuiThread.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressFragment.changeProgress(sendedMessages, phoneNumbersCount);
                        }
                    });
                }
            });
        }
    }

    private final View.OnClickListener stopFABListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Sms.stopWorking();
            if (progressFragment.isVisible()) {
                progressFragment.showWaitinginfo(true);
            }
        }
    };

    /**
     * <p>
     * Podmiana fragmentu.
     * </p>
     *
     * @param fragment        Obiekt fragmentu,
     * @param containerViewId Kontener fragmentu.
     */
    private void replaceFragment(Fragment fragment, int containerViewId) {
        getFragmentManager().beginTransaction().replace(containerViewId, fragment, null).commit();
    }
}
