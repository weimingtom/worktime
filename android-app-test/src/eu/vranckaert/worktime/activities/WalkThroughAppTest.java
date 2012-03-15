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
package eu.vranckaert.worktime.activities;

import android.app.ListActivity;
import android.view.KeyEvent;
import android.widget.EditText;
import eu.vranckaert.worktime.R;
import eu.vranckaert.worktime.activities.reporting.ReportingCriteriaActivity;
import eu.vranckaert.worktime.activities.test.MyActivityTestCase;
import eu.vranckaert.worktime.activities.timeregistrations.AddEditTimeRegistrationsComment;
import eu.vranckaert.worktime.activities.timeregistrations.RegistrationDetailsActivity;
import eu.vranckaert.worktime.activities.timeregistrations.TimeRegistrationsActivity;
import eu.vranckaert.worktime.constants.Constants;
import eu.vranckaert.worktime.testutils.TestConstants;

/**
 * User: DIRK VRANCKAERT
 * Date: 14/03/12
 * Time: 16:21
 */
public class WalkThroughAppTest extends MyActivityTestCase<HomeActivity> {
    public WalkThroughAppTest() {
        super(HomeActivity.class);
    }

    public void testWalkThroughApp() {
        solo.assertCurrentActivity("The home activity is expected", HomeActivity.class);

        // Go to the time registrations activity
        solo.clickOnButton(0);
        solo.waitForActivity(TimeRegistrationsActivity.class.getSimpleName());
        solo.assertCurrentActivity("The time registrations activity is expected", TimeRegistrationsActivity.class);

        takeScreenshot();

        // Go to the reporting activity
        solo.clickOnImageButton(1);
        solo.waitForActivity(ReportingCriteriaActivity.class.getSimpleName());
        solo.assertCurrentActivity("The time reporting criteria activity is expected", ReportingCriteriaActivity.class);

        takeScreenshot();
        
        // Go to the home activity
        solo.clickOnImageButton(0);
        solo.waitForActivity(HomeActivity.class.getSimpleName());
        solo.assertCurrentActivity("The home activity is expected", HomeActivity.class);

        takeScreenshot();
        
        // Enable the punch-in bar on the time registrations screen
        setPreference(Constants.Preferences.Keys.TIME_REGISTRATION_PUNCH_BAR_ENABLED_ON_ALL_SCREENS, Boolean.TRUE);

        // Go to the time registrations activity
        solo.clickOnButton(0);
        solo.waitForActivity(TimeRegistrationsActivity.class.getSimpleName());
        solo.assertCurrentActivity("The time registrations activity is expected", TimeRegistrationsActivity.class);

        takeScreenshot();

        int trCount = ((ListActivity)solo.getCurrentActivity()).getListView().getAdapter().getCount();
        assertEquals("Expected no time registration to be available!", 0, trCount);

        // Start a new time registration
        solo.clickOnView(getActivity().findViewById(R.id.punchBarActionId));
        solo.clickOnText(getActivity().getString(R.string.default_project_name), 0, true);
        solo.clickOnText(getActivity().getString(R.string.default_task_name), 0, true);

        solo.waitForDialogToClose(TestConstants.Time.TWENTY_SECONDS);

        trCount = ((ListActivity)solo.getCurrentActivity()).getListView().getAdapter().getCount();
        assertEquals("Expected just one time registration to be available!", 1, trCount);

        //Go to the TR details activity using a long click and selecting the details option from the context-menu
        //Long click the first element
        solo.clickLongInList(0);
        //Select the 'View details' option
        solo.clickOnMenuItem(getActivity().getString(R.string.lbl_registrations_menu_details));
        solo.waitForActivity(TimeRegistrationsActivity.class.getSimpleName());
        solo.assertCurrentActivity("The time registration detail activity is expected", RegistrationDetailsActivity.class);

        //Go back with the device back-button
        solo.goBack();
        solo.waitForActivity(TimeRegistrationsActivity.class.getSimpleName());
        solo.assertCurrentActivity("The time registrations activity is expected", TimeRegistrationsActivity.class);

        //Add a comment to the TR
        //Long click the first element
        solo.clickLongInList(0);
        //Select the 'add comment' option
        solo.clickOnMenuItem(getActivity().getString(R.string.lbl_registrations_menu_add_comment));
        solo.waitForActivity(AddEditTimeRegistrationsComment.class.getSimpleName());
        solo.assertCurrentActivity("The add/edit time registration comment activity is expected", AddEditTimeRegistrationsComment.class);

        //Enter the comment
        EditText enterCommentEditText = (EditText)((AddEditTimeRegistrationsComment) solo.getCurrentActivity()).findViewById(R.id.tr_comment);
        String comment = "My android testcase comment";
        solo.enterText(enterCommentEditText, comment);
        assertEquals(comment, enterCommentEditText.getText().toString());

        //Click the ok button
        solo.clickOnButton(getActivity().getString(R.string.ok));
        solo.waitForDialogToClose(TestConstants.Time.FIVE_SECONDS);
        solo.waitForActivity(TimeRegistrationsActivity.class.getSimpleName());
        solo.assertCurrentActivity("The time registrations activity is expected", TimeRegistrationsActivity.class);

        //Prefrom the add comment on the TR again
        //Long click the first element
        solo.clickLongInList(0);
        //Select the 'add comment' option
        solo.clickOnMenuItem(getActivity().getString(R.string.lbl_registrations_menu_add_comment));
        solo.waitForActivity(AddEditTimeRegistrationsComment.class.getSimpleName());
        solo.assertCurrentActivity("The add/edit time registration comment activity is expected", AddEditTimeRegistrationsComment.class);
        String enteredComment = ((EditText)((AddEditTimeRegistrationsComment) solo.getCurrentActivity()).findViewById(R.id.tr_comment)).getText().toString();
        assertEquals(comment, enteredComment);

        //Click the cancel button
        solo.clickOnButton(getActivity().getString(R.string.cancel));
        solo.waitForDialogToClose(TestConstants.Time.FIVE_SECONDS);
        solo.waitForActivity(TimeRegistrationsActivity.class.getSimpleName());
        solo.assertCurrentActivity("The time registrations activity is expected", TimeRegistrationsActivity.class);

        //Go to the TR details activity using a single click on the list item
        solo.clickInList(0);
        solo.waitForActivity(TimeRegistrationsActivity.class.getSimpleName());
        solo.assertCurrentActivity("The time registration detail activity is expected", RegistrationDetailsActivity.class);
    }
}
