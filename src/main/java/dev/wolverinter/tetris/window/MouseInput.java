package dev.wolverinter.tetris.window;

import lombok.Getter;
import lombok.Setter;

import java.awt.*;

public class MouseInput {
    private final Canvas cavanas;
    public MouseInput(Canvas cav) {
        this.cavanas = cav;
    }

    @Getter
    @Setter
    private boolean entered;

    @Getter
    private int clickMode;
    @Getter
    private Point clickPoint = null;
    private long clickedTime;
    private Point releasePoint = null;

    public Point getCurrentMousePoint(){
        Point absPoint = MouseInfo.getPointerInfo().getLocation();
        Point windowPoint = cavanas.getLocationOnScreen();
        Point relPoint = new Point(absPoint.x-windowPoint.x, absPoint.y-windowPoint.y);
        return relPoint;
    }

    public boolean isInClick(){
        return clickPoint != null && releasePoint == null;
    }

    public void setClickMode(int clickMode) {
        this.clickMode = clickMode;
    }
    public void setClickPoint(Point clickPoint) {
        this.clickPoint = clickPoint;
    }
    public Point getReleasePoint() {
        return releasePoint;
    }
    public void setReleasePoint(Point releasePoint) {
        this.releasePoint = releasePoint;
    }
    public long getClickedTime() {
        return clickedTime;
    }
    public void setClickedTime(long clickedTime) {
        this.clickedTime = clickedTime;
    }
}
