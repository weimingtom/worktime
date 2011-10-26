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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import eu.vranckaert.worktime.activities.HomeActivity;
import eu.vranckaert.worktime.activities.timeregistrations.RegistrationDetailsActivity;
import eu.vranckaert.worktime.constants.Constants;
import eu.vranckaert.worktime.model.TimeRegistration;

/**
 * User: DIRK VRANCKAERT
 * Date: 06/02/11
 * Time: 04:40
 */
public class IntentUtil {
    private static final String LOG_TAG = IntentUtil.class.getSimpleName();

    /**
     * Navigate back to the home screen.
     * @param ctx The context.
     */
    public static void goHome(Context ctx) {
        Intent intent = new Intent(ctx, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        ctx.startActivity(intent);
    }

    /**
     * Opens the details activity.
     * @param activity The activity requesting the intent launch.
     * @param selectedRegistration The registration for which to open the details activity.
     */
    public static void openRegistrationDetailActivity(Activity activity, TimeRegistration selectedRegistration,
                                        TimeRegistration previousTimeRegistration,
                                        TimeRegistration nextTimeRegistration) {
        Intent registrationDetails = new Intent(activity, RegistrationDetailsActivity.class);
        Log.d(LOG_TAG, "Putting TR with id '" + selectedRegistration.getId() + "' on intent");
        registrationDetails.putExtra(Constants.Extras.TIME_REGISTRATION, selectedRegistration);
        registrationDetails.putExtra(Constants.Extras.TIME_REGISTRATION_PREVIOUS, previousTimeRegistration);
        registrationDetails.putExtra(Constants.Extras.TIME_REGISTRATION_NEXT, nextTimeRegistration);
        activity.startActivityForResult(registrationDetails, Constants.IntentRequestCodes.REGISTRATION_DETAILS);
    }

    /**
     * Opens the edit activities for a time registration. The activity will be started with the intent request code
     * {@link Constants.IntentRequestCodes#REGISTRATION_EDIT_DIALOG}.
     * @param activity The activity requesting the intent launch.
     * @param activityToLaunch The activity to launch.
     * @param timeRegistration A {@link TimeRegistration} instance to edit.
     */
    public static void openEditActivity(Activity activity, Class activityToLaunch, TimeRegistration timeRegistration) {
        Intent intent = new Intent(activity, activityToLaunch);
        intent.putExtra(Constants.Extras.TIME_REGISTRATION, timeRegistration);
        activity.startActivityForResult(intent, Constants.IntentRequestCodes.REGISTRATION_EDIT_DIALOG);
    }
}
