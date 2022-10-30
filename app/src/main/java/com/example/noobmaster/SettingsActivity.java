/*
* This class is responsible for handling the settings view. The user will be able to set their game
* settings here and the settings will be applied to the next game that the user starts.
* */
package com.example.noobmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    Button btnCancel, btnSave;
    SeekBar sbNumberOfCommands;
    CheckBox cbTap, cbDoubleTap, cbSwipe,cbZoom;
    TextView tvSeekBarAmountLabel;
    CheckBox[] actions;
    SharedPreferences settingPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Get settings if they already exist.
        settingPreference = getSharedPreferences("SettingsPrefs", Context.MODE_PRIVATE);

        // Link views.
        btnCancel = findViewById(R.id.btnCancel);
        btnSave = findViewById(R.id.btnSave);
        sbNumberOfCommands = findViewById(R.id.sbCommands);
        cbTap = findViewById(R.id.cbTap);
        cbDoubleTap = findViewById(R.id.cbDoubleTap);
        cbSwipe = findViewById(R.id.cbSwipe);
        cbZoom = findViewById(R.id.cbZoom);
        tvSeekBarAmountLabel = findViewById(R.id.tvAmountLabel);

        actions = new CheckBox[] {cbTap, cbDoubleTap,cbSwipe, cbZoom};

        // Initialize progress bar.
        sbNumberOfCommands.setProgress(settingPreference.getInt("numberOfCommands", 10));
        tvSeekBarAmountLabel.setText(sbNumberOfCommands.getProgress() + "");

        // Initialize the actions.
        cbTap.setChecked(settingPreference.getBoolean("cbTap", true));
        cbDoubleTap.setChecked(settingPreference.getBoolean("cbDoubleTap", true));
        cbSwipe.setChecked(settingPreference.getBoolean("cbSwipe", true));
        cbZoom.setChecked(settingPreference.getBoolean("cbZoom", true));

        // Load the settings view.
        btnCancel.setOnClickListener(view -> {
            startActivity(new Intent(this, MainActivity.class));
        });

        // Save preferences and return to main menu.
        btnSave.setOnClickListener(view -> {
            SharedPreferences.Editor editor = settingPreference.edit();
            int actionCount = 0;

            // Set checkbox preferences to true or false.
            for (CheckBox action : actions) {
                editor.putBoolean(getResources().getResourceEntryName(action.getId()), action.isChecked());
                actionCount = action.isChecked() ? actionCount + 1 : actionCount;
            }

            // If count is less than 2, show error. Else submit settings.
            if (actionCount < 2) {
                Toast.makeText(this, "At least 2 actions must be selected.", Toast.LENGTH_SHORT).show();
            }
            else {
                editor.putInt("numberOfCommands", sbNumberOfCommands.getProgress());
                editor.commit();
                startActivity(new Intent(this, MainActivity.class));
            }
        });

        // Dynamically change seekbar label to match seekbar value.
        sbNumberOfCommands.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                tvSeekBarAmountLabel.setText(i + "");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}