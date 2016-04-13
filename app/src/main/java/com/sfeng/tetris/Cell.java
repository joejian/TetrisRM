package com.sfeng.tetris;

/**
 * Created by Kirby on 2016-01-08.
 */
public class Cell {
    private int x, y, pos;
    private Integer content;
    public static final Integer DEFAULT_CELL = R.drawable.cell_shape;

    public Cell (int p) {
        content = DEFAULT_CELL;
        x = p / 10;
        y = p % 10;
        pos = p;
    }

    public Cell(Integer con, int p) {
        content = con;
        x = p / 10;
        y = p % 10;
        pos = p;
    }

    public Cell(Integer con, int xPos, int yPos) {
        content = con;
        x = xPos;
        y = yPos;
        pos = xPos * 10 + yPos;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Integer getContent() {
        return content;
    }

    public void setContent(Integer con) {
        content = con;
    }
}
