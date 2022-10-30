package com.example.noobmaster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class GameActivity extends AppCompatActivity {

    final String[] DEFAULT_ACTIONS = {"cbTap", "cbDoubleTap", "cbSwipe", "cbZoom"};
    final float MOVEMENT_THRESHOLD = 10;
    ArrayList<String> currentActions;
    TextView tvSwipeToStart, tvZone1, tvZone2, tvZone3, tvZone4;
    TextView[] tvZones;
    GameZone[] gameZones;

    TextView currentZone;
    SharedPreferences settingPreference;
    GestureDetector simpleGestureDetector;
    ScaleGestureDetector scaleGestureDetector;
    GameManager gameManager;
    boolean gameStarted = false;
    String TAG = "testing";
    String lastZoomValue;
    double lastValue = 0;
    int statusBarHeight;
    int zoneIndex;
    ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Get views.
        tvSwipeToStart = findViewById(R.id.tvSwipeToStart);
        tvZone1 = findViewById(R.id.tvZone1);
        tvZone2 = findViewById(R.id.tvZone2);
        tvZone3 = findViewById(R.id.tvZone3);
        tvZone4 = findViewById(R.id.tvZone4);

        // Create zone arrays.
        tvZones = new TextView[] {tvZone1, tvZone2, tvZone3, tvZone4};
        gameZones = new GameZone[tvZones.length];

        constraintLayout = findViewById(R.id.clGame);

        // Get saved preferences.
        settingPreference = getSharedPreferences("SettingsPrefs", Context.MODE_PRIVATE);

        // Create gesture listeners.
        simpleGestureDetector = new GestureDetector(this, new SimpleListener());
        scaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());

        currentActions = new ArrayList<>();
    }

    // Listen for on touch event.
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (simpleGestureDetector.onTouchEvent(event) || scaleGestureDetector.onTouchEvent(event)) {
            return true;
        }
        return super.onTouchEvent(event);
    }

    // Implement class to handle simple gestures.
    class SimpleListener extends GestureDetector.SimpleOnGestureListener {

        // Handle swipe gestures.
        @Override
        public boolean onFling(MotionEvent downEvent, MotionEvent moveEvent, float velocityX, float velocityY) {

            double velocity = GameManager.calculateVelocity(velocityX, velocityY);
            float xDifference = moveEvent.getX() - downEvent.getX();
            float yDifference = moveEvent.getY() - downEvent.getY();

            String userSwipeDirection = getSwipeDirection(xDifference, yDifference);

            // Start game and get first command.
            if (!gameStarted && userSwipeDirection.equals("up")) {
                gameStarted = true;
                startGame();
            }
            else if (gameStarted && gameManager.getCurrentCommand().equals("swipe") && gameManager.getCurrentDirection().equals(userSwipeDirection) ) {
                boolean actionWithinZone = gameZones[zoneIndex].checkIfWithinZone(downEvent.getX(), downEvent.getY());

                // On swipe command success.
                if (actionWithinZone) {
                    gameManager.incrementNumSwipes();
                    gameManager.decrementCommandsLeft();
                    gameManager.compareVelocity(velocity);

                    setDefaultTextForCurrentZone();

                    // Get next command if game not over.
                    if (!checkIfGameOver()) {
                        gameManager.generateRandomCommand();
                        setRandomZone();
                        displayCommandInZone();
                    }
                }
            }

            return userSwipeDirection == null;
        }

        // Handle double tap gestures.
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            float xCoordinate = e.getX();
            float yCoordinate = e.getY();

            // Check if double tap is current command and in the right zone.
            if (gameStarted && gameManager.getCurrentCommand().equals("double") && gameZones[zoneIndex].checkIfWithinZone(xCoordinate, yCoordinate)) {
                gameManager.incrementNumDoubleTaps();
                gameManager.decrementCommandsLeft();
                setDefaultTextForCurrentZone();

                // Get next command if game not over.
                if (!checkIfGameOver()) {
                    gameManager.generateRandomCommand();
                    setRandomZone();
                    displayCommandInZone();
                }
                return true;
            } // Check if single tap is current command and is executed in correct zone.
            else if (gameStarted && gameManager.getCurrentCommand().equals("tap") && gameZones[zoneIndex].checkIfWithinZone(xCoordinate, yCoordinate)) {
                gameManager.decrementTapsLeft(2);

                // If correct number of taps is reached.
                if (gameManager.getTapsLeft() == 0) {
                    gameManager.incrementNumTaps();
                    gameManager.decrementCommandsLeft();

                    setDefaultTextForCurrentZone();

                    // Get next command if game isn't over.
                    if (!checkIfGameOver()) {
                        gameManager.generateRandomCommand();
                        setRandomZone();
                    }
                } // Reset taps left if user goes over the number of taps.
                else if (gameManager.getTapsLeft() < 0) {
                    gameManager.resetTapsLeft();
                }

                // Update zone text if game not over.
                if (!checkIfGameOver()) {
                    displayCommandInZone();
                }

            }
            return false;
        }

        // Handle single tap.
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            float xCoordinate = e.getX();
            float yCoordinate = e.getY();

            // Check if tap is current command and in the correct zone.
            if (gameStarted && gameManager.getCurrentCommand().equals("tap") && gameZones[zoneIndex].checkIfWithinZone(xCoordinate, yCoordinate)) {
                gameManager.decrementTapsLeft(1);

                // If no taps left. Handle success.
                if (gameManager.getTapsLeft() == 0) {
                    gameManager.incrementNumTaps();
                    gameManager.decrementCommandsLeft();

                    setDefaultTextForCurrentZone();

                    // Get next command if game not over.
                    if (!checkIfGameOver()) {
                        gameManager.generateRandomCommand();
                        setRandomZone();
                    }
                } // Reset taps if user goes over.
                else if (gameManager.getTapsLeft() < 0) {
                    gameManager.resetTapsLeft();
                }

                // Display next command if game not over.
                if (!checkIfGameOver()) {
                    displayCommandInZone();
                }

                return true;

            }
            return super.onSingleTapConfirmed(e);
        }
    }

    // Implement class to handle zooming.
    class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        float zoomStartX, zoomStartY;
        @Override
        public boolean onScale(ScaleGestureDetector detector) {

            // Check if zoom in or zoom out.
            if (detector.getScaleFactor() > lastValue) {
                lastZoomValue = "in";
            } else if (detector.getScaleFactor() < lastValue) {
                lastZoomValue = "out";
            }

            // Set the last gesture.
            lastValue = scaleGestureDetector.getScaleFactor();

            return super.onScale(detector);
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            // Get start zone of zoom.
            zoomStartX = detector.getFocusX();
            zoomStartY = detector.getFocusY();

            return super.onScaleBegin(detector);
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {

            // If current command is zoom and direction is correct.
            if (gameStarted && gameManager.getCurrentCommand().equals("zoom") && gameManager.getCurrentDirection().equals(lastZoomValue)) {
                boolean actionWithinZone = gameZones[zoneIndex].checkIfWithinZone(zoomStartX, zoomStartY);

                // If start of gesture is within zone.
                if (actionWithinZone) {
                    gameManager.incrementNumZooms();
                    gameManager.decrementCommandsLeft();

                    setDefaultTextForCurrentZone();

                    // Get next command if game is not over.
                    if (!checkIfGameOver()) {
                        gameManager.generateRandomCommand();
                        setRandomZone();
                        displayCommandInZone();
                    }
                }
            }

            lastValue = 1.0; // Reset value.

            super.onScaleEnd(detector);
        }
    }

    /**
     * Get the direction of the user swipe.
     * @param xDifference Difference between the starting x and ending x.
     * @param yDifference Difference between the starting y and ending y.
     * @return The swipe direction.
     */
    public String getSwipeDirection(float xDifference, float yDifference) {
        String swipeValue = "";

        // If x is bigger, then it is a left or right swipe.
        if (Math.abs(xDifference) > Math.abs(yDifference)) {
            if (Math.abs(xDifference) > MOVEMENT_THRESHOLD) {
                if (xDifference > 0) {
                    swipeValue = "right";
                } else {
                    swipeValue = "left";
                }
            }
        }
        else {
            // Down or up swipe.
            if (Math.abs(yDifference) > MOVEMENT_THRESHOLD) {
                if (yDifference > 0) {
                    swipeValue = "down";
                }
                else {
                    swipeValue = "up";
                }
            }
        }
        return swipeValue;
    }

    /**
     * Check if the game is over and end the game if it is. Show alert to go to summary.
     * @return true if game is over.
     */
    public boolean checkIfGameOver() {
        if (gameStarted && gameManager.getCommandsLeft() == 0) {
            gameManager.setSystemTimeEnd(Calendar.getInstance().getTime());
            gameManager.calculateOverallReactionSpeed();
            showResultAlert();

            return true;
        }

        return false;
    }

    /**
     * Handle methods required to start the game.
     */
    public void startGame() {
        createGame();
        tvSwipeToStart.setVisibility(View.INVISIBLE);
        gameManager.generateRandomCommand();
        calculateScreenZones();
        setRandomZone();
        displayCommandInZone();
    }

    /**
     * Get the screen size, padding, and calculate the start, middle, and edges of screen.
     * Assign zones to different sections of the screen.
     */
    public void calculateScreenZones() {
        statusBarHeight = getStatusBarHeight(this);
        View gameView = (View) constraintLayout.getParent();
        int top = gameView.getTop();
        int bottom = gameView.getBottom();
        int left = gameView.getLeft();
        int right = gameView.getRight();
        int startY = statusBarHeight + top;
        int bottomY = bottom + statusBarHeight;
        int middleY = (startY + bottomY) / 2;
        int middleX = right / 2;

        gameZones[0] = new GameZone(left, middleX, startY, middleY, tvZone1.getText().toString());
        gameZones[1] = new GameZone(middleX, right, startY, middleY, tvZone2.getText().toString());
        gameZones[2] = new GameZone(left, middleX, middleY, bottomY, tvZone3.getText().toString());
        gameZones[3] = new GameZone(middleX, right, middleY, bottomY, tvZone4.getText().toString());
    }

    /**
     * Get the height of the status bar at the top of the screen.
     * @param activity Activity that the bar is located.
     * @return The height of the bar.
     */
    public int getStatusBarHeight(Activity activity) {
        Rect rectangle = new Rect();
        Window window = activity.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(rectangle);
        return rectangle.top;
    }

    /**
     * Change the text of the zone to match the current command.
     */
    public void displayCommandInZone() {
        switch (gameManager.getCurrentCommand()) {
            case "tap":
                currentZone.setText("Tap " + gameManager.getTapsLeft() + " Times");
                break;
            case "double":
                currentZone.setText("Double Tap");
                break;
            case "swipe":
                currentZone.setText("Swipe " + gameManager.getCurrentDirection());
                break;
            case "zoom":
                currentZone.setText("Zoom " + gameManager.getCurrentDirection());
                break;
        }
    }

    /**
     * Reset zone text and set a new zone for next command.
     */
    public void setRandomZone() {
        //tvZones[zoneIndex].setText(gameZones[zoneIndex].getInitialText());
        zoneIndex = (int)(Math.random() * tvZones.length);
        currentZone = tvZones[zoneIndex];
    }

    /**
     * Set the default text for current zone displaying a command.
     */
    public void setDefaultTextForCurrentZone() {
        tvZones[zoneIndex].setText(gameZones[zoneIndex].getInitialText());
    }

    /**
     * Creates a new game manager.
     */
    public void createGame() {
        boolean actionExists;
        Date currentTime = Calendar.getInstance().getTime();

        // Get preference if they exist.
        for (String action : DEFAULT_ACTIONS) {
            actionExists = settingPreference.getBoolean(action, false);

            if (actionExists) {
                currentActions.add(action);
            }
        }

        // If no preference set defaults.
        if (currentActions.isEmpty()) {
            currentActions.addAll(Arrays.asList(DEFAULT_ACTIONS));
        }

        gameManager = new GameManager(settingPreference.getInt("numberOfCommands", 10), currentActions, currentTime);
    }

    /**
     * Display alert and set intent values for summary view. Route to summary view on click.
     */
    public void showResultAlert() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setMessage("All commands completed. Tap Next to View Results!");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "NEXT",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(getApplicationContext(), SummaryActivity.class);
                        intent.putExtra("numTap", gameManager.getNumTaps() + "");
                        intent.putExtra("numDouble", gameManager.getNumDoubleTaps() + "");
                        intent.putExtra("numSwipe", gameManager.getNumSwipes() + "");
                        intent.putExtra("numZoom", gameManager.getNumZooms() + "");
                        intent.putExtra("numCommand", gameManager.getTotalCommands() + "");
                        intent.putExtra("fastestSwipe", gameManager.getMaxSwipeVelocity() + "");
                        intent.putExtra("avgReaction", gameManager.getAvgReactionTime() + "");
                        startActivity(intent);
                    }
                });
        alertDialog.show();
    }
}