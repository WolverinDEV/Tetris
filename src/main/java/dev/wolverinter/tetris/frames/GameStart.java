package dev.wolverinter.tetris.frames;

import dev.wolverinter.tetris.image.DrawableImage;
import dev.wolverinter.tetris.window.*;
import dev.wolverinter.tetris.window.Frame;

import java.awt.*;

/**
 * Created by hadenfmar on 14.09.2017.
 */
public class GameStart extends Frame {
    private double yOffset = 0;

    @Override
    protected void render(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHight());

        DrawableImage.TETRIS_LOGO.draw(g,   getWidth() / 2 - WindowCanvas.scaleX(DrawableImage.TETRIS_LOGO.getWidth()) / 2,
                                            getHight() / 2 - WindowCanvas.scaleY(DrawableImage.TETRIS_LOGO.getHeigh()));

        g.setFont(new Font("Areal", 0, 400));
        g.setColor(Color.WHITE);
        drawStringCentered(g,"Press any key to start!", getWidth() / 2, Math.abs((int) yOffset) + getHight() / 2 + WindowCanvas.scaleX(DrawableImage.TETRIS_LOGO.getHeigh()) / 2 + 500);
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
