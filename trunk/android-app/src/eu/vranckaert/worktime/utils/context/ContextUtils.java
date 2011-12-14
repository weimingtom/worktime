/*
 *  Copyright 2011 Dirk Vranckaert
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package eu.vranckaert.worktime.utils.context;

import android.R;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import eu.vranckaert.worktime.constants.OSContants;

import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

/**
 * Context utils.
 * @author Dirk Vranckaert
 */
public class ContextUtils {
    private static final String LOG_TAG = ContextUtils.class.getSimpleName();

    /**
     * Get the current user locale.
     * @param context The context on which to search for the locale.
     * @return The locale.
     */
    public static Locale getCurrentLocale(Context context) {
        return context.getResources().getConfiguration().locale;
    }

    /**
     * Hides the soft keyboard of the device.
     * @param context The context on which a keyboard is shown.
     * @param someEditText Some {@link EditText} instance available on the view on which the keyboard should be hidden.
     */
    public static void hideKeyboard(Context context, EditText someEditText) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(someEditText.getWindowToken(), 0);
    }

    /**
     * Checks if an SD card is available in the current device.
     * @return {@link Boolean#TRUE} if SD card is available, {@link Boolean#FALSE} if no SD card is available.
     */
    public static boolean isSdCardAvailable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * Checks if data can be written to the SD card.
     * @return {@link Boolean#TRUE} if the SD card is writable. {@link Boolean#FALSE} if the SD card is not writable.
     */
    public static boolean isSdCardWritable() {
        return !Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED_READ_ONLY);
    }

    /**
     * A lookup method for the name of the current version (something like 1.0.3).
     * @param ctx The context.
     * @return The current version name.
     */
    public static String getCurrentApplicationVersionName(Context ctx) {
        String name = ctx.getPackageName();
        try {
            PackageInfo info = ctx.getPackageManager().getPackageInfo(name,0);
            return info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return "ERROR";
        }
    }

    /**
     * A lookup method for the code of the current version.
     * @param ctx The context.
     * @return The current version code.
     */
    public static int getCurrentApplicationVersionCode(Context ctx) {
        String name = ctx.getPackageName();
        try {
            PackageInfo info = ctx.getPackageManager().getPackageInfo(name,0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            return -1;
        }
    }

    /**
     * Get the version of the Android OS the user is running the application on.
     * @return The version (api) of the Android OS.
     */
    public static int getAndroidApiVersion() {
        return Build.VERSION.SDK_INT;
    }

    /**
     * Set the theme of an activity. This should only be used if it's a standard activity. <b>CAUTION: This will only
     * work if this method is called BEFORE the {@link android.app.Activity#onCreate(android.os.Bundle)} method!</b>
     * @param ctx The context.
     */
    public static void setActivityTheme(Context ctx) {
        Log.d(LOG_TAG, "Setting a default activity theme...");
        if (ContextUtils.getAndroidApiVersion() >= OSContants.API.HONEYCOMB_3_2) {
            Log.d(LOG_TAG, "Device is \"HOLO\" compatible, setting theme: android.R.style.Theme_Holo_Light_NoActionBar");
            ctx.setTheme(android.R.style.Theme_Holo_Light_NoActionBar);
        } else {
            Log.d(LOG_TAG, "Device is NOT YET \"HOLO\" compatible, setting theme: android.R.style.Theme_Light_NoTitleBar");
            ctx.setTheme(android.R.style.Theme_Light_NoTitleBar);
        }
    }

    /**
     * Set the theme of a popup. This should only be used for anything like a popup with a translucent background.
     * <b>CAUTION: This will only work if this method is called BEFORE the
     * {@link android.app.Activity#onCreate(android.os.Bundle)} method!</b>
     * @param ctx The context.
     */
    public static void setPopupTheme(Context ctx) {
        Log.d(LOG_TAG, "Setting a popup theme...");
        if (ContextUtils.getAndroidApiVersion() >= OSContants.API.HONEYCOMB_3_2) {
            Log.d(LOG_TAG, "Device is \"HOLO\" compatible, setting theme: android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth");
            ctx.setTheme(android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth);
        } else {
            Log.d(LOG_TAG, "Device is NOT YET \"HOLO\" compatible, setting theme: android.R.style.Theme_Translucent_NoTitleBar");
            //ctx.setTheme(android.R.style.Theme_Translucent_NoTitleBar);
            ctx.setTheme(android.R.style.Theme_Translucent_NoTitleBar);
        }
    }
}
