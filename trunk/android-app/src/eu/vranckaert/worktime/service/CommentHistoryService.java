package eu.vranckaert.worktime.service;

import android.content.Context;

import java.util.List;

/**
 * User: DIRK VRANCKAERT
 * Date: 26/04/11
 * Time: 18:32
 */
public interface CommentHistoryService {
    /**
     * Get all comments available in the system.
     * @return The list of {@link String} instances.
     */
    List<String> getAll();

    /**
     * Save a comment in the comment history.
     * @param comment The comment to save.
     */
    void saveComment(String comment);

    /**
     * Delete the entire comment history.
     */
    void deleteAll();

    /**
     * Check the number of comments stored in the {@link eu.vranckaert.worktime.model.CommentHistory}.
     */
    void checkNumberOfCommentsStored();
}
