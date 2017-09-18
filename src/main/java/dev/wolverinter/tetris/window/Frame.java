package dev.wolverinter.tetris.window;

import java.awt.*;

/**
 * Created by hadenfmar on 06.09.2017.
 */
public abstract class Frame {
    protected WindowCanvas cavanas = null;
    protected Object canvasLock = new Object();

    protected abstract void render(Graphics2D g);

    int getAbsX(){
        return 40;
    }
    int getAbsY(){
        return 220;
    }

    protected int getWidth(){
        return (int) (cavanas.getWidth() / WindowCanvas.X_PAINT_SCALE);
    }

    protected int getHight(){
        return (int) (cavanas.getHeight() / WindowCanvas.Y_PAINT_SCALE);
    }

    protected void runTick(){};


    protected void drawStringCentered(Graphics2D g, String str, int x, int y){
        int absY = y - g.getFontMetrics().getHeight();
        int absX = (int) (x - g.getFontMetrics().getStringBounds(str, g).getWidth() / 2);

        g.drawString(str, absX, absY);
    }
}
