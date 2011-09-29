package eu.vranckaert.worktime.exceptions;

/**
 * User: DIRK VRANCKAERT
 * Date: 30/03/11
 * Time: 19:31
 */
public class TaskStillInUseException extends Exception {
    public TaskStillInUseException(String message) {
        super(message);
    }
}
