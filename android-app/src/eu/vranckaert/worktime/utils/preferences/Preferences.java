package eu.vranckaert.worktime.utils.preferences;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import eu.vranckaert.worktime.constants.Constants;
import eu.vranckaert.worktime.enums.export.CsvSeparator;
import eu.vranckaert.worktime.enums.export.FileType;
import eu.vranckaert.worktime.utils.date.DateUtils;
import eu.vranckaert.worktime.utils.date.HourPreference12Or24;

/**
 * Access all the preferences. This class is mainly used to read the preferences but can be used in some rare cases
 * to also update or insert preferences.
 *
 * User: DIRK VRANCKAERT
 * Date: 19/02/11
 * Time: 14:22
 */
public class Preferences {
    /**
     * Get an instance of {@link SharedPreferences} to access the preferences.
     * @param ctx The context when accessing the preferences.
     * @return The instance based on the context.
     */
    private static final SharedPreferences getSharedPreferences(Context ctx) {
        return ctx.getSharedPreferences(Constants.Preferences.PREFERENCES_NAME, Activity.MODE_PRIVATE);
    }

    /**
     * Get the preference for key {@link Constants.Preferences.Keys#EXPORT_TIME_REG_FILE_NAME}.
     * @param ctx The context when accessing the preference.
     * @return The {@link String} value for the key.
     */
    public static String getTimeRegistrationExportFileName(Context ctx) {
        SharedPreferences preferences = getSharedPreferences(ctx);
        return preferences.getString(
                Constants.Preferences.Keys.EXPORT_TIME_REG_FILE_NAME,
                Constants.Preferences.EXPORT_TIME_REG_FILE_NAME_DEFAULT_VALUE
        );
    }

    /**
     * Updates the preference {@link Constants.Preferences.Keys#EXPORT_TIME_REG_FILE_NAME}.
     * @param ctx The context when updating the preference.
     * @param fileName The {@link String} to store in the preferences.
     */
    public static void setTimeRegistrationExportFileName(Context ctx, String fileName) {
        if(fileName == null) {
            return;
        }
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(Constants.Preferences.Keys.EXPORT_TIME_REG_FILE_NAME, fileName);
        editor.commit();
    }

    /**
     * Get the preference for key {@link Constants.Preferences.Keys#EXPROT_TIME_REG_FILE_TYPE}.
     * @param ctx The context when accessing the preference.
     * @return The {@link FileType} represented by the value in the preferences. If the value in the preferences
     * could not be matched on any instance of the enum it will return null.
     */
    public static FileType getTimeRegistrationExportFileType(Context ctx) {
        String extension = getSharedPreferences(ctx).getString(
                Constants.Preferences.Keys.EXPROT_TIME_REG_FILE_TYPE,
                FileType.COMMA_SERPERATED_VALUES.getExtension()
        );
        return FileType.matchFileType(extension);
    }

    /**
     * Updates the preference {@link Constants.Preferences.Keys#EXPROT_TIME_REG_FILE_TYPE}.
     * @param ctx The context when updating the preference.
     * @param fileType The {@link FileType} to store in the preferences.
     */
    public static void setTimeRegistrationExportFileType(Context ctx, FileType fileType) {
        if(fileType == null) {
            return;
        }
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(Constants.Preferences.Keys.EXPROT_TIME_REG_FILE_TYPE, fileType.getExtension());
        editor.commit();
    }

    /**
     * Get the preference for key {@link Constants.Preferences.Keys#EXPROT_TIME_REG_CSV_SEPARATOR}.
     * @param ctx The context when accessing the preference.
     * @return The {@link CsvSeparator} represented by the value in the preferences. If the value in the preferences
     * could not be matched on any instance of the enum it will return null.
     */
    public static CsvSeparator getTrimeRegistrationCsvSeparator(Context ctx) {
        String seperator = getSharedPreferences(ctx).getString(
                Constants.Preferences.Keys.EXPROT_TIME_REG_CSV_SEPARATOR,
                String.valueOf(CsvSeparator.SEMICOLON.getSeperator())
        );
        return CsvSeparator.matchFileType(seperator);
    }

