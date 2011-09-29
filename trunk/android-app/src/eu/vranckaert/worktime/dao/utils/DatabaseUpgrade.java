package eu.vranckaert.worktime.dao.utils;

/**
 * User: DIRK VRANCKAERT
 * Date: 05/08/11
 * Time: 19:14
 */
public enum DatabaseUpgrade {
    UPGRADE1(21, new String[]{
            "alter table project add column flags " + DataTypes.TEXT + ";",
            "alter table task add column flags " + DataTypes.TEXT + ";",
            "alter table commenthistory add column flags " + DataTypes.TEXT + ";",
            "alter table timeregistration add column flags " + DataTypes.TEXT + ";",
            "alter table project add column finished " + DataTypes.BOOLEAN + " default 0;",
            "alter table task add column finished " + DataTypes.BOOLEAN + " default 0;",
    }),
    ;

    int toVersion;
    String[] sqlQueries;

    DatabaseUpgrade(int toVersion, String[] sqlQueries) {
        this.toVersion = toVersion;
        this.sqlQueries = sqlQueries;
    }

    public int getToVersion() {
        return toVersion;
    }

    public void setToVersion(int toVersion) {
        this.toVersion = toVersion;
    }

    public String[] getSqlQueries() {
        return sqlQueries;
    }

    public void setSqlQueries(String[] sqlQueries) {
        this.sqlQueries = sqlQueries;
    }

    private class DataTypes {
        private static final String SMALLINT = "SMALLINT";
        private static final String BIGINT = "BIGINT";
        private static final String INTEGER = "INTEGER";
        private static final String BOOLEAN = "SMALLINT";
        private static final String TEXT = "TEXT";
        private static final String VARCHAR = "VARCHAR";
    }
}
