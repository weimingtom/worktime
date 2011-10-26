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
package eu.vranckaert.worktime.enums.reporting;

/**
 * User: DIRK VRANCKAERT
 * Date: 15/09/11
 * Time: 23:38
 */
public enum ReportingDateRange {
    TODAY(0),
    THIS_WEEK(1),
    LAST_WEEK(2),
    ALL_TIMES(3),
    CUSTOM(4);

    private int order;

    ReportingDateRange(int order) {
        this.order = order;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
