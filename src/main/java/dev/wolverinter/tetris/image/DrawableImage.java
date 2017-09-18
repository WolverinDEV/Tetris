package dev.wolverinter.tetris.image;

import dev.wolverinter.tetris.window.WindowCanvas;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Created by hadenfmar on 06.09.2017.
 */
public class DrawableImage {
    public static DrawableImage TETRIS_LOGO = loadImage("logo");

    public static DrawableImage loadImage(String name){
        DrawableImage img = new DrawableImage(new File("src\\main\\resources\\", name + ".png"));
        img.load();
        return img;
    }

    private File file;
    private BufferedImage image;

    public DrawableImage(File file){
        this.file = file;
    }

    public void load(){
        try {
            image = ImageIO.read(file);
        } catch (Exception e){
            System.out.print("Cant load file: " + file.getAbsoluteFile());
            e.printStackTrace();
        }
    }

    public int getHeigh(){
        return image.getHeight();
    }

    public int getWidth(){
        return image.getWidth();
    }

    public void draw(Graphics2D g, int x, int y){
        drawScaled(g, x, y, 1, 1);
    }

    public void drawAbs(Graphics2D g, int x, int y, int width, int height){
        double XScale = (double) width / getWidth();
        double YScale = (double) height / getHeigh();
        drawScaled(g, (int) (x / XScale), (int) (y / YScale), XScale, YScale);
    }

    public void drawScaled(Graphics2D g, int x, int y, double xScale, double yScale){
        double XScale = g.getTransform().getScaleX();
        double YScale = g.getTransform().getScaleY();

        g.scale(1 / XScale, 1 / YScale);
        g.scale(xScale, yScale);

        g.drawImage(image, WindowCanvas.unscaleX(x), WindowCanvas.unscaleY(y), null);

        g.scale(1 / xScale, 1 / yScale);
        g.scale(XScale, YScale);
    }
}
