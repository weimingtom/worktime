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
package eu.vranckaert.worktime.activities.preferences;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.*;
import android.util.Log;
import android.view.View;
import com.google.inject.Inject;
import eu.vranckaert.worktime.R;
import eu.vranckaert.worktime.activities.backup.BackupActivity;
import eu.vranckaert.worktime.activities.backup.RestoreActivity;
import eu.vranckaert.worktime.constants.Constants;
import eu.vranckaert.worktime.constants.TrackerConstants;
import eu.vranckaert.worktime.service.*;
import eu.vranckaert.worktime.utils.context.IntentUtil;
import eu.vranckaert.worktime.utils.preferences.Preferences;
import eu.vranckaert.worktime.utils.preferences.SeekBarPreference;
import eu.vranckaert.worktime.utils.tracker.AnalyticsTracker;
import roboguice.activity.GuicePreferenceActivity;

/**
 * User: DIRK VRANCKAERT
 * Date: 05/02/11
 * Time: 19:09
 */
public class PreferencesActivity extends GuicePreferenceActivity {
    private static final String LOG_TAG = PreferencesActivity.class.getSimpleName();

    @Inject
    private CommentHistoryService commentHistoryService;

    @Inject
    private WidgetService widgetService;

    private int maxNumberOfCommentsOriginal;

    private AnalyticsTracker tracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        maxNumberOfCommentsOriginal = Preferences.
            getWidgetEndingTimeRegistrationCommentMaxHistoryStoragePreference(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);
        tracker = AnalyticsTracker.getInstance(getApplicationContext());
        tracker.trackPageView(TrackerConstants.PageView.PREFERENCES_ACTIVITY);

