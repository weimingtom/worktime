/*
 *  Copyright 2012 Dirk Vranckaert
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
package eu.vranckaert.worktime.activities.test;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import com.j256.ormlite.support.ConnectionSource;
import com.jayway.android.robotium.solo.Solo;
import eu.vranckaert.worktime.constants.Constants;
import eu.vranckaert.worktime.dao.utils.DaoConstants;
import eu.vranckaert.worktime.dao.utils.DatabaseHelper;
import eu.vranckaert.worktime.testutils.ScreenshotUtil;
import eu.vranckaert.worktime.utils.preferences.Preferences;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * User: DIRK VRANCKAERT
 * Date: 24/02/12
 * Time: 9:31
 */
public class MyActivityTestCase<V extends Activity> extends ActivityInstrumentationTestCase2<V> {
    private final String LOG_TAG = MyActivityTestCase.class.getSimpleName();
    private Class testClass;
    private int screenshotCountNumber;
    
    private Intent customIntent = null;

    protected Solo solo;

    protected MyActivityTestCase(Class clazz) {
        super("eu.vranckaert.worktime", clazz);
        this.testClass = clazz;
    }

    /**
     * Gets you the full package and class name of this test.
     * @return The full package and class name of this test.
     */
    private String getFullTestClassName() {
        return this.getClass().getName();
    }

    @Override
    protected void setUp() throws Exception {
        beforeTestBeforeActivityLaunch();
        super.setUp();
        launchActivity();
        removeAllPreferences();
        solo = new Solo(getInstrumentation(), getActivity());
        beforeTestAfterActivityLaunch();
    }

    @Override
    protected void runTest() throws Throwable {
        super.runTest();
        afterTest();
        getActivity().finish();
    }

