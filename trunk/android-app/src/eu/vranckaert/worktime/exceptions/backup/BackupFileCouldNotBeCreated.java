package eu.vranckaert.worktime.exceptions.backup;

/**
 * User: DIRK VRANCKAERT
 * Date: 12/09/11
 * Time: 16:59
 */
public class BackupFileCouldNotBeCreated extends Exception {
    public BackupFileCouldNotBeCreated() {
        super();
    }

    public BackupFileCouldNotBeCreated(String message) {
        super(message);
    }

    public BackupFileCouldNotBeCreated(Exception e) {
        super(e);
    }

    public BackupFileCouldNotBeCreated(String message, Exception e) {
        super(message, e);
    }
}
