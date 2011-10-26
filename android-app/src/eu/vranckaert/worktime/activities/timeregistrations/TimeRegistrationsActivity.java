package eu.vranckaert.worktime.activities.timeregistrations;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.google.inject.Inject;
import eu.vranckaert.worktime.R;
import eu.vranckaert.worktime.comparators.timeregistration.TimeRegistrationDescendingByStartdate;
import eu.vranckaert.worktime.constants.Constants;
import eu.vranckaert.worktime.constants.TrackerConstants;
import eu.vranckaert.worktime.model.TimeRegistration;
import eu.vranckaert.worktime.service.TimeRegistrationService;
import eu.vranckaert.worktime.service.WidgetService;
import eu.vranckaert.worktime.utils.context.IntentUtil;
import eu.vranckaert.worktime.utils.context.ContextMenuUtils;
import eu.vranckaert.worktime.utils.context.ContextUtils;
import eu.vranckaert.worktime.utils.date.DateFormat;
import eu.vranckaert.worktime.utils.date.DateUtils;
import eu.vranckaert.worktime.utils.date.TimeFormat;
import eu.vranckaert.worktime.utils.notifications.NotificationBarManager;
import eu.vranckaert.worktime.utils.string.StringUtils;
import eu.vranckaert.worktime.utils.tracker.AnalyticsTracker;
import roboguice.activity.GuiceListActivity;

import java.util.Collections;
import java.util.List;

/**
 * User: DIRK VRANCKAERT
 * Date: 05/02/11
 * Time: 18:58
 */
public class TimeRegistrationsActivity extends GuiceListActivity {
    private static final String LOG_TAG = TimeRegistrationsActivity.class.getSimpleName();

    @Inject
    TimeRegistrationService timeRegistrationService;
    @Inject
    WidgetService widgetService;

    List<TimeRegistration> timeRegistrations;
    //Vars for deleting time registrations
    TimeRegistration timeRegistrationToDelete = null;

