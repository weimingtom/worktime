package eu.vranckaert.worktime.service;

import android.content.Context;
import eu.vranckaert.worktime.enums.export.ExportType;
import eu.vranckaert.worktime.model.Project;
import eu.vranckaert.worktime.model.Task;
import eu.vranckaert.worktime.model.TimeRegistration;

import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * User: DIRK VRANCKAERT
 * Date: 07/02/11
 * Time: 00:13
 */
public interface TimeRegistrationService {
    /**
     * Find all persisted time registrations.
     * @return All time registrations.
     */
    List<TimeRegistration> findAll();

    /**
     * Find all time registrations related to a list of tasks.
     * @param tasks The list of {@link Task} to which the time registrations has to be related to.
     * @return A list of {@link TimeRegistration} instances found for the specified list of tasks.
     */
    List<TimeRegistration> getTimeRegistrationForTasks(List<Task> tasks);

    /**
     * Find all time registrations matching the given criteria.
     * @param startDate The starting date is the lower limit of the list of {@link TimeRegistration}. Every time
     * registration must have a starting date greater than or equals to this date. This is a required value.
     * @param endDate The end date is the higher limit of the list of {@link TimeRegistration}. Every time registration
     * must have an end date lower than or equals to this date. This is a required value.
     * @param project The project to which a time registration must be linked. If this parameter is null it will be
     * ignored. If the task-parameter is not null the project will be ignored as the specified task is supposed to be
     * linked to this project.
     * @param task The task to which a time registration must be linked. If this parameter is null it will be ignored.
     * If it's not null the project-parameter will be ignored as the task is always supposed to be linked to the
     * specified project.
     * @return A list of {@link TimeRegistration} instances based on the specified criteria.
     */
    List<TimeRegistration> getTimeRegistrations(Date startDate, Date endDate, Project project, Task task);

    /**
     * Create a new instance of {@link TimeRegistration}.
     * @param timeRegistration The instance to create.
     */
    void create(TimeRegistration timeRegistration);

    /**
     * Update a time registration instance.
     * @param timeRegistration The instance to update.
     */
    void update(TimeRegistration timeRegistration);

    /**
     * Find the latest time registration. Returns <b>null</b> if no time regstrations are found!
     * @return The latest time registration.
     */
    TimeRegistration getLatestTimeRegistration();

    /**
     * Export all time registrations.
     * @param exportType The type of export.
     * @param ctx The context.
     * @return The exported file.
     */
    File export(final ExportType exportType, final Context ctx);

    /**
     * Removes an existing timeregistration.
     *
     * @param timeRegistration The registration to remove.
     */
    void remove(TimeRegistration timeRegistration);

    /**
     * Find a specific time registration.
     * @param id The unique identifier of the time registration.
     * @return Null if nothing found for the identifier, otherwise the time registration.
     */
    TimeRegistration get(Integer id);
}
