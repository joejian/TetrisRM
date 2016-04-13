package com.sfeng.tetris;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;


public class Tetris extends AppCompatActivity {
    private GridView gridview, holdview, nextview0, nextview1, nextview2;
    private ImageView leftview, rightview, leftdownview, rightdownview,
            leftrotateview, rightrotateview;
    private TextView scoreview;
    private Thread game;
    private Boolean active = false;
    private long speed = (long) 1000.0;
    private double difficulty = 0.0;

    private String TAG = Tetris.class.getSimpleName();
    float initialX, initialY;
    long startTime;
    private int score = 0;
    static final int MAX_DURATION = 250;

    Handler handlescore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tetris);

        gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(this));

        holdview = (GridView) findViewById(R.id.holdview);
        holdview.setAdapter(new HoldAdapter(this));

        nextview0 = (GridView) findViewById(R.id.nextview0);
        nextview0.setAdapter(new NextAdapter(this));
        nextview1 = (GridView) findViewById(R.id.nextview1);
        nextview1.setAdapter(new NextAdapter(this));
        nextview2 = (GridView) findViewById(R.id.nextview2);
        nextview2.setAdapter(new NextAdapter(this));

        leftview = (ImageView) findViewById(R.id.leftview);
        rightview = (ImageView) findViewById(R.id.rightview);

        leftdownview = (ImageView) findViewById(R.id.leftdownview);
        rightdownview = (ImageView) findViewById(R.id.rightdownview);

        leftrotateview = (ImageView) findViewById(R.id.rotateview);
        rightrotateview = (ImageView) findViewById(R.id.crotateview);


        scoreview = (TextView) findViewById(R.id.scoreview);

        handlescore = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                scoreview.setText(Integer.toString(msg.arg1));
                if (msg.arg1 < 1000) {
                    score = msg.arg1;
                }
            }
        };

        game = null;
        active = false;
        setControls();
        createThread();
        newGame();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tetris, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //------------------------------------------------------------------------------------
    //  Game Thread Functions
    //------------------------------------------------------------------------------------

    // Checks if the game is active.
    // Used to start the game initially, then pause and resume after.
    private void startGame() {
        ImageAdapter ia = (ImageAdapter) gridview.getAdapter();
        if (active) {
            active = false;
        } else {
            active = true;
            createThread();
            game.start();

        }
        ia.getBoard().setActive(active);
    }

    // Creates a thread that controls the game.
    // More specifically, will move the block/shape down every interval.
    private void createThread() {
        game = new Thread() { //new thread
            public void run() {
                try {
                    do {
                        Thread.sleep(speed- (long) difficulty);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // TODO Auto-generated method stub
                                if (active) {
                                    moveShape("down");
                                    updateNext();
                                    Message message = Message.obtain();
                                    message.arg1 = getScore();
                                    handlescore.sendMessage(message);
                                }
                            }
                        });
                        difficulty = (score / 10)*3;
                        if (difficulty > score) {
                            difficulty = score;
                        }
                    }
                    while (active);
                    //handler.sendEmptyMessage(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                finally {
                }
            };
        };
    }

    private int getScore() {
        ImageAdapter ia = (ImageAdapter) gridview.getAdapter();
        return ia.getBoard().getScore();
    }


    //------------------------------------------------------------------------------------
    //  Control Setters
    //------------------------------------------------------------------------------------

    // Really pointless helper function that puts all my buttons/controls
    // initiators into one.
    private void setControls() {
        setGridControl();
        setLeftControl();
        setRightControl();
        setLeftDownControl();
        setRightDownControl();
        setLeftRotateControl();
        setRightRotateControl();
        setHold();
    }

    // Grid Control Initiator
    // Can rotate shape, reverse shape, and move down using swipe gestures.
    private void setGridControl() {
        gridview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                int action = event.getActionMasked();

                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = event.getX();
                        initialY = event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                    case MotionEvent.ACTION_UP:
                        float finalX = event.getX();
                        float finalY = event.getY();

                        if (initialX < finalX) {
                            if (finalX - initialX > 150) {
                                moveShape("rotate");
                            }
                        }

                        if (initialX > finalX) {
                            if (initialX - finalX > 150) {
                                moveShape("reverse");
                            }
                        }

                        if (initialY < finalY) {
                            if (finalY - initialY > 250) {
                                moveShape("down");
                            }
                        }

                        if (initialY > finalY) {
                            if (initialY - finalY > 250) {
                                moveShape("drop");
                            }
                        }

                        break;

                    case MotionEvent.ACTION_CANCEL:
                        break;

                    case MotionEvent.ACTION_OUTSIDE:
                        break;

                }

                return true;
            }
        });
    }

    // Left Control View Initiator
    // Can move shape left, down, and rotate using gestures.
    // Attempts to move left if you stay on the view (action_move is unreliable)
    private void setLeftControl() {
        leftview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getActionMasked();
                float finalX;
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        initialY = event.getY();
                        initialX = event.getX();
                        startTime = System.currentTimeMillis();
                        break;

                    case MotionEvent.ACTION_MOVE:
                        long time = System.currentTimeMillis() - startTime;
                        finalX = event.getX();
                        if ((time >= MAX_DURATION) && (finalX-initialX < 100)) {
                            moveShape("left");
                            startTime = System.currentTimeMillis();
                        }
                        break;

                    case MotionEvent.ACTION_UP:
                        float finalY = event.getY();
                        finalX = event.getX();

                        if (finalX - initialX > 200) {
                            moveShape("reverse");
                        } else if (initialY < finalY) {
                            if (finalY - initialY > 200) {
                                moveShape("down");
                            }
                        } else {
                            moveShape("left");
                        }
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        break;

                    case MotionEvent.ACTION_OUTSIDE:
                        break;

                }

                return true;
            }
        });
    }

    // Bottom Left Down View Initiator
    // Can be "pressed" to move shape down.
    // Can be "held" to attempt to move shape down at a fixed interval.
    // Action_move unreliable.
    private void setLeftDownControl() {
        leftdownview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getActionMasked();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        startTime = System.currentTimeMillis();
                        break;

                    case MotionEvent.ACTION_MOVE:
                        long time = System.currentTimeMillis() - startTime;
                        if (time >= MAX_DURATION) {
                            moveShape("down");
                            startTime = System.currentTimeMillis();
                        }

                        break;

                    case MotionEvent.ACTION_UP:
                        moveShape("down");
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        break;

                    case MotionEvent.ACTION_OUTSIDE:
                        break;

                }

                return true;
            }
        });
    }

    private void setLeftRotateControl() {
        leftrotateview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getActionMasked();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        break;

                    case MotionEvent.ACTION_MOVE:
                        break;

                    case MotionEvent.ACTION_UP:
                        moveShape("rotate");
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        break;

                    case MotionEvent.ACTION_OUTSIDE:
                        break;

                }

                return true;
            }
        });
    }

    // Right Control View Initiator
    // Can move shape right, down, and reverse using gestures.
    // Attempts to move right if you stay on the view (action_move is unreliable)
    private void setRightControl() {
        rightview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getActionMasked();
                float finalX;
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        initialY = event.getY();
                        initialX = event.getX();
                        startTime = System.currentTimeMillis();
                        break;

                    case MotionEvent.ACTION_MOVE:
                        long time = System.currentTimeMillis() - startTime;
                        finalX = event.getX();
                        if ((time >= MAX_DURATION) && (initialX - finalX < 100)) {
                            moveShape("right");
                            startTime = System.currentTimeMillis();
                        }
                        break;

                    case MotionEvent.ACTION_UP:
                        float finalY = event.getY();
                        finalX = event.getX();

                        if (initialX - finalX > 200) {
                            moveShape("rotate");
                        } else if (initialY < finalY) {
                            if (finalY - initialY > 250) {
                                moveShape("down");
                            }
                        } else {
                            moveShape("right");
                        }
                        break;

                    case MotionEvent.ACTION_CANCEL:
                        break;

                    case MotionEvent.ACTION_OUTSIDE:
                        break;

                }

                return true;
            }
        });
    }

    // Bottom Right Down View Initiator
    // Can be "pressed" to move shape down.
    // Can be "held" to attempt to move shape down at a fixed interval.
    // Action_move unreliable.
    private void setRightDownControl() {
        rightdownview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getActionMasked();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        startTime = System.currentTimeMillis();
                        break;

                    case MotionEvent.ACTION_MOVE:
                        long time = System.currentTimeMillis() - startTime;
                        if (time >= MAX_DURATION) {
                            moveShape("down");
                            startTime = System.currentTimeMillis();
                        }
                        break;

                    case MotionEvent.ACTION_UP:
                        moveShape("down");
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        break;

                    case MotionEvent.ACTION_OUTSIDE:
                        break;

                }

                return true;
            }
        });
    }

    private void setRightRotateControl() {
        rightrotateview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getActionMasked();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        break;

                    case MotionEvent.ACTION_MOVE:
                        break;

                    case MotionEvent.ACTION_UP:
                        moveShape("reverse");
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        break;

                    case MotionEvent.ACTION_OUTSIDE:
                        break;

                }

                return true;
            }
        });
    }

    // Used to swap the current piece and the hold piece
    // Only active while game is active.
    public void setHold() {
        holdview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                ImageAdapter la = (ImageAdapter) gridview.getAdapter();
                ;
                HoldAdapter ha = (HoldAdapter) holdview.getAdapter();
                active = la.getBoard().getActive();
                if (active) {
                    Block temphold = new Block(ha.getBoard().getBlock());
                    Block tempcur = new Block(la.getBoard().getBlock());

                    ha.update(tempcur.getShape(), la.getBoard().getHeld());
                    ha.notifyDataSetChanged();
                    la.update(temphold.getShape());
                    la.notifyDataSetChanged();
                }
            }
        });
    }

    //------------------------------------------------------------------------------------
    //  Button Functions
    //------------------------------------------------------------------------------------

    // Used to start game.
    // Will also pause/resume the game.
    public void start(View view) {
        startGame();
    }

    // Used to clear the grid and begin a new game.
    public void newGame(View view) {
        newGame();
    }

    public void newGame() {
        ImageAdapter la = (ImageAdapter) gridview.getAdapter();
        la.newGame();
        la.update();
        active = false;
        score = 0;
        la.notifyDataSetChanged();

        HoldAdapter ha = (HoldAdapter) holdview.getAdapter();
        ha.update();
        ha.notifyDataSetChanged();

        updateNext();
    }


    //------------------------------------------------------------------------------------
    //  Move and Rotate Functions
    //------------------------------------------------------------------------------------

    // Pass either movement or rotation to imageadapter to handle shape change.
    public void moveShape(String dir) {
        ImageAdapter la = (ImageAdapter) gridview.getAdapter();
        la.move(dir);
        la.notifyDataSetChanged();
    }

    //------------------------------------------------------------------------------------
    //  Useless and Inprogress Functions
    //------------------------------------------------------------------------------------

    private void updateNext() {
        ImageAdapter la = (ImageAdapter) gridview.getAdapter();

        NextAdapter na0 = (NextAdapter) nextview0.getAdapter();
        na0.update(la.getBoard().getNextBlocks()[0], la.getBoard().getHeld());
        na0.notifyDataSetChanged();

        NextAdapter na1 = (NextAdapter) nextview1.getAdapter();
        na1.update(la.getBoard().getNextBlocks()[1], la.getBoard().getHeld());
        na1.notifyDataSetChanged();

        NextAdapter na2 = (NextAdapter) nextview2.getAdapter();
        na2.update(la.getBoard().getNextBlocks()[2], la.getBoard().getHeld());
        na2.notifyDataSetChanged();
    }

    private void updateScore() {
        ImageAdapter la = (ImageAdapter) gridview.getAdapter();
        if (active) {
            int temp = la.getBoard().getScore();
            scoreview.setText(temp);
        }
    }


}
