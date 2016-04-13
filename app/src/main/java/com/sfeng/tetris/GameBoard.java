package com.sfeng.tetris;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Kirby on 2016-01-08.
 */
public class GameBoard {
    private static final int BOARD_HEIGHT = 24;
    private static final int BOARD_WIDTH = 10;
    private static final int NUM_NEXT = 3;
    private static final Integer DEFAULT_CELL = R.drawable.cell_shape;
    private static final Integer DEFAULT_BOTTOM = R.drawable.cell_shape_bottom;
    private static final Integer DEFAULT_OVER = R.drawable.cell_game_over;
    private static final Integer DEFAULT_OVER_BOTTOM = R.drawable.cell_game_over_bottom;

    private Cell[] board;
    private Integer[] mygrid;
    private Block curblock, oldblock;
    private int width, height, size;
    private boolean active=false, held;
    private int endRow, score;
    private String[] nextBlocks;

    public GameBoard(Integer[] grid) {
        board = new Cell[grid.length+BOARD_WIDTH];
        nextBlocks = new String[NUM_NEXT];
        mygrid = grid;
        curblock = null;
        oldblock = null;
        width = BOARD_WIDTH;
        height = BOARD_HEIGHT;
        size = width * height;
        active=true;
        endRow = 3;
        held = false;
        score = 0;
        this.createBoard(grid);
        fillNext();
        next();
    }

    public GameBoard(Integer[] grid, int w, int h) {
        board = new Cell[grid.length+w];
        nextBlocks = new String[NUM_NEXT];
        mygrid = grid;
        curblock = null;
        oldblock = null;
        width = w;
        height = h;
        size = width * height;
        active=true;
        endRow = 3;
        held = false;
        score = 0;
        this.createBoard(grid);
        fillNext();
        next(w);
    }

    public void clearBoard() {
        this.createBoard(mygrid);
    }

    public Integer[] getGrid() {
        return mygrid;
    }

    public void createBoard(Integer[] grid) {
        for (int pos=0; pos < grid.length; pos++) {
            if (pos / width == endRow) {
                board[pos] = new Cell(DEFAULT_BOTTOM, pos);
            } else {
                board[pos] = new Cell(pos);
            }
        }
        this.toInteger(grid);
    }

    public Cell getCell(int pos) {
        return board[pos];
    }

    public void toInteger(Integer[] grid) {
        for (int i=0; i < grid.length; i++) {
            grid[i] = this.getCell(i).getContent();
        }
    }

    private void gridToBoard(Integer[] grid) {
        for (int pos=0; pos < grid.length; pos++) {
            board[pos] = new Cell(grid[pos], pos);
        }
    }

    public Block getBlock() {
        return curblock;
    }

    public boolean getActive() { return active; }

    public void setActive(boolean act) { active = act; }

    public int getScore() { return score; }

    public boolean getHeld() { return held; }

    public void setHeld(boolean h) { held = h; }

    public String[] getNextBlocks() { return nextBlocks; }

    //------------------------------------------------------------------------------------
    //  Board functions
    //------------------------------------------------------------------------------------

    public void changeCell(int pos, int w, String shape) {
        Integer sh;
        switch (shape) {
            case "i":
                sh = R.drawable.i_shape;
                break;
            case "j":
                sh = R.drawable.j_shape;
                break;
            case "o":
                sh = R.drawable.o_shape;
                break;
            case "l":
                sh = R.drawable.l_shape;
                break;
            case "s":
                sh = R.drawable.s_shape;
                break;
            case "t":
                sh = R.drawable.t_shape;
                break;
            case "z":
                sh = R.drawable.z_shape;
                break;

            default:
                sh = R.drawable.cell_shape;
        }

        curblock = new Block(shape, pos, sh);
        curblock.setWidth(w, pos);
        this.redraw();
    }

    public void changeCell(int pos, String shape) {
        Integer sh;
        switch (shape) {
            case "i":
                sh = R.drawable.i_shape;
                break;
            case "j":
                sh = R.drawable.j_shape;
                break;
            case "o":
                sh = R.drawable.o_shape;
                break;
            case "l":
                sh = R.drawable.l_shape;
                break;
            case "s":
                sh = R.drawable.s_shape;
                break;
            case "t":
                sh = R.drawable.t_shape;
                break;
            case "z":
                sh = R.drawable.z_shape;
                break;

            default:
                sh = R.drawable.cell_shape;
        }

        curblock = new Block(shape, pos, sh);
        this.redraw();
    }

    public void changeCell(Block b) {
        curblock = new Block(b.getShape(), curblock.getPivot() ,b.getColour());
    }

    public void moveBlock(String dir) {
        if (active) {
            oldblock = new Block(curblock);
            //oldblock.setPos(curblock.getPos());
            switch (dir) {
                case "drop":
                    this.drop();
                    break;
                case "left":
                    this.moveLeft();
                    break;
                case "right":
                    this.moveRight();
                    break;
                case "down":
                    this.moveDown();
                    break;
                case "rotate":
                    if (checkRotate()) {
                        curblock.rotate();
                    }
                    break;
                case "reverse":
                    if (checkReverse()) {
                        curblock.reverse();
                    }
                default:
                    break;
            }
            if (active) {
                this.redraw();
            }
        }
    }

