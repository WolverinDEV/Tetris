package dev.wolverinter.tetris.frames;

import dev.wolverinter.tetris.Main;
import dev.wolverinter.tetris.game.ObjectMatrix;
import dev.wolverinter.tetris.game.TimingHelper;
import dev.wolverinter.tetris.image.Brick;
import dev.wolverinter.tetris.image.DrawableImage;
import dev.wolverinter.tetris.window.Frame;

import java.awt.*;
import java.awt.event.KeyEvent;

public class GameFrame extends Frame {
    private Brick[][] bricks = new Brick[10][20];
    private ObjectMatrix currentMatrix = ObjectMatrix.TOP_MATRIX;
    private ObjectMatrix nextMatrix = ObjectMatrix.L_MATRIX;

    private int matrixXPos = 0;
    private int matrixYPos = 0;

    public GameFrame(){
        for(int x = 0;x < bricks.length; x++)
            for(int y = 0; y < bricks[x].length; y++)
                bricks[x][y] = Brick.EMPTY;

        /*
        bricks[2][10] = Brick.RED;
        for(int x = 0; x < 10; x++)
            bricks[x][bricks[0].length - 2] = Brick.RED;

        bricks[2][bricks[0].length - 1] = Brick.RED;
        */

        currentMatrix = ObjectMatrix.newMatrix(Brick.RED);
        nextMatrix = ObjectMatrix.newMatrix(Brick.RED);
    }

    private TimingHelper gravityTimingHelper = new TimingHelper(300);
    private long lastRotatePress = 0;

    @Override
    protected void runTick() {
        if(this.cavanas.getKeyboard().keyPressed(KeyEvent.VK_SPACE, lastRotatePress, 200)){
            lastRotatePress = System.currentTimeMillis();
            currentMatrix.rotateCC();
            if(matrixCollide(currentMatrix, matrixXPos, matrixYPos)) currentMatrix.restoreMatrix();
        }
        /*
        if(this.cavanas.getKeyboard().keyPressed(KeyEvent.VK_LEFT, lastRotatePress, 200)) {
            lastRotatePress = System.currentTimeMillis();
            currentMatrix.rotateCCW();

            if(matrixCollide(currentMatrix, matrixXPos, matrixYPos)) currentMatrix.restoreMatrix();
        }
        */

        if(this.cavanas.getKeyboard().keyPressed(KeyEvent.VK_RIGHT, lastRotatePress, 200)) {
            lastRotatePress = System.currentTimeMillis();

            if(!matrixCollide(currentMatrix, matrixXPos + 1, matrixYPos)) matrixXPos += 1;
        }

        if(this.cavanas.getKeyboard().keyPressed(KeyEvent.VK_LEFT, lastRotatePress, 200)) {
            lastRotatePress = System.currentTimeMillis();

            if(!matrixCollide(currentMatrix, matrixXPos - 1, matrixYPos)) matrixXPos -= 1;
        }

        if(this.cavanas.getKeyboard().keyPressed(KeyEvent.VK_DOWN, lastRotatePress, 200)) {
            lastRotatePress = System.currentTimeMillis();
            if(!matrixCollide(currentMatrix, matrixXPos, matrixYPos + 1)) matrixYPos += 1;
        }

        if(gravityTimingHelper.shouldExecute()){
            gravityTimingHelper.triggerExecute();

            matrixYPos += 1;
            if(matrixCollide(currentMatrix, matrixXPos, matrixYPos)){
                copyMatrixInfoField(currentMatrix, matrixXPos, matrixYPos - 1);
                spawnMatrix(nextMatrix);
                nextMatrix = ObjectMatrix.newMatrix(Brick.RED);
            }
        }
        /*
        if(rotateHelper.shouldExecute()){
            rotateHelper.triggerExecute();
            currentMatrix.rotateCC();
        }
        */
        super.runTick();
    }

    @Override
    protected void render(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHight());

        DrawableImage.TETRIS_LOGO.draw(g, 3500, 200);

        int xOff = 500;
        int yOff = 500;

        //The boarder
        renderGridBoarder(g, xOff, yOff);

        //The grid
        for(int x = 0;x < 10; x++)
            for(int y = 0; y < 20; y++)
                renderGrid(g, xOff, yOff, bricks[x][y], x, y);

