package dev.wolverinter.tetris.image;

import lombok.Getter;

@Getter
public enum Brick {
    BLUE("p_blue"),
    CYAN("P_cyan"),
    GRAY("p_gray"),
    GREEN("p_green"),
    ORANGE("p_orange"),
    RED("p_red"),
    YELLOW("p_yellow"),

    EMPTY("piece"),
    BORDER("border");

    private final String fileName;
    private DrawableImage image;

    Brick(String fileName) {
        this.fileName = fileName;
        image = DrawableImage.loadImage(fileName);
    }
}
