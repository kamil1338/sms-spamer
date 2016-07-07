package com.google.kamil1338.smsspamer.utils.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.kamil1338.smsspamer.utils.App;

/**
 * <p>
 * Klasa pomocnicza do zarządzania ustawieniami aplikacji.
 * Created by pierudzki on 2016-05-31.
 * </p>
 */
public class PreferencesManager {
    /**
     * Nazwa pliku w którym przechowywane będą ustawienia prywatne.
     */
    private static final String PREFERENCES_NAME = "Yanosik";

    private static PreferencesManager instance;

    private SharedPreferences privatePreferences;
    private SharedPreferences defaultPreferences;

    /**
     * Zwraca instancję PreferencesManager.
     */
    public static PreferencesManager getInstance(Context context) {
        if (instance == null)
            instance = new PreferencesManager(context);
        return instance;
    }

    private PreferencesManager(Context context) {
        privatePreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        defaultPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    /**
     * Zwraca obiekt z prywatnymi ustawieniami aplikacji.
     */
    public SharedPreferences getPreferences() {
        return privatePreferences;
    }

    public void clearPreferences() {
        App.getContext().getSharedPreferences(PREFERENCES_NAME, 0).edit().clear().commit();
    }

    /**
     * Zwraca obiekt z ustawieniami aplikacji (dostępnymi z poziomu okna
     * ustawień aplikacji).
     */
    public SharedPreferences getDefaultPreferences() {
        return defaultPreferences;
    }
}
