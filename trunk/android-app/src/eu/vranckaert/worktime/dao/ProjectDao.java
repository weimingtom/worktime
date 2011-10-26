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

/**
 * User: DIRK VRANCKAERT
 * Date: 05/02/11
 * Time: 16:56
 */
public interface ProjectDao extends GenericDao<Project, Integer> {
    /**
     * Checks if a certain name for a project is already in use.
     *
     * @param projectName The name of the project to check for.
     * @return {@link Boolean#TRUE} if a project with this name already exists, {@link Boolean#FALSE} if not.
     */
    boolean isNameAlreadyUsed(String projectName);

    /**
     * Count the total number of projects available in the database.
     * @return The number of available projects.
     */
    int countTotalNumberOfProjects();

    /**
     * Find the default {@link Project}.
     * @return The default {@link Project}.
     */
    Project findDefaultProject();
}