    @Override
    protected void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }

    /**
     * Everything to be done before the test starts, AND before the activity is launched. In here no calls can be done
     * to the {@link android.test.ActivityInstrumentationTestCase2#getActivity()} method. 
     */
    private void beforeTestBeforeActivityLaunch() {
        String[] tables = {"commentHistory", "project", "task", "timeregistration"};
        cleanUpDatabase(Arrays.asList(tables));
    }

    /**
     * Launches the activity. If the {@link MyActivityTestCase#customIntent} is empty it will just start the activity.
     * If the intent is available it will use start the activity with the provided intent!
     */
    private void launchActivity() {
        if (customIntent != null) {
            setActivityIntent(customIntent);
        }
        getActivity();
    }

    /**
     * Everything to be done before the test starts, but after the activity is launched. In here you can call the 
     * {@link android.test.ActivityInstrumentationTestCase2#getActivity()} method.
     */
    private void beforeTestAfterActivityLaunch() {
        screenshotCountNumber = -1;
        takeScreenshot();
    }

    /**
     * Everything to be done after test
     */
    private void afterTest() {
        if (solo.getViews().size() > 0) {
            try {
                ScreenshotUtil.takeScreenShot(
                        getActivity(),
                        solo.getViews().get(0),
                        getFullTestClassName(),
                        getName(),
                        getLastScreenshotCountNumber()
                );
            } catch (Exception e) {
                Log.e(LOG_TAG, "Not creating a screenshot due to an exception!", e);
            }
        } else {
            Log.w(LOG_TAG, "Solo does not contain any views, not creating a screenshot!");
        }
    }

    /**
     * Get the next number for the next screenshot.
     * @return The number of the next screenshot in {@link String} format.
     * @throws Exception An exception if you are exceeding the limit of 9999 screenshots.
     */
    private String getNextScreenshotCountNumber() throws Exception {
        screenshotCountNumber++;

        if (screenshotCountNumber < 10) {
            return "000" + screenshotCountNumber;
        } else if (screenshotCountNumber >= 10 && screenshotCountNumber < 100) {
            return "00" + screenshotCountNumber;
        } else if (screenshotCountNumber >= 100 && screenshotCountNumber < 1000) {
            return "0" + screenshotCountNumber;
        } else if (screenshotCountNumber >= 1000 && screenshotCountNumber < 9999) {
            return "" + screenshotCountNumber;
        }

        throw new Exception("No more than 9999 screenshots are allowed!");
    }

    /**
     * Get the number of the last screenshot to be taken of the app.
     * @return The number of the last screenshot (9999).
     */
    private String getLastScreenshotCountNumber() {
        return "9999";
    }

    /**
     * Removes all the preferences
     */
    protected void removeAllPreferences() {
        List<String> preferenceKeys = new ArrayList<String>();

        Class prefKeysClass = Constants.Preferences.Keys.class;
        Field[] keyFields = prefKeysClass.getFields();
        for(Field field : keyFields) {
            try {
                preferenceKeys.add((String)field.get(null));
            } catch (IllegalAccessException e) {}
        }
        
        for (String key : preferenceKeys) {
            Preferences.removePreference(getActivity(), key);
        }
    }

    /**
     * Removes the database
     */
    private void cleanUpDatabase(List<String> dbTables) {
        Log.i(LOG_TAG, "Preparing to clean up database...");
        DatabaseHelper dbHelper = new DatabaseHelper(getInstrumentation().getTargetContext());
        ConnectionSource cs = dbHelper.getConnectionSource();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        
        Log.i(LOG_TAG, "Dropping all tables");
        for (String table : dbTables) {
            db.execSQL("DROP TABLE IF EXISTS " + table);
        }

        Log.i(LOG_TAG, "Executing the onCreate(..)");
        dbHelper.onCreate(db, cs);
        
        Log.i(LOG_TAG, "Verifying the data...");
        for (String table : dbTables) {
            Cursor c = db.query(table, new String[]{"id"}, null, null, null, null, null);
            int count = c.getCount();
            if (count != 1 && (table.equals("project") || table.equals("task"))) {
                dbHelper.close();
                Log.e(LOG_TAG, "We should have 1 record for table " + table + " after cleanup but we found " + count + " record(s)");
                throw new RuntimeException("Error during cleanup of DB, exactly one record should be present for table " + table + " but we found " + count + " record(s)");
            } else if (count != 0 && !(table.equals("project") || table.equals("task"))) {
                dbHelper.close();
                Log.e(LOG_TAG, "We should have 0 records for table " + table + " after cleanup but we found " + count + " record(s)");
                throw new RuntimeException("Error during cleanup of DB, no records should be present for table " + table + " but we found " + count + " record(s)");
            }
        }

        Log.i(LOG_TAG, "The database has been cleaned!");
        dbHelper.close();
    }
    
    /*
     * PROTECTED METHODS
     */

    protected void takeScreenshot() {
        if (solo.getViews().size() > 0) {
            try {
                ScreenshotUtil.takeScreenShot(
                        getActivity(),
                        solo.getViews().get(0),
                        getFullTestClassName(),
                        getName(),
                        getNextScreenshotCountNumber()
                );
            } catch (Exception e) {
                Log.e(LOG_TAG, "Not creating a screenshot due to an exception!", e);
            }
        } else {
            Log.w(LOG_TAG, "Solo does not contain any views, not creating a screenshot!");
        }
    }
    
    protected void setPreference(String key, Object value) {
        SharedPreferences sp = getActivity().getSharedPreferences(Constants.Preferences.PREFERENCES_NAME, Activity.MODE_PRIVATE);

        if (value == null) {
            return;
        }
        
        SharedPreferences.Editor editor = sp.edit();
        if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
        } else if (value instanceof Float) {
            editor.putFloat(key, (Float) value);
        } else if (value instanceof Integer) {
            editor.putInt(key, (Integer) value);
        } else if (value instanceof Long) {
            editor.putLong(key, (Long) value);
        } else {
            editor.putString(key, value.toString());
        }
        editor.commit();
    }

    protected void setCustomIntent(Intent customIntent) {
        customIntent = customIntent;
    }
}
