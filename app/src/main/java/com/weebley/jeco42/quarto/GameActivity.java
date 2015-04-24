package com.weebley.jeco42.quarto;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Justin on 4/3/2015.
 */
public class GameActivity extends ActionBarActivity {

    private static final String TAG = "GameActivity";
    private static final String KEY_WINNER = "winner";
    private static final String KEY_HISTORY = "history";
    public static final String KEY_MODE = "mode";

    private gameLogic mGame;
    private ImageView[][] mBoardButtons;
    private ImageView[] mPieceButtons;
    private Button mGiveButton;
    private Button mPlaceButton;
    private ImageView mCurrentPieceView;
    private TextView mTurnView;
    private int[] mPieceImages;
    private int[] mTurnMessages;
    private int row, col, turn, mWinner;
    private int piece = -1;
    private HashMap<Character, Integer> mPMap;
    private String moveHistory;
    private int mGameMode;
    private int[] aiMove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        mGameMode = getIntent().getIntExtra(KEY_MODE, 0);
        mGiveButton = (Button)findViewById(R.id.give_button);
        mPlaceButton = (Button)findViewById(R.id.place_button);
        mCurrentPieceView = (ImageView)findViewById(R.id.current_piece);
        mTurnView = (TextView)findViewById(R.id.turn);
        mBoardButtons = new ImageView[4][4];
        mPieceButtons = new ImageView[16];
        mPieceImages = new int[16];
        mTurnMessages = new int[4];
        mPMap = new HashMap<Character, Integer>();
        moveHistory = "";
        aiMove = new int[3];

        //The gameboard imageviews
        mBoardButtons[0][0] = (ImageView)findViewById(R.id.imageButton);      mBoardButtons[0][1] = (ImageView)findViewById(R.id.imageButton2);
        mBoardButtons[0][2] = (ImageView)findViewById(R.id.imageButton3);     mBoardButtons[0][3] = (ImageView)findViewById(R.id.imageButton4);
        mBoardButtons[1][0] = (ImageView)findViewById(R.id.imageButton5);     mBoardButtons[1][1] = (ImageView)findViewById(R.id.imageButton6);
        mBoardButtons[1][2] = (ImageView)findViewById(R.id.imageButton7);     mBoardButtons[1][3] = (ImageView)findViewById(R.id.imageButton8);
        mBoardButtons[2][0] = (ImageView)findViewById(R.id.imageButton9);     mBoardButtons[2][1] = (ImageView)findViewById(R.id.imageButton10);
        mBoardButtons[2][2] = (ImageView)findViewById(R.id.imageButton11);    mBoardButtons[2][3] = (ImageView)findViewById(R.id.imageButton12);
        mBoardButtons[3][0] = (ImageView)findViewById(R.id.imageButton13);    mBoardButtons[3][1] = (ImageView)findViewById(R.id.imageButton14);
        mBoardButtons[3][2] = (ImageView)findViewById(R.id.imageButton15);    mBoardButtons[3][3] = (ImageView)findViewById(R.id.imageButton16);

        //The piece-pool imageviews
        mPieceButtons[0] = (ImageView)findViewById(R.id.imageButton17);   mPieceButtons[1] = (ImageView)findViewById(R.id.imageButton18);
        mPieceButtons[2] = (ImageView)findViewById(R.id.imageButton19);   mPieceButtons[3] = (ImageView)findViewById(R.id.imageButton20);
        mPieceButtons[4] = (ImageView)findViewById(R.id.imageButton21);   mPieceButtons[5] = (ImageView)findViewById(R.id.imageButton22);
        mPieceButtons[6] = (ImageView)findViewById(R.id.imageButton23);   mPieceButtons[7] = (ImageView)findViewById(R.id.imageButton24);
        mPieceButtons[8] = (ImageView)findViewById(R.id.imageButton25);   mPieceButtons[9] = (ImageView)findViewById(R.id.imageButton26);
        mPieceButtons[10] = (ImageView)findViewById(R.id.imageButton27);  mPieceButtons[11] = (ImageView)findViewById(R.id.imageButton28);
        mPieceButtons[12] = (ImageView)findViewById(R.id.imageButton29);  mPieceButtons[13] = (ImageView)findViewById(R.id.imageButton30);
        mPieceButtons[14] = (ImageView)findViewById(R.id.imageButton31);  mPieceButtons[15] = (ImageView)findViewById(R.id.imageButton32);

