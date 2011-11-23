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
package eu.vranckaert.worktime.activities.projects;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.google.inject.Inject;
import eu.vranckaert.worktime.R;
import eu.vranckaert.worktime.constants.Constants;
import eu.vranckaert.worktime.constants.TrackerConstants;
import eu.vranckaert.worktime.model.Project;
import eu.vranckaert.worktime.service.ProjectService;
import eu.vranckaert.worktime.service.WidgetService;
import eu.vranckaert.worktime.utils.context.IntentUtil;
import eu.vranckaert.worktime.utils.context.ContextUtils;
import eu.vranckaert.worktime.utils.tracker.AnalyticsTracker;
import roboguice.activity.GuiceActivity;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;

/**
 * User: DIRK VRANCKAERT
 * Date: 06/02/11
 * Time: 03:51
 */
public class AddEditProjectActivity extends GuiceActivity {
    private static final String LOG_TAG = AddEditProjectActivity.class.getSimpleName();

    @InjectView(R.id.projectname) private EditText projectNameInput;
    @InjectView(R.id.projectcomment) private EditText projectCommentInput;
    @InjectView(R.id.projectname_required) private TextView projectnameRequired;
    @InjectView(R.id.projectname_unique) private TextView projectnameUnique;
    @InjectView(R.id.title_text) private TextView titleText;

    @Inject
    private ProjectService projectService;

    @Inject
    private WidgetService widgetService;

    @InjectExtra(value = Constants.Extras.PROJECT, optional = true)
    private Project editProject;

    private AnalyticsTracker tracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_project);
        tracker = AnalyticsTracker.getInstance(getApplicationContext());
        tracker.trackPageView(TrackerConstants.PageView.ADD_EDIT_PROJECT_ACTIVITY);

        if (inUpdateMode()) {
            titleText.setText(R.string.lbl_edit_project_title);
            projectNameInput.setText(editProject.getName());
            projectCommentInput.setText(editProject.getComment());
        }
    }

    /**
     * Navigate home.
     * @param view The view.
     */
    public void onHomeClick(View view) {
        IntentUtil.goHome(this);
    }

    /**
     * Save the project.
     * @param view The view.
     */
    public void onSaveClick(View view) {
        hideValidationErrors();

        String name = projectNameInput.getText().toString();
        String comment = projectCommentInput.getText().toString();
        if (name.length() > 0) {
            if (checkForDuplicateProjectNames(name)) {
                Log.d(LOG_TAG, "A project with this name already exists... Choose another name!");
                projectnameUnique.setVisibility(View.VISIBLE);
            } else {
                ContextUtils.hideKeyboard(AddEditProjectActivity.this, projectNameInput);
                Log.d(LOG_TAG, "Ready to save new project");

                ImageView saveButton = (ImageView) findViewById(R.id.btn_save);
                ProgressBar progressBar = (ProgressBar) findViewById(R.id.title_refresh_progress);
                saveButton.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);

                Project project;
                if (!inUpdateMode()) {
                    project = new Project();
                } else {
                    project = editProject;
                }
                project.setName(name);
                project.setComment(comment);

                if (!inUpdateMode()) {
                    project = projectService.save(project);
                    tracker.trackEvent(
                            TrackerConstants.EventSources.ADD_EDIT_PROJECT_ACTIVITY,
                            TrackerConstants.EventActions.ADD_PROJECT
                    );
                    Log.d(LOG_TAG, "New project persisted");
                } else {
                    project = projectService.update(project);
                    tracker.trackEvent(
                            TrackerConstants.EventSources.ADD_EDIT_PROJECT_ACTIVITY,
                            TrackerConstants.EventActions.EDIT_PROJECT
                    );
                    Log.d(LOG_TAG, "Project with id " + editProject + " and name " + editProject.getName() + " is updated");
                    Log.d(LOG_TAG, "About to update the wiget and notifications");
                    widgetService.updateWidget(getApplicationContext());
                }

                Intent intentData = new Intent();
                intentData.putExtra(Constants.Extras.PROJECT, project);
                setResult(RESULT_OK, intentData);
                finish();
            }
        } else {
            Log.d(LOG_TAG, "Validation error!");
            projectnameRequired.setVisibility(View.VISIBLE);
        }
    }

    private boolean checkForDuplicateProjectNames(String projectName) {
        if (!inUpdateMode()) {
            return projectService.isNameAlreadyUsed(projectName);
        } else  {
            return projectService.isNameAlreadyUsed(projectName, editProject);
        }
    }

    /**
     * Hide all the validation errors.
     */
    private void hideValidationErrors() {
        projectnameRequired.setVisibility(View.GONE);
        projectnameUnique.setVisibility(View.GONE);
    }

    /**
     * Checks if the activity is in update mode. If not it's create mode!
     * @return {@link Boolean#TRUE} if the project is about to be updated, {@link Boolean#FALSE} if in creation mode.
     */
    private boolean inUpdateMode() {
        if (editProject == null || editProject.getId() < 0) {
            return false;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        tracker.stopSession();
    }
}
