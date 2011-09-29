package eu.vranckaert.worktime.utils.date;

/**
 * User: DIRK VRANCKAERT
 * Date: 03/08/11
 * Time: 16:47
 */
public enum HourPreference12Or24 {
	HOURS_24("24-hour"), HOURS_12("12-hour");

    String value;

    HourPreference12Or24(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static HourPreference12Or24 findHourPreference12Or24(String value) {
        HourPreference12Or24[] preferences = HourPreference12Or24.values();
        for (HourPreference12Or24 preference : preferences) {
            if (value.equals(preference.getValue())) {
                return preference;
            }
        }
        return null;
    }
}
