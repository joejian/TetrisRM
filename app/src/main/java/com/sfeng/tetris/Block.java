package com.sfeng.tetris;

/**
 * Created by Kirby on 2016-01-11.
 */
public class Block {
    private String shape;
    private Integer colour;
    private int state, width, piv;
    private int[] pos = new int[4];
    private boolean active;
    private static int DEFAULT_WIDTH = 10;

    public Block(String sh, int p, Integer c) {
        shape = sh;
        colour = c;
        state = 0;
        active = true;
        width = DEFAULT_WIDTH;
        this.createShape(sh, p);
    }

    public Block(Block b) {
        shape = b.getShape();
        colour = b.getColour();
        state = b.getState();
        active = b.checkActive();
        pos = b.getPos().clone();
        piv = b.getPivot();
        width = b.getWidth();
    }

    public int[] getPos() {
        return pos;
    }

    public void setPos(int[] newPos) {
        pos = newPos.clone();
    }

    public String getShape() {
        return shape;
    }

    public int getState() { return state; }

    public Integer getColour() {
        return colour;
    }

    public void setInactive() {
        active = false;
    }

    public boolean checkActive() {
        return active;
    }

    public void setWidth(int w, int p) {
        width = w;
        this.createShape(shape, p);
    }

    public int getWidth() {
        return width;
    }

    public int getPivot() { return piv; }

    //------------------------------------------------------------------------------------
    //------------------------------------------------------------------------------------
    //------------------------------------------------------------------------------------

    private void createShape(String sh, int p) {
        switch (sh) {
            case "i":
                pos[0] = p;
                pos[1] = p+width;
                pos[2] = p+width*2;
                pos[3] = p+width*3;
                break;
            case "j":
                pos[0] = p-1;
                pos[1] = p;
                pos[2] = p+1;
                pos[3] = p+width+1;
                break;
            case "l":
                pos[0] = p-1;
                pos[1] = p;
                pos[2] = p+1;
                pos[3] = p+width-1;
                break;
            case "o":
                pos[0] = p;
                pos[1] = p+1;
                pos[2] = p+width;
                pos[3] = p+width+1;
                break;
            case "s":
                pos[0] = p;
                pos[1] = p+1;
                pos[2] = p+width-1;
                pos[3] = p+width;
                break;
            case "t":
                pos[0] = p-1;
                pos[1] = p;
                pos[2] = p+1;
                pos[3] = p+width;
                break;
            case "z":
                pos[0] = p-1;
                pos[1] = p;
                pos[2] = p+width;
                pos[3] = p+width+1;
                break;
            default:
                pos = new int[]{-1,-1,-1,-1};
                break;
        }

        piv = p;
    }

    //------------------------------------------------------------------------------------
    //------------------------------------------------------------------------------------
    //------------------------------------------------------------------------------------

    private int nextState(int m) {
        return (state+1) % m;
    }

    private int prevState(int m) {
        return (state-1+m) % m;
    }

    public void rotate() {
        switch (shape) {
            case "i":
                state = this.nextState(4);
                rotateI();
                break;
            case "j":
                state = this.nextState(4);
                rotateJ();
                break;
            case "l":
                state = this.nextState(4);
                rotateL();
                break;
            case "o":
                break;
            case "s":
                state = this.nextState(2);
                rotateS();
                break;
            case "t":
                state = this.nextState(4);
                rotateT();
                break;
            case "z":
                state = this.nextState(2);
                rotateZ();
                break;
            default:
                break;
        }

    }

    public void reverse() {
        switch (shape) {
            case "i":
                state = this.prevState(4);
                reverseI();
                break;
            case "j":
                state = this.prevState(4);
                reverseJ();
                break;
            case "l":
                state = this.prevState(4);
                reverseL();
                break;
            case "o":
                break;
            case "s":
                state = this.prevState(2);
                rotateS();
                break;
            case "t":
                state = this.prevState(4);
                reverseT();
                break;
            case "z":
                state = this.prevState(2);
                rotateZ();
                break;
            default:
                break;
        }

    }

    //------------------------------------------------------------------------------------
    //------------------------------------------------------------------------------------
    //------------------------------------------------------------------------------------

