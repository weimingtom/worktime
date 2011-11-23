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
package eu.vranckaert.worktime.activities.timeregistrations;

import android.app.*;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.TimePicker;
import com.google.inject.Inject;
import com.google.inject.internal.Nullable;
import eu.vranckaert.worktime.R;
import eu.vranckaert.worktime.constants.Constants;
import eu.vranckaert.worktime.model.TimeRegistration;
import eu.vranckaert.worktime.service.TimeRegistrationService;
import eu.vranckaert.worktime.utils.date.DateFormat;
import eu.vranckaert.worktime.utils.date.DateUtils;
import eu.vranckaert.worktime.utils.date.HourPreference12Or24;
import eu.vranckaert.worktime.utils.date.TimeFormat;
import eu.vranckaert.worktime.utils.preferences.Preferences;
import roboguice.activity.GuiceActivity;
import roboguice.inject.InjectExtra;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * User: DIRK VRANCKAERT
 * Date: 28/04/11
 * Time: 15:19
 */
public class EditTimeRegistrationStartTimeActivity extends GuiceActivity {
    private static final String LOG_TAG = EditTimeRegistrationStartTimeActivity.class.getSimpleName();

    @InjectExtra(Constants.Extras.TIME_REGISTRATION)
    private TimeRegistration timeRegistration;

    @InjectExtra(value = Constants.Extras.TIME_REGISTRATION_PREVIOUS)
    @Nullable
    private TimeRegistration previousTimeRegistration;

    @Inject
    private TimeRegistrationService timeRegistrationService;

    private Calendar newStartTime = null;
    private Date lowerLimit = null;
    private Date higherLimit = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setInitialDateAndTime();

