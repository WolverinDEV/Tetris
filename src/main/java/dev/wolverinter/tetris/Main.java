package dev.wolverinter.tetris;

import dev.wolverinter.tetris.frames.GameStart;
import dev.wolverinter.tetris.game.SoundHelper;
import dev.wolverinter.tetris.window.Window;
import lombok.Getter;

/**
 * Created by hadenfmar on 06.09.2017.
 */
public class Main {
    @Getter
    private static Window window;
    public static void main(String... args){
        System.out.print("Loading programm");

        new Thread(() -> SoundHelper.playSound("soundtrack")).start();

        window = new Window();
        window.open();
        window.getCanvas().setCurrentFrame(new GameStart());

        window.waitFor();
        window.close();
    }
}