    /**
     * Updates the preference {@link Constants.Preferences.Keys#EXPROT_TIME_REG_CSV_SEPARATOR}.
     * @param ctx The context when updating the preference.
     * @param separator The {@link CsvSeparator} to store in the preferences.
     */
    public static void setTrimeRegistrationCsvSeparator(Context ctx, CsvSeparator separator) {
        if(separator == null) {
            return;
        }
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(Constants.Preferences.Keys.EXPROT_TIME_REG_CSV_SEPARATOR, String.valueOf(separator.getSeperator()));
        editor.commit();
    }

    /**
     * Get the preference for key {@link Constants.Preferences.Keys#SELECTED_PROJECT_ID}. If no selected project id is
     * found the default value will be {@link Constants.Preferences#SELECTED_PROJECT_ID_DEFAULT_VALUE}.
     * @param ctx The context when getting the selected project id.
     * @return The unique identifier of a {@link eu.vranckaert.worktime.model.Project} if one is found. Otherwise the
     * default value {@link Constants.Preferences#SELECTED_PROJECT_ID_DEFAULT_VALUE}.
     */
    public static int getSelectedProjectId(Context ctx) {
        return getSharedPreferences(ctx).getInt(
                Constants.Preferences.Keys.SELECTED_PROJECT_ID,
                Constants.Preferences.SELECTED_PROJECT_ID_DEFAULT_VALUE
        );
    }

