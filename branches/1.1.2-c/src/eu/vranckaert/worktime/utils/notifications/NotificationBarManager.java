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
package eu.vranckaert.worktime.utils.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import eu.vranckaert.worktime.R;
import eu.vranckaert.worktime.activities.notifcationbar.EndTimeRegistration;
import eu.vranckaert.worktime.utils.preferences.Preferences;

/**
 * User: DIRK VRANCKAERT
 * Date: 27/04/11
 * Time: 14:30
 */
public class NotificationBarManager {
    private static final String LOG_TAG = NotificationBarManager.class.getName();

    private Context ctx;

    /**
     * The default constructor which should not be used externally!
     */
    private NotificationBarManager() {}

    /**
     * The method to retrieve an instance of the {@link NotificationBarManager}.
     * @return A new instance of the {@link NotificationBarManager}.
     */
    public static NotificationBarManager getInstance(Context ctx) {
        Log.d(LOG_TAG, "Creating and returning a NotificationBarManager instance");

        NotificationBarManager notificationBarManager = new NotificationBarManager();
        notificationBarManager.ctx = ctx;
        return notificationBarManager;
    }

    /**
     * Get an instance of the Android {@link NotificationManager}.
     * @return An instance of the {@link NotificationManager}.
     */
    private NotificationManager getNotificationManager() {
        Log.d(LOG_TAG, "Creating a NoticationManager instance");

        String ns = Context.NOTIFICATION_SERVICE;
        return (NotificationManager) ctx.getSystemService(ns);
    }

    /**
     * This class specifies some constants which represent each message id.
     */
    public class NotificationIds {
        public static final int ONGOING_TIME_REGISTRATION_MESSAGE = 1;
    }

    /**
     * Removes all messages from the notification bar.
     */
    public void removeAllMessages() {
        Log.d(LOG_TAG, "Remove all messages");

        NotificationManager notificationManager = getNotificationManager();
        notificationManager.cancelAll();
    }

    /**
     * Remove a message form the notification bar.
     * @param id The id of the message to be removed from the notification bar. The id should be found in
     * {@link NotificationBarManager.NotificationIds};
     */
    public void removeMessage(int id) {
        Log.d(LOG_TAG, "Remove status bar notification messages with ID: " + id);

        NotificationManager notificationManager = getNotificationManager();
        notificationManager.cancel(id);
    }

    /**
     * Create a new notification message when a new time registration gets started. This message cannot be cleared from
     * the notifications with the clear button.
     * @param projectName The name of the {@link eu.vranckaert.worktime.model.Project}.
     * @param taskName The name of the {@link eu.vranckaert.worktime.model.Task}.
     */
    public void addOngoingTimeRegistrationMessage(String projectName, String taskName) {
        Log.d(LOG_TAG, "Adding ongoing TR message...");

        boolean showStatusBarNotifications = Preferences.getShowStatusBarNotificationsPreference(ctx);
        Log.d(LOG_TAG, "Status bar notifications enabled? " + (showStatusBarNotifications?"Yes":"No"));

        if (showStatusBarNotifications) {
            Log.d(LOG_TAG, "Set the ongoing TR message");
            setOngoingTimeRegistrationMessage(projectName, taskName, R.string.lbl_notif_new_tr_started);
        } else {
            removeAllMessages();
        }
    }

    /**
     * Update the notification that a time registration is running. This message cannot be cleared from
     * the notifications with the clear button.
     * @param projectName The name of the {@link eu.vranckaert.worktime.model.Project}.
     * @param taskName The name of the {@link eu.vranckaert.worktime.model.Task}.
     */
    public void updateOngoingTimeRegistrationMessage(String projectName, String taskName) {
        Log.d(LOG_TAG, "Updating ongoing TR message...");

        boolean showStatusBarNotifications = Preferences.getShowStatusBarNotificationsPreference(ctx);
        Log.d(LOG_TAG, "Status bar notifications enabled? " + (showStatusBarNotifications?"Yes":"No"));

        if (showStatusBarNotifications) {
            Log.d(LOG_TAG, "Set the updated ongoing TR message");
            setOngoingTimeRegistrationMessage(projectName, taskName, R.string.lbl_notif_update_tr);
        } else {
            removeAllMessages();
        }
    }

    /**
     * Sets a notification message indicating that a time registration is ongoing. This message cannot be cleared from
     * the  notifications with the clear button.
     * @param projectName The name of the {@link eu.vranckaert.worktime.model.Project}.
     * @param taskName The name of the {@link eu.vranckaert.worktime.model.Task}.
     * @param tickerString The ticker message to show in the notification bar.
     */
    private void setOngoingTimeRegistrationMessage(String projectName, String taskName, int tickerString) {
        NotificationManager notificationManager = getNotificationManager();

        int icon = R.drawable.logo_notif_bar;
        CharSequence tickerText = ctx.getString(tickerString);
        Long when = System.currentTimeMillis();

        Notification notification = new Notification(icon, tickerText, when);
        notification.flags |= Notification.FLAG_NO_CLEAR;

        CharSequence contentTitle = ctx.getString(R.string.lbl_notif_title_ongoing_tr);
        CharSequence contentText = ctx.getString(R.string.lbl_notif_project_task_name, projectName, taskName);

        Intent notificationIntent = new Intent(ctx, EndTimeRegistration.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        PendingIntent contentIntent = PendingIntent.getActivity(ctx, 0, notificationIntent, 0);
        notification.setLatestEventInfo(ctx, contentTitle, contentText, contentIntent);

        notificationManager.notify(
                NotificationIds.ONGOING_TIME_REGISTRATION_MESSAGE,
                notification
        );
    }
}
