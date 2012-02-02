/*
 *  Copyright 2012 Dirk Vranckaert
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
package eu.vranckaert.worktime.activities.backup;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import eu.vranckaert.worktime.R;
import eu.vranckaert.worktime.constants.Constants;
import roboguice.activity.GuiceActivity;

/**
 * User: DIRK VRANCKAERT
 * Date: 1/02/12
 * Time: 8:34
 */
public class BackupRestoreInfoActivity extends GuiceActivity {
    private static final String LOG_TAG = BackupRestoreInfoActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        showDialog(Constants.Dialog.BACKUP_RESTORE_DOCUMENTATION);
    }

    protected Dialog onCreateDialog(int id) {
        Dialog dialog = null;

        switch (id) {
            case Constants.Dialog.BACKUP_RESTORE_DOCUMENTATION: {
                AlertDialog.Builder alertBackupSuccess = new AlertDialog.Builder(this);
                alertBackupSuccess
                        .setTitle(R.string.lbl_backup_restore_documentation_title)
                        .setMessage(R.string.msg_backup_restore_documentation_content)
                        .setCancelable(true)
                        .setPositiveButton(R.string.close, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                dialog = alertBackupSuccess.create();
                break;
            }
        }

        return dialog;
    }
}
