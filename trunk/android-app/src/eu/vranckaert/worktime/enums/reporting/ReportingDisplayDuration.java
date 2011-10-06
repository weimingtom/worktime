package eu.vranckaert.worktime.enums.reporting;

/**
 * User: DIRK VRANCKAERT
 * Date: 06/10/11
 * Time: 23:18
 */
public enum ReportingDisplayDuration {
    HOUR_MINUTES_SECONDS(0),
    DAYS_HOUR_MINUTES_SECONDS_24H(1),
    DAYS_HOUR_MINUTES_SECONDS_08H(2);

    private int order;

    ReportingDisplayDuration(int order) {
        setOrder(order);
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
