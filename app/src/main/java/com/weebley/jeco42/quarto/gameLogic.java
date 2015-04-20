package com.weebley.jeco42.quarto;

import android.util.Log;

/**
 * Created by Justin on 4/3/2015.
 */
public class gameLogic {
    final static int[] pieces = {170, 169, 166, 165,
                                154, 153, 150, 149,
                                106, 105, 102, 101,
                                90,   89,  86,  85};

    private boolean[] played;
    private int[][] board;
    private int gameover;
    private int round;
    private String state;

    public gameLogic(){
        played = new boolean[16];
        board = new int[4][4];
        gameover = 0;
        round = 0;

        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){
                board[i][j] = 0;
                played[i*4+j] = false;
            }
        }
    }

    //Attempts to place piece p on the board at r,c
    //returns true on success, false on fail
    public boolean place(int p, int r, int c){
        if(played[p])
            return false;
        if(board[r][c] != 0)
            return false;

        played[p] = true;
        board[r][c] = pieces[p];
        round++;
        saveState();
        Log.d("GAME_LOGIC", state);
        return true;
    }

    //checks if the board is in a winning state
    //returns 1 if a winning condition has been met, 2 if draw, 0 if not
    public int checkWinner(){
        //10 checks to do
        for(int i = 0; i < 4; i++){
            if((board[i][0] & board[i][1] & board[i][2] & board[i][3]) != 0)
                return gameover = 1;
            if((board[0][i] & board[1][i] & board[2][i] & board[3][i]) != 0)
                return gameover = 1;
        }
        if((board[0][0] & board[1][1] & board[2][2] & board[3][3]) != 0)
            return gameover = 1;
        if((board[0][3] & board[1][2] & board[2][1] & board[3][0]) != 0)
            return gameover = 1;
        if(round >= 16)
            return gameover = 2;
        return gameover = 0;
    }

    public boolean canGive(int p){
        if (p < 0) return false;
        return !played[p];
    }

    public void loadState(String s){
        //reset game to new state
        resetBoard();

        //fill in board
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){
                if(s.charAt(i*4+j) != '_'){
                    board[i][j] = (int)(s.charAt(i*4+j)) + 48;
                    played[i*4+j] = true;
                    round++;
                }
                else{
                    board[i][j] = 0;
                }
            }
        }

        saveState();
    }

    public String saveState(){
        state = "";

        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){
                if(board[i][j] != 0){
                    state += (char)(board[i][j] - 48);
                }
                else{
                    state += "_";
                }
            }
        }

        return state;
    }

    public String getCurrentState(){ return state; }

    public void resetBoard(){

        round = 0;
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){
                board[i][j] = 0;
                played[i*4+j] = false;
            }
        }
    }

}