        //The actual piece images.  These are placed into the gameboard and piece-pool imageviews
        mPieceImages[0] = R.drawable.piece0;    mPieceImages[1] = R.drawable.piece1;    mPieceImages[2] = R.drawable.piece2;    mPieceImages[3] = R.drawable.piece3;
        mPieceImages[4] = R.drawable.piece4;    mPieceImages[5] = R.drawable.piece5;    mPieceImages[6] = R.drawable.piece6;    mPieceImages[7] = R.drawable.piece7;
        mPieceImages[8] = R.drawable.piece8;    mPieceImages[9] = R.drawable.piece9;    mPieceImages[10] = R.drawable.piece10;  mPieceImages[11] = R.drawable.piece11;
        mPieceImages[12] = R.drawable.piece12;  mPieceImages[13] = R.drawable.piece13;  mPieceImages[14] = R.drawable.piece14;  mPieceImages[15] = R.drawable.piece15;

        mTurnMessages[0] = R.string.p1turnGive;
        mTurnMessages[1] = R.string.p2turnPlace;
        mTurnMessages[2] = R.string.p2turnGive;
        mTurnMessages[3] = R.string.p1turnPlace;

        mPMap.put('z', 0); mPMap.put('y', 1); mPMap.put('v', 2); mPMap.put('u', 3);
        mPMap.put('j', 4); mPMap.put('i', 5); mPMap.put('f', 6); mPMap.put('e', 7);
        mPMap.put(':', 8); mPMap.put('9', 9); mPMap.put('6', 10); mPMap.put('5', 11);
        mPMap.put('*', 12); mPMap.put(')', 13); mPMap.put('&', 14); mPMap.put('%', 15);

        mGiveButton.setText(R.string.give);
        mPlaceButton.setText(R.string.place);

