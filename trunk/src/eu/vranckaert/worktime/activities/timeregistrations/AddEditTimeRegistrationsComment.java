package eu.vranckaert.worktime.activities.timeregistrations;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import com.google.inject.Inject;
import eu.vranckaert.worktime.R;
import eu.vranckaert.worktime.constants.Constants;
import eu.vranckaert.worktime.model.TimeRegistration;
import eu.vranckaert.worktime.service.CommentHistoryService;
import eu.vranckaert.worktime.service.TimeRegistrationService;
import eu.vranckaert.worktime.utils.context.ContextUtils;
import eu.vranckaert.worktime.utils.string.StringUtils;
import roboguice.activity.GuiceActivity;
import roboguice.inject.InjectExtra;

import java.util.List;

/**
 * User: DIRK VRANCKAERT
 * Date: 27/04/11
 * Time: 23:27
 */
public class AddEditTimeRegistrationsComment extends GuiceActivity {
    private static final String LOG_TAG = AddEditTimeRegistrationsComment.class.getSimpleName();

    @InjectExtra(Constants.Extras.TIME_REGISTRATION)
    private TimeRegistration timeRegistration;

    @Inject
    private TimeRegistrationService timeRegistrationService;

    @Inject
    private CommentHistoryService commentHistoryService;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        showDialog(Constants.Dialog.ENTER_COMMENT_FOR_TR);
    }

    @Override
    protected Dialog onCreateDialog(int dialogId) {
        Dialog dialog = null;
        switch(dialogId) {
            case Constants.Dialog.ENTER_COMMENT_FOR_TR: {
                boolean enterNewComment = false;
                if (StringUtils.isBlank(timeRegistration.getComment())) {
                    enterNewComment = true;
                }
                Log.d(LOG_TAG, "Entering a new comment? " + enterNewComment);

                Log.d(LOG_TAG, "Creating enter comment dialog for a time registration");
                AlertDialog.Builder enterComment = new AlertDialog.Builder(this);

                final Context mContext = AddEditTimeRegistrationsComment.this;
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
                final View layout = inflater.inflate(R.layout.dialog_add_tr_comment,
                                               (ViewGroup) findViewById(R.id.dialog_layout_root));
                if (enterNewComment) {
                    enterComment.setTitle(R.string.lbl_widget_dialog_title_enter_comment);
                } else {
                    enterComment.setTitle(R.string.lbl_widget_dialog_title_edit_comment);
                }

                enterComment.setCancelable(false);
                enterComment.setPositiveButton(R.string.ok, new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.d(LOG_TAG, "CommentHistory entered, ready to save...");
                        removeDialog(Constants.Dialog.ENTER_COMMENT_FOR_TR);
                        AutoCompleteTextView commentEditText =
                                (AutoCompleteTextView) layout.findViewById(R.id.tr_comment);
                        String comment = commentEditText.getText().toString();
                        ContextUtils.hideKeyboard(mContext, commentEditText);
                        Log.d(LOG_TAG, "Time Registration will be saved with comment: " + comment);
                        updateComment(comment);
                    }
                });
                enterComment.setNegativeButton(R.string.cancel, new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.d(LOG_TAG, "Ending time registration cancelled on entering comment...");
                        removeDialog(Constants.Dialog.ENTER_COMMENT_FOR_TR);
                        finish();
                    }
                });

                AutoCompleteTextView commentEditText = (AutoCompleteTextView) layout.findViewById(R.id.tr_comment);
                List<String> options = commentHistoryService.getAll();
                ArrayAdapter<String> autoCompleteAdapter =
                        new ArrayAdapter<String>(this, R.layout.autocomplete_list_item, options);
                commentEditText.setAdapter(autoCompleteAdapter);

                if (!enterNewComment) {
                    commentEditText.setText(timeRegistration.getComment());
                }

                enterComment.setView(layout);
                dialog = enterComment.create();

                break;
            }
        };
        return dialog;
    }

    /**
     * Updates the time registration with a new comment. If the comment is empty or null the comment will be erased.
     * @param comment The comment to update on the {@link TimeRegistration} instance.
     */
    private void updateComment(String comment) {
        if (StringUtils.isNotBlank(comment)) {
            timeRegistration.setComment(comment);
        } else {
            timeRegistration.setComment(null);
        }
        timeRegistrationService.update(timeRegistration);

        setResult(RESULT_OK);
        finish();
    }
}