    /**
     * Updates the preference {@link Constants.Preferences.Keys#SELECTED_PROJECT_ID}.
     * @param ctx The context when updating the preference.
     * @param projectId The projectId to put in the preferences.
     * @param projectId The projectId to put in the preferences.
     */
    public static void setSelectedProjectId(Context ctx, int projectId) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putInt(Constants.Preferences.Keys.SELECTED_PROJECT_ID, projectId);
        editor.commit();
    }

    /**
     * Get the preference for key {@link Constants.Preferences.Keys#WIDGET_ASK_FOR_TASK_SELECTION_IF_ONLY_ONE}.
     * If no value is found for the preference the default value will be
     * {@link Constants.Preferences#WIDGET_ASK_FOR_TASK_SELECTION_IF_ONLY_ONE_DEFAULT_VALUE}.
     * @param ctx The context when getting the preference for asking a comment when ending a time registration.
     * @return The {@link Boolean} which represents weather should be asked to select a task or not if only one task for
     * a project is available.
     */
    public static boolean getWidgetAskForTaskSelectionIfOnlyOnePreference(Context ctx) {
        return getSharedPreferences(ctx).getBoolean(
                Constants.Preferences.Keys.WIDGET_ASK_FOR_TASK_SELECTION_IF_ONLY_ONE,
                Constants.Preferences.WIDGET_ASK_FOR_TASK_SELECTION_IF_ONLY_ONE_DEFAULT_VALUE
        );
    }

    /**
     * Updates the preference {@link Constants.Preferences.Keys#WIDGET_ASK_FOR_TASK_SELECTION_IF_ONLY_ONE}.
     * @param ctx The context when updating the preference.
     * @param askComment Weather should be asked to select a task or not if only one task for a project is available.
     */
    public static void setWidgetAskForTaskSelectionIfOnlyOnePreference(Context ctx, boolean askComment) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putBoolean(Constants.Preferences.Keys.WIDGET_ASK_FOR_TASK_SELECTION_IF_ONLY_ONE, askComment);
        editor.commit();
    }

    /**
     * Get the preference for key {@link Constants.Preferences.Keys#WIDGET_ENDING_TIME_REGISTRATION_COMMENT_PREFERENCE}.
     * If no value is found for the preference the default value will be
     * {@link Constants.Preferences#WIDGET_ENDING_TIME_REGISTRATION_COMMENT_PREFERENCE_DEFAULT_VALUE}.
     * @param ctx The context when getting the preference for asking a comment when ending a time registration.
     * @return The {@link Boolean} which represents weather should be asked for a comment on ending a time registration
     * or not.
     */
    public static boolean getWidgetEndingTimeRegistrationCommentPreference(Context ctx) {
        return getSharedPreferences(ctx).getBoolean(
                Constants.Preferences.Keys.WIDGET_ENDING_TIME_REGISTRATION_COMMENT_PREFERENCE,
                Constants.Preferences.WIDGET_ENDING_TIME_REGISTRATION_COMMENT_PREFERENCE_DEFAULT_VALUE
        );
    }

    /**
     * Updates the preference {@link Constants.Preferences.Keys#WIDGET_ENDING_TIME_REGISTRATION_COMMENT_PREFERENCE}.
     * @param ctx The context when updating the preference.
     * @param askComment Weather or not to ask for a preference when ending a time registration.
     */
    public static void setWidgetEndingTimeRegistrationCommentPreference(Context ctx, boolean askComment) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putBoolean(Constants.Preferences.Keys.WIDGET_ENDING_TIME_REGISTRATION_COMMENT_PREFERENCE, askComment);
        editor.commit();
    }

    /**
     * Get the preference for key
     * {@link Constants.Preferences.Keys#WIDGET_ENDING_TIME_REGISTRATION_FINISH_TASK_PREFERENCE}. If no value is found
     * for the preference the default value will be
     * {@link Constants.Preferences#WIDGET_ENDING_TIME_REGISTRATION_FINISH_TASK_PREFERENCE_DEFAULT_VALUE}.
     * @param ctx The context when getting the preference for asking to mark the task finished when ending a
     * time registration.
     * @return The {@link Boolean} which represents weather should be asked for marking the task finished on ending a
     * time registration or not.
     */
    public static boolean getWidgetEndingTimeRegistrationFinishTaskPreference(Context ctx) {
        return getSharedPreferences(ctx).getBoolean(
                Constants.Preferences.Keys.WIDGET_ENDING_TIME_REGISTRATION_FINISH_TASK_PREFERENCE,
                Constants.Preferences.WIDGET_ENDING_TIME_REGISTRATION_FINISH_TASK_PREFERENCE_DEFAULT_VALUE
        );
    }

    /**
     * Updates the preference {@link Constants.Preferences.Keys#WIDGET_ENDING_TIME_REGISTRATION_FINISH_TASK_PREFERENCE}.
     * @param ctx The context when updating the preference.
     * @param finishTask Weather or not to ask for marking the task as finished when ending the time registration.
     */
    public static void setWidgetEndingTimeRegistrationFinishTaskPreference(Context ctx, boolean finishTask) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putBoolean(Constants.Preferences.Keys.WIDGET_ENDING_TIME_REGISTRATION_FINISH_TASK_PREFERENCE, finishTask);
        editor.commit();
    }

    /**
     * Get the preference for key
     * {@link Constants.Preferences.Keys#WIDGET_ENDING_TIME_REGISTRATION_COMMENT_MAX_HISTORY_STORAGE_PREFERENCE}. If no
     * value is found for the preference the default value will be
     * {@link Constants.Preferences#WIDGET_ENDING_TIME_REGISTRATION_COMMENT_MAX_HISTORY_STORAGE_PREFERENCE_DEFAULT_VALUE}.
     * @param ctx The context when getting the preference for asking a comment when ending a time registration.
     * @return The {@link int} which represents the number of comments to be stored.
     */
    public static int getWidgetEndingTimeRegistrationCommentMaxHistoryStoragePreference(Context ctx) {
        return getSharedPreferences(ctx).getInt(
                Constants.Preferences.Keys.WIDGET_ENDING_TIME_REGISTRATION_COMMENT_MAX_HISTORY_STORAGE_PREFERENCE,
                Constants.Preferences.WIDGET_ENDING_TIME_REGISTRATION_COMMENT_MAX_HISTORY_STORAGE_PREFERENCE_DEFAULT_VALUE
        );
    }

    /**
     * Updates the preference
     * {@link Constants.Preferences.Keys#WIDGET_ENDING_TIME_REGISTRATION_COMMENT_MAX_HISTORY_STORAGE_PREFERENCE}.
     * @param ctx The context when updating the preference.
     * @param number The number of comments to store.
     */
    public static void setWidgetEndingTimeRegistrationCommentMaxHistoryStoragePreference(Context ctx, int number) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putInt(Constants.Preferences.Keys.WIDGET_ENDING_TIME_REGISTRATION_COMMENT_MAX_HISTORY_STORAGE_PREFERENCE, number);
        editor.commit();
    }

    /**
     * Get the preference for key {@link Constants.Preferences.Keys#SHOW_STATUS_BAR_NOTIFICATIONS_PREFERENCE}. If no
     * value is found for the preference the default value will be
     * {@link Constants.Preferences#SHOW_STATUS_BAR_NOTIFICATIONS_PREFERENCE_DEFAULT_VALUE}.
     * @param ctx The context when getting the preference for showing status bar notifications.
     * @return The {@link boolean} which represents the users' choice to show or hide status bar notifications.
     */
    public static boolean getShowStatusBarNotificationsPreference(Context ctx) {
        return getSharedPreferences(ctx).getBoolean(
                Constants.Preferences.Keys.SHOW_STATUS_BAR_NOTIFICATIONS_PREFERENCE,
                Constants.Preferences.SHOW_STATUS_BAR_NOTIFICATIONS_PREFERENCE_DEFAULT_VALUE
        );
    }

    /**
     * Updates the preference {@link Constants.Preferences.Keys#SHOW_STATUS_BAR_NOTIFICATIONS_PREFERENCE}.
     * @param ctx The context when updating the preference.
     * @param showNotif The {@link boolean} which represents the users' choice to show or hide status bar notifications.
     */
    public static void setShowStatusBarNotificationsPreference(Context ctx, boolean showNotif) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putBoolean(Constants.Preferences.Keys.SHOW_STATUS_BAR_NOTIFICATIONS_PREFERENCE, showNotif);
        editor.commit();
    }

    /**
     * Get the preference for key {@link Constants.Preferences.Keys#DISPLAY_HOUR_12_24_FORMAT}. If no value is found for
     * the preference the default value will be {@link Constants.Preferences#DISPLAY_HOUR_12_24_FORMAT_DEFAULT_VALUE}.
     * @param ctx The context when getting the preference for asking a comment when ending a time registration.
     * @return The {@link HourPreference12Or24} value or null.
     */
    public static HourPreference12Or24 getDisplayHour1224Format(Context ctx) {
        String value = getSharedPreferences(ctx).getString(
                Constants.Preferences.Keys.DISPLAY_HOUR_12_24_FORMAT,
                Constants.Preferences.DISPLAY_HOUR_12_24_FORMAT_DEFAULT_VALUE
        );
        HourPreference12Or24 preference = HourPreference12Or24.findHourPreference12Or24(value);

        if (preference == null) {
            if (DateUtils.is24HourClock(ctx)) {
                preference = HourPreference12Or24.HOURS_24;
            } else {
                preference = HourPreference12Or24.HOURS_12;
            }
        }
        return preference;
    }

    /**
     * Updates the preference {@link Constants.Preferences.Keys#DISPLAY_HOUR_12_24_FORMAT}.
     * @param ctx The context when updating the preference.
     * @param preference An {@link HourPreference12Or24} value or null.
     */
    public static void setDisplayHour1224Format(Context ctx, HourPreference12Or24 preference) {
        String value = "system-default";
        if (preference != null) {
            value = preference.getValue();
        }
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(Constants.Preferences.Keys.DISPLAY_HOUR_12_24_FORMAT, value);
        editor.commit();
    }

    /**
     * Get the preference for key {@link Constants.Preferences.Keys#SELECT_TASK_HIDE_FINISHED}. If no
     * value is found for the preference the default value will be
     * {@link Constants.Preferences#SELECT_TASK_HIDE_FINISHED_DEFAULT_VALUE}.
     * @param ctx The context when getting the preference for hiding or showing finished tasks.
     * @return The {@link boolean} which represents the users' choice to show or hide finished tasks.
     */
    public static boolean getSelectTaskHideFinished(Context ctx) {
        return getSharedPreferences(ctx).getBoolean(
                Constants.Preferences.Keys.SELECT_TASK_HIDE_FINISHED,
                Constants.Preferences.SELECT_TASK_HIDE_FINISHED_DEFAULT_VALUE
        );
    }

    /**
     * Updates the preference {@link Constants.Preferences.Keys#SELECT_TASK_HIDE_FINISHED}.
     * @param ctx The context when updating the preference.
     * @param hideFinished The {@link boolean} which represents the users' choice to show or hide finished tasks.
     */
    public static void setSelectTaskHideFinished(Context ctx, boolean hideFinished) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putBoolean(Constants.Preferences.Keys.SELECT_TASK_HIDE_FINISHED, hideFinished);
        editor.commit();
    }

    /**
     * Get the preference for key {@link Constants.Preferences.Keys#DISPLAY_TASKS_HIDE_FINISHED}. If no
     * value is found for the preference the default value will be
     * {@link Constants.Preferences#DISPLAY_TASKS_HIDE_FINISHED_DEFAULT_VALUE}.
     * @param ctx The context when getting the preference for hiding or showing finished tasks.
     * @return The {@link boolean} which represents the users' choice to show or hide finished tasks.
     */
    public static boolean getDisplayTasksHideFinished(Context ctx) {
        return getSharedPreferences(ctx).getBoolean(
                Constants.Preferences.Keys.DISPLAY_TASKS_HIDE_FINISHED,
                Constants.Preferences.DISPLAY_TASKS_HIDE_FINISHED_DEFAULT_VALUE
        );
    }

    /**
     * Updates the preference {@link Constants.Preferences.Keys#DISPLAY_TASKS_HIDE_FINISHED}.
     * @param ctx The context when updating the preference.
     * @param hideFinished The {@link boolean} which represents the users' choice to show or hide finished tasks.
     */
    public static void setDisplayTasksHideFinished(Context ctx, boolean hideFinished) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putBoolean(Constants.Preferences.Keys.DISPLAY_TASKS_HIDE_FINISHED, hideFinished);
        editor.commit();
    }

    /**
     * Get the preference for key {@link Constants.Preferences.Keys#WEEK_STARTS_ON}. If no value is found for
     * the preference the default value will be {@link Constants.Preferences#WEEK_STARTS_ON_DEFAULT_VALUE}.
     * @param ctx The context when getting the preference for the day the week starts on.
     * @return The {@link Integer} value.
     */
    public static Integer getWeekStartsOn(Context ctx) {
        String value = getSharedPreferences(ctx).getString(
                Constants.Preferences.Keys.WEEK_STARTS_ON,
                Constants.Preferences.WEEK_STARTS_ON_DEFAULT_VALUE
        );
        try {
            return Integer.parseInt(value);
        } catch(Exception e) {
            return null;
        }
    }

    /**
     * Updates the preference {@link Constants.Preferences.Keys#WEEK_STARTS_ON}.
     * @param ctx The context when updating the preference.
     * @param weekStartsOn An {@link Integer} value.
     */
    public static void setWeekStartsOn(Context ctx, Integer weekStartsOn) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(Constants.Preferences.Keys.WEEK_STARTS_ON, weekStartsOn.toString());
        editor.commit();
    }
}