        showDialog(Constants.Dialog.CHOOSE_DATE);
    }

    /**
     * Sets the initial start date and time.
     */
    private void setInitialDateAndTime() {
        newStartTime = GregorianCalendar.getInstance();
        newStartTime.setTime(timeRegistration.getStartTime());
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        Dialog dialog = null;

        switch (id) {
            case Constants.Dialog.CHOOSE_DATE: {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        EditTimeRegistrationStartTimeActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            public void onDateSet(DatePicker datePickerView
                                    , int year, int monthOfYear, int dayOfMonth) {
                                newStartTime.set(Calendar.YEAR, year);
                                newStartTime.set(Calendar.MONTH, monthOfYear);
                                newStartTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                showDialog(Constants.Dialog.CHOOSE_TIME);
                            }
                        },
                        newStartTime.get(Calendar.YEAR),
                        newStartTime.get(Calendar.MONTH),
                        newStartTime.get(Calendar.DAY_OF_MONTH)
                );
                datePickerDialog.setTitle(R.string.lbl_registration_edit_pick_time);
                datePickerDialog.setButton2(getString(android.R.string.cancel), new DatePickerDialog.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
                datePickerDialog.setOnCancelListener(new DatePickerDialog.OnCancelListener() {
                    public void onCancel(DialogInterface dialogInterface) {
                        finish();
                    }
                });
                dialog = datePickerDialog;
                break;
            }
            case Constants.Dialog.CHOOSE_TIME: {
                HourPreference12Or24 hourFormatPreference = Preferences.getDisplayHour1224Format(getApplicationContext());
                boolean is24HourClock = hourFormatPreference.equals(HourPreference12Or24.HOURS_24)?true:false;
                Log.d(LOG_TAG, "Using " + (is24HourClock?"24-hour":"12-hour") + " clock");
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        EditTimeRegistrationStartTimeActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                                newStartTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                newStartTime.set(Calendar.MINUTE, minute);
                                validateInput();
                            }
                        },
                        newStartTime.get(Calendar.HOUR_OF_DAY),
                        newStartTime.get(Calendar.MINUTE),
                        is24HourClock
                );
                timePickerDialog.setTitle(R.string.lbl_registration_edit_pick_time);
                timePickerDialog.setButton2(getString(android.R.string.cancel), new DatePickerDialog.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        showDialog(Constants.Dialog.CHOOSE_DATE);
                    }
                });
                timePickerDialog.setOnCancelListener(new DatePickerDialog.OnCancelListener() {
                    public void onCancel(DialogInterface dialogInterface) {
                        showDialog(Constants.Dialog.CHOOSE_DATE);
                    }
                });
                dialog = timePickerDialog;
                break;
            }
            case Constants.Dialog.VALIDATION_DATE_LOWER_LIMIT: {
                String lowerLimitStr =
                        DateUtils.convertDateTimeToString(lowerLimit, DateFormat.MEDIUM,
                                TimeFormat.MEDIUM, getApplicationContext());
                AlertDialog.Builder alertValidationError = new AlertDialog.Builder(this);
				alertValidationError
                           .setTitle(R.string.lbl_registration_edit_validation_error)
						   .setMessage( getString(
                                   R.string.lbl_registration_edit_start_validation_error_date_lower_limit,
                                   lowerLimitStr
                           ))
						   .setCancelable(false)
						   .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                               public void onClick(DialogInterface dialog, int which) {
                                   dialog.cancel();
                                   showDialog(Constants.Dialog.CHOOSE_DATE);
                               }
                           });
				dialog = alertValidationError.create();
                break;
            }
            case Constants.Dialog.VALIDATION_DATE_HIGHER_LIMIT: {
                String higherLimitStr =
                        DateUtils.convertDateTimeToString(higherLimit, DateFormat.MEDIUM,
                                TimeFormat.MEDIUM, getApplicationContext());
                AlertDialog.Builder alertValidationError = new AlertDialog.Builder(this);
				alertValidationError
                           .setTitle(R.string.lbl_registration_edit_validation_error)
                           .setMessage(getString(
                                   R.string.lbl_registration_edit_start_validation_error_date_higher_limit,
                                   higherLimitStr
                           ))
						   .setCancelable(false)
						   .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                               public void onClick(DialogInterface dialog, int which) {
                                   dialog.cancel();
                                   showDialog(Constants.Dialog.CHOOSE_DATE);
                               }
                           });
				dialog = alertValidationError.create();
                break;
            }
        }

        return dialog;
    }

    private void validateInput() {
        if (timeRegistration.getStartTime().getTime() == newStartTime.getTimeInMillis()) {
            finish();
        } else {
            newStartTime.set(Calendar.SECOND, 0);
            newStartTime.set(Calendar.MILLISECOND, 0);
        }

        //Define the limits...
        Log.d(LOG_TAG, "Defining the limits...");
        if (previousTimeRegistration != null) {
            lowerLimit = previousTimeRegistration.getEndTime();
            Log.d(LOG_TAG, "LowerLimit set to " + DateUtils.convertDateTimeToString(lowerLimit, DateFormat.FULL,
                    TimeFormat.MEDIUM, getApplicationContext()));
        }
        if (!timeRegistration.isOngoingTimeRegistration()) {
            higherLimit = timeRegistration.getEndTime();
        } else {
            higherLimit = new Date();
        }
        Log.d(LOG_TAG, "higherLimit set to " + DateUtils.convertDateTimeToString(higherLimit, DateFormat.FULL,
                TimeFormat.MEDIUM, getApplicationContext()));

        //Validation
        if (lowerLimit != null && !newStartTime.getTime().after(lowerLimit)) {
            Log.d(LOG_TAG, "The new start time is not after the lowerLimit!");
            showDialog(Constants.Dialog.VALIDATION_DATE_LOWER_LIMIT);
        } else if (higherLimit != null && !newStartTime.getTime().before(higherLimit)) {
            Log.d(LOG_TAG, "The new start time is not before the higherLimit!");
            showDialog(Constants.Dialog.VALIDATION_DATE_HIGHER_LIMIT);
        } else {
            Log.d(LOG_TAG, "No validation errors...");
            updateTimeRegistration();
        }
    }

    private void updateTimeRegistration() {
        timeRegistration.setStartTime(newStartTime.getTime());
        timeRegistrationService.update(timeRegistration);
        setResult(RESULT_OK);
        finish();
    }
}