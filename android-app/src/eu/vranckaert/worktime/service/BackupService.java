package eu.vranckaert.worktime.service;

import android.content.Context;
import android.os.Environment;
import eu.vranckaert.worktime.constants.Constants;
import eu.vranckaert.worktime.exceptions.SDCardUnavailableException;
import eu.vranckaert.worktime.exceptions.backup.BackupFileCouldNotBeCreated;
import eu.vranckaert.worktime.exceptions.backup.BackupFileCouldNotBeWritten;

import java.io.File;
import java.util.List;

/**
 * User: DIRK VRANCKAERT
 * Date: 12/09/11
 * Time: 16:26
 */
public interface BackupService {
    static final String BASE_FILE_NAME = "worktimedb-";
    static final String FILE_EXTENSION = ".bak";
    static final String APP_PACKAGE = "eu.vranckaert.worktime";
    static final String BACKUP_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() +
                File.separator +
                Constants.Export.BACKUP_DIRECTORY +
                File.separator;
    /**
     * Backup the entire database.
     * @param ctx The context.
     * @return The full path for the backup file.
     * @throws SDCardUnavailableException When the SD-card is not available or not writable.
     * @throws BackupFileCouldNotBeCreated When the backup file could not be created.
     * @throws BackupFileCouldNotBeWritten When the content of the backup file could not written.
     */
    String backup(Context ctx) throws SDCardUnavailableException, BackupFileCouldNotBeCreated, BackupFileCouldNotBeWritten;

    /**
     * Resotre the entire database.
     * @param ctx The context.
     * @param backupFile the backup file to restore.
     * @return {@link Boolean#TRUE} if the restore was successful. Otherwise {@link Boolean#FALSE}.
     * @throws SDCardUnavailableException When the SD-card is not available or not writable.
     */
    boolean restore(Context ctx, File backupFile) throws SDCardUnavailableException, BackupFileCouldNotBeWritten;

    /**
     * Find a list of all possible backup/restore files.
     * @param ctx The context.
     * @return A list of all possible backup/restore files.
     * @throws SDCardUnavailableException When the SD-card is not available or not writable.
     */
    List<File> getPossibleRestoreFiles(Context ctx) throws SDCardUnavailableException;
}
