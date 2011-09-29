package eu.vranckaert.worktime.dao;

import eu.vranckaert.worktime.dao.generic.GenericDao;
import eu.vranckaert.worktime.model.CommentHistory;

/**
 * User: DIRK VRANCKAERT
 * Date: 26/04/11
 * Time: 18:30
 */
public interface CommentHistoryDao extends GenericDao<CommentHistory, Integer> {
    /**
     * Save a comment as a {@link CommentHistory}.
     * @param comment The comment to save.
     */
    void save(String comment);

    /**
     * Delete the entire comment history.
     */
    void deleteAll();

    /**
     * Check the number of comments stored in the {@link eu.vranckaert.worktime.model.CommentHistory}.
     */
    void checkNumberOfCommentsStored();
}
