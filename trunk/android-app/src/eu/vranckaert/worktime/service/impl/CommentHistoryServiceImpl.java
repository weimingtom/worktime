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
