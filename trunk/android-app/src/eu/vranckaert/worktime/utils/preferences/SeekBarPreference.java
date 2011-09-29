package eu.vranckaert.worktime.utils.preferences;

import android.app.AlertDialog;
import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import eu.vranckaert.worktime.R;

/**
 * User: DIRK VRANCKAERT
 * Date: 26/04/11
 * Time: 19:05
 */
public class SeekBarPreference extends DialogPreference {
    private SeekBar seekBar;

    private int maxValue = 10;
    private int increment = 1;
    private int defaultValue = 5;

    public SeekBarPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SeekBarPreference(Context context) {
        super(context, null);
    }

    protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {

        LinearLayout layout = new LinearLayout(getContext());
        layout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        );
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setMinimumWidth(400);
        layout.setPadding(20, 20, 20, 20);

        seekBar = new SeekBar(getContext());
        seekBar.setMax(maxValue);
        seekBar.setKeyProgressIncrement(increment);
        seekBar.setLayoutParams(new
                ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        seekBar.setProgress(getPersistedInt(defaultValue));

        final TextView textView = new TextView(getContext());
        textView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        );
        textView.setText(getContext().getString(R.string.value) + " :" + seekBar.getProgress());

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                textView.setText(getContext().getString(R.string.value) + " :" + i);
                seekBar.setProgress(i);
            }

            public void onStartTrackingTouch(SeekBar seekBar) {}

            public void onStopTrackingTouch(SeekBar seekBar) {}
        });



        layout.addView(seekBar);
        layout.addView(textView);


        builder.setView(layout);
        builder.setTitle(getTitle());



        super.onPrepareDialogBuilder(builder);
    }

    protected void onDialogClosed(boolean positiveResult) {
        if(positiveResult){
            persistInt(seekBar.getProgress());
        }
    }

    public int getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    public int getIncrement() {
        return increment;
    }

    public void setIncrement(int increment) {
        this.increment = increment;
    }

    public int getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(int defaultValue) {
        this.defaultValue = defaultValue;
    }
}
