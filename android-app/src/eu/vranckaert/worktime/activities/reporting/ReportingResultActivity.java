package eu.vranckaert.worktime.activities.reporting;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.google.inject.Inject;
import com.google.inject.internal.Nullable;
import eu.vranckaert.worktime.R;
import eu.vranckaert.worktime.constants.Constants;
import eu.vranckaert.worktime.model.Project;
import eu.vranckaert.worktime.model.Task;
import eu.vranckaert.worktime.model.TimeRegistration;
import eu.vranckaert.worktime.service.TimeRegistrationService;
import eu.vranckaert.worktime.utils.date.DateUtils;
import roboguice.activity.GuiceActivity;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;

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
                return timeRegistrations;
            }

            @Override
            protected void onPostExecute(Object o) {
                removeDialog(Constants.Dialog.LOADING_REPORTING_RESULTS);
                List<TimeRegistration> timeRegistrations = (List<TimeRegistration>)o;
                buildTable(timeRegistrations);
            }
        };
        asyncTask.execute();
    }

    private void buildTable(List<TimeRegistration> timeRegistrations) {
        String totalDuration = DateUtils.calculatePeriod(ReportingResultActivity.this, timeRegistrations);
        Toast.makeText(ReportingResultActivity.this, "Number of TR's found: " + timeRegistrations.size() + ". Total duration: " + totalDuration, Toast.LENGTH_LONG).show();

        //Default view-elements...
        TextView emptyTextViewCol2 = new TextView(ReportingResultActivity.this);
        TextView emptyTextViewCol3 = new TextView(ReportingResultActivity.this);
        //AttributeSet

        TableRow totalRow = new TableRow(ReportingResultActivity.this);
        TextView totalTextViewCol1 = new TextView(ReportingResultActivity.this);
        totalTextViewCol1.setText("Total");
        TextView totalTextViewCol4 = new TextView(ReportingResultActivity.this);
        totalTextViewCol4.setText(totalDuration);

        totalRow.addView(totalTextViewCol1);
        totalRow.addView(emptyTextViewCol2);
        totalRow.addView(emptyTextViewCol3);
        totalRow.addView(totalTextViewCol4);

        View view = findViewById(R.id.title_container);
        LinearLayout viewParent = (LinearLayout)view.getParent();
        ScrollView child1 = (ScrollView) viewParent.getChildAt(1);
        LinearLayout child2 = (LinearLayout) child1.getChildAt(0);
        TableLayout resultTable = (TableLayout) child2.getChildAt(0);
        resultTable.addView(totalRow);
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