    private void rotateI() {
        int pivot = -1;
        switch (state) {
            case 0:
                pivot = pos[1];
                pos[0] = pivot-2*width;
                pos[1] = pivot-width;
                pos[2] = pivot;
                pos[3] = pivot+width;
                break;
            case 1:
                pivot = pos[1];
                pos[0] = pivot-1;
                pos[1] = pivot;
                pos[2] = pivot+1;
                pos[3] = pivot+2;
                break;
            case 2:
                pivot = pos[2];
                pos[0] = pivot-width;
                pos[1] = pivot;
                pos[2] = pivot+width;
                pos[3] = pivot+2*width;
                break;
            case 3:
                pivot = pos[2];
                pos[0] = pivot-2;
                pos[1] = pivot-1;
                pos[2] = pivot;
                pos[3] = pivot+1;
                break;
            default:
                break;
        }
        piv = pivot;
    }

    private void rotateJ() {
        int pivot = -1;
        switch (state) {
            case 0:
                pivot = pos[2];
                pos[0] = pivot-1;
                pos[1] = pivot;
                pos[2] = pivot+1;
                pos[3] = pivot+width+1;
                break;
            case 1:
                pivot = pos[1];
                pos[0] = pivot-width;
                pos[1] = pivot;
                pos[2] = pivot+9;
                pos[3] = pivot+width;
                break;
            case 2:
                pivot = pos[1];
                pos[0] = pivot-width-1;
                pos[1] = pivot-1;
                pos[2] = pivot;
                pos[3] = pivot+1;
                break;
            case 3:
                pivot = pos[2];
                pos[0] = pivot-width;
                pos[1] = pivot-width+1;
                pos[2] = pivot;
                pos[3] = pivot+width;
                break;
            default:
                break;
        }
        piv = pivot;
    }

    private void rotateL() {
        int pivot = -1;
        switch (state) {
            case 0:
                pivot = pos[1];
                pos[0] = pivot-1;
                pos[1] = pivot;
                pos[2] = pivot+1;
                pos[3] = pivot+width-1;
                break;
            case 1:
                pivot = pos[1];
                pos[0] = pivot-width-1;
                pos[1] = pivot-width;
                pos[2] = pivot;
                pos[3] = pivot+width;
                break;
            case 2:
                pivot = pos[2];
                pos[0] = pivot-width+1;
                pos[1] = pivot-1;
                pos[2] = pivot;
                pos[3] = pivot+1;
                break;
            case 3:
                pivot = pos[2];
                pos[0] = pivot-width;
                pos[1] = pivot;
                pos[2] = pivot+width;
                pos[3] = pivot+width+1;
                break;
            default:
                break;
        }
        piv = pivot;
    }

    private void rotateS() {
        int pivot = -1;
        switch (state) {
            case 0:
                pivot = pos[1];
                pos[0] = pivot;
                pos[1] = pivot+1;
                pos[2] = pivot+width-1;
                pos[3] = pivot+width;
                break;
            case 1:
                pivot = pos[0];
                pos[0] = pivot-width;
                pos[1] = pivot;
                pos[2] = pivot+1;
                pos[3] = pivot+width+1;
                break;
            default:
                break;
        }
        piv = pivot;
    }

    private void rotateT() {
        int pivot = -1;
        switch (state) {
            case 0:
                pivot = pos[1];
                pos[0] = pivot-1;
                pos[1] = pivot;
                pos[2] = pivot+1;
                pos[3] = pivot+width;
                break;
            case 1:
                pivot = pos[1];
                pos[0] = pivot-width;
                pos[1] = pivot-1;
                pos[2] = pivot;
                pos[3] = pivot+width;
                break;
            case 2:
                pivot = pos[2];
                pos[0] = pivot-width;
                pos[1] = pivot-1;
                pos[2] = pivot;
                pos[3] = pivot+1;
                break;
            case 3:
                pivot = pos[2];
                pos[0] = pivot-width;
                pos[1] = pivot;
                pos[2] = pivot+1;
                pos[3] = pivot+width;
                break;
            default:
                break;
        }
        piv = pivot;
    }

