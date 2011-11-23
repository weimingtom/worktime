/*
 *  Copyright 2011 Dirk Vranckaert
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package eu.vranckaert.worktime.dao;

import eu.vranckaert.worktime.dao.generic.GenericDao;
import eu.vranckaert.worktime.model.Project;
import eu.vranckaert.worktime.model.Task;
import eu.vranckaert.worktime.model.TimeRegistration;

import java.util.Date;
import java.util.List;

/**
 * User: DIRK VRANCKAERT
 * Date: 05/02/11
 * Time: 17:31
 */
public interface TimeRegistrationDao extends GenericDao<TimeRegistration, Integer> {
    /**
     * Count the total number of time registrations available in the database.
     * @return The number of available time registrations.
     */
    int countTotalNumberOfTimeRegistrations();

    /**
     * Find the latest time registration. Returns <b>null</b> if no time regstrations are found!
     * @return The latest time registration.
     */
    TimeRegistration getLatestTimeRegistration();

    /**
     * Find all time registrations bound to one specific task.
     * @param task The {@link Task}.
     * @return The list of {@link TimeRegistration} for that task.
     */
    List<TimeRegistration> findTimeRegistrationsForTask(Task task);

    /**
     * Find all time registrations bound to one of the specified tasks.
     * @param tasks The list of {@link Task}.
     * @return The list of {@link TimeRegistration} for the specified tasks.
     */
    List<TimeRegistration> findTimeRegistrationsForTaks(List<Task> tasks);

        /**
     * Find all time registrations matching the given criteria.
     * @param startDate The starting date is the lower limit of the list of {@link TimeRegistration}. Every time
     * registration must have a starting date greater than or equals to this date. This is a required value.
     * @param endDate The end date is the higher limit of the list of {@link TimeRegistration}. Every time registration
     * must have an end date lower than or equals to this date. This is a required value.
     * @param tasks A list of tasks to which the time registrations have to be linked. If the list is null or empty the
     * parameter will be ignored.
     * @return A list of {@link TimeRegistration} instances based on the specified criteria.
     */
    List<TimeRegistration> getTimeRegistrations(Date startDate, Date endDate, List<Task> tasks);
}
