package eu.vranckaert.worktime.constants;

/**
 * User: DIRK VRANCKAERT
 * Date: 17/08/11
 * Time: 17:33
 */
public class TrackerConstants {
    public class PageView {
        public static final String ABOUT_ACTIVITY = "aboutActivity";
        public static final String HOME_ACTIVITY = "homeActivity";
        public static final String MANAGE_PROJECTS_ACTIVITY = "manageProjectsActivity";
        public static final String PREFERENCES_ACTIVITY = "preferencesActivity";
        public static final String TIME_REGISTRATIONS_ACTIVITY = "timeRegistrationsActivity";
        public static final String PROJECTS_DETAILS_ACTIVITY = "projectsDetailsActivity";
        public static final String REGISTRATIONS_DETAILS_ACTIVITY = "registrationsDetailsActivity";
        public static final String ADD_EDIT_PROJECT_ACTIVITY = "addEditProjectActivity";
        public static final String ADD_EDIT_TASK_ACTIVITY = "addEditTaskActivity";
        public static final String REPORTING_CRITERIA_ACTIVITY = "reportingCriteriaActivity";
    }

    public class EventSources {
        public static final String PROJECT_DETAILS_ACTIVITY = "projectDetailsActivity";
        public static final String MANAGE_PROJECTS_ACTIVITY = "manageProjectsActivity";
        public static final String STOP_TIME_REGISTRATION_ACTIVITY = "stopTimeRegistrationActivity";
        public static final String START_TIME_REGISTRATION_ACTIVITY = "startTimeRegistrationActivity";
        public static final String ADD_EDIT_PROJECT_ACTIVITY = "addEditProjectActivity";
        public static final String REGISTRATION_DETAILS_ACTIVITY = "registrationDetailsActivity";
        public static final String TIME_REGISTRATIONS_ACTIVITY = "timeRegistrationsActivity";
        public static final String ADD_EDIT_TASK_ACTIVITY = "addEditTaskActivity";
        public static final String REPORTING_CRITERIA_SELECT_END_DATE = "reportingCriteriaActivity";
    }

    public class EventActions {
        public static final String ADD_PROJECT = "addProject";
        public static final String EDIT_PROJECT = "editProject";
        public static final String DELETE_PROJECT = "deleteProject";
        public static final String ADD_TASK = "addTask";
        public static final String EDIT_TASK = "editTask";
        public static final String DELETE_TASK = "deleteTask";
        public static final String MARK_TASK_FINISHED = "markTaskFinished";
        public static final String MARK_TASK_UNFINISHED = "markTaskUnfinished";
        public static final String START_TIME_REGISTRATION = "startTimeRegistration";
        public static final String END_TIME_REGISTRATION = "endTimeRegistration";
        public static final String DELETE_TIME_REGISTRATION = "deleteTimeRegistration";
        public static final String ADD_TR_COMMENT = "addTrComment";
        public static final String EDIT_TR_COMMENT = "editTrComment";
        public static final String EDIT_TR_END_TIME = "editTrEndTime";
        public static final String EDIT_TR_START_TIME = "editTrStartTime";
        public static final String EDIT_TR_PROJECT_AND_TASK = "editTrProjectAndTask";
        public static final String RESTART_TIME_REGISTRATION = "restartTimeRegistration";
        public static final String START_REPORTING = "startReporting";
    }
}
