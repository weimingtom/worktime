package eu.vranckaert.worktime.exceptions.backup;

/**
 * User: DIRK VRANCKAERT
 * Date: 12/09/11
 * Time: 16:59
 */
public class BackupFileCouldNotBeWritten extends Exception {
    public BackupFileCouldNotBeWritten() {
        super();
    }

    public BackupFileCouldNotBeWritten(String message) {
        super(message);
    }

    public BackupFileCouldNotBeWritten(Exception e) {
        super(e);
    }

    public BackupFileCouldNotBeWritten(String message, Exception e) {
        super(message, e);
    }
}
