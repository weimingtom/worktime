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
import android.net.Uri;
import android.util.Log;
import eu.vranckaert.worktime.activities.HomeActivity;
import eu.vranckaert.worktime.activities.timeregistrations.RegistrationDetailsActivity;
import eu.vranckaert.worktime.constants.Constants;
import eu.vranckaert.worktime.model.TimeRegistration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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

    /**
     * Send something with a single file attached. Using this method you can specify the subject, body and application
     * chooser title using the string resource id's.
     * @param activity The activity starting the action from.
     * @param subjectId The subject string resource id.
     * @param bodyId The body string resource id.
     * @param file The file attach.
     * @param chooserTitleId The application chooser's title string resource id.
     */
    public static void sendSomething(Activity activity, int subjectId, int bodyId, File file, int chooserTitleId) {
        sendSomething(
                activity,
                activity.getString(subjectId),
                activity.getString(bodyId),
                file,
                activity.getString(chooserTitleId)
        );
    }

    /**
     * Send something with a single file attached.
     * @param activity The activity starting the action from.
     * @param subject The subject string.
     * @param body The body string.
     * @param file The file attach.
     * @param chooserTitle The application chooser's title string.
     */
    public static void sendSomething(Activity activity, String subject, String body, File file, String chooserTitle) {
        List<File> files = new ArrayList<File>();
        files.add(file);
        sendSomething(activity, subject, body, files, chooserTitle);
    }

    /**
     * Send something with a bunch of files attached. Using this method you can specify the subject, body and application
     * chooser title using the string resource id's.
     * @param activity The activity starting the action from.
     * @param subjectId The subject string resource id.
     * @param bodyId The body string resource id.
     * @param files The files to attach.
     * @param chooserTitleId The application chooser's title string resource id.
     */
    public static void sendSomething(Activity activity, int subjectId, int bodyId, List<File> files, int chooserTitleId) {
        sendSomething(
                activity,
                activity.getString(subjectId),
                activity.getString(bodyId),
                files,
                activity.getString(chooserTitleId)
        );
    }

    /**
     * Send something with a bunch of files attached. If only one file is attached it will be handled as a mail with a
     * single attachment. If no files are attached it will be handled as a simple mail.
     * @param activity The activity starting the action from.
     * @param subject The subject string.
     * @param body The body string.
     * @param files The files to attach. Can be null or of size 0 if you do not want to attach any file.
     * @param chooserTitle The application chooser's title string.
     */
    public static void sendSomething(Activity activity, String subject, String body, List<File> files, String chooserTitle) {
        Log.d(LOG_TAG, "About to send something...");
        Log.d(LOG_TAG, "At least one attachment included? " + (files.size()>0?"Yes":"No"));

        String action = Intent.ACTION_SEND;
        if (files != null && files.size() > 1) {
            Log.d(LOG_TAG, "More than one attachment included");
            action = Intent.ACTION_SEND_MULTIPLE;
        }

        Intent emailIntent = new Intent(action);
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, body);

        if (files != null && files.size() == 1) {
            Log.d(LOG_TAG, "Adding one file...");
            emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:/" + files.get(0)));
        } else if(files != null) {
            Log.d(LOG_TAG, "Adding multiple files...");
            ArrayList<Uri> uris = new ArrayList<Uri>();
            for (File file : files) {
                Uri uri = Uri.fromFile(file);
                uris.add(uri);
                emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
            }
            emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
        }

        Log.d(LOG_TAG, "Launching share, application chooser if needed!");
        activity.startActivity(Intent.createChooser(emailIntent, chooserTitle));
    }
}
