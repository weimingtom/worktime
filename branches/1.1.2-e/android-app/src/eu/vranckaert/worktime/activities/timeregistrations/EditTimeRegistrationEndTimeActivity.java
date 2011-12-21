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

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
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
 * Time: 16:38
 */
public class EditTimeRegistrationEndTimeActivity extends GuiceActivity {
    private static final String LOG_TAG = EditTimeRegistrationEndTimeActivity.class.getSimpleName();

    @InjectExtra(Constants.Extras.TIME_REGISTRATION)
    private TimeRegistration timeRegistration;

    @InjectExtra(value = Constants.Extras.TIME_REGISTRATION_NEXT)
    @Nullable
    private TimeRegistration nextTimeRegistration;

    @Inject
    private TimeRegistrationService timeRegistrationService;

    private Calendar newEndTime = null;
    private Calendar lowerLimit = null;
    private Calendar higherLimit = null;

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
        newEndTime = GregorianCalendar.getInstance();
        newEndTime.setTime(timeRegistration.getEndTime());
        newEndTime.set(Calendar.SECOND, 0);
        newEndTime.set(Calendar.MILLISECOND, 0);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        Dialog dialog = null;

        switch (id) {
            case Constants.Dialog.CHOOSE_DATE: {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        EditTimeRegistrationEndTimeActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            public void onDateSet(DatePicker datePickerView
                                    , int year, int monthOfYear, int dayOfMonth) {
                                newEndTime.set(Calendar.YEAR, year);
                                newEndTime.set(Calendar.MONTH, monthOfYear);
                                newEndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                showDialog(Constants.Dialog.CHOOSE_TIME);
                            }
                        },
                        newEndTime.get(Calendar.YEAR),
                        newEndTime.get(Calendar.MONTH),
                        newEndTime.get(Calendar.DAY_OF_MONTH)
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
                        EditTimeRegistrationEndTimeActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                                newEndTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                newEndTime.set(Calendar.MINUTE, minute);
                                validateInput();
                            }
                        },
                        newEndTime.get(Calendar.HOUR_OF_DAY),
                        newEndTime.get(Calendar.MINUTE),
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
                        DateUtils.DateTimeConverter.convertDateTimeToString(lowerLimit.getTime(), DateFormat.MEDIUM,
                                TimeFormat.SHORT, getApplicationContext());
                AlertDialog.Builder alertValidationError = new AlertDialog.Builder(this);
				alertValidationError
                           .setTitle(R.string.lbl_registration_edit_validation_error)
						   .setMessage( getString(
                                   R.string.lbl_registration_edit_validation_error_greater_than,
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
                        DateUtils.DateTimeConverter.convertDateTimeToString(higherLimit.getTime(), DateFormat.MEDIUM,
                                TimeFormat.SHORT, getApplicationContext());
                AlertDialog.Builder alertValidationError = new AlertDialog.Builder(this);
				alertValidationError
                           .setTitle(R.string.lbl_registration_edit_validation_error)
						   .setMessage( getString(
                                   R.string.lbl_registration_edit_validation_error_less_than_equal_to,
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
        if (timeRegistration.getEndTime().getTime() == newEndTime.getTimeInMillis()) {
            finish();
        } else {
            newEndTime.set(Calendar.SECOND, 0);
            newEndTime.set(Calendar.MILLISECOND, 0);
        }

        //Define the limits...
        Log.d(LOG_TAG, "Defining the limits...");
        //Lower Limit
        lowerLimit = Calendar.getInstance();
        lowerLimit.setTime(timeRegistration.getStartTime());
        Log.d(LOG_TAG, "LowerLimit set to " + DateUtils.DateTimeConverter.convertDateTimeToString(lowerLimit.getTime(), DateFormat.FULL,
                TimeFormat.SHORT, getApplicationContext()));
        //Higher Limit
        higherLimit = Calendar.getInstance();
        if (nextTimeRegistration != null) {
            higherLimit.setTime(nextTimeRegistration.getStartTime());
        } else {
            higherLimit.setTime(new Date());
        }
        Log.d(LOG_TAG, "higherLimit set to " + DateUtils.DateTimeConverter.convertDateTimeToString(higherLimit.getTime(), DateFormat.FULL,
                TimeFormat.SHORT, getApplicationContext()));

        //Validation
        /*
         * Fix for issue 61
         * Checks newStartTime >= lowerLimit
         */
        boolean validOnLowerLimit = validateAgainstLowerLimit(newEndTime, lowerLimit);
        boolean validOnHigherLimit = validateAgainstHigherLimit(newEndTime, higherLimit);

        if (!validOnLowerLimit) {
            Log.d(LOG_TAG, "The new start time is not greater than or equal to the lowerLimit!");
            showDialog(Constants.Dialog.VALIDATION_DATE_LOWER_LIMIT);
        } else if (!validOnHigherLimit) {
            Log.d(LOG_TAG, "The new start time is not lower than or equals to the higherLimit!");
            showDialog(Constants.Dialog.VALIDATION_DATE_HIGHER_LIMIT);
        } else {
            Log.d(LOG_TAG, "No validation errors...");
            updateTimeRegistration();
        }

//        if (!newEndTime.getTime().after(lowerLimit)) {
//            Log.d(LOG_TAG, "The new start time is not after the lowerLimit!");
//            showDialog(Constants.Dialog.VALIDATION_DATE_LOWER_LIMIT);
//        } else if (!newEndTime.getTime().before(higherLimit)) {
//            Log.d(LOG_TAG, "The new start time is not before the higherLimit!");
//            showDialog(Constants.Dialog.VALIDATION_DATE_HIGHER_LIMIT);
//        } else {
//            Log.d(LOG_TAG, "No validation errors...");
//            updateTimeRegistration();
//        }
    }

    /**
     * Validate a certain time against a certain limit. The validation formula is: time > limit.
     * @param time The time to be validated.
     * @param limit The limit to which the time should be validated. This is an optional parameter. If null the
     * validation will always succeed.
     * @return {@link Boolean#TRUE} if valid against the validation formula, {@link Boolean#FALSE} if not.
     */
    private boolean validateAgainstLowerLimit(Calendar time, Calendar limit) {
        Log.d(LOG_TAG, "About to start validating time > limit");

        if (limit == null) {
            //No limit is defined so the time can be anything!
            Log.d(LOG_TAG, "No limitations defined so validation is ok!");
            return true;
        }

        if(time.getTime().after(limit.getTime())) {
            Log.d(LOG_TAG, "The new time is greater than the limit, validation ok!");
            return true;
        }

        Log.d(LOG_TAG, "Validation failed! The new time is not greater than the limit!");

        return false;
    }

    /**
     * Validate a certain time against a certain limit. The validation formula is: time <= limit.
     * @param time The time to be validated.
     * @param limit The limit to which the time should be validated. This is an optional parameter. If null the
     * validation will always succeed.
     * @return {@link Boolean#TRUE} if valid against the validation formula, {@link Boolean#FALSE} if not.
     */
    private boolean validateAgainstHigherLimit(Calendar time, Calendar limit) {
        Log.d(LOG_TAG, "About to start validating time <= limit");

        if (limit == null) {
            //No limit is defined so the time can be anything!
            Log.d(LOG_TAG, "No limitations defined so validation is ok!");
            return true;
        }

        Long timeMilis = time.getTimeInMillis();
        Long limitMilis = limit.getTimeInMillis();

        //First check if the time is after the limit, if so everything is ok!
        //=> checks the greater than part
        if (time.getTime().before(limit.getTime())) {
            Log.d(LOG_TAG, "The new time is less than or equal to the limit, validation ok!");
            return true;
        }

        //Check if the time and the limit are on the same day and in the same minute... If so it's ok and we set the
        //the new time to the same seconds and milliseconds as the limit.
        //=> checks the equals part
        Calendar timeSameMinuteCheck = Calendar.getInstance();
        timeSameMinuteCheck.setTimeInMillis(timeMilis);
        timeSameMinuteCheck.set(Calendar.MILLISECOND, 0);
        timeSameMinuteCheck.set(Calendar.SECOND, 0);
        Calendar limitSameMinuteCheck = Calendar.getInstance();
        limitSameMinuteCheck.setTimeInMillis(limitMilis);
        limitSameMinuteCheck.set(Calendar.MILLISECOND, 0);
        limitSameMinuteCheck.set(Calendar.SECOND, 0);
        if (timeSameMinuteCheck.getTimeInMillis() == limitSameMinuteCheck.getTimeInMillis()) {
            Log.d(LOG_TAG, "The new time is equal to the limit, validation ok!");
            Log.d(LOG_TAG, "New time is updated with the seconds and milliseconds of the limit!");
            time.set(Calendar.MILLISECOND, limit.get(Calendar.MILLISECOND));
            time.set(Calendar.SECOND, limit.get(Calendar.SECOND));
            return true;
        }

        Log.d(LOG_TAG, "Validation failed! The new time is not less than or equal to the limit!");

        return false;
    }

    private void updateTimeRegistration() {
        timeRegistration.setEndTime(newEndTime.getTime());
        timeRegistrationService.update(timeRegistration);
        setResult(RESULT_OK);
        finish();
    }
}