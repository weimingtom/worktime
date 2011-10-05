package eu.vranckaert.worktime.dao.impl;

import android.content.Context;
import android.util.Log;
import com.google.inject.Inject;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import eu.vranckaert.worktime.dao.TaskDao;
import eu.vranckaert.worktime.dao.generic.GenericDaoImpl;
import eu.vranckaert.worktime.model.Project;
import eu.vranckaert.worktime.model.Task;

import java.sql.SQLException;
import java.util.List;

public class TaskDaoImpl extends GenericDaoImpl<Task, Integer> implements TaskDao {
    private static final String LOG_TAG = TaskDaoImpl.class.getSimpleName();

    @Inject
    public TaskDaoImpl(final Context context) {
        super(Task.class, context);
    }

    /**
     * {@inheritDoc}
     */
    public List<Task> findTasksForProject(Project project) {
        QueryBuilder<Task, Integer> qb = dao.queryBuilder();
        try {
            qb.where().eq("projectId", project.getId());
            PreparedQuery<Task> pq = qb.prepare();
            return dao.query(pq);
        } catch (SQLException e) {
            Log.e(LOG_TAG, "Could not execute the query... Returning null.", e);
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    public List<Task> findNotFinishedTasksForProject(Project project) {
        QueryBuilder<Task, Integer> qb = dao.queryBuilder();
        try {
            qb.where().eq("projectId", project.getId())
                    .and().eq("finished", false);
            PreparedQuery<Task> pq = qb.prepare();
            return dao.query(pq);
        } catch (SQLException e) {
            Log.e(LOG_TAG, "Could not execute the query... Returning null.", e);
            return null;
        }
    }
}
