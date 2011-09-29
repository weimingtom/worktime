package eu.vranckaert.worktime.activities.notifcationbar;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import eu.vranckaert.worktime.R;
import eu.vranckaert.worktime.activities.widget.StopTimeRegistrationActivity;
import eu.vranckaert.worktime.constants.Constants;
import eu.vranckaert.worktime.utils.preferences.Preferences;
import roboguice.activity.GuiceActivity;

/**
 * User: DIRK VRANCKAERT
 * Date: 27/04/11
 * Time: 21:46
 */
public class EndTimeRegistration extends GuiceActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!Preferences.getWidgetEndingTimeRegistrationCommentPreference(getApplicationContext())) {
            showDialog(Constants.Dialog.END_TIME_REGISTRATION_YES_NO);
        } else {
            launchStopTimeRegistrationActivity();
        }
    }

    @Override
    protected Dialog onCreateDialog(int dialogId) {
        Dialog dialog = null;
        switch(dialogId) {
            case Constants.Dialog.END_TIME_REGISTRATION_YES_NO: {
                AlertDialog.Builder alertEndTimeRegistration = new AlertDialog.Builder(this);
				alertEndTimeRegistration.setTitle(getString(R.string.lbl_notif_end_time_registration_request_title))
						   .setMessage(R.string.lbl_notif_end_time_registration_request)
						   .setCancelable(false)
						   .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                               public void onClick(DialogInterface dialog, int which) {
                                   removeDialog(Constants.Dialog.DELETE_PROJECT_YES_NO);
                                   launchStopTimeRegistrationActivity();
                               }
                           })
						   .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                               public void onClick(DialogInterface dialog, int which) {
                                   removeDialog(Constants.Dialog.DELETE_PROJECT_YES_NO);
                                   finish();
                               }
                           });
				dialog = alertEndTimeRegistration.create();
                break;
            }
        };
        return dialog;
    }

    private void launchStopTimeRegistrationActivity() {
        Intent intent = new Intent(getApplicationContext(), StopTimeRegistrationActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        startActivityForResult(intent, Constants.IntentRequestCodes.STOP_TIME_REGISTRATION);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        finish();
    }
}