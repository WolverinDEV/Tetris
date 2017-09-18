package dev.wolverinter.tetris.game;

import dev.wolverinter.tetris.image.Brick;
import jdk.nashorn.internal.ir.Block;
import lombok.Getter;

import java.util.Random;

/**
 * Created by hadenfmar on 07.09.2017.
 */
public class ObjectMatrix {
    public static ObjectMatrix L_MATRIX = new ObjectMatrix(new String[]{"??X?", "??X?", "??X?", "?XX?"}, Brick.GRAY);
    public static ObjectMatrix FULL_MATRIX = new ObjectMatrix(new String[]{"XXXX", "XXXX", "XXXX", "XXXX"}, Brick.GRAY);
    public static ObjectMatrix TOP_MATRIX = new ObjectMatrix(new String[]{"XXXX", "????", "????", "????"}, Brick.GRAY);

    private static ObjectMatrix[] objects = {
            //new ObjectMatrix("????", "????", "????", "????") //Empty
            new ObjectMatrix("????", "??X?", "XXXX", "????"),
            new ObjectMatrix("????", "?????", "?XXX", "????"),
            new ObjectMatrix("????", "??XX?", "?XX?", "????"),
            new ObjectMatrix("????", "???X", "?XXX", "????")
    };

    private static Random rnd = new Random();
    public static ObjectMatrix newMatrix(Brick color){
        ObjectMatrix mat = objects[rnd.nextInt(objects.length)].clone();
        if(color != null) mat.colorize(color);
        return mat;
    }

    @Getter
    private Brick[][] matrix = new Brick[4][4];

    @Getter
    private Brick[][] matrixSave = null;

    public ObjectMatrix() { }

    public ObjectMatrix(String... pattern) {
        this(pattern, Brick.RED);
    }

    public ObjectMatrix(Brick brick, String... pattern) {
        this(pattern, brick);
    }

    public ObjectMatrix(String[] pattern, Brick brick) {
        for (int x = 0; x < 4; x++)
            for (int y = 0; y < 4; y++)
                matrix[x][y] = pattern[y].charAt(x) == 'X' ? brick : Brick.EMPTY;
    }

    private ObjectMatrix(ObjectMatrix ref){
        for (int x = 0; x < 4; x++)
            for (int y = 0; y < 4; y++)
                matrix[x][y] = ref.matrix[x][y];
    }

    public int getWidth() {
        return matrix.length;
    }

    public int getHight() {
        return matrix[0].length;
    }

    public void rotateCCW() {
        matrixSave = matrix;

        Brick[][] rotated = new Brick[4][4];
        for (int i = 0; i < matrix[0].length; ++i) {
            for (int j = 0; j < matrix.length; ++j) {
                rotated[i][j] = matrix[matrix.length - j - 1][i];
            }
        }
        matrix = rotated;
    }

    public void rotateCC() {
        matrixSave = matrix;

        Brick[][] rotated = new Brick[4][4];

        for (int i = 0; i < matrix[0].length; ++i) {
            for (int j = 0; j < matrix.length; ++j) {
                rotated[i][j] = matrix[j][matrix[0].length - i - 1];
            }
        }
        matrix = rotated;
    }

    public void restoreMatrix(){
        matrix = matrixSave;
    }

    public void colorize(Brick brick){
        for (int i = 0; i < matrix[0].length; ++i) {
            for (int j = 0; j < matrix.length; ++j) {
                if(matrix[i][j] != Brick.EMPTY)
                    matrix[i][j] = brick;
            }
        }
    }

    @Override
    public ObjectMatrix clone(){
        return new ObjectMatrix(this);
    }
}
