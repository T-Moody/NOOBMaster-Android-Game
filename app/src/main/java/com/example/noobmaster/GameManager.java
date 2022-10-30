/*
* The GameManager class is responsible for handling all of the data processed during an active game.
* */
package com.example.noobmaster;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;

public class GameManager {
    private final String[] SWIPE_DIRECTIONS = {"up", "down", "left", "right"};
    private final String[] ZOOM_DIRECTIONS = {"in", "out"};
    private final DecimalFormat df = new DecimalFormat("0.00");
    private final int MIN_NUM_TAP = 1;
    private final int MAX_NUM_TAP = 5;
    private String currentDirection, currentText, currentCommand;;
    private int startingNumberTaps, tapsLeft;
    private int totalCommands, commandsLeft, numTaps, numDoubleTaps, numSwipes, numZooms;
    private ArrayList<String> actions;
    private Date systemTimeStart, systemTimeEnd;
    private double timeToComplete, avgReactionTime, maxSwipeVelocity;

    /**
     * Constructor
     * @param totalCommands Total commands remaining for the game.
     * @param actions The actions that can be chosen from.
     * @param systemTime The starting time of the game.
     */
    public GameManager(int totalCommands, ArrayList<String> actions, Date systemTime) {
        this.systemTimeStart = systemTime;
        this.totalCommands = totalCommands;
        this.commandsLeft = totalCommands;
        this.actions = actions;
        this.maxSwipeVelocity = 0;
        this.avgReactionTime = 0;
    }

    /**
     * If new velocity is faster than the previous, save the new value.
     * @param velocity Last swipe velocity.
     */
    public void compareVelocity(double velocity) {
        if (velocity > this.maxSwipeVelocity) {
            this.maxSwipeVelocity = Double.parseDouble(df.format(velocity));
        }
    }

    /**
     * Calculate and set the average reaction time of user.
     */
    public void calculateOverallReactionSpeed() {
        calculateTimeToComplete();
        this.avgReactionTime = Double.parseDouble(df.format(this.timeToComplete / this.totalCommands));
    }

    /**
     * Calculate the total time to complete the game.
     */
    public void calculateTimeToComplete() {
        this.timeToComplete = ((this.systemTimeEnd.getTime() - this.systemTimeStart.getTime()) / 1000.0);
    }

    /**
     * Get a random command from the array list and initialize command variables.
     */
    public void generateRandomCommand() {
        String randomCommand = this.actions.get((int)(Math.random() * this.actions.size()));
        switch (randomCommand) {
            case "cbTap":
                this.currentCommand = "tap";
                this.startingNumberTaps = (int)Math.floor(Math.random() * (MAX_NUM_TAP - MIN_NUM_TAP + 1) + MIN_NUM_TAP);
                this.tapsLeft = this.startingNumberTaps;
                break;
            case "cbDoubleTap":
                this.currentCommand = "double";
                break;
            case "cbSwipe":
                this.currentCommand = "swipe";
                this.currentDirection = SWIPE_DIRECTIONS[(int)(Math.random() * this.SWIPE_DIRECTIONS.length)];
                break;
            case "cbZoom":
                this.currentCommand = "zoom";
                this.currentDirection = ZOOM_DIRECTIONS[(int)(Math.random() * this.ZOOM_DIRECTIONS.length)];
                break;
        }
    }

    /**
     * Calculate the velocity given an x and y value.
     * @param xValue X value.
     * @param yValue Y value.
     * @return The velocity.
     */
    public static double calculateVelocity(float xValue, float yValue) {
        return Math.sqrt(Math.pow(xValue, 2) + Math.pow(yValue, 2));
    }

    /**
     * Reset the number of taps.
     */
    public void resetTapsLeft() {
        this.tapsLeft = this.startingNumberTaps;
    }

    /**
     * Increment the number of taps.
     */
    public void incrementNumTaps() {
        this.numTaps++;
    }

