package eu.vranckaert.worktime.utils.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewParent;
import android.widget.DatePicker;

/**
 * Fixes issue with DatePicker used inside a ScrollView:
 * http://groups.google.com/group/android-developers/browse_thread/thread/16449f248e9d1fcc
 * User: DIRK VRANCKAERT
 * Date: 19/12/11
 * Time: 07:44
 */
public class CustomDatePicker extends DatePicker {
    public CustomDatePicker(Context context) {
        super(context);
    }

    public CustomDatePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomDatePicker(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        /* Prevent parent controls from stealing our events once we've  gotten a touch down */

        if (ev.getActionMasked() == MotionEvent.ACTION_DOWN) {
            ViewParent p = getParent();
            if (p != null)
                p.requestDisallowInterceptTouchEvent(true);
        }

        return false;
    }
}
