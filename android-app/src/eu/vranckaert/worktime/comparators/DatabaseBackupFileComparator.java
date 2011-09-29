package eu.vranckaert.worktime.comparators;

import java.io.File;
import java.util.Comparator;

/**
 * User: DIRK VRANCKAERT
 * Date: 13/09/11
 * Time: 18:17
 */
public class DatabaseBackupFileComparator implements Comparator<File> {
    public int compare(File file, File file1) {
        return new Long(file1.lastModified()).compareTo(new Long(file.lastModified()));
    }
}