        configurePreferences(PreferencesActivity.this);
        createPreferences(PreferencesActivity.this);
    }

    private void configurePreferences(GuicePreferenceActivity ctx) {
        ctx.getPreferenceManager().setSharedPreferencesName(Constants.Preferences.PREFERENCES_NAME);
    }

    private void createPreferences(GuicePreferenceActivity ctx) {
        PreferenceScreen preferences = ctx.getPreferenceManager().createPreferenceScreen(ctx);
        setPreferenceScreen(preferences);

        //Category DATE AND TIME
        PreferenceCategory dateAndTimeCategory = new PreferenceCategory(ctx);
        dateAndTimeCategory.setTitle(R.string.pref_date_and_time_category_title);
        preferences.addPreference(dateAndTimeCategory);
        createDateAndTimeCategoryPreferences(ctx, dateAndTimeCategory);

        //Category TASKS
        PreferenceCategory tasksCategory = new PreferenceCategory(ctx);
        tasksCategory.setTitle(R.string.pref_tasks_category_title);
        preferences.addPreference(tasksCategory);
        createTasksCategoryPreferences(ctx, tasksCategory);

        //Category WIDGET
        PreferenceCategory widgetCategory = new PreferenceCategory(ctx);
        widgetCategory.setTitle(R.string.pref_widget_category_title);
        preferences.addPreference(widgetCategory);
        createWidgetCategoryPreferences(ctx, widgetCategory);

        //Category NOTIFICATIONS
        PreferenceCategory statBarNotifsCategory = new PreferenceCategory(ctx);
        statBarNotifsCategory.setTitle(R.string.pref_stat_bar_notifs_category_title);
        preferences.addPreference(statBarNotifsCategory);
        createStatBarNotifsCategoryPreferences(ctx, statBarNotifsCategory);

        //Category COMMENTS
        PreferenceCategory commentsCategory = new PreferenceCategory(ctx);
        commentsCategory.setTitle(R.string.pref_comments_category_title);
        preferences.addPreference(commentsCategory);
        createCommentsCategoryPreferences(ctx, commentsCategory);

        //Category BACKUP
        PreferenceCategory backupCategory = new PreferenceCategory(ctx);
        backupCategory.setTitle(R.string.pref_backup_category_title);
        preferences.addPreference(backupCategory);
        createBackupCategory(ctx, backupCategory);
    }

    private void createDateAndTimeCategoryPreferences(GuicePreferenceActivity ctx, PreferenceCategory generalCategory) {
        ListPreference displayHour1224Format = new ListPreference(ctx);
        displayHour1224Format.setKey(Constants.Preferences.Keys.DISPLAY_HOUR_12_24_FORMAT);
        displayHour1224Format.setDefaultValue(Constants.Preferences.DISPLAY_HOUR_12_24_FORMAT_DEFAULT_VALUE);
        displayHour1224Format.setTitle(R.string.pref_date_and_time_display_hour_12_24_format_prompt);
        displayHour1224Format.setSummary(R.string.pref_date_and_time_display_hour_12_24_format_summary);
        displayHour1224Format.setEntries(R.array.pref_date_and_time_display_hour_12_24_format_options);
        displayHour1224Format.setEntryValues(R.array.pref_date_and_time_display_hour_12_24_format_option_values);
        generalCategory.addPreference(displayHour1224Format);

        ListPreference weekStartOn = new ListPreference(ctx);
        weekStartOn.setKey(Constants.Preferences.Keys.WEEK_STARTS_ON);
        weekStartOn.setDefaultValue(Constants.Preferences.WEEK_STARTS_ON_DEFAULT_VALUE);
        weekStartOn.setTitle(R.string.pref_date_and_time_week_starts_on_prompt);
        weekStartOn.setSummary(R.string.pref_date_and_time_week_starts_on_summary);
        weekStartOn.setEntries(R.array.pref_date_and_time_week_starts_on_options);
        weekStartOn.setEntryValues(R.array.pref_date_and_time_week_starts_on_option_values);
        generalCategory.addPreference(weekStartOn);
    }

    private void createTasksCategoryPreferences(GuicePreferenceActivity ctx, PreferenceCategory tasksCategory) {
        CheckBoxPreference selectTaskHideFinished = new CheckBoxPreference(ctx);
        selectTaskHideFinished.setDefaultValue(Constants.Preferences.SELECT_TASK_HIDE_FINISHED_DEFAULT_VALUE);
        selectTaskHideFinished.setKey(Constants.Preferences.Keys.SELECT_TASK_HIDE_FINISHED);
        selectTaskHideFinished.setTitle(R.string.pref_tasks_select_task_hide_finished_title);
        selectTaskHideFinished.setSummaryOn(R.string.pref_tasks_select_task_hide_finished_summary_on);
        selectTaskHideFinished.setSummaryOff(R.string.pref_tasks_select_task_hide_finished_summary_off);
        tasksCategory.addPreference(selectTaskHideFinished);

        CheckBoxPreference displayTasksHideFinisehd = new CheckBoxPreference(ctx);
        displayTasksHideFinisehd.setDefaultValue(Constants.Preferences.DISPLAY_TASKS_HIDE_FINISHED_DEFAULT_VALUE);
        displayTasksHideFinisehd.setKey(Constants.Preferences.Keys.DISPLAY_TASKS_HIDE_FINISHED);
        displayTasksHideFinisehd.setTitle(R.string.pref_tasks_display_tasks_hide_finished_title);
        displayTasksHideFinisehd.setSummaryOn(R.string.pref_tasks_display_tasks_hide_finished_summary_on);
        displayTasksHideFinisehd.setSummaryOff(R.string.pref_tasks_display_tasks_hide_finished_summary_off);
        tasksCategory.addPreference(displayTasksHideFinisehd);
    }

    private void createWidgetCategoryPreferences(GuicePreferenceActivity ctx, PreferenceCategory widgetCategory) {
        CheckBoxPreference askForTaskSelectionIfOnlyOne = new CheckBoxPreference(ctx);
        askForTaskSelectionIfOnlyOne.setDefaultValue(Constants.Preferences.WIDGET_ASK_FOR_TASK_SELECTION_IF_ONLY_ONE_DEFAULT_VALUE);
        askForTaskSelectionIfOnlyOne.setKey(Constants.Preferences.Keys.WIDGET_ASK_FOR_TASK_SELECTION_IF_ONLY_ONE);
        askForTaskSelectionIfOnlyOne.setTitle(R.string.pref_widget_ask_for_task_selection_if_only_one_title);
        askForTaskSelectionIfOnlyOne.setSummaryOn(R.string.pref_widget_ask_for_task_selection_if_only_one_summary_on);
        askForTaskSelectionIfOnlyOne.setSummaryOff(R.string.pref_widget_ask_for_task_selection_if_only_one_summary_off);
        widgetCategory.addPreference(askForTaskSelectionIfOnlyOne);

        CheckBoxPreference askForComment = new CheckBoxPreference(ctx);
        askForComment.setDefaultValue(Constants.Preferences.WIDGET_ENDING_TIME_REGISTRATION_COMMENT_PREFERENCE_DEFAULT_VALUE);
        askForComment.setKey(Constants.Preferences.Keys.WIDGET_ENDING_TIME_REGISTRATION_COMMENT_PREFERENCE);
        askForComment.setTitle(R.string.pref_widget_ask_for_comment_title);
        askForComment.setSummaryOn(R.string.pref_widget_ask_for_comment_summary_on);
        askForComment.setSummaryOff(R.string.pref_widget_ask_for_comment_summary_off);
        widgetCategory.addPreference(askForComment);

        CheckBoxPreference askForFinishingTask = new CheckBoxPreference(ctx);
        askForFinishingTask.setDefaultValue(Constants.Preferences.WIDGET_ENDING_TIME_REGISTRATION_FINISH_TASK_PREFERENCE_DEFAULT_VALUE);
        askForFinishingTask.setKey(Constants.Preferences.Keys.WIDGET_ENDING_TIME_REGISTRATION_FINISH_TASK_PREFERENCE);
        askForFinishingTask.setTitle(R.string.pref_widget_ask_for_finishing_task_title);
        askForFinishingTask.setSummaryOn(R.string.pref_widget_ask_for_finishing_task_summary_on);
        askForFinishingTask.setSummaryOff(R.string.pref_widget_ask_for_finishing_task_summary_off);
        widgetCategory.addPreference(askForFinishingTask);
    }

    private void createStatBarNotifsCategoryPreferences(GuicePreferenceActivity ctx, PreferenceCategory widgetCategory) {
        final CheckBoxPreference showStatusBarNotifications = new CheckBoxPreference(ctx);
        showStatusBarNotifications.setDefaultValue(Constants.Preferences.SHOW_STATUS_BAR_NOTIFICATIONS_PREFERENCE_DEFAULT_VALUE);
        showStatusBarNotifications.setKey(Constants.Preferences.Keys.SHOW_STATUS_BAR_NOTIFICATIONS_PREFERENCE);
        showStatusBarNotifications.setTitle(R.string.pref_stat_bar_notifs_ask_for_comment_title);
        showStatusBarNotifications.setSummaryOn(R.string.pref_stat_bar_notifs_ask_for_comment_summary_on);
        showStatusBarNotifications.setSummaryOff(R.string.pref_stat_bar_notifs_ask_for_comment_summary_off);

        showStatusBarNotifications.setOnPreferenceChangeListener(new CheckBoxPreference.OnPreferenceChangeListener() {
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean result = (Boolean) newValue;

                Log.d(LOG_TAG, "The newly selected value for 'Show status bar notification' is " + result);
                Preferences.setShowStatusBarNotificationsPreference(getApplicationContext(), result);
                Log.d(LOG_TAG, "Show status bar notifications checkbox to be updated");
                showStatusBarNotifications.setChecked(result);
                Log.d(LOG_TAG, "Delegate the change of the notifications to the widget service");
                widgetService.updateWidget(getApplicationContext());

                return false;
            }
        });

        widgetCategory.addPreference(showStatusBarNotifications);
    }

    private void createCommentsCategoryPreferences(GuicePreferenceActivity ctx, PreferenceCategory commentsCategory) {
        SeekBarPreference maxCommentsInMemory = new SeekBarPreference(ctx);
        maxCommentsInMemory.setMaxValue(10);
        maxCommentsInMemory.setIncrement(1);
        maxCommentsInMemory.setKey(Constants.Preferences.Keys.WIDGET_ENDING_TIME_REGISTRATION_COMMENT_MAX_HISTORY_STORAGE_PREFERENCE);
        maxCommentsInMemory.setDefaultValue(Constants.Preferences.WIDGET_ENDING_TIME_REGISTRATION_COMMENT_MAX_HISTORY_STORAGE_PREFERENCE_DEFAULT_VALUE);
        maxCommentsInMemory.setTitle(R.string.pref_comments_max_history_storage_title);
        maxCommentsInMemory.setSummary(R.string.pref_comments_max_history_storage_summary);
        commentsCategory.addPreference(maxCommentsInMemory);

        Preference clearCommentsPreference = new Preference(ctx);
        clearCommentsPreference.setTitle(R.string.pref_comments_clear_history_storage_title);
        clearCommentsPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                commentHistoryService.deleteAll();
                return true;
            }
        });
        commentsCategory.addPreference(clearCommentsPreference);
    }

    private void createBackupCategory(GuicePreferenceActivity ctx, PreferenceCategory backupCategory) {
        Preference backupButton = new Preference(ctx);
        backupButton.setTitle(R.string.pref_backup_title);
        backupButton.setSummary(R.string.pref_backup_summary);
        backupCategory.addPreference(backupButton);
        backupButton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                Intent backupIntent = new Intent(getApplicationContext(), BackupActivity.class);
                startActivity(backupIntent);
                return true;
            }
        });

        Preference restoreButton = new Preference(ctx);
        restoreButton.setTitle(R.string.pref_restore_title);
        restoreButton.setSummary(R.string.pref_restore_summary);
        backupCategory.addPreference(restoreButton);
        restoreButton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                Intent restoreIntent = new Intent(getApplicationContext(), RestoreActivity.class);
                startActivity(restoreIntent);
                return true;
            }
        });

        Preference backupRestoreDocButton = new Preference(ctx);
        backupRestoreDocButton.setTitle(R.string.pref_backup_restore_doc_title);
        backupRestoreDocButton.setSummary(R.string.pref_backup_restore_doc_summary);
        backupCategory.addPreference(backupRestoreDocButton);
        backupRestoreDocButton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                showDialog(Constants.Dialog.BACKUP_RESTORE_DOCUMENTATION);
                return true;
            }
        });
    }

    protected Dialog onCreateDialog(int id) {
        Dialog dialog = null;

        switch (id) {
            case Constants.Dialog.BACKUP_RESTORE_DOCUMENTATION: {
                AlertDialog.Builder alertBackupSuccess = new AlertDialog.Builder(this);
				alertBackupSuccess
                           .setTitle(R.string.lbl_backup_restore_documentation_title)
						   .setMessage(R.string.msg_backup_restore_documentation_content)
						   .setCancelable(true)
						   .setPositiveButton(R.string.close, new DialogInterface.OnClickListener() {
                               public void onClick(DialogInterface dialog, int which) {
                                   dialog.cancel();
                               }
                           });
				dialog = alertBackupSuccess.create();
                break;
            }
        }

        return dialog;
    }

    public void onHomeClick(View view) {
        IntentUtil.goHome(this);
    }

    @Override
    public void finish() {
        int maxNumberOfCommentsInTheEnd = Preferences.
            getWidgetEndingTimeRegistrationCommentMaxHistoryStoragePreference(getApplicationContext());
        if (maxNumberOfCommentsOriginal > maxNumberOfCommentsInTheEnd) {
            Log.d(LOG_TAG, "Number of comments in history has changed, verifying the database state...");
            commentHistoryService.checkNumberOfCommentsStored();
        }
        super.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        tracker.stopSession();
    }
}
