package eu.vranckaert.worktime.dao.impl;

import android.content.Context;
import android.util.Log;
import com.google.inject.Inject;
import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.RawResults;
import com.j256.ormlite.stmt.PreparedStmt;
import com.j256.ormlite.stmt.StatementBuilder;
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
    Context ctx;

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

        StatementBuilder<CommentHistory, Integer> sb = dao.statementBuilder();
        List<CommentHistory> comments = new ArrayList<CommentHistory>();
        try {
            sb.where().eq("comment", comment);
            PreparedStmt<CommentHistory> ps = sb.prepareStatement();
            comments = dao.query(ps);
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
     * Counts the number of records available in the database table corresponding to the entity {@link CommentHistory}.
     * @return The number of records available.
     */
    private int count() {
        Log.d(LOG_TAG, "Counting the number of comments in the history...");
        int numRecs = 0;
        try {
            RawResults result = dao.queryForAllRaw("select count(*) from commenthistory");
            Log.d(LOG_TAG, result.getNumberColumns() + " number of columns found!");
            CloseableIterator<String[]> iterator = result.iterator();
            while(iterator.hasNext()) {
                String[] values = iterator.next();
                numRecs = Integer.parseInt(values[0]);
            }
        } catch (SQLException e) {
            throwFatalException(e);
        }
        Log.d(LOG_TAG, numRecs + " comments found in the history");
        return numRecs;
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
        int numberOfCommentsAllowed = Preferences.getWidgetEndingTimeRegistrationCommentMaxHistoryStoragePreference(ctx);
        Log.d(LOG_TAG, "Maximum number of comments allowed in history: " + numberOfCommentsAllowed);
        StatementBuilder<CommentHistory, Integer> sb = dao.statementBuilder();
        List<CommentHistory> comments = new ArrayList<CommentHistory>();
        try {
            sb.orderBy("entranceDate", true);
            PreparedStmt<CommentHistory> ps = sb.prepareStatement();
            comments = dao.query(ps);
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
