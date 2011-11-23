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
package eu.vranckaert.worktime.service.impl;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import com.google.inject.Inject;
import eu.vranckaert.worktime.dao.ProjectDao;
import eu.vranckaert.worktime.dao.TaskDao;
import eu.vranckaert.worktime.dao.TimeRegistrationDao;
import eu.vranckaert.worktime.model.Project;
import eu.vranckaert.worktime.model.Task;
import eu.vranckaert.worktime.model.TimeRegistration;
import eu.vranckaert.worktime.service.TimeRegistrationService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User: DIRK VRANCKAERT
 * Date: 07/02/11
 * Time: 00:14
 */
public class TimeRegistrationServiceImpl implements TimeRegistrationService {
    private static final String LOG_TAG = TimeRegistrationServiceImpl.class.getSimpleName();

    @Inject
    TimeRegistrationDao dao;

    @Inject
    ProjectDao projectDao;

    @Inject
    TaskDao taskDao;

    /**
     * {@inheritDoc}
     */
    public List<TimeRegistration> findAll() {
        List<TimeRegistration> timeRegistrations = dao.findAll();
        for(TimeRegistration timeRegistration : timeRegistrations) {
            Log.d(LOG_TAG, "Found timeregistration with ID: " + timeRegistration.getId() + " and according task with ID: " + timeRegistration.getTask().getId());
            taskDao.refresh(timeRegistration.getTask());
            projectDao.refresh(timeRegistration.getTask().getProject());
        }
        return timeRegistrations;
    }

    /**
     * {@inheritDoc}
     */
    public List<TimeRegistration> getTimeRegistrationForTasks(List<Task> tasks) {
        return dao.findTimeRegistrationsForTaks(tasks);
    }

    /**
     * {@inheritDoc}
     */
    public List<TimeRegistration> getTimeRegistrations(Date startDate, Date endDate, Project project, Task task) {
        List<Task> tasks = new ArrayList<Task>();
        if (task != null) {
            Log.d(LOG_TAG, "Querying for 1 specific task!");
            tasks.add(task);
        } else if(project != null) {
            Log.d(LOG_TAG, "Querying for a specific project!");
            tasks = taskDao.findTasksForProject(project);
            Log.d(LOG_TAG, "Number of tasks found for that project: " + tasks.size());
        }

        return dao.getTimeRegistrations(startDate, endDate, tasks);
    }

    /**
     * {@inheritDoc}
     */
    public void create(TimeRegistration timeRegistration) {
        dao.save(timeRegistration);
    }

    /**
     * {@inheritDoc}
     */
    public void update(TimeRegistration timeRegistration) {
        dao.update(timeRegistration);
    }

    /**
     * {@inheritDoc}
     */
    public TimeRegistration getLatestTimeRegistration() {
        return dao.getLatestTimeRegistration();
    }

    /**
     * {@inheritDoc}
     */
    public void remove(TimeRegistration timeRegistration) {
        dao.delete(timeRegistration);
    }

    /**
     * {@inheritDoc}
     */
    public TimeRegistration get(Integer id) {
        return dao.findById(id);
    }
}
