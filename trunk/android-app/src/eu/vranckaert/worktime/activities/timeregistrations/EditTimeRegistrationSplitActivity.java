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

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import eu.vranckaert.worktime.R;
import eu.vranckaert.worktime.utils.wizard.WizardActivity;

/**
 * User: DIRK VRANCKAERT
 * Date: 07/12/11
 * Time: 07:17
 */
public class EditTimeRegistrationSplitActivity extends WizardActivity {
    private int[] layouts = {R.layout.wizard_test_1, R.layout.wizard_test_2, R.layout.wizard_test_3};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentViews(true, false, layouts);
    }

    @Override
    protected void initialize(View view) {
        Toast.makeText(EditTimeRegistrationSplitActivity.this, "In wizard activity!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean beforePageChange(int currentViewIndex, int nextViewIndex, View view) {
        Toast.makeText(EditTimeRegistrationSplitActivity.this, "Before changing page...", Toast.LENGTH_SHORT).show();
        return true;
    }

    @Override
    protected void afterPageChange(int currentViewIndex, int previousViewIndex, View view) {
        Toast.makeText(EditTimeRegistrationSplitActivity.this, "Page has been changed to page " + currentViewIndex, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected boolean onCancel(View view, View button) {
        Toast.makeText(EditTimeRegistrationSplitActivity.this, "Canceling wizard...", Toast.LENGTH_SHORT).show();
        return true;
    }

    @Override
    protected boolean onFinish(View view, View button) {
        Toast.makeText(EditTimeRegistrationSplitActivity.this, "Finishing wizard...", Toast.LENGTH_SHORT).show();
        return true;
    }
}
