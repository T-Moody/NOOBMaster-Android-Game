/*
 * The MainActivity class is responsible for routing the user to the settings view or the game view.
 */
package com.example.noobmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    Button btnNewGame;
    Button btnSettings;
    ImageView ivBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get views.
        btnNewGame = findViewById(R.id.btnNewGame);
        btnSettings = findViewById(R.id.btnSettings);
        ivBackground = findViewById(R.id.ivBackground);


        // Set background image.
        ivBackground.setImageResource(R.drawable.home_background);

        // Load the settings view.
        btnSettings.setOnClickListener(view -> {
            startActivity(new Intent(this, SettingsActivity.class));
        });

        // Load the game activity.
        btnNewGame.setOnClickListener(view -> {
            startActivity(new Intent(this, GameActivity.class));
        });
    }
}