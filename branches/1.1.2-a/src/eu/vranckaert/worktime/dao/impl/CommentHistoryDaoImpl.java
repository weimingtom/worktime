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
package eu.vranckaert.worktime.dao.impl;

import android.content.Context;
import android.util.Log;
import com.google.inject.Inject;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import eu.vranckaert.worktime.dao.CommentHistoryDao;
import eu.vranckaert.worktime.dao.generic.GenericDaoImpl;
import eu.vranckaert.worktime.model.CommentHistory;
import eu.vranckaert.worktime.utils.preferences.Preferences;
import eu.vranckaert.worktime.utils.string.StringUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * User: DIRK VRANCKAERT
 * Date: 26/04/11
 * Time: 18:30
 */
public class CommentHistoryDaoImpl extends GenericDaoImpl<CommentHistory, Integer> implements CommentHistoryDao {
    private static final String LOG_TAG = CommentHistoryDaoImpl.class.getSimpleName();

    @Inject
    public CommentHistoryDaoImpl(final Context context) {
        super(CommentHistory.class, context);
    }

    /**
     *
     * {@inheritDoc}
     */
    public void save(final String comment) {
        Log.d(LOG_TAG, "About to save a new comment: " + comment);
        String optimizedComment = StringUtils.optimizeString(comment);

        QueryBuilder<CommentHistory, Integer> qb = dao.queryBuilder();
        List<CommentHistory> comments = new ArrayList<CommentHistory>();
        try {
            qb.where().eq("comment", comment);
            PreparedQuery<CommentHistory> pq = qb.prepare();
            comments = dao.query(pq);
            Log.d(LOG_TAG, "Did we found the same comment already? " + (comments.size()>0));
        } catch (SQLException e) {
            Log.e(LOG_TAG, "Could not execute the query...", e);
        }

        if (comments == null || comments.size() == 0) {
            Log.d(LOG_TAG, "We did not find the comment in the DB. Creating a new one right now...");
            this.save(new CommentHistory(comment));
            Log.d(LOG_TAG, "Executing check after save...");
            checkNumberOfCommentsStored();
        }
    }

    /**
     *
     * {@inheritDoc}
     */
    public void deleteAll() {
        Log.d(LOG_TAG, "Ready to delete all the items in the comment history");
        try {
            List<CommentHistory> comments = findAll();
            Log.d(LOG_TAG, "Number of comments found to delete: " + comments.size());
            List<Integer> ids = new ArrayList<Integer>();
            for (CommentHistory comment : comments) {
                ids.add(comment.getId());
            }
            if (ids.size() > 0) {
                dao.deleteIds(ids);
            }
        } catch (SQLException e) {
            Log.d(LOG_TAG, "Could not execute the query... Returning false");
            return;
        }
        Log.d(LOG_TAG, "All comments are deleted!");
    }

    /**
     * {@inheritDoc}
     */
    public void checkNumberOfCommentsStored() {
        Log.d(LOG_TAG, "Checking the preference-rule for the maximum number of comments to be stored...");
        int numberOfCommentsAllowed = Preferences.getWidgetEndingTimeRegistrationCommentMaxHistoryStoragePreference(getContext());
        Log.d(LOG_TAG, "Maximum number of comments allowed in history: " + numberOfCommentsAllowed);
        QueryBuilder<CommentHistory, Integer> qb = dao.queryBuilder();
        List<CommentHistory> comments = new ArrayList<CommentHistory>();
        try {
            qb.orderBy("entranceDate", true);
            PreparedQuery<CommentHistory> pq = qb.prepare();
            comments = dao.query(pq);
            Log.d(LOG_TAG, "Number of comments found in the history: " + comments.size());
        } catch (SQLException e) {
            Log.e(LOG_TAG, "Could not execute the query...", e);
        }
        int numberOfRecordsToDelete = 0;

        if (comments.size() > numberOfCommentsAllowed) {
            Log.d(LOG_TAG, "Too many comments available in the database");
            numberOfRecordsToDelete = comments.size() - numberOfCommentsAllowed;
            Log.d(LOG_TAG, numberOfRecordsToDelete + " comments should be deleted!");
        }

        for (int i = 0; i<numberOfRecordsToDelete; i++) {
            CommentHistory comment = comments.get(i);
            Log.d(LOG_TAG, "Comment with id " + comment.getId() + " will be deleted. The text for the comment was: " + comment.getComment());
            delete(comment);
        }
    }
}
