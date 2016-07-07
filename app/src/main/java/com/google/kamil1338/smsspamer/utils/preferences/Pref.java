package com.google.kamil1338.smsspamer.utils.preferences;


import android.content.SharedPreferences;

import com.google.kamil1338.smsspamer.utils.App;

/**
 * <p>
 *     Klasa pomocnicza odpowiedzialna za zarządzanie ustawieniami aplikacji (odczytywanie, zapisywanie).
 *     Created by pierudzki on 2016-05-31.
 * </p>
 */
public class Pref {

        public static void clear(){
            PreferencesManager.getInstance(App.getContext()).clearPreferences();
        }

        public static boolean getBoolean(PrefType preferenceType) {
            return getBoolean(preferenceType, preferenceType.getDefaultBooleanValue());
        }

        public static boolean getBoolean(PrefType preferenceType, boolean defaultValue) {
            return getDefaultPreferences().getBoolean(getKey(preferenceType), defaultValue);
        }

        public static void setValue(PrefType preferenceType, Object newValue) {
            SharedPreferences.Editor edit = getDefaultPreferences().edit();

            if(preferenceType.isBoolUsed())
                edit.putBoolean(getKey(preferenceType), (Boolean)newValue);
            if(preferenceType.isIntUsed())
                edit.putInt(getKey(preferenceType), (Integer) newValue);
            if(preferenceType.isLongUsed())
                edit.putLong(getKey(preferenceType), (Integer) newValue);
            if(preferenceType.isStringUsed())
                edit.putString(getKey(preferenceType), (String) newValue);

            edit.commit();
        }

        public static void setBoolean(PrefType preferenceType, boolean newValue) {
            SharedPreferences.Editor edit = getDefaultPreferences().edit();
            edit.putBoolean(getKey(preferenceType), newValue);
            edit.commit();
        }

        public static String getString(PrefType preferenceType) {
            return getDefaultPreferences().getString(getKey(preferenceType), preferenceType.getDefaultStringValue());
        }

        public static String getString(PrefType preferenceType, String defaultValue) {
            return getDefaultPreferences().getString(getKey(preferenceType), defaultValue);
        }

        public static void setString(PrefType preferenceType, String newValue) {
            SharedPreferences.Editor edit = getDefaultPreferences().edit();
            edit.putString(getKey(preferenceType), newValue);
            edit.commit();
        }

        public static void addToInt(PrefType preferenceType, int value) {
            setInt(preferenceType, getInt(preferenceType) + value);
        }

        public static int getInt(PrefType preferenceType) {
            return getDefaultPreferences().getInt(getKey(preferenceType), preferenceType.getDefaultIntValue());
        }

        public static int getInt(PrefType preferenceType, int defaultValue) {
            return getDefaultPreferences().getInt(getKey(preferenceType), defaultValue);
        }

        public static int getInt(String prefName, int defaultValue) {
            return getDefaultPreferences().getInt(prefName, defaultValue);
        }


        public static void setInt(PrefType preferenceType, int newValue) {
            SharedPreferences.Editor edit = getDefaultPreferences().edit();
            edit.putInt(getKey(preferenceType), newValue);
            edit.commit();
        }

        public static void setInt(String prefName, int newValue) {
            SharedPreferences.Editor edit = getDefaultPreferences().edit();
            edit.putInt(prefName, newValue);
            edit.commit();
        }

        public static long getLong(PrefType preferenceType) {
            return getDefaultPreferences().getLong(getKey(preferenceType), preferenceType.getDefaultLongValue());
        }

        public static long getLong(PrefType preferenceType, long defaultValue) {
            return getDefaultPreferences().getLong(getKey(preferenceType), defaultValue);
        }

        public static long getLong(String prefName, long defaultValue) {
            return getDefaultPreferences().getLong(prefName, defaultValue);
        }

        public static void setLong(PrefType preferenceType, long newValue) {
            SharedPreferences.Editor edit = getDefaultPreferences().edit();
            edit.putLong(getKey(preferenceType), newValue);
            edit.commit();
        }

        public static void setLong(String name, long newValue) {
            SharedPreferences.Editor edit = getDefaultPreferences().edit();
            edit.putLong(name, newValue);
            edit.commit();
        }

        public static boolean isSet(PrefType preferenceType){
            return getDefaultPreferences().contains(getKey(preferenceType));
        }

        private static String getKey(PrefType preferenceType) {
            return preferenceType.toString();
        }

        public static SharedPreferences getDefaultPreferences() {
            return PreferencesManager.getInstance(App.getContext()).getDefaultPreferences();
        }

        public static PrefType getKey(String key) {
            for (PrefType value : PrefType.values()) {
                if(key.equals(value.name()))
                    return value;
            }

            return null;
        }
}
