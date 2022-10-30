/*
* The SummaryActivity is responsible for displaying the previous games statistics to the user.
* */

package com.example.noobmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class SummaryActivity extends AppCompatActivity {
    String numTap, numDouble, numSwipe, numZoom, numCommands, avgReaction, fastestSwipe;
    TextView tvTapSummary, tvDoubleSummary, tvSwipeSummary, tvZoomSummary;
    TextView tvFastestSwipeSummary, tvTotalCommandsSummary, tvReactionTimeSummary;
    Button btnPlayAgain, btnMainMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        // Get game stats.
        Intent intent = getIntent();
        numTap = intent.getStringExtra("numTap");
        numDouble = intent.getStringExtra("numDouble");
        numSwipe = intent.getStringExtra("numSwipe");
        numZoom = intent.getStringExtra("numZoom");
        numCommands = intent.getStringExtra("numCommand");
        avgReaction = intent.getStringExtra("avgReaction");
        fastestSwipe = intent.getStringExtra("fastestSwipe");

        // Get views.
        tvTapSummary = findViewById(R.id.tvTapSummary);
        tvDoubleSummary = findViewById(R.id.tvDoubleSummary);
        tvSwipeSummary = findViewById(R.id.tvSwipeSummary);
        tvZoomSummary = findViewById(R.id.tvZoomSummary);
        tvFastestSwipeSummary = findViewById(R.id.tvFastestSwipeSummary);
        tvTotalCommandsSummary = findViewById(R.id.tvTotalCommandSummary);
        tvReactionTimeSummary = findViewById(R.id.tvReactionTimeSummary);
        btnPlayAgain = findViewById(R.id.btnPlayAgain);
        btnMainMenu = findViewById(R.id.btnMainMenu);

        // Set views.
        tvTapSummary.setText(numTap);
        tvDoubleSummary.setText(numDouble);
        tvSwipeSummary.setText(numSwipe);
        tvZoomSummary.setText(numZoom);
        tvTotalCommandsSummary.setText(numCommands);
        tvFastestSwipeSummary.setText(fastestSwipe + " pixels/sec");
        tvReactionTimeSummary.setText(avgReaction + " seconds");

        // Go to game activity to start a new game.
        btnPlayAgain.setOnClickListener(view -> {
            startActivity(new Intent(this, GameActivity.class));
        });

        // Return to main menu.
        btnMainMenu.setOnClickListener(view -> {
            startActivity(new Intent(this, MainActivity.class));
        });
    }
}