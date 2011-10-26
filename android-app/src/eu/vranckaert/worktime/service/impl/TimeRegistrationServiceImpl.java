package eu.vranckaert.worktime.service.impl;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import com.google.inject.Inject;
import eu.vranckaert.worktime.R;
import eu.vranckaert.worktime.comparators.timeregistration.TimeRegistrationDescendingByStartdate;
import eu.vranckaert.worktime.constants.Constants;
import eu.vranckaert.worktime.constants.TextConstants;
import eu.vranckaert.worktime.dao.ProjectDao;
import eu.vranckaert.worktime.dao.TaskDao;
import eu.vranckaert.worktime.dao.TimeRegistrationDao;
import eu.vranckaert.worktime.enums.export.CsvSeparator;
import eu.vranckaert.worktime.enums.export.ExportType;
import eu.vranckaert.worktime.enums.export.FileType;
import eu.vranckaert.worktime.model.Project;
import eu.vranckaert.worktime.model.Task;
import eu.vranckaert.worktime.model.TimeRegistration;
import eu.vranckaert.worktime.service.TimeRegistrationService;
import eu.vranckaert.worktime.utils.date.DateFormat;
import eu.vranckaert.worktime.utils.date.DateUtils;
import eu.vranckaert.worktime.utils.date.TimeFormat;
import eu.vranckaert.worktime.utils.preferences.Preferences;
import eu.vranckaert.worktime.utils.string.StringUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * User: DIRK VRANCKAERT
 * Date: 07/02/11
 * Time: 00:14
 */
public class TimeRegistrationServiceImpl implements TimeRegistrationService {
    private static final String LOG_TAG = TimeRegistrationServiceImpl.class.getSimpleName();

    @Inject
    TimeRegistrationDao dao;

    @Inject
    ProjectDao projectDao;

    @Inject
    TaskDao taskDao;

    /**
     * {@inheritDoc}
     */
    public List<TimeRegistration> findAll() {
        List<TimeRegistration> timeRegistrations = dao.findAll();
        for(TimeRegistration timeRegistration : timeRegistrations) {
            Log.d(LOG_TAG, "Found timeregistration with ID: " + timeRegistration.getId() + " and according task with ID: " + timeRegistration.getTask().getId());
            taskDao.refresh(timeRegistration.getTask());
            projectDao.refresh(timeRegistration.getTask().getProject());
        }
        return timeRegistrations;
    }

    /**
     * {@inheritDoc}
     */
    public List<TimeRegistration> getTimeRegistrationForTasks(List<Task> tasks) {
        return dao.findTimeRegistrationsForTaks(tasks);
    }

    /**
     * {@inheritDoc}
     */
    public List<TimeRegistration> getTimeRegistrations(Date startDate, Date endDate, Project project, Task task) {
        List<Task> tasks = new ArrayList<Task>();
        if (task != null) {
            Log.d(LOG_TAG, "Querying for 1 specific task!");
            tasks.add(task);
        } else if(project != null) {
            Log.d(LOG_TAG, "Querying for a specific project!");
            tasks = taskDao.findTasksForProject(project);
            Log.d(LOG_TAG, "Number of tasks found for that project: " + tasks.size());
        }

        return dao.getTimeRegistrations(startDate, endDate, tasks);
    }

    /**
     * {@inheritDoc}
     */
    public void create(TimeRegistration timeRegistration) {
        dao.save(timeRegistration);
    }

    /**
     * {@inheritDoc}
     */
    public void update(TimeRegistration timeRegistration) {
        dao.update(timeRegistration);
    }

    /**
     * {@inheritDoc}
     */
    public TimeRegistration getLatestTimeRegistration() {
        return dao.getLatestTimeRegistration();
    }

