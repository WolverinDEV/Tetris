package dev.wolverinter.tetris.window;

import lombok.Getter;

import javax.swing.*;

/**
 * Created by hadenfmar on 06.09.2017.
 */
public class Window {
    private JFrame frame;
    @Getter
    private WindowCanvas canvas;

    public void open(){
        int width = 800;
        int height = 800;

        frame = new JFrame("Tetris");

        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setSize(width, height);

        canvas = new WindowCanvas();
        canvas.setSize(width, height);
        frame.add(canvas);
        canvas.initCanvas();

    }

    public void waitFor(){
        while(true){
            if(!frame.isActive()) return;
            try {
                Thread.sleep(100);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void close(){

    }
}
