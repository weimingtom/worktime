package eu.vranckaert.worktime.ui.reporting;

/**
 * User: DIRK VRANCKAERT
 * Date: 01/10/11
 * Time: 16:41
 */
public class ReportingTableRecord {
    private String column1;
    private String column2;
    private String column3;
    private String columnTotal;

    private ReportingTableRecordLevel level;

    public ReportingTableRecord() {}

    public ReportingTableRecord(String column1, String column2, String column3, String columnTotal, ReportingTableRecordLevel level) {
        this.column1 = column1;
        this.column2 = column2;
        this.column3 = column3;
        this.columnTotal = columnTotal;
        this.level = level;
    }

    public String getColumn1() {
        return column1;
    }

    public void setColumn1(String column1) {
        this.column1 = column1;
    }

    public String getColumn2() {
        return column2;
    }

    public void setColumn2(String column2) {
        this.column2 = column2;
    }

    public String getColumn3() {
        return column3;
    }

    public void setColumn3(String column3) {
        this.column3 = column3;
    }

    public String getColumnTotal() {
        return columnTotal;
    }

    public void setColumnTotal(String columnTotal) {
        this.columnTotal = columnTotal;
    }

    public ReportingTableRecordLevel getLevel() {
        return level;
    }

    public void setLevel(ReportingTableRecordLevel level) {
        this.level = level;
    }
}

