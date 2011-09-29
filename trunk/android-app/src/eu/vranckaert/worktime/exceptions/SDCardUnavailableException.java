package eu.vranckaert.worktime.exceptions;

/**
 * This exception is used both for when the SD card is unavailable and when the SD card is not writable.
 * User: DIRK VRANCKAERT
 * Date: 12/09/11
 * Time: 16:36
 */
public class SDCardUnavailableException extends Exception {
    public SDCardUnavailableException() {
        super();
    }

    public SDCardUnavailableException(String message) {
        super(message);
    }
}
