package eu.vranckaert.worktime.comparators;

import eu.vranckaert.worktime.model.Task;

import java.util.Comparator;

/**
 * User: DIRK VRANCKAERT
 * Date: 30/03/11
 * Time: 19:11
 */
public class TaskByNameComparator implements Comparator<Task> {
    public int compare(Task task1, Task task2) {
        return task1.getName().compareTo(task2.getName());
    }
}
