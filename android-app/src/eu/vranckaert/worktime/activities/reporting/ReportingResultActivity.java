package eu.vranckaert.worktime.activities.reporting;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.google.inject.Inject;
import com.google.inject.internal.Nullable;
import eu.vranckaert.worktime.R;
import eu.vranckaert.worktime.constants.Constants;
import eu.vranckaert.worktime.enums.reporting.ReportingDataGrouping;
import eu.vranckaert.worktime.enums.reporting.ReportingDisplayDuration;
import eu.vranckaert.worktime.model.Project;
import eu.vranckaert.worktime.model.Task;
import eu.vranckaert.worktime.model.TimeRegistration;
import eu.vranckaert.worktime.service.TimeRegistrationService;
import eu.vranckaert.worktime.ui.reporting.ReportingTableRecord;
import eu.vranckaert.worktime.utils.date.DateUtils;
import roboguice.activity.GuiceActivity;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User: DIRK VRANCKAERT
 * Date: 24/09/11
 * Time: 20:03
 */
public class ReportingResultActivity extends GuiceActivity {
    private static final String LOG_TAG = ReportingResultActivity.class.getSimpleName();

    @Inject
    private TimeRegistrationService timeRegistrationService;

    @InjectExtra(value= Constants.Extras.TIME_REGISTRATION_START_DATE)
    private Date startDate;
    @InjectExtra(value= Constants.Extras.TIME_REGISTRATION_END_DATE)
    private Date endDate;
    @InjectExtra(value = Constants.Extras.PROJECT, optional = true)
    @Nullable
    private Project project;
    @InjectExtra(value = Constants.Extras.TASK, optional = true)
    @Nullable
    private Task task;
    @InjectExtra(value = Constants.Extras.REPORTING_DATA_GROUPING)
    private ReportingDataGrouping dataGrouping;
    @InjectExtra(value = Constants.Extras.REPORTING_DATA_DISPLAY_DURATION)
    private ReportingDisplayDuration displayDuration;

    @InjectView(R.id.reporting_result_includes_ongoing_tr_label)
    private TextView resultIncludesOngoingTrsLabel;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reporting_result);

        initializeView();
    }

    private void initializeView() {
        AsyncTask asyncTask = new AsyncTask() {
            @Override
            protected void onPreExecute() {
                showDialog(Constants.Dialog.LOADING_REPORTING_RESULTS);
            }

            @Override
            protected Object doInBackground(Object... objects) {
                List<TimeRegistration> timeRegistrations = timeRegistrationService
                    .getTimeRegistrations(startDate, endDate, project, task);
                Log.d(LOG_TAG, "Number of time registrations found: " + timeRegistrations.size());
                List<ReportingTableRecord> tableRecords = buildTableRecords(timeRegistrations, dataGrouping);
                return tableRecords;
            }

            @Override
            protected void onPostExecute(Object o) {
                removeDialog(Constants.Dialog.LOADING_REPORTING_RESULTS);
                List<ReportingTableRecord> tableRecords = (List<ReportingTableRecord>)o;
                buildTable(tableRecords);
            }
        };
        asyncTask.execute();
    }

    private void buildTable(List<ReportingTableRecord> tableRecords) {
        //TODO clean code to get TableLayout (this is a dirty fix because @InjectView didn't work for some reason...)
        View titleBarView = findViewById(R.id.title_container);
        LinearLayout windowView = (LinearLayout) titleBarView.getParent();
        ScrollView scrollView = (ScrollView) windowView.getChildAt(1);
        LinearLayout scrollViewContent = (LinearLayout) scrollView.getChildAt(0);
        TableLayout resultTable = (TableLayout) scrollViewContent.getChildAt(1);

        for (ReportingTableRecord record : tableRecords) {
            TableRow row = new TableRow(ReportingResultActivity.this);
            TextView recordTotalCol1 = new TextView(ReportingResultActivity.this);
            recordTotalCol1.setText(record.getColumn1());
            TextView recordTotalCol2 = new TextView(ReportingResultActivity.this);
            recordTotalCol2.setText(record.getColumn2());
            TextView recordTotalCol3 = new TextView(ReportingResultActivity.this);
            recordTotalCol3.setText(record.getColumn3());
            TextView recordTotalCol4 = new TextView(ReportingResultActivity.this);
            recordTotalCol4.setText(record.getColumnTotal());

            row.addView(recordTotalCol1);
            row.addView(recordTotalCol2);
            row.addView(recordTotalCol3);
            row.addView(recordTotalCol4);

            resultTable.addView(row);

            if (record.isOngoingTr()) {
                resultIncludesOngoingTrsLabel.setVisibility(View.VISIBLE);
            }
        }
    }

    private List<ReportingTableRecord> buildTableRecords(List<TimeRegistration> timeRegistrations, ReportingDataGrouping reportingDataGrouping) {
        List<ReportingTableRecord> tableRecords = new ArrayList<ReportingTableRecord>();

        ReportingTableRecord totalRecord = new ReportingTableRecord();
        String totalDuration = DateUtils.calculatePeriod(ReportingResultActivity.this, timeRegistrations, displayDuration);
        totalRecord.setColumn1(getText(R.string.lbl_reporting_results_table_total).toString());
        totalRecord.setColumn3(totalDuration);

        //TODO move this code to determine if an ongoing TR is in the list to the switch...
        for (TimeRegistration timeRegistration : timeRegistrations) {
            if (timeRegistration.isOngoingTimeRegistration()) {
                totalRecord.setOngoingTr(true);
                break;
            }
        }

        tableRecords.add(totalRecord);

        switch (reportingDataGrouping) {
            case GROUPED_BY_START_DATE: {
                break;
            }
            case GROUPED_BY_PROJECT: {
                break;
            }
        }

        return tableRecords;
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        Log.d(LOG_TAG, "Received request to create dialog with id " + id);
        Dialog dialog = null;
        switch(id) {
            case Constants.Dialog.LOADING_REPORTING_RESULTS: {
                dialog = ProgressDialog.show(
                        ReportingResultActivity.this,
                        "",
                        getText(R.string.lbl_reporting_result_loading_dialog),
                        true,
                        false
                );
                break;
            }
            default:
                Log.e(LOG_TAG, "Dialog id " + id + " is not supported in this activity!");
        }
        return dialog;
    }
}