    /**
     * {@inheritDoc}
     */
    public File export(final ExportType exportType, final Context ctx) {
        String fileName = Preferences.getTimeRegistrationExportFileName(ctx);
        FileType fileType = Preferences.getTimeRegistrationExportFileType(ctx);
        CsvSeparator csvSeperator = Preferences.getTrimeRegistrationCsvSeparator(ctx);

        List<TimeRegistration> timeRegistrations = findAll();
        Collections.sort(timeRegistrations, new TimeRegistrationDescendingByStartdate());

        StringBuilder export = new StringBuilder();

        boolean isFirstLine = true;
        for (TimeRegistration timeRegistration : timeRegistrations) {
            String startDate = DateUtils.convertDateToString(timeRegistration.getStartTime(), DateFormat.SHORT, ctx);
            String startTime = DateUtils.convertTimeToString(timeRegistration.getStartTime(), TimeFormat.MEDIUM, ctx);
            String endDate = null;
            String endTime = null;
            if(timeRegistration.getEndTime() != null) {
                endDate = DateUtils.convertDateToString(timeRegistration.getEndTime(), DateFormat.SHORT, ctx);
                endTime = DateUtils.convertTimeToString(timeRegistration.getEndTime(), TimeFormat.MEDIUM, ctx);
            } else {
                endDate = ctx.getString(R.string.now);
                endTime = "";
            }

            if (!isFirstLine) {
                export.append(TextConstants.NEW_LINE);
            }

            switch (fileType) {
                case COMMA_SERPERATED_VALUES: {
                    char separator = csvSeperator.getSeperator();

                    if (isFirstLine) {
                        /*Add the column headers*/
                        export.append(ctx.getString(R.string.lbl_registrations_export_file_csv_startdate));
                        export.append(separator);
                        export.append(ctx.getString(R.string.lbl_registrations_export_file_csv_starttime));
                        export.append(separator);
                        export.append(ctx.getString(R.string.lbl_registrations_export_file_csv_enddate));
                        export.append(separator);
                        export.append(ctx.getString(R.string.lbl_registrations_export_file_csv_endtime));
                        export.append(separator);
                        export.append(ctx.getString(R.string.lbl_registrations_export_file_csv_comment));
                        export.append(separator);
                        export.append(ctx.getString(R.string.lbl_registrations_export_file_csv_project));
                        export.append(separator);
                        export.append(ctx.getString(R.string.lbl_registrations_export_file_csv_task));
                        export.append(separator);
                        export.append(ctx.getString(R.string.lbl_registrations_export_file_csv_projectcomment));
                        export.append(separator);
                        export.append(TextConstants.NEW_LINE);
                    }

                    export.append("\"" + startDate + "\"");
                    export.append(separator);
                    export.append("\"" + startTime + "\"");
                    export.append(separator);
                    export.append("\"" + endDate + "\"");
                    export.append(separator);
                    export.append("\"" + endTime + "\"");
                    export.append(separator);
                    if (StringUtils.isNotBlank(timeRegistration.getComment())) {
                        export.append("\"" + timeRegistration.getComment() + "\"");
                    } else {
                        export.append("\"\"");
                    }
                    export.append(separator);
                    export.append("\"" + timeRegistration.getTask().getProject().getName() + "\"");
                    export.append(separator);
                    export.append("\"" + timeRegistration.getTask().getName() + "\"");
                    export.append(separator);
                    if (StringUtils.isNotBlank(timeRegistration.getTask().getProject().getComment())) {
                        export.append("\"" + timeRegistration.getTask().getProject().getComment() + "\"");
                    } else {
                        export.append("\"\"");
                    }

                    break;
                }
            }

            if (isFirstLine) {
                isFirstLine = false;
            }
        }

        File defaultStorageDirectory = Environment.getExternalStorageDirectory();

        File folder = new File(defaultStorageDirectory.getAbsolutePath() +
                File.separator +
                Constants.Export.EXPORT_DIRECTORY +
                File.separator);
        if (folder.isFile()) {
            Log.d(LOG_TAG, "Directory seems to be a file... Deleting it now...");
            folder.delete();
            if (folder.isFile()) {
                Log.d(LOG_TAG, "Directory still seems to be a file... hmmm... very strange... :\\");
            }
        }
        if (!folder.exists()) {
            Log.d(LOG_TAG, "Directory does not exist yet! Creating it now!");
            folder.mkdir();
            if (!folder.exists()) {
                Log.d(LOG_TAG, "The directory still does not exist!");
            }
        }

        File file = new File(defaultStorageDirectory.getAbsolutePath() +
                File.separator +
                Constants.Export.EXPORT_DIRECTORY +
                File.separator +
                fileName +
                "." +
                fileType.getExtension().toLowerCase()
        );

        try {
            boolean fileAlreadyExists = file.createNewFile();
            if(fileAlreadyExists) {
                file.delete();
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(export.toString());
            bw.close();
            fw.close();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Exception occured during export...", e);
            //TODO handle exception
        }

        return file;
    }

    /**
     * {@inheritDoc}
     */
    public void remove(TimeRegistration timeRegistration) {
        dao.delete(timeRegistration);
    }

    /**
     * {@inheritDoc}
     */
    public TimeRegistration get(Integer id) {
        return dao.findById(id);
    }
}
