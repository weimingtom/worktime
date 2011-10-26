/*
 *  Copyright 2011 Dirk Vranckaert
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package eu.vranckaert.worktime.activities.timeregistrations;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.inject.Inject;
import eu.vranckaert.worktime.R;
import eu.vranckaert.worktime.constants.Constants;
import eu.vranckaert.worktime.enums.export.CsvSeparator;
import eu.vranckaert.worktime.enums.export.ExportType;
import eu.vranckaert.worktime.enums.export.FileType;
import eu.vranckaert.worktime.service.TimeRegistrationService;
import eu.vranckaert.worktime.utils.context.IntentUtil;
import eu.vranckaert.worktime.utils.context.ContextUtils;
import eu.vranckaert.worktime.utils.preferences.Preferences;
import eu.vranckaert.worktime.utils.string.StringUtils;
import roboguice.activity.GuiceActivity;
import roboguice.inject.InjectView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * User: DIRK VRANCKAERT
 * Date: 15/02/11
 * Time: 00:15
 */
public class ExportTimeRegistrationsActivity extends GuiceActivity {
    private static final String LOG_TAG = ExportTimeRegistrationsActivity.class.getSimpleName();

    @InjectView(R.id.timeRegistrationsExportFileNameText) private EditText fileNameInput;
    @InjectView(R.id.export_filename_required) private TextView fileNameInputRequired;
    @InjectView(R.id.timeRegistrationsExportFileTypeBtn) private Button fileTypeBtn;
    @InjectView(R.id.timeRegistrationsExportCsvSeperatorContainer) private View csvSeparatorContainer;
    @InjectView(R.id.timeRegistrationsExportCsvSeparatorBtn) private Button csvSeparatorBtn;

    @Inject private SharedPreferences preferences;

    @Inject private TimeRegistrationService service;