    private void redraw() {
        Cell draw;
        if (oldblock != null) {
            for (int pos=0; pos < 4; pos++) {
                if (oldblock.getPos()[pos] / width == endRow) {
                    board[oldblock.getPos()[pos]] = new Cell(DEFAULT_BOTTOM, oldblock.getPos()[pos]);
                } else {
                    board[oldblock.getPos()[pos]] = new Cell(oldblock.getPos()[pos]);
                }
            }
        }
        for (int i=0; i < 4; i++) {

            draw = new Cell(curblock.getColour(), curblock.getPos()[i]);
            board[curblock.getPos()[i]] = draw;
        }
        this.toInteger(mygrid);
    }

    public void drawBottom() {
        for (int i=11; i < 16; i++) {
            if (board[i].getContent() == DEFAULT_BOTTOM) {
                board[i].setContent(DEFAULT_CELL);
            }
        }
    }

    //------------------------------------------------------------------------------------
    //  Check Functions
    //------------------------------------------------------------------------------------

    private boolean checkLeft() {
        List<Integer> checklist = new ArrayList<Integer>();
        List<Integer> pos = new ArrayList<Integer>();
        for (int i=0; i < 4; i++) {
            pos.add(curblock.getPos()[i]);
            checklist.add(curblock.getPos()[i]-1);
            if (curblock.getPos()[i] % width == 0) {
                return false;
            }
        }
        checklist.removeAll(pos);
        for (int j=0; j < checklist.size(); j++) {
            Integer check = mygrid[checklist.get(j)];
            if (!check.equals(DEFAULT_CELL) && !check.equals(DEFAULT_BOTTOM)) {
                return false;
            }
        }
        return true;
    }

    private boolean checkRight() {
        List<Integer> checklist = new ArrayList<Integer>();
        List<Integer> pos = new ArrayList<Integer>();
        for (int i=0; i < 4; i++) {
            pos.add(curblock.getPos()[i]);
            checklist.add(curblock.getPos()[i]+1);
            if (curblock.getPos()[i] % width == (width-1)) {
                return false;
            }
        }
        checklist.removeAll(pos);
        for (int j=0; j < checklist.size(); j++) {
            Integer check = mygrid[checklist.get(j)];
            if (!check.equals(DEFAULT_CELL) && !check.equals(DEFAULT_BOTTOM)) {
                return false;
            }
        }
        return true;
    }

    private boolean checkDown() {
        List<Integer> checklist = new ArrayList<Integer>();
        List<Integer> pos = new ArrayList<Integer>();
        for (int i=0; i < 4; i++) {
            pos.add(curblock.getPos()[i]);
            checklist.add(curblock.getPos()[i]+width);
            if (curblock.getPos()[i] / width == (height-1)) {
                return false;
            }
            //need to check if board does not have a piece either
        }
        checklist.removeAll(pos);
        for (int j=0; j < checklist.size(); j++) {
            Integer check = mygrid[checklist.get(j)];
            if (!check.equals(DEFAULT_CELL) && !check.equals(DEFAULT_BOTTOM)) {
                return false;
            }
        }

        return true;
    }

    private boolean checkRotate() {
        List<Integer> checklist = new ArrayList<Integer>();
        List<Integer> pos = new ArrayList<Integer>();
        Block temp = new Block(curblock);
        temp.rotate();
        for (int i=0; i < 4; i++) {
            pos.add(curblock.getPos()[i]);
            checklist.add(temp.getPos()[i]);
            if (temp.getPos()[i] % width == (width-1)) {

                for (int j=0; j < 4; j++) {
                    if (temp.getPos()[j] % width == 0) {
                        return false;
                    }
                }
            }
            if (temp.getPos()[i] > size || temp.getPos()[i] < 0) {
                return false;
            }
        }
        checklist.removeAll(pos);
        for (int j=0; j < checklist.size(); j++) {
            Integer check = mygrid[checklist.get(j)];
            if (!check.equals(DEFAULT_CELL) && !check.equals(DEFAULT_BOTTOM)) {
                return false;
            }
        }

        return true;
    }

    private boolean checkReverse() {
        List<Integer> checklist = new ArrayList<Integer>();
        List<Integer> pos = new ArrayList<Integer>();
        Block temp = new Block(curblock);
        temp.reverse();
        for (int i=0; i < 4; i++) {
            pos.add(curblock.getPos()[i]);
            checklist.add(temp.getPos()[i]);
            if (temp.getPos()[i] % width == (width-1)) {
                for (int j=0; j < 4; j++) {
                    if (temp.getPos()[j] % width == 0) {
                        return false;
                    }
                }
            }
            if (temp.getPos()[i] > size || temp.getPos()[i] < 0) {
                return false;
            }
        }
        checklist.removeAll(pos);
        for (int j=0; j < checklist.size(); j++) {
            Integer check = mygrid[checklist.get(j)];
            if (!check.equals(DEFAULT_CELL) && !check.equals(DEFAULT_BOTTOM)) {
                return false;
            }
        }

        return true;
    }

