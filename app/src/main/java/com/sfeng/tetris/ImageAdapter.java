package com.sfeng.tetris;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private Integer[] grid;
    private GameBoard board;
    public static final int BOARD_HEIGHT = 24;
    public static final int BOARD_WIDTH = 10;
    public static final int BOARD_SIZE = BOARD_HEIGHT*BOARD_WIDTH;

    public ImageAdapter(Context c) {
        mContext = c;
        grid = new Integer[BOARD_SIZE];
        if (board == null) {
            board = new GameBoard(grid);
        }
    }

    public void newGame() {
        board.createBoard(grid);
    }

    public int getCount() {
        return BOARD_SIZE;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(64, 64));

        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(board.getCell(position).getContent());

        return imageView;
    }

    //------------------------------------------------------------------------------------
    //  Image Adapter Functions
    //------------------------------------------------------------------------------------

    public void update() {
        board.createBoard(grid);
        grid = new Integer[BOARD_SIZE];
        board = new GameBoard(grid);
        board.setActive(false);
        board.next();
    }

    public void update(String shape) {
        board.next(BOARD_WIDTH, shape);
    }

    public void move(String dir) {
        board.moveBlock(dir);
    }

    public GameBoard getBoard() {
        return board;
    }

}