        //The matrix
        renderMatrix(g, xOff + 200 , yOff + 200, this.currentMatrix, matrixXPos, matrixYPos);

        //Matrix preview
        g.setColor(new Color(122, 122, 122));
        g.setFont(new Font("Areal", Font.PLAIN, 300));
        g.drawString("Next Object:", 3500, 1500);
        renderMatrix(g, 3500, 1700, this.nextMatrix, 0, 0);
    }

    private void renderMatrix(Graphics2D g, int xOff, int yOff, ObjectMatrix matrix, int xPos, int yPos){
        for(int x = 0; x < matrix.getWidth(); x++)
            for(int y = 0; y < matrix.getWidth(); y++)
                if(matrix.getMatrix()[x][y] != Brick.EMPTY)
                    renderGrid(g, xOff + xPos * 200 + x * 200, yOff + yPos * 200 + y * 200, matrix.getMatrix()[x][y]);
    }

    private void renderGrid(Graphics2D g, int xOff, int yOff, Brick brick, int xPos, int yPos){
        xOff += 200;
        yOff += 200; //boarder

        renderGrid(g, xOff + 200 * xPos, yOff + 200 * yPos, brick);
    }

    private void renderGrid(Graphics2D g, int xOff, int yOff, Brick brick){
        brick.getImage().draw(g, xOff, yOff);
    }

    private void renderGridBoarder(Graphics2D g, int xOff, int yOff){
        for(int i = 0;i < this.bricks.length + 2; i++)
            Brick.BORDER.getImage().draw(g, xOff + i * 200, yOff);

        for(int i = 0;i < this.bricks[0].length; i++)
            Brick.BORDER.getImage().draw(g, xOff, yOff + (i + 1) * 200);

        for(int i = 0;i < this.bricks[0].length; i++)
            Brick.BORDER.getImage().draw(g, xOff + (this.bricks.length + 1) * 200, yOff + (i + 1) * 200);

        for(int i = 0;i < this.bricks.length + 2; i++)
            Brick.BORDER.getImage().draw(g, xOff + i * 200, yOff + (this.bricks[0].length + 1) * 200);
    }

    private boolean matrixCollide(ObjectMatrix matrix, int xPos, int yPos){
        for(int x = 0; x < 4; x++)
            for(int y = 0; y < 4; y++){
                if(matrix.getMatrix()[x][y] == Brick.EMPTY) continue;

                if(x + xPos < 0 || x + xPos >= bricks.length) return true; //Out of bounds (X)
                if(y + yPos < 0 || y + yPos >= bricks[x + xPos].length) return true; //Out of bounds (Y)

                if(bricks[x + xPos][y + yPos] != Brick.EMPTY) return true;
            }
        return false; //No colision
    }

    private void removeLine(int y){
        for(int x = 0; x < bricks.length; x++){
            Brick[] array = bricks[x];
            System.arraycopy(array, 0, array, 1, y);
        }
    }

    private void testFullLine(){
        yLoop:
        for(int y = 0; y < bricks[0].length; y++){
            for(int x = 0; x < bricks.length; x++)
                if(bricks[x][y] == Brick.EMPTY) continue yLoop;
            removeLine(y);
        }
    }

    private void copyMatrixInfoField(ObjectMatrix matrix, int xPos, int yPos) {
        for (int x = 0; x < 4; x++)
            for (int y = 0; y < 4; y++) {
                if(matrix.getMatrix()[x][y] == Brick.EMPTY) continue;
                bricks[x + xPos][y + yPos] = matrix.getMatrix()[x][y];
            }
        testFullLine();
    }

    private void spawnMatrix(ObjectMatrix matrix){
        this.currentMatrix = matrix;
        matrixXPos = 0;//TODO center!
        matrixYPos = -matrix.getHight();  //Absulute matrix start

        while (matrixCollide(this.currentMatrix, matrixXPos, matrixYPos)){
            matrixYPos += 1;
            if(matrixYPos > matrix.getHight()){
                System.err.println("No space left!");
                Main.getWindow().getCanvas().setCurrentFrame(new GameOver());
                return;
            }
        }
    }
}
