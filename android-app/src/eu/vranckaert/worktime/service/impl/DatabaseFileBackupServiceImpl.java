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
import eu.vranckaert.worktime.dao.utils.DaoConstants;
import eu.vranckaert.worktime.exceptions.SDCardUnavailableException;
import eu.vranckaert.worktime.exceptions.backup.BackupFileCouldNotBeCreated;
import eu.vranckaert.worktime.exceptions.backup.BackupFileCouldNotBeWritten;
import eu.vranckaert.worktime.service.BackupService;
import eu.vranckaert.worktime.utils.context.ContextUtils;
import eu.vranckaert.worktime.utils.date.DateUtils;
import eu.vranckaert.worktime.utils.file.FileUtil;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * User: DIRK VRANCKAERT
 * Date: 12/09/11
 * Time: 16:31
 */
public class DatabaseFileBackupServiceImpl implements BackupService {
    private static final String LOG_TAG = DatabaseFileBackupServiceImpl.class.getSimpleName();

    public String backup(Context ctx) throws SDCardUnavailableException, BackupFileCouldNotBeCreated, BackupFileCouldNotBeWritten {
        if (!ContextUtils.isSdCardAvailable() || !ContextUtils.isSdCardWritable()) {
            throw new SDCardUnavailableException("Make sure the SD-card is in the device and the SD-card is mounted.");
        }

        String fileName = BASE_FILE_NAME + DateUtils.DateTimeConverter.getUniqueTimestampString() + FILE_EXTENSION;

        File folder = new File(BACKUP_PATH);
        if (folder.isFile()) {
            Log.d(LOG_TAG, "Directory seems to be a file... Deleting it now...");
            folder.delete();
            if (folder.isFile()) {
                Log.d(LOG_TAG, "Directory still seems to be a file... hmmm... very strange... :\\");
            }
        }
        if (!folder.exists()) {
            Log.d(LOG_TAG, "Directory does not exist yet! Creating it now!");
            folder.mkdir();
            if (!folder.exists()) {
                Log.d(LOG_TAG, "The directory still does not exist!");
            }
        }

        File backupFile = new File(BACKUP_PATH + fileName);
        try {
            backupFile.createNewFile();
        } catch (IOException e) {
            throw new BackupFileCouldNotBeCreated(e);
        }
        File dbFile = new File(Environment.getDataDirectory() + "/data/" + APP_PACKAGE + "/databases/" + DaoConstants.DATABASE);

        try {
            FileUtil.copyFile(dbFile, backupFile);
        } catch (IOException e) {
            throw new BackupFileCouldNotBeWritten(e);
        }

        return backupFile.getAbsolutePath();
    }

    public boolean restore(Context ctx, File backupFile) throws SDCardUnavailableException, BackupFileCouldNotBeWritten {
        if (!ContextUtils.isSdCardAvailable() || !ContextUtils.isSdCardWritable()) {
            throw new SDCardUnavailableException("Make sure the SD-card is in the device and the SD-card is mounted.");
        }

        File dbFile = new File(Environment.getDataDirectory() + "/data/" + APP_PACKAGE + "/databases/" + DaoConstants.DATABASE);
        if (!dbFile.exists()) {
            try {
                dbFile.createNewFile();
                Log.d(LOG_TAG, "Need to create the DB file on the device first...");
            } catch (IOException e) {}
        }

        try {
            FileUtil.copyFile(backupFile, dbFile);
        } catch (IOException e) {
            throw new BackupFileCouldNotBeWritten(e);
        }

        return false;
    }

    public List<File> getPossibleRestoreFiles(Context ctx) throws SDCardUnavailableException {
        if (!ContextUtils.isSdCardAvailable() || !ContextUtils.isSdCardWritable()) {
            throw new SDCardUnavailableException("Make sure the SD-card is in the device and the SD-card is mounted.");
        }

        File backupDirectory = new File(BACKUP_PATH);

        if (!backupDirectory.exists()) {
            return null;
        }

        FilenameFilter databaseBackupFilenameFilter = new FilenameFilter() {
            public boolean accept(File dir, String fileName) {
                if (fileName.startsWith(BASE_FILE_NAME) && fileName.endsWith(FILE_EXTENSION)) {
                    return true;
                }
                return false;
            }
        };
        File[] databaseBackupFiles = backupDirectory.listFiles(databaseBackupFilenameFilter);

        return Arrays.asList(databaseBackupFiles);
    }
}
