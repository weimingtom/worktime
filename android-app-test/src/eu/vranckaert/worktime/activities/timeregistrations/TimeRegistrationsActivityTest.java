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
package eu.vranckaert.worktime.activities.timeregistrations;

import android.app.ListActivity;
import eu.vranckaert.worktime.R;
import eu.vranckaert.worktime.activities.HomeActivity;
import eu.vranckaert.worktime.activities.test.ActivityTestCase;
import eu.vranckaert.worktime.activities.widget.StopTimeRegistrationActivity;
import eu.vranckaert.worktime.constants.Constants;
import eu.vranckaert.worktime.testutils.TestUtil;

/**
 * User: DIRK VRANCKAERT
 * Date: 20/03/12
 * Time: 9:41
 */
public class TimeRegistrationsActivityTest extends ActivityTestCase<TimeRegistrationsActivity> {
    public TimeRegistrationsActivityTest() {
        super(TimeRegistrationsActivity.class);
    }

    public void testGoHome() {
        solo.assertCurrentActivity("The time registrations activity is expected", TimeRegistrationsActivity.class);
        solo.clickOnImageButton(0);
        solo.assertCurrentActivity("The home activity is expected", HomeActivity.class);
    }

    public void testDefaultEmptyList() {
        int trCount = ((ListActivity)solo.getCurrentActivity()).getListView().getAdapter().getCount();
        assertEquals("No TR's are expected!", 0, trCount);
    }

    public void testPunchBar() {
        // Enable the punch-in bar for all screens...
        setPreference(Constants.Preferences.Keys.TIME_REGISTRATION_PUNCH_BAR_ENABLED_ON_ALL_SCREENS, Boolean.TRUE);
        reloadActivity();

        int trCount = ((ListActivity)solo.getCurrentActivity()).getListView().getAdapter().getCount();
        assertEquals("No TR's are expected!", 0, trCount);

        // Start a new time registration
        solo.clickOnView(getActivity().findViewById(R.id.punchBarActionId));
        solo.waitForDialogToClose(TestUtil.Time.THIRTY_SECONDS);
        solo.clickOnText(getActivity().getString(R.string.default_project_name), 0, true);
        solo.clickOnText(getActivity().getString(R.string.default_task_name), 0, true);

        solo.waitForDialogToClose(TestUtil.Time.THIRTY_SECONDS);
        solo.waitForActivity(TimeRegistrationsActivity.class.getSimpleName());
        trCount = ((ListActivity)solo.getCurrentActivity()).getListView().getAdapter().getCount();
        assertEquals("Expected just one time registration to be available!", 1, trCount);

        takeScreenshot();

        solo.clickLongInList(0);
        solo.waitForDialogToClose(TestUtil.Time.SIXTY_SECONDS);
        assertTrue(solo.searchText(getActivity().getString(R.string.lbl_registrations_menu_details), 1, true));
        assertTrue(solo.searchText(getActivity().getString(R.string.lbl_registrations_menu_edit_start), 1, true));
        assertTrue(solo.searchText(getActivity().getString(R.string.lbl_registrations_menu_split), 1, true));
        assertTrue(solo.searchText(getActivity().getString(R.string.lbl_registrations_menu_add_comment), 1, true));
        assertTrue(solo.searchText(getActivity().getString(R.string.lbl_registrations_menu_edit_project_task), 1, true));
        assertTrue(solo.searchText(getActivity().getString(R.string.lbl_registrations_menu_delete), 1, true));
        solo.goBack();


        // End the time registration
        solo.clickOnView(getActivity().findViewById(R.id.punchBarActionId));
        solo.waitForActivity(StopTimeRegistrationActivity.class.getSimpleName());
        solo.clickOnButton(getActivity().getString(R.string.btn_widget_stop));
        solo.waitForActivity(TimeRegistrationsActivity.class.getSimpleName());
        solo.waitForDialogToClose(TestUtil.Time.THIRTY_SECONDS);
        trCount = ((ListActivity)solo.getCurrentActivity()).getListView().getAdapter().getCount();
        assertEquals("Expected just one time registration to be available!", 1, trCount);
        assertTrue(solo.searchText(getActivity().getString(R.string.home_comp_start_stop_time_registration_no_ongoing), 0, true));

        takeScreenshot();

        solo.clickLongInList(0);
        solo.waitForDialogToClose(TestUtil.Time.THIRTY_SECONDS);
        assertTrue(solo.searchText(getActivity().getString(R.string.lbl_registrations_menu_details), 1, true));
        assertTrue(solo.searchText(getActivity().getString(R.string.lbl_registrations_menu_edit_start), 1, true));
        assertTrue(solo.searchText(getActivity().getString(R.string.lbl_registrations_menu_edit_end), 1, true));
        assertTrue(solo.searchText(getActivity().getString(R.string.lbl_registrations_menu_split), 1, true));
        assertTrue(solo.searchText(getActivity().getString(R.string.lbl_registrations_menu_add_comment), 1, true));
        assertTrue(solo.searchText(getActivity().getString(R.string.lbl_registration_menu_restart), 1, true));
        assertTrue(solo.searchText(getActivity().getString(R.string.lbl_registrations_menu_edit_project_task), 1, true));
        assertTrue(solo.searchText(getActivity().getString(R.string.lbl_registrations_menu_delete), 1, true));
    }

    private void reloadActivity() {
        // Go to the home-activity
        solo.clickOnImageButton(0);
        solo.waitForActivity(HomeActivity.class.getSimpleName());
        solo.assertCurrentActivity("The home activity is expected", HomeActivity.class);

        // Open the time registrations activity
        solo.clickOnButton(0);
        solo.waitForActivity(TimeRegistrationsActivity.class.getSimpleName());
        solo.assertCurrentActivity("The time registrations activity is expected", TimeRegistrationsActivity.class);
    }
}