    /**
     * Increment the number of double taps.
     */
    public void incrementNumDoubleTaps() {
        this.numDoubleTaps++;
    }

    /**
     * Increment the number of swipes.
     */
    public void incrementNumSwipes() {
        this.numSwipes++;
    }

    /**
     * Increment the number of zooms.
     */
    public void incrementNumZooms() {
        this.numZooms++;
    }

    /**
     * Decrement the number of commands left.
     */
    public void decrementCommandsLeft() {
        this.commandsLeft--;
    }

    /**
     * Decrement number of taps left.
     * @param tapsToSubtract The number of taps to subtract.
     */
    public void decrementTapsLeft(int tapsToSubtract) {
        this.tapsLeft -= tapsToSubtract;
    }


    /**
     * Setters and getters
     */


    public String[] getSWIPE_DIRECTIONS() {
        return SWIPE_DIRECTIONS;
    }

    public String[] getZOOM_DIRECTIONS() {
        return ZOOM_DIRECTIONS;
    }

    public int getMIN_NUM_TAP() {
        return MIN_NUM_TAP;
    }

    public int getMAX_NUM_TAP() {
        return MAX_NUM_TAP;
    }

    public String getCurrentDirection() {
        return currentDirection;
    }

    public void setCurrentDirection(String currentDirection) {
        this.currentDirection = currentDirection;
    }

    public int getStartingNumberTaps() {
        return startingNumberTaps;
    }

    public void setStartingNumberTaps(int startingNumberTaps) {
        this.startingNumberTaps = startingNumberTaps;
    }

    public int getTapsLeft() {
        return tapsLeft;
    }

    public void setTapsLeft(int tapsLeft) {
        this.tapsLeft = tapsLeft;
    }

    public int getCommandsLeft() {
        return commandsLeft;
    }

    public void setCommandsLeft(int commandsLeft) {
        this.commandsLeft = commandsLeft;
    }

    public String getCurrentCommand() {
        return currentCommand;
    }

    public void setCurrentCommand(String currentCommand) {
        this.currentCommand = currentCommand;
    }

    public double getMaxSwipeVelocity() {
        return maxSwipeVelocity;
    }

    public void setMaxSwipeVelocity(double maxSwipeVelocity) {
        this.maxSwipeVelocity = maxSwipeVelocity;
    }

    public String getCurrentText() {
        return currentText;
    }

    public void setCurrentText(String currentText) {
        this.currentText = currentText;
    }

    public Date getSystemTimeStart() {
        return systemTimeStart;
    }

    public void setSystemTimeStart(Date systemTimeStart) {
        this.systemTimeStart = systemTimeStart;
    }

    public int getTotalCommands() {
        return totalCommands;
    }

    public void setTotalCommands(int totalCommands) {
        this.totalCommands = totalCommands;
    }

    public int getNumTaps() {
        return numTaps;
    }

    public void setNumTaps(int numTaps) {
        this.numTaps = numTaps;
    }

    public int getNumDoubleTaps() {
        return numDoubleTaps;
    }

    public void setNumDoubleTaps(int numDoubleTaps) {
        this.numDoubleTaps = numDoubleTaps;
    }

    public int getNumSwipes() {
        return numSwipes;
    }

    public void setNumSwipes(int numSwipes) {
        this.numSwipes = numSwipes;
    }

    public int getNumZooms() {
        return numZooms;
    }

    public void setNumZooms(int numZooms) {
        this.numZooms = numZooms;
    }

    public double getAvgReactionTime() {
        return avgReactionTime;
    }

    public void setAvgReactionTime(double avgReactionTime) {
        this.avgReactionTime = avgReactionTime;
    }

    public ArrayList<String> getActions() {
        return actions;
    }

    public void setActions(ArrayList<String> actions) {
        this.actions = actions;
    }

    public Date getSystemTimeEnd() {
        return systemTimeEnd;
    }

    public void setSystemTimeEnd(Date systemTimeEnd) {
        this.systemTimeEnd = systemTimeEnd;
    }
}