        for(int i = 0; i < 4; i++) {
            for(int j = 0; j < 4; j++) {
                final int innerI = i;
                final int innerJ = j;
                mBoardButtons[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        row = innerI;
                        col = innerJ;
                        //EXPERIMENTING WITH HIGHLIGHTING SELECTED SQUARE
                        mBoardButtons[innerI][innerJ].setColorFilter(Color.parseColor("#FFFF33"), PorterDuff.Mode.DARKEN);

                        Timer t = new Timer();
                        t.schedule(new TimerTask() {

                            @Override
                            public void run() {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mBoardButtons[innerI][innerJ].clearColorFilter();
                                    }
                                });
                            }
                        }, 500);
                    }
                });
                mPieceButtons[i*4+j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        piece = innerI*4+innerJ;
                        showSelection();
                    }
                });
            }
        }

        mPlaceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placePiece();
            }
        });

        mGiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                givePiece();
            }
        });

        mGame = new gameLogic();
        hardReset();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        savedInstanceState.putString("GAME_STATE", mGame.getCurrentState());
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);

        String gameState = savedInstanceState.getString("GAME_STATE");
        if (gameState != null) {
            loadState(gameState);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.new_game) {
            hardReset();
            return true;
        }

        if (id == R.id.quit_game){
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadState(String state){
        hardReset();        //reset the board first
        mGame.loadState(state); //load state into the gameLogic object

        //Set board and picture pieces to reflect the state
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){
                Character c = state.charAt(i*4+j);
                if(c != '_') {
                    mBoardButtons[i][j].setImageResource(mPieceImages[mPMap.get(c)]);
                    mPieceButtons[i*4+j].setImageResource(R.drawable.blank);
                    toggleTurn();
                    toggleTurn();
                }
            }
        }
    }

    private void enablePieceSelection(){
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++) {
                mPieceButtons[i*4+j].setEnabled(true);
                mBoardButtons[i][j].setEnabled(false);
            }
        }
    }

    private void enableBoardPlacement(){
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++) {
                mPieceButtons[i*4+j].setEnabled(false);
                mBoardButtons[i][j].setEnabled(true);
            }
        }
    }

    private void checkWinner() {
        int check = mGame.checkWinner();
        if(check==1) {
            if (turn % 4 == 0) {
                mTurnView.setText(R.string.p1wins);
                mWinner = 1; //p1 won
                Log.d("test1", "in p1 won condition: " + mWinner);
            }
            else{
                mTurnView.setText(R.string.p2wins);
                mWinner = 2; //p2 won
                Log.d("test1", "in p2 won condition: " + mWinner);
            }
            //add logic to launch gameover screen
            launchResults();
            return;
        }
        else if(check==2){
            mTurnView.setText(R.string.draw);
            mWinner = 0; //draw
            launchResults();
            return;
        }
    }

    private void toggleTurn(){
        checkWinner();

        mTurnView.setText(mTurnMessages[turn]);

        if(mGameMode==0) {
            if (turn % 2 == 0) {
                enablePieceSelection();
            } else {
                enableBoardPlacement();
            }
            turn = ++turn % 4;
        }
        else if(mGameMode==1){
            if(turn % 4 == 0){
                enablePieceSelection();
                Log.d("GAME_LOGIC", "insde ==0: "+turn);
                turn = ++turn%4;
            }
            else if(turn % 4 == 1){
                aiMove = mGame.easyAImove(piece);
                row = aiMove[0];
                col = aiMove[1];
                Log.d("GAME_LOGIC", "insde ==1: "+turn);
                turn = ++turn%4;
                aiPlacePiece();
                checkWinner();
                piece = aiMove[2];
                turn = (turn+2)%4;
                hidePiece();
                showSelection();
                enableBoardPlacement();
            }
            else{
                enableBoardPlacement();
                Log.d("GAME_LOGIC", "insde else: "+turn);
                turn = ++turn%4;
            }
        }
        Log.d("GAME_LOGIC", "Turn: "+turn);
    }

    private void givePiece(){
        if(turn%2 == 1) {
            if (mGame.canGive(piece)) {
                hidePiece();
                toggleTurn();
            } else {
                //display toast for invalid piece selection..
            }
        }
    }

    private void hidePiece(){
        mPieceButtons[piece].setImageResource(R.drawable.blank);
    }

    private void aiPlacePiece(){
        mGame.place(piece, row, col);
        moveHistory += mGame.getCurrentState();
        Log.d("in  ai place", moveHistory);
        mBoardButtons[row][col].setImageResource(mPieceImages[piece]);
        mCurrentPieceView.setImageResource(R.drawable.blank);
    }


    private void placePiece(){
        if(turn%2 == 0) {
            if (mGame.place(piece, row, col)) {
                moveHistory += mGame.getCurrentState();
                Log.d("in gameActivity", moveHistory);
                mBoardButtons[row][col].setImageResource(mPieceImages[piece]);
                mCurrentPieceView.setImageResource(R.drawable.blank);
                toggleTurn();
            } else {
                //display a toast for illegal move
            }
        }
    }

    private void showSelection(){
        if(mGame.canGive(piece))
            mCurrentPieceView.setImageResource(mPieceImages[piece]);
        else{
            mCurrentPieceView.setImageResource(R.drawable.blank);
        }
    }

    private void softReset(){
        for(int i = 0; i < mBoardButtons.length; i++){
            for(int j = 0; j < mBoardButtons[i].length; j++){
                //add drawable resources!
                mBoardButtons[i][j].setImageResource(R.drawable.blank);
                mPieceButtons[i*4+j].setImageResource(mPieceImages[i*4+j]);
            }
        }
    }

    private void hardReset(){
        softReset();
        moveHistory = "";
        mGame.resetBoard();
        turn = 0;
        toggleTurn();
    }

    private void launchResults(){
        //launch a results activity with the winner and move history
        Intent i = new Intent(GameActivity.this, ResultsActivity.class);
        i.putExtra(ResultsActivity.KEY_WINNER, mWinner);
        Log.d("winner?", "The winner was " + mWinner);
        i.putExtra(ResultsActivity.KEY_HISTORY, moveHistory);
        i.putExtra(ResultsActivity.KEY_MODE, mGameMode);
        finish();
        startActivity(i);
    }
}
