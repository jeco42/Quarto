package com.weebley.jeco42.quarto;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;

/**
 * Created by Justin on 4/3/2015.
 */
public class GameActivity extends ActionBarActivity {

    private gameLogic mGame;

    private ImageButton[][] mBoardButtons;
    private ImageButton[] mPieceButtons;
    private Button mGiveButton;
    private Button mPlaceButton;
    private ImageView mCurrentPieceView;
    private TextView mTurnView;

    private int[] mPieceImages;
    private int[] mTurnMessages;
    private int row, col, piece, turn;
    private HashMap<Character, Integer> mPMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        mGiveButton = (Button)findViewById(R.id.give_button);
        mPlaceButton = (Button)findViewById(R.id.place_button);
        mCurrentPieceView = (ImageView)findViewById(R.id.current_piece);
        mTurnView = (TextView)findViewById(R.id.turn);
        mBoardButtons = new ImageButton[4][4];
        mPieceButtons = new ImageButton[16];
        mPieceImages = new int[16];
        mTurnMessages = new int[4];
        mPMap = new HashMap<Character, Integer>();

        mBoardButtons[0][0] = (ImageButton)findViewById(R.id.imageButton);      mBoardButtons[0][1] = (ImageButton)findViewById(R.id.imageButton2);
        mBoardButtons[0][2] = (ImageButton)findViewById(R.id.imageButton3);     mBoardButtons[0][3] = (ImageButton)findViewById(R.id.imageButton4);
        mBoardButtons[1][0] = (ImageButton)findViewById(R.id.imageButton5);     mBoardButtons[1][1] = (ImageButton)findViewById(R.id.imageButton6);
        mBoardButtons[1][2] = (ImageButton)findViewById(R.id.imageButton7);     mBoardButtons[1][3] = (ImageButton)findViewById(R.id.imageButton8);
        mBoardButtons[2][0] = (ImageButton)findViewById(R.id.imageButton9);     mBoardButtons[2][1] = (ImageButton)findViewById(R.id.imageButton10);
        mBoardButtons[2][2] = (ImageButton)findViewById(R.id.imageButton11);    mBoardButtons[2][3] = (ImageButton)findViewById(R.id.imageButton12);
        mBoardButtons[3][0] = (ImageButton)findViewById(R.id.imageButton13);    mBoardButtons[3][1] = (ImageButton)findViewById(R.id.imageButton14);
        mBoardButtons[3][2] = (ImageButton)findViewById(R.id.imageButton15);    mBoardButtons[3][3] = (ImageButton)findViewById(R.id.imageButton16);

        mPieceButtons[0] = (ImageButton)findViewById(R.id.imageButton17);   mPieceButtons[1] = (ImageButton)findViewById(R.id.imageButton18);
        mPieceButtons[2] = (ImageButton)findViewById(R.id.imageButton19);   mPieceButtons[3] = (ImageButton)findViewById(R.id.imageButton20);
        mPieceButtons[4] = (ImageButton)findViewById(R.id.imageButton21);   mPieceButtons[5] = (ImageButton)findViewById(R.id.imageButton22);
        mPieceButtons[6] = (ImageButton)findViewById(R.id.imageButton23);   mPieceButtons[7] = (ImageButton)findViewById(R.id.imageButton24);
        mPieceButtons[8] = (ImageButton)findViewById(R.id.imageButton25);   mPieceButtons[9] = (ImageButton)findViewById(R.id.imageButton26);
        mPieceButtons[10] = (ImageButton)findViewById(R.id.imageButton27);  mPieceButtons[11] = (ImageButton)findViewById(R.id.imageButton28);
        mPieceButtons[12] = (ImageButton)findViewById(R.id.imageButton29);  mPieceButtons[13] = (ImageButton)findViewById(R.id.imageButton30);
        mPieceButtons[14] = (ImageButton)findViewById(R.id.imageButton31);  mPieceButtons[15] = (ImageButton)findViewById(R.id.imageButton32);

        mPieceImages[0] = R.drawable.piece0;    mPieceImages[1] = R.drawable.piece1;    mPieceImages[2] = R.drawable.piece2;    mPieceImages[3] = R.drawable.piece3;
        mPieceImages[4] = R.drawable.piece4;    mPieceImages[5] = R.drawable.piece5;    mPieceImages[6] = R.drawable.piece6;    mPieceImages[7] = R.drawable.piece7;
        mPieceImages[8] = R.drawable.piece8;    mPieceImages[9] = R.drawable.piece9;    mPieceImages[10] = R.drawable.piece10;  mPieceImages[11] = R.drawable.piece11;
        mPieceImages[12] = R.drawable.piece12;  mPieceImages[13] = R.drawable.piece13;  mPieceImages[14] = R.drawable.piece14;  mPieceImages[15] = R.drawable.piece15;

        mTurnMessages[0] = R.string.p1turnGive; mTurnMessages[1] = R.string.p2turnPlace; mTurnMessages[2] = R.string.p2turnGive; mTurnMessages[3] = R.string.p1turnPlace;

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
        turn = 0;
        newGame();
    }

    private void loadState(String state){
        //reset the board
        newGame();

        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){
                Character c = state.charAt(i*4+j);
                if(c != '_') {
                    mBoardButtons[i][j].setImageResource(mPieceImages[mPMap.get(c)]);
                    mPieceButtons[i*4+j].setImageResource(R.drawable.blank);
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

    private void toggleTurn(){
        int check = mGame.checkWinner();
        if(check==1) {
            if (turn % 2 == 0) {
                mTurnView.setText(R.string.p1wins);
            }
            else{
                mTurnView.setText(R.string.p2wins);
            }
            //add logic to launch gameover screen
            return;
        }
        else if(check==2){
            mTurnView.setText(R.string.p2wins);
            return;
        }

        mTurnView.setText(mTurnMessages[turn]);
        if(turn%2 == 0){
            enablePieceSelection();
        }
        else{
            enableBoardPlacement();
        }
        turn = ++turn%4;
    }

    private void givePiece(){

        if(mGame.canGive(piece)){
            toggleTurn();
        }
        else{
            //display toast for invalid piece selection..
        }
    }

    private void placePiece(){
        if(mGame.place(piece, row, col)) {
            mBoardButtons[row][col].setImageResource(mPieceImages[piece]);
            mPieceButtons[piece].setImageResource(R.drawable.blank);
            mCurrentPieceView.setImageResource(R.drawable.blank);
            toggleTurn();
        }
        else{
            //display a toast for illegal move
        }

    }

    private void showSelection(){
        if(mGame.canGive(piece))
            mCurrentPieceView.setImageResource(mPieceImages[piece]);
        else{
            mCurrentPieceView.setImageResource(R.drawable.blank);
        }
    }

    private void newGame(){
        for(int i = 0; i < mBoardButtons.length; i++){
            for(int j = 0; j < mBoardButtons[i].length; j++){
                //add drawable resources!
                mBoardButtons[i][j].setImageResource(R.drawable.blank);
                mPieceButtons[i*4+j].setImageResource(mPieceImages[i*4+j]);
            }
        }
        toggleTurn();
    }

}
