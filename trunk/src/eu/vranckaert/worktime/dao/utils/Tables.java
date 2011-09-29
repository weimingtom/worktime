package eu.vranckaert.worktime.dao.utils;

import eu.vranckaert.worktime.model.CommentHistory;
import eu.vranckaert.worktime.model.Project;
import eu.vranckaert.worktime.model.Task;
import eu.vranckaert.worktime.model.TimeRegistration;

/**
 * User: DIRK VRANCKAERT
 * Date: 05/02/11
 * Time: 16:35
 */
public enum Tables {
    TIMEREGISTRATION(TimeRegistration.class),
    PROJECT(Project.class),
    TASK(Task.class),
    COMMENT_HISTORY(CommentHistory.class);
    ;

    Tables(Class tableClass) {
        this.tableClass = tableClass;
    }

    private Class tableClass;

    public Class getTableClass() {
        return tableClass;
    }

    public void setTableClass(Class tableClass) {
        this.tableClass = tableClass;
    }
}
