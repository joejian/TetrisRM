package com.sfeng.tetris;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * Created by Kirby on 2016-01-12.
 */
public class HoldAdapter extends BaseAdapter {
    private Context mContext;
    private Integer[] grid;
    private GameBoard board;
    public static final int BOARD_HEIGHT = 4;
    public static final int BOARD_WIDTH = 4;
    public static final int BOARD_SIZE = BOARD_HEIGHT * BOARD_WIDTH;

    public HoldAdapter(Context c) {
        mContext = c;
        grid = new Integer[BOARD_SIZE];
        if (board == null) {
            board = new GameBoard(grid, BOARD_WIDTH, BOARD_HEIGHT);
        }
    }

    public void newGame() {
        board.createBoard(grid);
    }

    public int getCount() {
        //return mThumbIds.length;
        return BOARD_SIZE;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(56, 56));
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(board.getCell(position).getContent());

        return imageView;
    }


    public void update() {
        board.createBoard(grid);
        board.next(BOARD_WIDTH);
        board.drawBottom();
    }

    public void update(String shape, boolean h) {
        //board.createBoard(grid);
        board.setHeld(h);
        board.next(BOARD_WIDTH, shape);
        board.drawBottom();
    }

    public void move(String dir) {
        board.moveBlock(dir);
    }

    public GameBoard getBoard() {
        return board;
    }


}