    private boolean checkRow(int r) {
        for (int i=0; i < width; i++) {
            if (mygrid[r+i].equals(DEFAULT_CELL) || mygrid[r+1].equals(DEFAULT_BOTTOM)) {
                return false;
            }
        }
        return true;
    }

    private boolean checkOver() {

        for (int i=0; i < width; i++) {
            if (!mygrid[endRow*width+i].equals(DEFAULT_BOTTOM)) {
                return true;
            }
        }

        return false;
    }

    //------------------------------------------------------------------------------------
    //  Move Functions
    //------------------------------------------------------------------------------------

    private void moveRight() {

        if (checkRight()) {
            int[] pos = curblock.getPos();

            for (int i = 0; i < 4; i++) {
                pos[i] = pos[i] + 1;
            }

            curblock.setPos(pos);
        }
    }

    private void moveLeft() {

        if (checkLeft()) {
            int[] pos = curblock.getPos();

            for (int i=0; i < 4; i++) {
                pos[i] = pos[i]-1;
            }

            curblock.setPos(pos);
        }
    }

    private void moveDown() {

        if (checkDown()) {

            int[] pos = curblock.getPos();

            for (int i = 0; i < 4; i++) {
                // Gives pos in the next row
                pos[i] = pos[i] + width;
            }

            curblock.setPos(pos);

        } else {
            // Kind of weird, gives time to move and possibly rotate and
            // move back up. Will leave for now.
            if (curblock.checkActive()) {
                curblock.setInactive();
            } else {
                curblock = null;
                oldblock = null;

                this.clearFull();
                this.gridToBoard(mygrid);
                if (!checkOver()) {
                    this.next();
                } else {
                    active = false;
                    gameOver();
                }
            }

        }
    }

    private void drop() {

        while (checkDown()) {
            moveDown();
        }

    }

    //------------------------------------------------------------------------------------
    //  Functions to generate next block
    //------------------------------------------------------------------------------------

    public void next() {
        if (active) {
            //String shape = this.randomNext();
            String shape = nextBlocks[0];
            makeNext();
            this.changeCell((width / 2) - 1, shape);
            held = false;
        }
    }

    public void next(int c) {
        if (active) {
            //String shape = this.randomNext();
            String shape = nextBlocks[0];
            makeNext();
            this.changeCell((width / 2) - 1, c, shape);
        }
    }

    public void next(int c, String shape) {
        if (active && !held) {
            for (int pos=0; pos < 4; pos++) {
                if (curblock.getPos()[pos] / width == endRow) {
                    board[curblock.getPos()[pos]] = new Cell(DEFAULT_BOTTOM, curblock.getPos()[pos]);
                } else {
                    board[curblock.getPos()[pos]] = new Cell(curblock.getPos()[pos]);
                }
            }
            this.changeCell((width / 2) - 1, c, shape);
            held = true;
        }
    }

    private String randomNext() {
        Random rand = new Random();
        int ri = rand.nextInt(7);
        switch (ri) {
            case 0:
                return "i";
            case 1:
                return "j";
            case 2:
                return "l";
            case 3:
                return "o";
            case 4:
                return "s";
            case 5:
                return "t";
            case 6:
                return "z";
            default:
                return "";
        }

    }

    //------------------------------------------------------------------------------------
    //------------------------------------------------------------------------------------
    //------------------------------------------------------------------------------------

    private void clearFull() {
        for (int r=0; r < height; r++) {
            if (checkRow(r*width)) {
                deleteRow(r);
                score++;
            }
        }
    }

    private void deleteRow(int delr) {
        for (int r=(delr*width-1); r > (endRow*width-1); r--) {
            mygrid[r+width] = mygrid[r];
        }
        for (int i=(endRow+1)*width; i < (endRow+1)*width + width; i++) {
            mygrid[i] = mygrid[i-2*width];
        }
    }

    //------------------------------------------------------------------------------------
    //------------------------------------------------------------------------------------
    //------------------------------------------------------------------------------------

    private void gameOver() {
        for (int pos=0; pos < mygrid.length; pos++) {
            if (pos / width == endRow) {
                board[pos] = new Cell(DEFAULT_OVER_BOTTOM, pos);
            } else {
                board[pos] = new Cell(DEFAULT_OVER, pos);
            }
        }
        this.toInteger(mygrid);
    }

    private void makeNext() {
        for (int i=0; i < NUM_NEXT-1; i++) {
            nextBlocks[i] = nextBlocks[i+1];
        }
        nextBlocks[NUM_NEXT-1] = randomNext();
    }

    private void fillNext() {
        for (int i=0; i < NUM_NEXT; i++) {
            nextBlocks[i] = randomNext();
        }
    }
}