    private void rotateZ() {
        int pivot = -1;
        switch (state) {
            case 0:
                pivot = pos[2];
                pos[0] = pivot-1;
                pos[1] = pivot;
                pos[2] = pivot+width;
                pos[3] = pivot+width+1;
                break;
            case 1:
                pivot = pos[1];
                pos[0] = pivot-width;
                pos[1] = pivot-1;
                pos[2] = pivot;
                pos[3] = pivot+width-1;
                break;
            default:
                break;
        }
        piv = pivot;
    }

    //------------------------------------------------------------------------------------
    //------------------------------------------------------------------------------------
    //------------------------------------------------------------------------------------

    private void reverseI() {
        int pivot = -1;
        switch (state) {
            case 0:
                pivot = pos[1];
                pos[0] = pivot-width;
                pos[1] = pivot;
                pos[2] = pivot+width;
                pos[3] = pivot+2*width;
                break;
            case 1:
                pivot = pos[1];
                pos[0] = pivot-2;
                pos[1] = pivot-1;
                pos[2] = pivot;
                pos[3] = pivot+1;
                break;
            case 2:
                pivot = pos[2];
                pos[0] = pivot-2*width;
                pos[1] = pivot-width;
                pos[2] = pivot;
                pos[3] = pivot+width;
                break;
            case 3:
                pivot = pos[2];
                pos[0] = pivot-1;
                pos[1] = pivot;
                pos[2] = pivot+1;
                pos[3] = pivot+2;
                break;
            default:
                break;
        }
        piv = pivot;
    }

    private void reverseJ() {
        int pivot = -1;
        switch (state) {
            case 0:
                pivot = pos[1];
                pos[0] = pivot-1;
                pos[1] = pivot;
                pos[2] = pivot+1;
                pos[3] = pivot+width+1;
                break;
            case 1:
                pivot = pos[2];
                pos[0] = pivot-width;
                pos[1] = pivot;
                pos[2] = pivot+9;
                pos[3] = pivot+width;
                break;
            case 2:
                pivot = pos[2];
                pos[0] = pivot-width-1;
                pos[1] = pivot-1;
                pos[2] = pivot;
                pos[3] = pivot+1;
                break;
            case 3:
                pivot = pos[1];
                pos[0] = pivot-width;
                pos[1] = pivot-width+1;
                pos[2] = pivot;
                pos[3] = pivot+width;
                break;
            default:
                break;
        }
        piv = pivot;
    }

    private void reverseL() {
        int pivot = -1;
        switch (state) {
            case 0:
                pivot = pos[2];
                pos[0] = pivot-1;
                pos[1] = pivot;
                pos[2] = pivot+1;
                pos[3] = pivot+width-1;
                break;
            case 1:
                pivot = pos[2];
                pos[0] = pivot-width-1;
                pos[1] = pivot-width;
                pos[2] = pivot;
                pos[3] = pivot+width;
                break;
            case 2:
                pivot = pos[1];
                pos[0] = pivot-width+1;
                pos[1] = pivot-1;
                pos[2] = pivot;
                pos[3] = pivot+1;
                break;
            case 3:
                pivot = pos[1];
                pos[0] = pivot-width;
                pos[1] = pivot;
                pos[2] = pivot+width;
                pos[3] = pivot+width+1;
                break;
            default:
                break;
        }
        piv = pivot;
    }

    private void reverseT() {
        int pivot = -1;
        switch (state) {
            case 0:
                pivot = pos[2];
                pos[0] = pivot-1;
                pos[1] = pivot;
                pos[2] = pivot+1;
                pos[3] = pivot+width;
                break;
            case 1:
                pivot = pos[2];
                pos[0] = pivot-width;
                pos[1] = pivot-1;
                pos[2] = pivot;
                pos[3] = pivot+width;
                break;
            case 2:
                pivot = pos[1];
                pos[0] = pivot-width;
                pos[1] = pivot-1;
                pos[2] = pivot;
                pos[3] = pivot+1;
                break;
            case 3:
                pivot = pos[1];
                pos[0] = pivot-width;
                pos[1] = pivot;
                pos[2] = pivot+1;
                pos[3] = pivot+width;
                break;
            default:
                break;
        }
        piv = pivot;
    }
}
