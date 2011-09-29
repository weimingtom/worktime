package eu.vranckaert.worktime.enums.reporting;

import eu.vranckaert.worktime.R;

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