    private ExportType exportType = null;
    private File exportedFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_export_registrations);

        initForm(ExportTimeRegistrationsActivity.this);
    }

    /**
     * Updates the entire form when launching this activity.
     * @param ctx The context of the activity.
     */
    private void initForm(Context ctx) {
        fileNameInput.setText(Preferences.getTimeRegistrationExportFileName(ctx));

        updateFileTypeAndCsvSeparator(ctx);

        fileTypeBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                showDialog(Constants.Dialog.CHOOSE_EXPORT_FILE_TYPE);
            }
        });
        csvSeparatorBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                showDialog(Constants.Dialog.CHOOSE_EXPORT_CSV_SEPARATOR);
            }
        });
    }

    /**
     * Update both the file type and CSV seperator views.
     * @param ctx The context.
     */
    private void updateFileTypeAndCsvSeparator(Context ctx) {
        FileType fileType = updateFileType(ctx);
        updateCsvSeparator(ctx);

        if(fileType.equals(FileType.COMMA_SERPERATED_VALUES)) {
            csvSeparatorContainer.setVisibility(View.VISIBLE);
        } else {
            csvSeparatorContainer.setVisibility(View.GONE);
        }
    }

    /**
     * Updates the view elements specified for showing the chosen file type.
     * @param ctx The context.
     * @return The {@link FileType} that is displayed.
     */
    private FileType updateFileType(Context ctx) {
        FileType fileType = Preferences.getTimeRegistrationExportFileType(ctx);
        switch (fileType) {
            case COMMA_SERPERATED_VALUES: {
                fileTypeBtn.setText(R.string.lbl_registrations_export_file_type_CSV);
                break;
            }
        }
        return fileType;
    }

    /**
     * Updates the view elements specified for showing the chosen CSV seperator if the chosen filetype is CSV!
     * @param ctx The context.
     */
    private void updateCsvSeparator(Context ctx) {
        CsvSeparator csvSeparator = Preferences.getTrimeRegistrationCsvSeparator(ctx);
        switch (csvSeparator) {
            case COMMA:
                csvSeparatorBtn.setText(R.string.lbl_registrations_export_csv_separator_comma);
                break;
            case SEMICOLON:
                csvSeparatorBtn.setText(R.string.lbl_registrations_export_csv_separator_semicolon);
                break;
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        Log.d(LOG_TAG, "Received request to create loading dialog with id " + id);
        Dialog dialog = null;
        switch(id) {
            case Constants.Dialog.CHOOSE_EXPORT_FILE_TYPE: {
                FileType selectedFileType = Preferences.getTimeRegistrationExportFileType(this);
                final FileType[] availableFileTypes = FileType.values();
                int selectedItem = -1;

                List<String> fileTypes = new ArrayList<String>();
                for (FileType fileType : availableFileTypes) {
                    switch(fileType) {
                        case COMMA_SERPERATED_VALUES:
                            fileTypes.add(getString(R.string.lbl_registrations_export_file_type_CSV));
                            break;
                    }
                    if(fileType.equals(selectedFileType)) {
                        selectedItem = fileTypes.size()-1;
                    }
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.lbl_registrations_export_file_type_chooser_title)
                       .setSingleChoiceItems(
                        StringUtils.convertListToArray(fileTypes),
                        selectedItem, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int index) {
                                Preferences.setTimeRegistrationExportFileType(
                                        ExportTimeRegistrationsActivity.this,
                                        availableFileTypes[index]
                                );
                            }
                        })
                       .setOnCancelListener(new DialogInterface.OnCancelListener() {
                           public void onCancel(DialogInterface dialogInterface) {
                               ExportTimeRegistrationsActivity.this.updateFileTypeAndCsvSeparator(
                                       ExportTimeRegistrationsActivity.this
                               );
                           }
                       });
                dialog = builder.create();
                break;
            }
            case Constants.Dialog.CHOOSE_EXPORT_CSV_SEPARATOR: {
                CsvSeparator selectedSeparator = Preferences.getTrimeRegistrationCsvSeparator(this);
                final CsvSeparator[] availableSeperators = CsvSeparator.values();
                int selectedItem = -1;

                List<String> fileTypes = new ArrayList<String>();
                for (CsvSeparator separator : availableSeperators) {
                    switch(separator) {
                        case COMMA:
                            fileTypes.add(getString(R.string.lbl_registrations_export_csv_separator_comma));
                            break;
                        case SEMICOLON:
                            fileTypes.add(getString(R.string.lbl_registrations_export_csv_separator_semicolon));
                            break;
                    }
                    if(separator.equals(selectedSeparator)) {
                        selectedItem = fileTypes.size()-1;
                    }
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.lbl_registrations_export_csv_separator_chooser_title)
                       .setSingleChoiceItems(
                        StringUtils.convertListToArray(fileTypes),
                        selectedItem, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int index) {
                                Preferences.setTrimeRegistrationCsvSeparator(
                                        ExportTimeRegistrationsActivity.this,
                                        availableSeperators[index]
                                );
                            }
                        })
                       .setOnCancelListener(new DialogInterface.OnCancelListener() {
                           public void onCancel(DialogInterface dialogInterface) {
                               ExportTimeRegistrationsActivity.this.updateCsvSeparator(
                                       ExportTimeRegistrationsActivity.this
                               );
                           }
                       });
                dialog = builder.create();
                break;
            }
            case Constants.Dialog.LOADING_TIMEREGISTRATIONS_EXPORT: {
                int messageText = -1;
                switch(exportType) {
                    case MAIL:
                        messageText = R.string.msg_registrations_export_saving_to_mail;
                        break;
                }
                dialog = ProgressDialog.show(
                        ExportTimeRegistrationsActivity.this,
                        "",
                        getString(messageText),
                        true,
                        false
                );
                break;
            }
            case Constants.Dialog.TIMEREGISTRATIONS_EXPORT_FILE_EXPORT_DONE: {
                AlertDialog.Builder alertExportDone = new AlertDialog.Builder(this);
				alertExportDone
						   .setMessage(getString(R.string.msg_export_done, exportedFile.getAbsolutePath()))
						   .setCancelable(true)
						   .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
                                    removeDialog(Constants.Dialog.TIMEREGISTRATIONS_EXPORT_FILE_EXPORT_DONE);
								}
							})
						   .setNegativeButton(R.string.lbl_export_send_file, new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
                                    removeDialog(Constants.Dialog.TIMEREGISTRATIONS_EXPORT_FILE_EXPORT_DONE);
                                    sendExportedFileByMail();
								}
							});
				dialog = alertExportDone.create();
                break;
            }
            default:
                Log.d(LOG_TAG, "Dialog id " + id + " is not supported in this activity!");
        }
        return dialog;
    }

    /**
     * Go Home.
     * @param view The view.
     */
    public void onHomeClick(View view) {
        IntentUtil.goHome(this);
    }

    /**
     * Save the file name to the preferences in case it has changed.
     * Afterwards Export the time registrations.
     * @param view The view.
     */
    public void onExportClick(View view) {
        Log.d(LOG_TAG, "Export button clicked!");
        Log.d(LOG_TAG, "Validate input...");
        if(fileNameInput.getText().toString().length() < 3) {
            Log.d(LOG_TAG, "Validation failed! Showing applicable error messages...");
            fileNameInputRequired.setVisibility(View.VISIBLE);
            return;
        } else {
            Log.d(LOG_TAG, "Validation successful. Hiding all error messages...");
            fileNameInputRequired.setVisibility(View.GONE);
            Log.d(LOG_TAG, "Hide the soft keyboard if visible");
            ContextUtils.hideKeyboard(ExportTimeRegistrationsActivity.this, fileNameInput);
        };
        initiateExport(ExportType.MAIL);
    }

    public void initiateExport(final ExportType exportType) {
        this.exportType = exportType;

        AsyncTask task = new AsyncTask() {

            @Override
            protected void onPreExecute() {
                Log.d(LOG_TAG, "About to show loading dialog for export");
                showDialog(Constants.Dialog.LOADING_TIMEREGISTRATIONS_EXPORT);
                Log.d(LOG_TAG, "Loading dialog for export showing!");
            }

            @Override
            protected Object doInBackground(Object... objects) {
                Log.d(LOG_TAG, "Starting export background process...");
                Preferences.setTimeRegistrationExportFileName(
                        ExportTimeRegistrationsActivity.this,
                        fileNameInput.getText().toString()
                );
                File file = doExport(exportType);
                Log.d(LOG_TAG, "Export in background process finished!");
                return file;
            }

            @Override
            protected void onPostExecute(Object o) {
                Log.d(LOG_TAG, "About to remove loading dialog for export");
                removeDialog(Constants.Dialog.LOADING_TIMEREGISTRATIONS_EXPORT);
                Log.d(LOG_TAG, "Loading dialog for export removed!");
                exportedFile = (File) o;
                showDialog(Constants.Dialog.TIMEREGISTRATIONS_EXPORT_FILE_EXPORT_DONE);
            }
        }.execute();
    }

    public File doExport(final ExportType exportType) {
        Log.d(LOG_TAG, "About to start export...");
        File file = service.export(exportType, ExportTimeRegistrationsActivity.this);
        Log.d(LOG_TAG, "Export finished");
        return file;
    }

    private void sendExportedFileByMail() {
        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.setType("text/csv");
        emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:/" + exportedFile));
        startActivity(Intent.createChooser(emailIntent, getText(R.string.lbl_export_sebd_file_app_chooser_title)));
    }
}
