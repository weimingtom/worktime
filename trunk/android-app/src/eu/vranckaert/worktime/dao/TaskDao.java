package eu.vranckaert.worktime.dao;

import eu.vranckaert.worktime.dao.generic.GenericDao;
import eu.vranckaert.worktime.model.Project;
import eu.vranckaert.worktime.model.Task;

import java.util.List;

/**
 * User: DIRK VRANCKAERT
 * Date: 05/02/11
 * Time: 16:56
 */
public interface TaskDao extends GenericDao<Task, Integer> {
    /**
     * Find all tasks linked to a certain {@link Project}.
     * @param project The project.
     * @return A list of {@link Task} instances linked to the {@link Project}.
     */
    List<Task> findTasksForProject(Project project);

    /**
     * Find all tasks linked to a certain {@link Project} for which the flag {@link Task#finished} is
     * {@link Boolean#FALSE}.
     * @param project The project.
     * @return A list of {@link Task} instances linked to the {@link Project} and with the flag {@link Task#finished}
     * set to {@link Boolean#FALSE}.
     */
    List<Task> findNotFinishedTasksForProject(Project project);
}
