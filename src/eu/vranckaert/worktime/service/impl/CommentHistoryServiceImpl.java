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
import com.google.inject.Inject;
import eu.vranckaert.worktime.dao.CommentHistoryDao;
import eu.vranckaert.worktime.model.CommentHistory;
import eu.vranckaert.worktime.service.CommentHistoryService;
import eu.vranckaert.worktime.utils.preferences.Preferences;

import java.util.ArrayList;
import java.util.List;

/**
 * User: DIRK VRANCKAERT
 * Date: 26/04/11
 * Time: 18:34
 */
public class CommentHistoryServiceImpl implements CommentHistoryService {
    @Inject
    private CommentHistoryDao dao;

    /**
     * {@inheritDoc}
     */
    public List<String> getAll() {
        List<CommentHistory> commentHistories = dao.findAll();

        List<String> commentStrings = new ArrayList<String>();
        for (CommentHistory commentHistory : commentHistories) {
            commentStrings.add(commentHistory.getComment());
        }
        return commentStrings;
    }

    /**
     * {@inheritDoc}
     */
    public void saveComment(String comment) {
        dao.save(comment);
    }

    /**
     * {@inheritDoc}
     */
    public void deleteAll() {
        dao.deleteAll();
    }

    /**
     * {@inheritDoc}
     */
    public void checkNumberOfCommentsStored() {
        dao.checkNumberOfCommentsStored();
    }
}
