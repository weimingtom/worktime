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
import android.content.SharedPreferences;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import com.jayway.android.robotium.solo.Solo;
import eu.vranckaert.worktime.constants.Constants;
import eu.vranckaert.worktime.testutils.ScreenshotUtil;
import eu.vranckaert.worktime.utils.preferences.Preferences;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
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

    protected Solo solo;

    protected MyActivityTestCase(Class clazz) {
        super("eu.vranckaert.worktime", clazz);
        this.testClass = clazz;
    }
    
    private String getFullTestedClassPackageName() {
        String name = "";
        
        name += testClass.getPackage().getName();
        name += ".";
        name += testClass.getSimpleName();
        
        return name;
    }
    
    private String getFullTestClassName() {
        return this.getClass().getName();
    }

    @Override
    protected void setUp() throws Exception {
        beforeTestBeforeActivityLaunch();
        super.setUp();
        solo = new Solo(getInstrumentation(), getActivity());
        beforeTestAfterActivityLaunch();
    }

    @Override
    protected void runTest() throws Throwable {
        super.runTest();
        afterTest();
    }

    @Override
    protected void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }

    private void beforeTestBeforeActivityLaunch() {
        cleanUpDatabase();
        removeAllPreferences();
    }

    private void beforeTestAfterActivityLaunch() {
        screenshotCountNumber = -1;
        takeScreenshot();
    }

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
        removeAllPreferences();
        cleanUpDatabase();
    }

    protected String getNextScreenshotCountNumber() throws Exception {
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

    private String getLastScreenshotCountNumber() {
        return "9999";
    }

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
    
    private void cleanUpDatabase() {
        File dir = new File("/data/data/eu.vranckaert.worktime/databases/");
        if (!dir.exists()) {
            return;
        }
        File files [] = dir.listFiles();
        for (File file : files) {
            file.delete();
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
}
