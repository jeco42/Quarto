package com.weebley.jeco42.quarto;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;

/**
 * Created by Justin on 4/16/2015.
 */
public class ResultsActivity extends ActionBarActivity{

    public static final String KEY_WINNER = "winner";
    public static final String KEY_HISTORY = "history";

    private int mWinner, index, numMoves;
    private String moveHistory;
    private TextView mWinnerTextview;
    private Button mPrevButton, mNextButton, mRematchButton, mQuitButton;
    private ImageView[][] mBoard;
    private HashMap<Character, Integer> mPMap;
    private int[] mPieceImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        mWinner = getIntent().getIntExtra(KEY_WINNER, 0);
        moveHistory = getIntent().getStringExtra(KEY_HISTORY);

        Log.d("winner", "received winner: " + mWinner);

        mBoard = new ImageView[4][4];
        mPieceImages = new int[16];
        mPMap = new HashMap<>();
        numMoves = moveHistory.length()/16;
        index = numMoves-1;
        Log.d("test", "numMoves: " + numMoves);
        Log.d("test", moveHistory);

        mWinnerTextview = (TextView)findViewById(R.id.winnerTextview);
        mPrevButton = (Button)findViewById(R.id.prev);
        mNextButton = (Button)findViewById(R.id.next);
        mRematchButton = (Button)findViewById(R.id.rematch);
        mQuitButton = (Button)findViewById(R.id.quit);

        //The board imageviews
        mBoard[0][0] = (ImageView)findViewById(R.id.imageView);      mBoard[0][1] = (ImageView)findViewById(R.id.imageView2);
        mBoard[0][2] = (ImageView)findViewById(R.id.imageView3);     mBoard[0][3] = (ImageView)findViewById(R.id.imageView4);
        mBoard[1][0] = (ImageView)findViewById(R.id.imageView5);     mBoard[1][1] = (ImageView)findViewById(R.id.imageView6);
        mBoard[1][2] = (ImageView)findViewById(R.id.imageView7);     mBoard[1][3] = (ImageView)findViewById(R.id.imageView8);
        mBoard[2][0] = (ImageView)findViewById(R.id.imageView9);     mBoard[2][1] = (ImageView)findViewById(R.id.imageView10);
        mBoard[2][2] = (ImageView)findViewById(R.id.imageView11);    mBoard[2][3] = (ImageView)findViewById(R.id.imageView12);
        mBoard[3][0] = (ImageView)findViewById(R.id.imageView13);    mBoard[3][1] = (ImageView)findViewById(R.id.imageView14);
        mBoard[3][2] = (ImageView)findViewById(R.id.imageView15);    mBoard[3][3] = (ImageView)findViewById(R.id.imageView16);

        //The actual piece images.  These are placed into the board imageviews
        mPieceImages[0] = R.drawable.piece0;    mPieceImages[1] = R.drawable.piece1;    mPieceImages[2] = R.drawable.piece2;    mPieceImages[3] = R.drawable.piece3;
        mPieceImages[4] = R.drawable.piece4;    mPieceImages[5] = R.drawable.piece5;    mPieceImages[6] = R.drawable.piece6;    mPieceImages[7] = R.drawable.piece7;
        mPieceImages[8] = R.drawable.piece8;    mPieceImages[9] = R.drawable.piece9;    mPieceImages[10] = R.drawable.piece10;  mPieceImages[11] = R.drawable.piece11;
        mPieceImages[12] = R.drawable.piece12;  mPieceImages[13] = R.drawable.piece13;  mPieceImages[14] = R.drawable.piece14;  mPieceImages[15] = R.drawable.piece15;

        mPMap.put('z', 0); mPMap.put('y', 1); mPMap.put('v', 2); mPMap.put('u', 3);
        mPMap.put('j', 4); mPMap.put('i', 5); mPMap.put('f', 6); mPMap.put('e', 7);
        mPMap.put(':', 8); mPMap.put('9', 9); mPMap.put('6', 10); mPMap.put('5', 11);
        mPMap.put('*', 12); mPMap.put(')', 13); mPMap.put('&', 14); mPMap.put('%', 15);

        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index--;
                showPosition();
            }
        });

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index++;
                showPosition();
            }
        });

        mRematchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rematch();
            }
        });

        mQuitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quit();
            }
        });

        if(mWinner == 1) {
            mWinnerTextview.setText(R.string.p1wins);
        }
        else if(mWinner == 2){
            mWinnerTextview.setText(R.string.p2wins);
        }
        else{
            mWinnerTextview.setText(R.string.draw);
        }

        showPosition();
    }

    private void showPosition() {
        if(index < 0) {
            index = 0;
            return;
        }
        else if(index == numMoves){
            index = numMoves - 1;
            return;
        }

        String current = moveHistory.substring(index*16, index*16+16);
        Integer x;
        for(int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                x = mPMap.get(current.charAt(i * 4 + j));
                if(x != null)
                    mBoard[i][j].setImageResource(mPieceImages[x]);
                else
                    mBoard[i][j].setImageResource(R.drawable.blank);
            }
        }
    }

    private void rematch() {
        Intent i = new Intent(ResultsActivity.this, GameActivity.class);
        finish();
        startActivity(i);
    }

    private void quit(){
        finish();
    }


}
