package eu.vranckaert.worktime.utils.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Hack on the {@link TextView} view-class of Android.
 * This implementation fixes the marquee. Marquee will only work when an element is selected, however the
 * {@link TextView} element can never have focus so the focus methods are overriden here!
 *
 * User: DIRK VRANCKAERT
 * Date: 24/04/11
 * Time: 15:33
 */
public class CustomTextView extends TextView {
    public CustomTextView(Context context) {
        super(context);
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        if(focused)
            super.onFocusChanged(focused, direction, previouslyFocusedRect);
    }

    @Override
    public void onWindowFocusChanged(boolean focused) {
        if(focused)
            super.onWindowFocusChanged(focused);
    }


    @Override
    public boolean isFocused() {
        return true;
    }
}
