package eu.vranckaert.worktime.enums.reporting;

/**
 * User: DIRK VRANCKAERT
 * Date: 27/09/11
 * Time: 00:05
 */
public enum ReportingDataDisplay {
    GROUPED_BY_START_DATE(0),
    GROUPED_BY_PROJECT(1);

    private int order;

    ReportingDataDisplay(int order) {
        setOrder(order);
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
