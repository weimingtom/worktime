package eu.vranckaert.worktime.utils.string;

import java.util.List;

/**
 * User: DIRK VRANCKAERT
 * Date: 19/02/11
 * Time: 18:32
 */
public class StringUtils {
    /**
     * Converts a list of {@link String} objects to an array.
     * @param strings The list to convert.
     * @return The concerted array containing the original {@link String} objects in the same order.
     */
    public static String[] convertListToArray(final List<String> strings) {
        String[] stringArray = new String[strings.size()];
        for(int i = 0; i<strings.size(); i++) {
            stringArray[i] = strings.get(i);
        }
        return stringArray;
    }

    /**
     * Checks if a provided {@link String} instance is null or has a zero-length.
     * @param string The string to check.
     * @return {@link Boolean#TRUE} if the provided string is null or has length zero.
     */
    public static boolean isBlank(final String string) {
        if(string == null || string.trim().length() == 0) {
            return true;
        }
        return false;
    }

    /**
     * Checks if a provided {@link String} instance is NOT null or has length greater than zero.
     * @param string The string to check.
     * @return {@link Boolean#TRUE} if the provided string is NOT null or has NOT length zero.
     */
    public static boolean isNotBlank(final String string) {
        return !isBlank(string);
    }

    /**
     * Optimizes a string to be saved in the database. The string gets trimmed at begin and end.
     * @param string The string to handle.
     * @return The optimized string.
     */
    public static String optimizeString(final String string) {
        return string.trim();
    }
}
