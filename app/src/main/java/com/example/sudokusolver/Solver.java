package com.example.sudokusolver;

import java.util.ArrayList;

public class Solver {
    int selRow;
    int selCol;
    int [][] board;
    ArrayList<ArrayList<Object>> emptyBoxIndex;

    public Solver() {
        selRow = -1;
        selCol = -1;

        board = new int[9][9];
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                board[row][col] = 0;
            }
        }


        emptyBoxIndex = new ArrayList<>();
    }

    public void setNumAtPos(int num){
        if(selRow != -1 && selCol != -1){
            if(board[selRow-1][selCol-1] == num){
                board[selRow-1][selCol-1] = 0;
            }
            else{
                board[selRow-1][selCol-1] = num;
            }
        }
    }

    public int[][] getBoard(){
        return board;
    }

    public void getEmptyBoxIndexes(){
        for(int row = 0; row < 9; row++){
            for(int col = 0; col < 9; col++){
                if (board[row][col] == 0) {
                    emptyBoxIndex.add(new ArrayList<>());
                    emptyBoxIndex.get(emptyBoxIndex.size()-1).add(row);
                    emptyBoxIndex.get(emptyBoxIndex.size()-1).add(col);
                }
            }
        }
    }

    public ArrayList<ArrayList<Object>> getEmptyBoxIndex(){
        return emptyBoxIndex;
    }

    public int getSelRow() {
        return selRow;
    }

    public int getSelCol() {
        return selCol;
    }

    public void setSelRow(int selRow){
        this.selRow = selRow;
    }

    public void setSelCol(int selCol) {
        this.selCol = selCol;
    }

    public boolean isInRow(int[][] board, int num, int row){
        for(int i = 0; i < 9; i++){
            if(board[row][i] == num){
                return true;
            }
        }
        return false;
    }

    public boolean isInCol(int[][] board, int num, int col){
        for(int i = 0; i < 9; i++){
            if(board[i][col] == num){
                return true;
            }
        }
        return false;
    }

    public boolean isInBox(int[][] board, int num, int row, int col){
        int locBoxRow, locBoxCol;
        locBoxRow = row - row%3;
        locBoxCol = col - col%3;

        for(int i = locBoxRow; i < (locBoxRow+3); i++){
            for(int j = locBoxCol; j < (locBoxCol+3); j++){
                if (board[i][j] == num) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isValid(int[][] board, int num, int row, int col){
        return !isInRow(board, num, row) && !isInCol(board, num, col) && !isInBox(board, num, row, col);
    }

    public boolean solveBoard(SudokuBoard display){
        for(int row = 0; row < 9; row++){
            for(int col = 0; col < 9; col++){
                if(board[row][col] == 0){
                    for(int n = 1; n <= 9; n++){
                        if(isValid(board, n, row, col)){
                            board[row][col] = n;
                            display.invalidate();

                            if(solveBoard(display)){
                                return true;
                            }
                            else{
                                board[row][col] = 0;
                            }
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    public void clearBoard(){
        for(int row = 0; row < 9; row++){
            for(int col = 0; col < 9; col++){
                board[row][col] = 0;
            }
        }

        emptyBoxIndex = new ArrayList<>();
    }
}
