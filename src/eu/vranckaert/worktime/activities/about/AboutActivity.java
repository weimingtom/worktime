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
package eu.vranckaert.worktime.activities.about;

import android.os.Bundle;
import android.text.util.Linkify;
import android.view.View;
import android.widget.TextView;
import eu.vranckaert.worktime.R;
import eu.vranckaert.worktime.constants.TextConstants;
import eu.vranckaert.worktime.constants.TrackerConstants;
import eu.vranckaert.worktime.dao.utils.DaoConstants;
import eu.vranckaert.worktime.utils.context.IntentUtil;
import eu.vranckaert.worktime.utils.context.ContextUtils;
import eu.vranckaert.worktime.utils.tracker.AnalyticsTracker;
import roboguice.activity.GuiceActivity;
import roboguice.inject.InjectView;

/**
 * User: DIRK VRANCKAERT
 * Date: 05/02/11
 * Time: 19:06
 */
public class AboutActivity extends GuiceActivity {

    @InjectView(R.id.about_project_text) TextView projectText;
    @InjectView(R.id.about_version_name_text) TextView versionNameText;
    @InjectView(R.id.about_version_code_text) TextView versionCodeText;
    @InjectView(R.id.about_database_name_text) TextView databaseNameText;
    @InjectView(R.id.about_database_version_text) TextView databaseVersionText;
    @InjectView(R.id.about_website_text) TextView websiteText;
    @InjectView(R.id.about_bug_tracking_website_text) TextView bugTrackingWebsiteText;

    private AnalyticsTracker tracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        tracker = AnalyticsTracker.getInstance(getApplicationContext());
        tracker.trackPageView(TrackerConstants.PageView.ABOUT_ACTIVITY);

        String project = getString(R.string.app_name);
        String versionName = ContextUtils.getCurrentApplicationVersionName(AboutActivity.this);
        int versionCode = ContextUtils.getCurrentApplicationVersionCode(AboutActivity.this);

        int databaseVersionCode = DaoConstants.VERSION;
        String databaseName  = DaoConstants.DATABASE;

        String website = "http://code.google.com/p/worktime/";
        String bugTrackingWebsite = "http://code.google.com/p/worktime/issues/entry";

        projectText.setText(TextConstants.SPACE + project);
        versionNameText.setText(TextConstants.SPACE + versionName);
        versionCodeText.setText(TextConstants.SPACE + String.valueOf(versionCode));
        databaseNameText.setText(TextConstants.SPACE + databaseName);
        databaseVersionText.setText(TextConstants.SPACE + String.valueOf(databaseVersionCode));
        websiteText.setText(TextConstants.SPACE + website);
        bugTrackingWebsiteText.setText(TextConstants.SPACE + bugTrackingWebsite);

        Linkify.addLinks(websiteText, Linkify.WEB_URLS);
        Linkify.addLinks(bugTrackingWebsiteText, Linkify.WEB_URLS);
    }

    public void onHomeClick(View view) {
        IntentUtil.goHome(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        tracker.stopSession();
    }
}
