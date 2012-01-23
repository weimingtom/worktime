/*
 *  Copyright 2012 Dirk Vranckaert
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
package eu.vranckaert.worktime.activities;

import android.test.ActivityInstrumentationTestCase2;

/**
 * User: DIRK VRANCKAERT
 * Date: 20/01/12
 * Time: 9:54
 */
public class MainActivityTest extends ActivityInstrumentationTestCase2<HomeActivity> {
    private HomeActivity activity;

    public MainActivityTest() {
        super("eu.vranckaert.worktime", HomeActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        activity = this.getActivity();
    }

    public void testPreconditions() {
        assertNotNull(activity);
    }
}
