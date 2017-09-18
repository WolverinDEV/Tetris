package dev.wolverinter.tetris.frames;

import dev.wolverinter.tetris.game.TimingHelper;
import dev.wolverinter.tetris.window.Frame;

import java.awt.*;
import java.util.Random;

/**
 * Created by hadenfmar on 12.09.2017.
 */
public class GameOver extends Frame {
    private double yOffset = 0;

    @Override
    protected void render(Graphics2D g) {
        g.setColor(Color.RED);
        g.setFont(new Font("Areal", 0, 600));
        drawStringCentered(g,"Game Over!", getWidth() / 2, getHight() / 2);

        g.setFont(new Font("Areal", 0, 400));
        g.setColor(Color.BLACK);
        drawStringCentered(g,"Press any key to continue!", getWidth() / 2, Math.abs((int) yOffset) + getHight() / 2 + 600 + 50);
    }

    @Override
    protected void runTick() {
        yOffset += 7.5;

        if(yOffset > 100)
            yOffset = -100;

        if(!this.cavanas.getKeyboard().pressedKeys().isEmpty()){
            cavanas.setCurrentFrame(new GameFrame());
        }
    }
}
