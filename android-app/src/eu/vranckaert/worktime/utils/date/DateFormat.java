package eu.vranckaert.worktime.utils.date;

/**
 * All possible DateFormat types.
 * @author Dirk Vranckaert
 */
public enum DateFormat {
    /**
     * FULL is pretty completely specified, such as Tuesday, April 12, 1952 AD or 3:30:42pm PST.
     */
    FULL(java.text.DateFormat.FULL),
    /**
     * LONG is longer, such as January 12, 1952 or 3:30:32pm
     */
    LONG(java.text.DateFormat.LONG),
    /**
     * MEDIUM is longer, such as Jan 12, 1952
     */
    MEDIUM(java.text.DateFormat.MEDIUM),
    /**
     * SHORT is completely numeric, such as 12.13.52 or 3:30pm
     */
    SHORT(java.text.DateFormat.SHORT);

    int style;

    DateFormat(int style) {
        this.style = style;
    }

    public int getStyle() {
        return style;
    }

    public void setStyle(int style) {
        this.style = style;
    }
}
