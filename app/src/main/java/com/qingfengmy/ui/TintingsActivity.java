package com.qingfengmy.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.qingfengmy.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2015/2/10.
 */
public class TintingsActivity extends BaseActivity {
    @InjectView(R.id.toolbar)
    Toolbar titleBar;

    // Set a drawable as the image to display
    @InjectView(R.id.image)
    ImageView mImage;

    // Get text labels and seekbars for the four color components: ARGB
    @InjectView(R.id.alphaSeek)
    SeekBar mAlphaBar;
    @InjectView(R.id.alphaText)
    TextView mAlphaText;
    @InjectView(R.id.greenSeek)
    SeekBar mGreenBar;
    @InjectView(R.id.greenText)
    TextView mGreenText;
    @InjectView(R.id.redSeek)
    SeekBar mRedBar;
    @InjectView(R.id.redText)
    TextView mRedText;
    @InjectView(R.id.blueText)
    TextView mBlueText;
    @InjectView(R.id.blueSeek)
    SeekBar mBlueBar;

    // Set up the spinner for blend mode selection from a string array resource
    @InjectView(R.id.blendSpinner)
    Spinner mBlendSpinner;
    /**
     * Computed color for tinting of drawable.
     */
    private int mHintColor;

    /**
     * Selected color tinting mode.
     */
    private PorterDuff.Mode mMode;

    /**
     * Available tinting modes. Note that this array must be kept in sync with the
     * <code>blend_modes</code> string array that provides labels for these modes.
     */
    private static final PorterDuff.Mode[] MODES = new PorterDuff.Mode[]{
            PorterDuff.Mode.ADD,
            PorterDuff.Mode.CLEAR,
            PorterDuff.Mode.DARKEN,
            PorterDuff.Mode.DST,
            PorterDuff.Mode.DST_ATOP,
            PorterDuff.Mode.DST_IN,
            PorterDuff.Mode.DST_OUT,
            PorterDuff.Mode.DST_OVER,
            PorterDuff.Mode.LIGHTEN,
            PorterDuff.Mode.MULTIPLY,
            PorterDuff.Mode.OVERLAY,
            PorterDuff.Mode.SCREEN,
            PorterDuff.Mode.SRC,
            PorterDuff.Mode.SRC_ATOP,
            PorterDuff.Mode.SRC_IN,
            PorterDuff.Mode.SRC_OUT,
            PorterDuff.Mode.SRC_OVER,
            PorterDuff.Mode.XOR
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tinting);
        ButterKnife.inject(this);

        titleBar.setTitle(getName(this));
        setSupportActionBar(titleBar);
        titleBar.setNavigationIcon(R.drawable.ic_menu_back);
        titleBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mImage.setImageResource(R.drawable.ic_launcher);
        // Set a listener to update tinted image when selections have changed
        mAlphaBar.setOnSeekBarChangeListener(mSeekBarListener);
        mRedBar.setOnSeekBarChangeListener(mSeekBarListener);
        mGreenBar.setOnSeekBarChangeListener(mSeekBarListener);
        mBlueBar.setOnSeekBarChangeListener(mSeekBarListener);

        SpinnerAdapter sa = ArrayAdapter.createFromResource(this,
                R.array.blend_modes, android.R.layout.simple_spinner_dropdown_item);
        mBlendSpinner.setAdapter(sa);
        // Set a listener to update the tinted image when a blend mode is selected
        mBlendSpinner.setOnItemSelectedListener(mBlendListener);
        // Select the first item
        mBlendSpinner.setSelection(0);
        mMode = MODES[0];
    }

    /**
     * Computes the {@link Color} value from selection on ARGB sliders.
     *
     * @return color computed from selected ARGB values
     */
    public int getColor() {
        final int alpha = mAlphaBar.getProgress();
        final int red = mRedBar.getProgress();
        final int green = mGreenBar.getProgress();
        final int blue = mBlueBar.getProgress();

        return Color.argb(alpha, red, green, blue);
    }

    /**
     * Returns the {@link android.graphics.PorterDuff.Mode} for the selected tint mode option.
     *
     * @return selected tint mode
     */
    public PorterDuff.Mode getTintMode() {
        return MODES[mBlendSpinner.getSelectedItemPosition()];
    }

    /**
     * Update the tint of the image with the color set in the seekbars and selected blend mode.
     * The seekbars are set to a maximum of 255, with one for each of the four components of the
     * ARGB color. (Alpha, Red, Green, Blue.) Once a color has been computed using
     * {@link android.graphics.Color#argb(int, int, int, int)}, it is set togethe with the blend mode on the background
     * image using
     * {@link android.widget.ImageView#setColorFilter(int, android.graphics.PorterDuff.Mode)}.
     */
    public void updateTint(int color, PorterDuff.Mode mode) {
        // Set the color hint of the image: ARGB
        mHintColor = color;

        // Set the color tint mode based on the selection of the Spinner
        mMode = mode;

        // Apply the color tint for the selected tint mode
        mImage.setColorFilter(mHintColor, mMode);

        // Update the text for each label with the value of each channel
        mAlphaText.setText(getString(R.string.value_alpha, Color.alpha(color)));
        mRedText.setText(getString(R.string.value_red, Color.red(color)));
        mGreenText.setText(getString(R.string.value_green, Color.green(color)));
        mBlueText.setText(getString(R.string.value_blue, Color.blue(color)));
    }

    /**
     * Listener that updates the tint when a blend mode is selected.
     */
    private AdapterView.OnItemSelectedListener mBlendListener =
            new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    // Selected a blend mode and update the tint of image
                    updateTint(getColor(), getTintMode());
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }

            };
    /**
     * Seekbar listener that updates the tinted color when the progress bar has changed.
     */
    private SeekBar.OnSeekBarChangeListener mSeekBarListener =
            new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                    // Update the tinted color from all selections in the UI
                    updateTint(getColor(), getTintMode());
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }
            };
}
