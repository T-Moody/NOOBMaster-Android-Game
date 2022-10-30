package com.example.noobmaster;

public class GameZone {
    int xStart, xEnd, yStart, yEnd;
    String initialText;

    /**
     * Constructor
     * @param xStart Left edge of zone.
     * @param xEnd Right edge of zone.
     * @param yStart Top of zone.
     * @param yEnd Bottom of zone.
     * @param initialText Zone number.
     */
    public GameZone(int xStart, int xEnd, int yStart, int yEnd, String initialText) {
        this.xStart = xStart;
        this.xEnd = xEnd;
        this.yStart = yStart;
        this.yEnd = yEnd;
        this.initialText = initialText;
    }

    /**
     * Check if given x and y values are within the zone.
     * @param xCoordinate X of event.
     * @param yCoordinate Y of event.
     * @return True if within zone.
     */
    public boolean checkIfWithinZone(float xCoordinate, float yCoordinate) {
        return checkIfWithinXAxis(xCoordinate) && checkIfWithinYAxis(yCoordinate);
    }

    /**
     * Check if given x is within the x start and end.
     * @param xCoordinate Given x.
     * @return True if within zone.
     */
    public boolean checkIfWithinXAxis(float xCoordinate) {
        return xCoordinate > xStart && xCoordinate < xEnd;
    }

    /**
     * Check if given y is within the y start and end.
     * @param yCoordinate Given y.
     * @return True if within zone.
     */
    public boolean checkIfWithinYAxis(float yCoordinate) {
        return yCoordinate > yStart && yCoordinate < yEnd;
    }

    /**
     * Tostring method
     * @return A string with zone info.
     */
    @Override
    public String toString() {
        return "GameZone{" +
                "xStart=" + xStart +
                ", xEnd=" + xEnd +
                ", yStart=" + yStart +
                ", yEnd=" + yEnd +
                '}';
    }

    /**
     * Getters and setters.
     */

    public int getxStart() {
        return xStart;
    }

    public void setxStart(int xStart) {
        this.xStart = xStart;
    }

    public int getxEnd() {
        return xEnd;
    }

    public void setxEnd(int xEnd) {
        this.xEnd = xEnd;
    }

    public int getyStart() {
        return yStart;
    }

    public void setyStart(int yStart) {
        this.yStart = yStart;
    }

    public int getyEnd() {
        return yEnd;
    }

    public void setyEnd(int yEnd) {
        this.yEnd = yEnd;
    }

    public String getInitialText() {
        return initialText;
    }

    public void setInitialText(String initialText) {
        this.initialText = initialText;
    }
}
