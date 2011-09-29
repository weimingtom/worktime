package eu.vranckaert.worktime.dao.impl;

import android.content.Context;
import android.util.Log;
import com.google.inject.Inject;
import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.RawResults;
import com.j256.ormlite.stmt.PreparedStmt;
import com.j256.ormlite.stmt.StatementBuilder;
import com.j256.ormlite.stmt.Where;
import com.sun.xml.internal.messaging.saaj.util.LogDomainConstants;
import eu.vranckaert.worktime.comparators.TimeRegistrationDescendingByStartdate;
import eu.vranckaert.worktime.dao.TimeRegistrationDao;
import eu.vranckaert.worktime.dao.generic.GenericDaoImpl;
import eu.vranckaert.worktime.dao.utils.DatabaseHelper;
import eu.vranckaert.worktime.model.Project;
import eu.vranckaert.worktime.model.Task;
import eu.vranckaert.worktime.model.TimeRegistration;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * User: DIRK VRANCKAERT
 * Date: 05/02/11
 * Time: 17:31
 */
public class TimeRegistrationDaoImpl extends GenericDaoImpl<TimeRegistration, Integer> implements TimeRegistrationDao{
    private static final String LOG_TAG = TimeRegistrationDaoImpl.class.getSimpleName();

    @Inject
    public TimeRegistrationDaoImpl(final Context context) {
        super(TimeRegistration.class, context);
    }

    public int countTotalNumberOfTimeRegistrations() {
        int numRecs = 0;
        try {
            RawResults result = dao.queryForAllRaw("select count(*) from timeregistration");
            Log.d(LOG_TAG, result.getNumberColumns() + " number of columns found!");
            CloseableIterator<String[]> iterator = result.iterator();
            while(iterator.hasNext()) {
                String[] values = iterator.next();
                numRecs = Integer.parseInt(values[0]);
            }
        } catch (SQLException e) {
            throwFatalException(e);
        }
        return numRecs;
    }

    /**
     * {@inheritDoc}
     */
    public TimeRegistration getLatestTimeRegistration() {
        List<TimeRegistration> timeRegistrations = findAll();
        if(timeRegistrations.size() > 0) {
            Collections.sort(timeRegistrations, new TimeRegistrationDescendingByStartdate());
            return timeRegistrations.get(0);
        } else {
            return null;
        }
    }

    public List<TimeRegistration> findTimeRegistrationsForTask(Task task) {
        StatementBuilder<TimeRegistration,Integer> sb = dao.statementBuilder();
        try {
            sb.where().eq("taskId", task.getId());
            PreparedStmt<TimeRegistration> ps = sb.prepareStatement();
            return dao.query(ps);
        } catch (SQLException e) {
            Log.e(LOG_TAG, "Could not execute the query...");
            throwFatalException(e);
        }

        return null;
    }

    public List<TimeRegistration> findTimeRegistrationsForTaks(List<Task> tasks) {
        List<Integer> taskIds = new ArrayList<Integer>();
        for (Task task : tasks) {
            taskIds.add(task.getId());
        }

        StatementBuilder<TimeRegistration,Integer> sb = dao.statementBuilder();
        try {
            sb.where().in("taskId", taskIds);
            PreparedStmt<TimeRegistration> ps = sb.prepareStatement();
            return dao.query(ps);
        } catch (SQLException e) {
            Log.e(LOG_TAG, "Could not execute the query...");
            throwFatalException(e);
        }

        return null;
    }

    public List<TimeRegistration> getTimeRegistrations(Date startDate, Date endDate, List<Task> tasks) {
        List<Integer> taskIds = null;
        if (tasks != null && !tasks.isEmpty()) {
            Log.d(LOG_TAG, tasks.size() + " task(s) are taken into account while querying...");
            for (Task task : tasks) {
                if (taskIds == null) {
                    taskIds = new ArrayList<Integer>();
                }
                taskIds.add(task.getId());
            }
        }

        StatementBuilder<TimeRegistration,Integer> sb = dao.statementBuilder();


        if (taskIds != null && !taskIds.isEmpty()) {
            Log.d(LOG_TAG, "Updating where clause for dates and taskIds...");
            try {
                sb.where().ge("startTime", DatabaseHelper.convertDateToSqliteDate(startDate, false))
                    .and().le("endTime", DatabaseHelper.convertDateToSqliteDate(endDate, true))
                    .and().in("taskId", taskIds);
            } catch (SQLException e) {
                Log.e(LOG_TAG, "Could not build the dates- and tasks-where-clause in the query...");
                throwFatalException(e);
            }
        } else {
            Log.d(LOG_TAG, "Updating where clause for dates...");
            try {
                sb.where().ge("startTime", DatabaseHelper.convertDateToSqliteDate(startDate, false))
                    .and().le("endTime", DatabaseHelper.convertDateToSqliteDate(endDate, true));
            } catch (SQLException e) {
                Log.e(LOG_TAG, "Could not build the dates-where-clause in the query...");
                throwFatalException(e);
            }
        }

        try {
            Log.d(LOG_TAG, "About to execute query: " + sb.toString());
            PreparedStmt<TimeRegistration> ps = sb.prepareStatement();
            Log.d(LOG_TAG, "Prepared statement: " + ps.toString());
            return dao.query(ps);
        } catch (SQLException e) {
            Log.e(LOG_TAG, "Could not execute the query...");
            throwFatalException(e);
        }

        return null;
    }
}
