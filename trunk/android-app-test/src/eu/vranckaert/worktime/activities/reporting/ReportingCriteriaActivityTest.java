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
package eu.vranckaert.worktime.activities.reporting;

import android.app.Activity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import eu.vranckaert.worktime.R;
import eu.vranckaert.worktime.constants.Constants;
import eu.vranckaert.worktime.testutils.MyAndroidActivityTestCase;
import eu.vranckaert.worktime.utils.date.DateConstants;
import eu.vranckaert.worktime.utils.date.DateFormat;
import eu.vranckaert.worktime.utils.date.DateUtils;
import eu.vranckaert.worktime.utils.file.FileUtil;

import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * User: DIRK VRANCKAERT
 * Date: 23/01/12
 * Time: 8:06
 */
public class ReportingCriteriaActivityTest extends MyAndroidActivityTestCase<ReportingCriteriaActivity> {
    private Spinner dateRanges;
    private Button startButton;
    private Button endButton;
    private View actionExportButton;

    public ReportingCriteriaActivityTest() {
        super(ReportingCriteriaActivity.class);
    }

    @Override
    public List<String> getPreferenceKeysForRemoval() {
        String[] keys = {Constants.Preferences.Keys.WEEK_STARTS_ON};
        return Arrays.asList(keys);
    }

    @Override
    public Map<String, Object> getPreferenceKeyValuePairs() {
        return null;
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        setActivityInitialTouchMode(false);

        this.dateRanges = (Spinner) findViewById(R.id.reporting_criteria_date_range_spinner);
        this.startButton  = (Button) findViewById(R.id.reporting_criteria_date_range_start);
        this.endButton = (Button) findViewById(R.id.reporting_criteria_date_range_end);
        this.actionExportButton = findViewById(R.id.btn_action_export);
    }

    public void testPreconditions() {
        String[] possibleDateRanges = activity.getResources().getStringArray(R.array.array_reporting_criteria_date_range_spinner);

        String dateRangeString = (String) dateRanges.getSelectedItem();
        assertEquals(possibleDateRanges[0], dateRangeString);

        assertFalse("The button should be disabled", startButton.isEnabled());
        assertFalse("The button should be disabled", endButton.isEnabled());

        File file = FileUtil.getExportDir(activity);
        if (file == null) {
            assertEquals("The export button should be invisible on the activity", View.INVISIBLE, actionExportButton.getVisibility());
        } else {
            assertEquals("The export button should be visible on the activity", View.VISIBLE, actionExportButton.getVisibility());
        }
    }

    public void testDateRangeSelection() {
        String[] possibleDateRanges = activity.getResources().getStringArray(R.array.array_reporting_criteria_date_range_spinner);

        setPreference(Constants.Preferences.Keys.WEEK_STARTS_ON, "1");

        assertFalse("The button should be disabled", startButton.isEnabled());
        assertFalse("The button should be disabled", endButton.isEnabled());
        
        activity.runOnUiThread(
            new Runnable() {
                public void run() {
                    dateRanges.requestFocus();
                }
            }
        );

        sendKeys(KeyEvent.KEYCODE_DPAD_CENTER);
        for (int i = 1; i <= 4; i++) {
            sendKeys(KeyEvent.KEYCODE_DPAD_DOWN);
        }
        sendKeys(KeyEvent.KEYCODE_DPAD_CENTER);

        int pos = dateRanges.getSelectedItemPosition();
        String result = (String) dateRanges.getItemAtPosition(pos);

        assertEquals(possibleDateRanges[pos], result);
        assertTrue("The button should be enabled", startButton.isEnabled());
        assertTrue("The button should be enabled", endButton.isEnabled());

        sendKeys(KeyEvent.KEYCODE_DPAD_CENTER);
        for (int i = 1; i <= 2; i++) {
            sendKeys(KeyEvent.KEYCODE_DPAD_UP);
        }
        sendKeys(KeyEvent.KEYCODE_DPAD_CENTER);

        pos = dateRanges.getSelectedItemPosition();
        result = (String) dateRanges.getItemAtPosition(pos);

        assertEquals(possibleDateRanges[pos], result);
        assertFalse("The button should be disabled", startButton.isEnabled());
        assertFalse("The button should be disabled", endButton.isEnabled());

        Map<Integer, Date> dateBoundaries = DateUtils.TimeCalculator.calculateWeekBoundaries(-1, activity);
        String lastWeekStart = DateUtils.DateTimeConverter.convertDateToString(dateBoundaries.get(DateConstants.FIRST_DAY_OF_WEEK), DateFormat.LONG, activity);
        String lastWeekEnd = DateUtils.DateTimeConverter.convertDateToString(dateBoundaries.get(DateConstants.LAST_DAY_OF_WEEK), DateFormat.LONG, activity);

        assertEquals(lastWeekEnd, endButton.getText());
        assertEquals(lastWeekStart, startButton.getText());
    }
}
