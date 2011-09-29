package eu.vranckaert.worktime.activities.timeregistrations;

import android.app.Activity;
import android.os.Bundle;
import com.google.inject.Inject;
import eu.vranckaert.worktime.constants.Constants;
import eu.vranckaert.worktime.model.TimeRegistration;
import eu.vranckaert.worktime.service.TimeRegistrationService;
import eu.vranckaert.worktime.service.WidgetService;
import roboguice.activity.GuiceActivity;
import roboguice.inject.InjectExtra;

/**
 * User: DIRK VRANCKAERT
 * Date: 10/08/11
 * Time: 20:09
 */
public class EditTimeRegistrationRestart extends GuiceActivity {
    @InjectExtra(Constants.Extras.TIME_REGISTRATION)
    private TimeRegistration timeRegistration;

    @Inject
    private TimeRegistrationService timeRegistrationService;

    @Inject
    private WidgetService widgetService;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        timeRegistration.setEndTime(null);
        timeRegistrationService.update(timeRegistration);
        widgetService.updateWidget(getApplicationContext());
        setResult(RESULT_OK);
        finish();
    }
}