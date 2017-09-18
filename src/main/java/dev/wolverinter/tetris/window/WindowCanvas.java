package dev.wolverinter.tetris.window;

import lombok.Getter;

import java.awt.*;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class WindowCanvas extends java.awt.Canvas {
    public static final double X_PAINT_SCALE = .1;
    public static final double Y_PAINT_SCALE = .1;

    public static int scaleX(double in){
        return (int) (in / X_PAINT_SCALE);
    }
    public static int unscaleX(double in){
        return (int) (in * X_PAINT_SCALE);
    }

    public static int scaleY(double in){
        return (int) (in / Y_PAINT_SCALE);
    }
    public static int unscaleY(double in){
        return (int) (in * Y_PAINT_SCALE);
    }

    private Frame currentFrame = null;

    private boolean active = true;

    private int fps;
    private int fpsCountLength = 500;

    private MouseInput mouse = new MouseInput(this);
    @Getter
    private KeyInput keyboard = new KeyInput();

    private Runnable heartBeat = () -> {
        long lastSecond = System.currentTimeMillis();
        int frames = 0;
        while (active) {
            long current = System.currentTimeMillis();
            if(current - fpsCountLength > lastSecond){
                lastSecond = current;
                fps = frames * (1000/fpsCountLength);
                frames = 0;
            }
            frames++;
            repaintGrafics();
        }
    };

    private final static int TICKS_PER_SECOND = 20;
    private Runnable tickHeartBeat = () -> {
        while (active) {
            long start = System.currentTimeMillis();
            runTick();
            keyboard.pushStates();
            long diff = System.currentTimeMillis()-start;
            try { Thread.sleep((int) ((1000/TICKS_PER_SECOND)-diff)); } catch (Exception e) {}
        }
    };

    private ArrayList<Thread> threads = new ArrayList<>();

    public WindowCanvas() {
        threads.add(new Thread(heartBeat));
        threads.add(new Thread(tickHeartBeat));

        for(Thread t : threads)
            t.start();

        addMouseListener(new MouseListener() {
            @Override
            public void mouseReleased(MouseEvent e) {
                System.out.println("mouseReleased("+e+")");
                mouse.setReleasePoint(e.getPoint());
            }

            @Override
            public void mousePressed(MouseEvent e) {
                System.out.println("mousePressed("+e+")");
                mouse.setClickPoint(e.getPoint());
                mouse.setClickMode(e.getButton());
                mouse.setClickedTime(System.currentTimeMillis());
                mouse.setReleasePoint(null);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                System.out.println("mouseExited("+e+")");
                mouse.setEntered(false);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                System.out.println("mouseEntered("+e+")");
                mouse.setEntered(true);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("mouseClicked("+e+")");
            }
        });

        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) { }

            @Override
            public void keyPressed(KeyEvent e) {
                if(keyboard.currentTickStates[e.getKeyCode()] != 0) return;

                System.out.println("Pressed: " + e.getKeyCode());
                keyboard.currentTickStates[e.getKeyCode()] = System.currentTimeMillis();
                keyboard.lastTickStates[e.getKeyCode()] = System.currentTimeMillis();
            }

            @Override
            public void keyReleased(KeyEvent e) {
                System.out.println("Released: " + e.getKeyCode());
                keyboard.currentTickStates[e.getKeyCode()] = 0;
            }
        });
    }

    public void initCanvas(){
        createBufferStrategy(2);
    }

    public void repaintGrafics() {
        BufferStrategy str = getBufferStrategy();
        if(str == null) //Not inizalisized
            return;

        Graphics g = getBufferStrategy().getDrawGraphics();

        Graphics2D baseBuffer = (Graphics2D) g;
        if(currentFrame == null){
            g.setColor(Color.red);
            g.fillRect(0, 0, getWidth(), getHeight());

            g.setColor(Color.BLACK);
            g.drawString("No frame!", getHeight()/2, getWidth() / 2);
        } else {
            Graphics2D buffer = (Graphics2D) baseBuffer.create();
            buffer.setColor(Color.white);
            buffer.fillRect(0, 0, getWidth(), getHeight());

            //	buffer.translate(4, 22);
            //	buffer.scale(.7, 0.7);
            buffer.scale(X_PAINT_SCALE, Y_PAINT_SCALE);
            synchronized (currentFrame.canvasLock){
                currentFrame.render(buffer);
            }
        }
        baseBuffer.scale(1, 1);
        g.setColor(Color.RED);
        g.setFont(new Font("arial", Font.BOLD, 13));
        baseBuffer.drawString("FPS: "+fps, getWidth()-70, 12);
        getBufferStrategy().show();
    }

    private void runTick(){
        if(currentFrame != null)
            synchronized (currentFrame.canvasLock){
                currentFrame.runTick();
            }
    }

    public MouseInput getMouse() {
        return mouse;
    }

    public void setCurrentFrame(Frame currentFrame) {
        if(this.currentFrame != null){
            Frame oldFrame = this.currentFrame;
            this.currentFrame = null;
            synchronized (oldFrame.canvasLock){
                oldFrame.cavanas = null;
            }
        }
        currentFrame.cavanas = this;
        this.currentFrame = currentFrame;
    }
}
