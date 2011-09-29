package eu.vranckaert.worktime.guice;

import android.util.Log;
import eu.vranckaert.worktime.dao.CommentHistoryDao;
import eu.vranckaert.worktime.dao.ProjectDao;
import eu.vranckaert.worktime.dao.TaskDao;
import eu.vranckaert.worktime.dao.TimeRegistrationDao;
import eu.vranckaert.worktime.dao.impl.CommentHistoryDaoImpl;
import eu.vranckaert.worktime.dao.impl.ProjectDaoImpl;
import eu.vranckaert.worktime.dao.impl.TaskDaoImpl;
import eu.vranckaert.worktime.dao.impl.TimeRegistrationDaoImpl;
import eu.vranckaert.worktime.service.*;
import eu.vranckaert.worktime.service.impl.*;
import roboguice.config.AbstractAndroidModule;

public class Module extends AbstractAndroidModule {
    private static final String LOG_TAG = Module.class.getSimpleName();

    @Override
    protected void configure() {
        Log.i(LOG_TAG, "Configuring module " + getClass().getSimpleName());

        bindDaos();
        bindServices();

        Log.i(LOG_TAG, "DAO's and services are now bound!");
    }

    private void bindDaos() {
        bind(TimeRegistrationDao.class).to(TimeRegistrationDaoImpl.class);
        bind(ProjectDao.class).to(ProjectDaoImpl.class);
        bind(TaskDao.class).to(TaskDaoImpl.class);
        bind(CommentHistoryDao.class).to(CommentHistoryDaoImpl.class);
    }

    private void bindServices() {
        bind(DevelopmentService.class).to(DevelopmentServiceImpl.class);
        bind(ProjectService.class).to(ProjectServiceImpl.class);
        bind(TimeRegistrationService.class).to(TimeRegistrationServiceImpl.class);
        bind(TaskService.class).to(TaskServiceImpl.class);
        bind(CommentHistoryService.class).to(CommentHistoryServiceImpl.class);
        bind(BackupService.class).to(DatabaseFileBackupServiceImpl.class);
        //Widget service
        bind(WidgetService.class).to(WidgetServiceImpl.class);
    }
}
