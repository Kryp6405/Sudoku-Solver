package com.example.sudokusolver;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    SudokuBoard sudokuBoard;
    Solver solver;
    Button solve;
    ImageButton back;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sudokuBoard = findViewById(R.id.sudokuBoard);
        solver = sudokuBoard.getSolver();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Solver");
        solve = findViewById(R.id.solve);
        back = findViewById(R.id.back);

        solve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(solve.getText().toString().equals("Solve")){
                    solve.setText("Clear");

                    solver.getEmptyBoxIndexes();
                    new Thread(new Runnable() {
                        public void run() {
                            addDataToFirebase2(solver);
                            solver.solveBoard(sudokuBoard);
                            addDataToFirebase1(solver);
                        }
                    }).start();
                    sudokuBoard.invalidate();
                }
                else{
                    solve.setText("Solve");

                    solver.clearBoard();
                    sudokuBoard.invalidate();
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, HomePage.class));
                finish();
            }
        });
    }

    public void onClick(View view){
        solver.setNumAtPos(Integer.parseInt(((Button)view).getText().toString()));
        sudokuBoard.invalidate();
    }

    public void addDataToFirebase1(Solver solvedBoard){
        int [][] solvedArray = solvedBoard.getBoard();
        String data = "";
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
                data += solvedArray[i][j] +" ";
                System.out.print(solvedArray[i][j]+" ");
            }
            databaseReference.child("Solved Board").child("Row "+(i+1)).setValue(data);
            data = "";
            System.out.println();
        }
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Toast.makeText(MainActivity.this, "Data Saved Successfully", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void addDataToFirebase2(Solver solvedBoard){
        int [][] solvedArray = solvedBoard.getBoard();
        String data = "";
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
                data += solvedArray[i][j] +" ";
                System.out.print(solvedArray[i][j]+" ");
            }
            databaseReference.child("Input Board").child("Row "+(i+1)).setValue(data);
            data = "";
            System.out.println();
        }
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Toast.makeText(MainActivity.this, "Data Saved Successfully", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}