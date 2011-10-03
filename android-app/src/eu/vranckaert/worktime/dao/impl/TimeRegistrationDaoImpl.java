package eu.vranckaert.worktime.dao.impl;

import android.content.Context;
import android.util.Log;
import com.google.inject.Inject;
import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.stmt.*;
import eu.vranckaert.worktime.comparators.TimeRegistrationDescendingByStartdate;
import eu.vranckaert.worktime.dao.TimeRegistrationDao;
import eu.vranckaert.worktime.dao.generic.GenericDaoImpl;
import eu.vranckaert.worktime.dao.utils.DatabaseHelper;
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
        int rowCount = 0;
        List<String[]> results = null;
        try {
            GenericRawResults rawResults = dao.queryRaw("select count(*) from timeregistration");
            results = rawResults.getResults();
        } catch (SQLException e) {
            throwFatalException(e);
        }

        if (results != null && results.size() > 0) {
            rowCount = Integer.parseInt(results.get(0)[0]);
        }

        Log.d(LOG_TAG, "Rowcount: " + rowCount);

        return rowCount;
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
        QueryBuilder<TimeRegistration,Integer> qb = dao.queryBuilder();
        try {
            qb.where().eq("taskId", task.getId());
            PreparedQuery<TimeRegistration> pq = qb.prepare();
            return dao.query(pq);
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

        QueryBuilder<TimeRegistration,Integer> qb = dao.queryBuilder();
        try {
            qb.where().in("taskId", taskIds);
            PreparedQuery<TimeRegistration> pq = qb.prepare();
            return dao.query(pq);
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

        QueryBuilder<TimeRegistration,Integer> qb = dao.queryBuilder();


        endDate = DatabaseHelper.convertDateToSqliteDate(endDate, true);
        startDate = DatabaseHelper.convertDateToSqliteDate(startDate, false);
        boolean includeOngoingTimeRegistration = false;
        Date now = new Date();
        if (endDate.after(now)) {
            Log.e(LOG_TAG, "Ongoing time registration should be included in reporting result...");
            includeOngoingTimeRegistration = true;
        }

        Where where = qb.where();
        try {
            where.ge("startTime", startDate);
            if (includeOngoingTimeRegistration) {
                Where orClause = where.le("endTime", endDate).or().isNull("endTime");
                where.and(where, orClause);
            } else {
                where.and().le("endTime", endDate);
            }
            if (taskIds != null && !taskIds.isEmpty()) {
                where.and().in("taskId", taskIds);
            }
        } catch (SQLException e) {
            Log.e(LOG_TAG, "Could not build the dates- and tasks-where-clause in the query...");
            throwFatalException(e);
        }
        qb.setWhere(where);

        try {
            PreparedQuery<TimeRegistration> pq = qb.prepare();
            Log.d(LOG_TAG, "Prepared query: " + pq.toString());
            return dao.query(pq);
        } catch (SQLException e) {
            Log.e(LOG_TAG, "Could not execute the query...");
            throwFatalException(e);
        }

        return null;
    }
}
