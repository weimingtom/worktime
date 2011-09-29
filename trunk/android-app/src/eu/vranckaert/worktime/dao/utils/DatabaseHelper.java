package eu.vranckaert.worktime.dao.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.db.DatabaseType;
import com.j256.ormlite.db.SqliteAndroidDatabaseType;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import eu.vranckaert.worktime.R;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * A utility class to be used to setup and interact with a database.
 * @param <T> Entity.
 * @param <ID> ID type.
 */
public class DatabaseHelper<T, ID> extends OrmLiteSqliteOpenHelper {
    /**
     * Logging
     */
    private static final String LOG_TAG = DatabaseHelper.class.getSimpleName();

    /**
     * The database type.
     */
    private DatabaseType databaseType = new SqliteAndroidDatabaseType();

    /**
     * The context.
     */
    private Context context = null;

    private Map<String, Dao<T, ID>> daoCache = new HashMap<String, Dao<T, ID>>();

    /**
     * Create a new database helper.
     * @param context The context.
     * @param database The database name.
     * @param version The version of the database.
     */
    public DatabaseHelper(Context context, String database, int version) {
        super(context, database, null, version);
        this.context = context;
        Log.i(LOG_TAG, "Installing database, databasename = " + database + ", version = " + version);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            Log.d(LOG_TAG, "Creating the database");
            for(Tables table : Tables.values()) {
                TableUtils.createTable(databaseType, connectionSource, table.getTableClass());
            }

            int defaultProjectId = 1;

            Log.d(LOG_TAG, "Inserting default project");
            ContentValues projectValues = new ContentValues();
            projectValues.put("id", defaultProjectId);
            projectValues.put("name", context.getString(R.string.default_project_name));
            projectValues.put("comment", context.getString(R.string.default_project_comment));
            projectValues.put("defaultValue", true);
            projectValues.put("finished", false);
            projectValues.put("flags", "");
            database.insert("project", null, projectValues);

            Log.d(LOG_TAG, "Inserting default task for default project");
            ContentValues taskValues = new ContentValues();
            taskValues.put("name", context.getString(R.string.default_task_name));
            taskValues.put("comment", context.getString(R.string.default_task_comment));
            taskValues.put("projectId", defaultProjectId);
            taskValues.put("finished", false);
            taskValues.put("flags", "");
            database.insert("task", null, taskValues);
        } catch (SQLException e) {
            Log.e(LOG_TAG, "Excpetion while creating the database", e);
            throw new RuntimeException("Excpetion while creating the database", e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        if (newVersion < oldVersion) {
            Log.w(LOG_TAG, "Trying to install an older database over a more recent one. Not executing update...");
            return;
        }

        Log.d(LOG_TAG, "Updating the database from version " + oldVersion + " to " + newVersion);

        DatabaseUpgrade[] databaseUpgrades = DatabaseUpgrade.values();
        int upgradeSqlCount = 0;
        int upgradeSqlBlockCount = 0;
        for (DatabaseUpgrade databaseUpgrade : databaseUpgrades) {
            if (oldVersion < databaseUpgrade.getToVersion()) {
                String[] queries = databaseUpgrade.getSqlQueries();
                for (String query : queries) {
                    try {
                        database.execSQL(query);
                    } catch (android.database.SQLException e) {
                        Log.d(LOG_TAG, "Exception while executing upgrade queries (toVersion: "
                                + databaseUpgrade.getToVersion() + ") during query: " + query, e);
                        throw new RuntimeException("Exception while executing upgrade queries (toVersion: "
                                + databaseUpgrade.getToVersion() + ") during query: " + query, e);
                    }
                    Log.d(LOG_TAG, "Executed an upgrade query to version " + databaseUpgrade.getToVersion()
                            + " with success: " + query);
                    upgradeSqlCount++;
                }
                Log.d(LOG_TAG, "Upgrade queries for version " + databaseUpgrade.getToVersion()
                        + " executed with success");
                upgradeSqlBlockCount++;
            }
        }
        if (upgradeSqlCount > 0) {
            Log.d(LOG_TAG, "All upadate queries exected with success. Total number of upgrade queries executed: "
                    + upgradeSqlCount + " in " + upgradeSqlBlockCount);
        } else {
            Log.d(LOG_TAG, "No database upgrade queries where necessary!");
        }


        /* This is the old code for upgrading a database: dropping the old one and creating a new one...
        try {

            for(Tables table : Tables.values()) {
                TableUtils.dropTable(databaseType, connectionSource, table.getTableClass(), true);
            }
            onCreate(database);
        } catch (SQLException e) {
            Log.e(LOG_TAG, "Excpetion while updating the database from version " + oldVersion + "to " + newVersion, e);
            throw new RuntimeException("Excpetion while updating the database from version " + oldVersion + "to " + newVersion, e);
        }*/
    }

    @Override
    public void close() {
        Log.d(LOG_TAG, "Closing connection");
        super.close();
    }

    /**
     * Retrieve a DAO object.
     * @param clazz The entity-class for which a DAO object must be retrieved.
     * @return The DAO-instance.
     */
    public Dao<T, ID> getDao(java.lang.Class<T> clazz) {
        String className = clazz.getName();

        if (daoCache.containsKey(className)) {
            Log.d(LOG_TAG, "DAO found DAO-cache, not creating a new instance...");
            return daoCache.get(className);
        }

        Dao<T, ID> dao = null;
        try {
            Log.d(LOG_TAG, "Creation of DAO for class " + clazz.getSimpleName());
            dao = BaseDaoImpl.createDao(databaseType, getConnectionSource(), clazz);
        } catch (SQLException e) {
            Log.e(LOG_TAG, "Creation of dao failed for " + clazz.getSimpleName());
            throw new RuntimeException("Creation of dao failed for " + clazz.getSimpleName(), e);
        }
        daoCache.put(className, dao);
        return dao;
    }

    public static String convertDateToSqliteDate(Date date, boolean maxTime) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        String seperator = "-";
        String result = "";

        result += cal.get(Calendar.YEAR) + seperator;

        int month = cal.get(Calendar.MONTH) + 1;
        String strMonth = String.valueOf(month);
        if (month < 10) {
            strMonth = "0" + strMonth;
        }
        result += strMonth + seperator;

        int day = cal.get(Calendar.DAY_OF_MONTH);
        String strDay = String.valueOf(day);
        if (day < 10) {
            strDay = "0" + strDay;
        }
        result += strDay;

        if (maxTime) {
            result += " 23:59:59.999999";
        }

        return result;
    }
}
