package com.example.sudokusolver;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class SudokuBoard extends View {
    final int boardColor;
    final int cellFillColor;
    final int cellsHighlightColor;

    final int letterColor;
    final int letterColorSolve;

    final Paint boardColorPaint = new Paint();
    final Paint cellFillColorPaint = new Paint();
    final Paint cellsHighlightColorPaint = new Paint();

    final Paint letterPaint = new Paint();
    final Rect letterPaintBounds = new Rect();
    int cellSize;

    final Solver solver = new Solver();

    public SudokuBoard(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SudokuBoard, 0, 0);
        try{
            boardColor = typedArray.getInteger(R.styleable.SudokuBoard_boardColor, 0);
            cellFillColor = typedArray.getInteger(R.styleable.SudokuBoard_cellFillColor, 0);
            cellsHighlightColor = typedArray.getInteger(R.styleable.SudokuBoard_cellsHighlightColor, 0);
            letterColor = typedArray.getInteger(R.styleable.SudokuBoard_letterColor, 0);
            letterColorSolve = typedArray.getInteger(R.styleable.SudokuBoard_letterColorSolve, 0);

        }finally {
            typedArray.recycle();
        }
    }

    @Override
    protected void onMeasure(int width, int height){
        super.onMeasure(width, height);

        int dim = Math.min(this.getMeasuredWidth(), this.getMeasuredHeight());
        cellSize = dim / 9;

        setMeasuredDimension(dim, dim);
    }

    @Override
    protected void onDraw(Canvas canvas){
        boardColorPaint.setStyle(Paint.Style.STROKE);
        boardColorPaint.setStrokeWidth(16);
        boardColorPaint.setColor(boardColor);
        boardColorPaint.setAntiAlias(true);

        cellFillColorPaint.setStyle(Paint.Style.FILL);
        cellFillColorPaint.setColor(cellFillColor);
        cellFillColorPaint.setAntiAlias(true);

        cellsHighlightColorPaint.setStyle(Paint.Style.FILL);
        cellsHighlightColorPaint.setColor(cellsHighlightColor);
        cellsHighlightColorPaint.setAntiAlias(true);

        letterPaint.setStyle(Paint.Style.FILL);
        letterPaint.setColor(letterColor);
        letterPaint.setAntiAlias(true);


        colorCell(canvas, solver.getSelRow(), solver.getSelCol());
        canvas.drawRect(0, 0, getWidth(), getHeight(), boardColorPaint);
        drawBoard(canvas);
        drawNumbers(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent){
        boolean isValid;

        float x = motionEvent.getX();
        float y = motionEvent.getY();

        int action = motionEvent.getAction();

        if(action == MotionEvent.ACTION_DOWN){
            solver.setSelRow((int)Math.ceil(y/cellSize));
            solver.setSelCol((int)Math.ceil(x/cellSize));
            isValid = true;
        }
        else{
            isValid = false;
        }

        return isValid;
    }

    public void drawNumbers(Canvas canvas){
        letterPaint.setTextSize(cellSize);

        for(int row = 0; row < 9; row++){
            for(int col = 0; col < 9; col++){
                if(solver.getBoard()[row][col] != 0){
                    String text = Integer.toString(solver.getBoard()[row][col]);
                    float width, height;

                    letterPaint.getTextBounds(text, 0, text.length(), letterPaintBounds);
                    width = letterPaint.measureText(text);
                    height = letterPaintBounds.height();

                    canvas.drawText(text, (col*cellSize)+((cellSize - width)/2), (row*cellSize + cellSize) - ((cellSize - height)/2), letterPaint);
                }
            }
        }

        letterPaint.setColor(letterColorSolve);

        for(ArrayList<Object> letter: solver.getEmptyBoxIndex()){
            int row = (int) letter.get(0);
            int col = (int) letter.get(1);

            String text = Integer.toString(solver.getBoard()[row][col]);
            float width, height;

            letterPaint.getTextBounds(text, 0, text.length(), letterPaintBounds);
            width = letterPaint.measureText(text);
            height = letterPaintBounds.height();

            canvas.drawText(text, (col*cellSize)+((cellSize - width)/2), (row*cellSize + cellSize) - ((cellSize - height)/2), letterPaint);
        }
    }

    public void colorCell(Canvas canvas, int row, int col){
        if(solver.getSelRow() != -1 && solver.getSelCol() != -1){
            canvas.drawRect((col-1)*cellSize, 0, col*cellSize, cellSize*9, cellsHighlightColorPaint);
            canvas.drawRect(0, (row-1)*cellSize, cellSize*9, row*cellSize, cellsHighlightColorPaint);
            canvas.drawRect((col-1)*cellSize, (row-1)*cellSize, col*cellSize, row*cellSize, cellsHighlightColorPaint);
        }

        invalidate();
    }

    public void drawThickLine(){
        boardColorPaint.setStyle(Paint.Style.STROKE);
        boardColorPaint.setStrokeWidth(10);
        boardColorPaint.setColor(boardColor);
    }

    public void drawThinLine(){
        boardColorPaint.setStyle(Paint.Style.STROKE);
        boardColorPaint.setStrokeWidth(4);
        boardColorPaint.setColor(boardColor);
    }

    public void drawBoard(Canvas canvas){
        for(int row = 0; row < 10; row++){
            if(row%3 == 0){
                drawThickLine();
            }
            else{
                drawThinLine();
            }

            canvas.drawLine(0, cellSize*row, getHeight(), cellSize*row, boardColorPaint);
        }

        for(int col = 0; col < 10; col++){
            if(col%3 == 0){
                drawThickLine();
            }
            else{
                drawThinLine();
            }

            canvas.drawLine(cellSize*col, 0, cellSize*col, getWidth(), boardColorPaint);
        }
    }

    public Solver getSolver(){
        return solver;
    }
}