    private AnalyticsTracker tracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrations);
        tracker = AnalyticsTracker.getInstance(getApplicationContext());
        tracker.trackPageView(TrackerConstants.PageView.TIME_REGISTRATIONS_ACTIVITY);

        loadTimeRegistrations(true);

        getListView().setOnItemClickListener(new ListView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(LOG_TAG, "Clicked on TR-item " + position);
                TimeRegistration selectedRegistration = timeRegistrations.get(position);

                TimeRegistration previousTimeRegistration = null;
                if (timeRegistrations.size() > position + 1) {
                    previousTimeRegistration = timeRegistrations.get(position + 1);
                }

                TimeRegistration nextTimeRegistration = null;
                if (position > 0) {
                    nextTimeRegistration = timeRegistrations.get(position - 1);
                }
                IntentUtil.openRegistrationDetailActivity(TimeRegistrationsActivity.this, selectedRegistration,
                        previousTimeRegistration, nextTimeRegistration);
            }
        });

        registerForContextMenu(getListView());
    }

    private void loadTimeRegistrations(boolean reloadTimeRegistrations) {
        if (reloadTimeRegistrations) {
            this.timeRegistrations = timeRegistrationService.findAll();
            Collections.sort(timeRegistrations, new TimeRegistrationDescendingByStartdate());
            Log.d(LOG_TAG, this.timeRegistrations.size() + " timeregistrations loaded!");
        }

        TimRegistrationsListAdapter adapter = new TimRegistrationsListAdapter(timeRegistrations);
        adapter.notifyDataSetChanged();
        setListAdapter(adapter);
    }

    /**
     * Go Home.
     * @param view The view.
     */
    public void onHomeClick(View view) {
        IntentUtil.goHome(this);
    }

    /**
     * Add a time registration.
     * @param view The view.
     */
    public void onAddClick(View view) {
        //Not yet implemented
    }

    /**
     * Export the time registrations.
     * @param view The view.
     */
    public void onExportClick(View view) {
        if(ContextUtils.isSdCardAvailable() && ContextUtils.isSdCardWritable()) {
            Intent intent = new Intent(TimeRegistrationsActivity.this, ExportTimeRegistrationsActivity.class);
            startActivity(intent);
        } else {
            showDialog(Constants.Dialog.EXPORT_UNAVAILABLE);
        }
    }

    //TimRegistrationsListAdapter
    /**
     * The list adapater private inner-class used to display the manage projects list.
     */
    private class TimRegistrationsListAdapter extends ArrayAdapter<TimeRegistration> {
        private final String LOG_TAG = TimRegistrationsListAdapter.class.getSimpleName();
        /**
         * {@inheritDoc}
         */
        public TimRegistrationsListAdapter(List<TimeRegistration> timeRegistrations) {
            super(TimeRegistrationsActivity.this, R.layout.list_item_time_registrations, timeRegistrations);
            Log.d(LOG_TAG, "Creating the time registrations list adapater");
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Log.d(LOG_TAG, "Start rendering/recycling row " + position);
            View row;
            final TimeRegistration tr = timeRegistrations.get(position);
            Log.d(LOG_TAG, "Got time registration with startDate " +
                    DateUtils.convertDateTimeToString(tr.getStartTime(),
                    DateFormat.FULL,
                    TimeFormat.MEDIUM,
                    TimeRegistrationsActivity.this));

            if (convertView == null) {
                Log.d(LOG_TAG, "Render a new line in the list");
                row = getLayoutInflater().inflate(R.layout.list_item_time_registrations, parent, false);
            } else {
                Log.d(LOG_TAG, "Recycling an existing line in the list");
                row = convertView;
            }

            Log.d(LOG_TAG, "Ready to update the startdate, enddate and projectname of the timeregistration...");
            TextView startDate = (TextView) row.findViewById(R.id.lbl_timereg_startdate);
            startDate.setText(DateUtils.convertDateTimeToString(tr.getStartTime(), DateFormat.MEDIUM,
                    TimeFormat.MEDIUM, TimeRegistrationsActivity.this));
            TextView endDate = (TextView) row.findViewById(R.id.lbl_timereg_enddate);
            String endDateStr = "";
            if(tr.getEndTime() == null) {
                endDateStr = getString(R.string.now);
            } else {
                endDateStr = DateUtils.convertDateTimeToString(tr.getEndTime(), DateFormat.MEDIUM,
                    TimeFormat.MEDIUM, TimeRegistrationsActivity.this);
            }
            endDate.setText(endDateStr);
            TextView projectNameTaskName = (TextView) row.findViewById(R.id.lbl_timereg_projectname_taskname);
            String projectAndTaskText = tr.getTask().getProject().getName() +
                    " " + getString(R.string.dash) + " " + tr.getTask().getName();
            projectNameTaskName.setText(projectAndTaskText);

            Log.d(LOG_TAG, "Ready to update the duration of the timeregistration...");
            TextView durationView = (TextView) row.findViewById(R.id.lbl_timereg_duration);
            String durationText = DateUtils.calculatePeriod(getApplicationContext(), tr);
            durationView.setText(durationText);

            Log.d(LOG_TAG, "Bind an on click event on the delete button");
            View deleteButton =  row.findViewById(R.id.btn_delete);
            deleteButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    deleteTimeRegistration(tr, true);
                }
            });

            Log.d(LOG_TAG, "Ready to set the comment if available...");
            View view = row.findViewById(R.id.registrations_comment_view);
            if (StringUtils.isNotBlank(tr.getComment())) {
                Log.d(LOG_TAG, "CommentHistory available...");
                view.setVisibility(View.VISIBLE);
                TextView commentTextView = (TextView) row.findViewById(R.id.lbl_registrations_comment);
                commentTextView.setText(tr.getComment());
            } else {
                Log.d(LOG_TAG, "CommentHistory not available...");
                view.setVisibility(View.GONE);
            }

            Log.d(LOG_TAG, "Done rendering row " + position);
            return row;
        }
    }

    private void deleteTimeRegistration(final TimeRegistration timeRegistration, boolean askPermission) {
        if(askPermission) {
            timeRegistrationToDelete = timeRegistration;
            showDialog(Constants.Dialog.DELETE_TIME_REGISTRATION_YES_NO);
            return;
        }

        timeRegistrationService.remove(timeRegistration);
        timeRegistrations.remove(timeRegistration);

        tracker.trackEvent(
                TrackerConstants.EventSources.TIME_REGISTRATIONS_ACTIVITY,
                TrackerConstants.EventActions.DELETE_TIME_REGISTRATION
        );

        timeRegistrationToDelete = null;
        widgetService.updateWidget(TimeRegistrationsActivity.this);
        loadTimeRegistrations(false);

        if (timeRegistration.isOngoingTimeRegistration()) {
            NotificationBarManager notificationBarManager = NotificationBarManager.getInstance(getApplicationContext());
            notificationBarManager.removeMessage(
                    NotificationBarManager.NotificationIds.ONGOING_TIME_REGISTRATION_MESSAGE
            );
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        Dialog dialog = null;
        switch (id) {
            case Constants.Dialog.DELETE_TIME_REGISTRATION_YES_NO: {
                AlertDialog.Builder alertRemoveAllRegs = new AlertDialog.Builder(this);
				alertRemoveAllRegs
						   .setMessage(R.string.msg_delete_registration_confirmation)
						   .setCancelable(false)
						   .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
                                    deleteTimeRegistration(timeRegistrationToDelete, false);
                                    dialog.cancel();
								}
							})
						   .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
                                    timeRegistrationToDelete = null;
									dialog.cancel();
								}
							});
				dialog = alertRemoveAllRegs.create();
                break;
            }
            case Constants.Dialog.EXPORT_UNAVAILABLE: {
                AlertDialog.Builder alertExportUnavailable = new AlertDialog.Builder(this);
				alertExportUnavailable.setTitle(R.string.msg_export_not_available)
                           .setMessage(R.string.msg_export_not_available_detail)
						   .setCancelable(false)
						   .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                               public void onClick(DialogInterface dialog, int which) {
                                   dialog.cancel();
                               }
                           });
				dialog = alertExportUnavailable.create();
                break;
            }
        }
        return dialog;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Constants.IntentRequestCodes.REGISTRATION_DETAILS : {
                if (resultCode == RESULT_OK) {
                    Log.d(LOG_TAG, "A TR has been updated on the registrations details view, it's necessary to reload the list of time registrations upon return!");
                    loadTimeRegistrations(true);
                }
                break;
            }
            case Constants.IntentRequestCodes.REGISTRATION_EDIT_DIALOG: {
                if (resultCode == RESULT_OK) {
                    Log.d(LOG_TAG, "The time registration has been updated!");
                    loadTimeRegistrations(true);
                }
                break;
            }
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        Log.d(LOG_TAG, "In method onCreateContextMenu(...)");
        if (v.getId() == android.R.id.list) {
            super.onCreateContextMenu(menu, v, menuInfo);
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
            TimeRegistration timeRegistration = timeRegistrations.get(info.position);
            ContextMenuUtils.createTimeRegistrationEditContextMenu(
                    getApplicationContext(),
                    timeRegistration,
                    menu,
                    false
            );

            if (info.position > 0 || timeRegistration.isOngoingTimeRegistration()) {
                menu.removeItem(Constants.ContentMenuItemIds.TIME_REGISTRATION_RESTART);
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int element = info.position;
        TimeRegistration timeRegistrationForContext = timeRegistrations.get(element);

        TimeRegistration previousTimeRegistration = null;
        if (timeRegistrations.size() > element + 1) {
            previousTimeRegistration = timeRegistrations.get(element + 1);
        }

        TimeRegistration nextTimeRegistration = null;
        if (element > 0) {
            nextTimeRegistration = timeRegistrations.get(element - 1);
        }

        if (item.getItemId() == Constants.ContentMenuItemIds.TIME_REGISTRATION_DELETE) {
            deleteTimeRegistration(timeRegistrationForContext, true);
            return true;
        }

        return ContextMenuUtils.handleTimeRegistrationEditContextMenuSelection(
                TimeRegistrationsActivity.this,
                item,
                timeRegistrationForContext,
                previousTimeRegistration,
                nextTimeRegistration,
                tracker
        );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        tracker.stopSession();
    }
}
