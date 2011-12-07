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
package eu.vranckaert.worktime.utils.wizard;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import eu.vranckaert.worktime.R;

import java.util.ArrayList;
import java.util.List;

/**
 * User: DIRK VRANCKAERT
 * Date: 07/12/11
 * Time: 11:09
 */
public abstract class WizardActivity extends Activity {
    private static final String LOG_TAG = WizardActivity.class.getSimpleName();

    private View cancelButton;
    private View finishButton;
    private View nextButton;
    private View previousButton;

    private ViewGroup contentContainer;
    private View activeView;
    private int currentViewIndex = -1;

    private List<Integer> layoutResIDs = new ArrayList<Integer>();

    private boolean previousEnabled = true;
    private boolean cancelEnabled = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        super.setContentView(R.layout.wizard);
    }

    /**
     * Set the layouts to be used in this wizard. The order of the layouts is important as they are shown in the order
     * specified.
     * @param previousEnabled Set to {@link Boolean#TRUE} if you want to set the previous button enabled. To
     * {@link Boolean#FALSE} if you want to disable it. Default is {@link Boolean#TRUE}.
     * @param cancelEnabled Set to {@link Boolean#TRUE} if you want to set the cancel button enabled. To
     * {@link Boolean#FALSE} if you want to disable it. Default is {@link Boolean#TRUE}.
     * @param layoutResIDs The layout resource ids to be used in this activity.
     */
    protected void setContentViews(boolean previousEnabled, boolean cancelEnabled, int... layoutResIDs) {
        setPreviousEnabled(previousEnabled);
        setCancelEnabled(cancelEnabled);
        setContentViews(layoutResIDs);
    }

    /**
     * Set the layouts to be used in this wizard. The order of the layouts is important as they are shown in the order
     * specified.
     * @param layoutResIDs The layout resource ids to be used in this activity.
     */
    protected void setContentViews(int... layoutResIDs) {
        for (int layoutResID : layoutResIDs) {
            this.layoutResIDs.add(layoutResID);
        }

        if (this.layoutResIDs.size() > 0) {
            init();
        } else {
            Log.w(LOG_TAG, "No views have been defined for this wizard so we cannot the wizard!");
            finish();
        }
    }

    private void init() {
        contentContainer = (ViewGroup) findViewById(R.id.wizard_page_content_container);

        cancelButton = findViewById(R.id.wizard_navigation_container_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View button) {
                boolean result = onCancel(getActiveView(), button);
                if (result)
                    closeOnCancel();
            }
        });

        finishButton = findViewById(R.id.wizard_navigation_container_finish);
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View button) {
                boolean result = onFinish(getActiveView(), button);
                if (result)
                    closeOnFinish();
            }
        });

        nextButton = findViewById(R.id.wizard_navigation_container_next);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View button) {
                openNextPage();
            }
        });

        previousButton = findViewById(R.id.wizard_navigation_container_previous);
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View button) {
                openPreviousPage();
            }
        });

        openNextPage();
    }

    private void openNextPage() {
        int nextViewIndex = currentViewIndex + 1;
        changePage(currentViewIndex, nextViewIndex);
    }

    private void openPreviousPage() {
        int nextViewIndex = currentViewIndex - 1;
        changePage(currentViewIndex, nextViewIndex);
    }

    private void changePage(int currentIndex, int nextIndex) {
        boolean isInitialLoad = false;
        if (currentIndex == -1)
            isInitialLoad = true;

        boolean beforePageChangeResult = true;
        if (!isInitialLoad) {
            beforePageChangeResult = beforePageChange(currentIndex, nextIndex, getActiveView());
        }

        if (!beforePageChangeResult) {
            return;
        }

        int resId = Integer.parseInt(layoutResIDs.get(nextIndex).toString());
        Log.d(LOG_TAG, "Resource id to load next: " + resId);
        activeView = WizardActivity.this.getLayoutInflater().inflate(
                resId,
                contentContainer,
                false
        );

        currentViewIndex = nextIndex;
        if (isInitialLoad) {
            initialize(getActiveView());
        } else {
            afterPageChange(nextIndex, currentIndex, getActiveView());
        }

        invalidateNavigation(nextIndex);
    }

    private void invalidateNavigation(int newIndex) {
        Log.d(LOG_TAG, "Invalidating navigation components");

        //If first: show cancel button
        //Else: show previous button
        if (newIndex == 0) {
            Log.d(LOG_TAG, "Enable CANCEL, hide PREVIOUS");
            cancelButton.setVisibility(View.VISIBLE);
            previousButton.setVisibility(View.GONE);
        } else {
            Log.d(LOG_TAG, "Hide CANCEL, enable PREVIOUS");
            cancelButton.setVisibility(View.GONE);
            previousButton.setVisibility(View.VISIBLE);
        }

        //If last: show finish button
        //Else: show next button
        if (newIndex == layoutResIDs.size()-1) {
            Log.d(LOG_TAG, "Enable FINISH, hide NEXT");
            finishButton.setVisibility(View.VISIBLE);
            nextButton.setVisibility(View.GONE);
        } else {
            Log.d(LOG_TAG, "Hide FINISH, enable NEXT");
            finishButton.setVisibility(View.GONE);
            nextButton.setVisibility(View.VISIBLE);
        }

        //Override witch specific settings for cancel and previous button
        Log.d(LOG_TAG, "Override navigation components with user settings");
        if (!isCancelEnabled()) {
            Log.d(LOG_TAG, "CANCEL should not be enabled");
            cancelButton.setVisibility(View.GONE);
        }
        if (!isPreviousEnabled()) {
            Log.d(LOG_TAG, "PREVIOUS should not be enabled");
            previousButton.setVisibility(View.GONE);
        }
    }

    /**
     * This block of code is executed after the first view is loaded.
     * @param view The first view loaded.
     */
    protected abstract void initialize(View view);

    /**
     * This bock of code is executed before changing from one page in the wizard to another.
     * @param currentViewIndex The index of the page you are leaving (0-based).
     * @param nextViewIndex The index of the page you are going to (0-based).
     * @param view The view that is currently loaded (so the view of the page you are coming from).
     * @return If this method returns {@link Boolean#TRUE} execution will continue and the next page will be loaded. If
     * it returns {@link Boolean#FALSE} execution of the page change will be stopped and so the view will not change (in
     * case of a validation error for example).
     */
    public abstract boolean beforePageChange(int currentViewIndex, int nextViewIndex, View view);

    /**
     * This bock of code is executed before changing from one page in the wizard to another.
     * @param currentViewIndex The index of the page you are going to (0-based).
     * @param previousViewIndex The index of the page you left (0-based).
     * @param view The view that is currently loaded (so the view of the page you are going to).
     */
    protected abstract void afterPageChange(int currentViewIndex, int previousViewIndex, View view);

    /**
     * This block of code is executed when the "cancel" button is pressed. If you want to end this activity with a
     * certain result you should override the {@link WizardActivity#closeOnCancel()} method.
     * @param view The view that is current loaded.
     * @param button The "cancel" button.
     * @return If this method returns {@link Boolean#TRUE} the activity will be closed. If it returns
     * {@link Boolean#FALSE} the activity will remain open (in case of an error for example).
     */
    protected abstract boolean onCancel(View view, View button);

    /**
     * This block of code is executed when the "finish" button is pressed. If you want to end this activity with a
     * certain result you should override the {@link WizardActivity#closeOnFinish()} method.
     * @param view The view that is current loaded.
     * @param button The "finish" button.
     * @return If this method returns {@link Boolean#TRUE} the activity will be closed. If it returns
     * {@link Boolean#FALSE} the activity will remain open (in case of a validation error for example).
     */
    protected abstract boolean onFinish(View view, View button);

    public void closeOnCancel() {
        finish();
    }

    public void closeOnFinish() {
        finish();
    }

    /**
     * Checks if previous is enabled or not.
     * @return True or false.
     */
    private boolean isPreviousEnabled() {
        return previousEnabled;
    }

    /**
     * Change the behaviour of the application. If set to true the previous button will be enabled. If set to false it
     * will be disabled (gone). Default is enabled!
     * @param previousEnabled True or false.
     */
    private void setPreviousEnabled(boolean previousEnabled) {
        this.previousEnabled = previousEnabled;
    }

    /**
     * Checks if cancel is enabled or not.
     * @return True or false.
     */
    private boolean isCancelEnabled() {
        return cancelEnabled;
    }

    /**
     * Change the behaviour of the application. If set to true the cancel button will be enabled. If set to false it
     * will be disabled (gone). Default is enabled!
     * @param cancelEnabled True or false.
     */
    private void setCancelEnabled(boolean cancelEnabled) {
        this.cancelEnabled = cancelEnabled;
    }

    @Override
    @Deprecated
    public void setContentView(int layoutResID) {
        Log.w(LOG_TAG, "The content view cannot be changed. This method is deprecated for the WizardActivity!");
    }

    @Override
    @Deprecated
    public void setContentView(View view) {
        Log.w(LOG_TAG, "The content view cannot be changed. This method is deprecated for the WizardActivity!");
    }

    @Override
    @Deprecated
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        Log.w(LOG_TAG, "The content view cannot be changed. This method is deprecated for the WizardActivity!");
    }

    /**
     * Get the currently active view.
     * @return The currently active view. Null if the current view index is set to -1.
     */
    public View getActiveView() {
        return activeView;
    }
}
