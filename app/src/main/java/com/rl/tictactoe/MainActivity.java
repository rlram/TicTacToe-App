package com.rl.tictactoe;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private ImageView ivDropUpX;
    private ImageView ivDropUpO;
    private final TextView[][] tv = new TextView[3][3];
    private boolean playerXTurn = true;
    private int roundCount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ivDropUpX = findViewById(R.id.ivDropUpX);
        ivDropUpO = findViewById(R.id.ivDropUpO);

        ImageView ivReset = findViewById(R.id.ivReset);

        tv[0][0] = findViewById(R.id.tv00);
        tv[0][1] = findViewById(R.id.tv01);
        tv[0][2] = findViewById(R.id.tv02);
        tv[1][0] = findViewById(R.id.tv10);
        tv[1][1] = findViewById(R.id.tv11);
        tv[1][2] = findViewById(R.id.tv12);
        tv[2][0] = findViewById(R.id.tv20);
        tv[2][1] = findViewById(R.id.tv21);
        tv[2][2] = findViewById(R.id.tv22);


        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                final int row = i;
                final int col = j;
                tv[i][j].setOnClickListener(v -> handleMove(row, col));
            }
        }

        ivReset.setOnClickListener(v -> {
            resetGame();
        });
    }

    private void handleMove(int row, int col) {
        TextView cell = tv[row][col];

        if (!cell.getText().toString().isEmpty()) return;

        cell.setText(playerXTurn? "X": "O");
        roundCount++;

        if (checkForWin()) {
            showWinner(playerXTurn? "X": "O");
        } else if (roundCount == 9) {
            showDialog("It's a Draw!");
        } else {
            playerXTurn = !playerXTurn;
            updateTurnIcon();
        }
    }

    private void updateTurnIcon() {
        if (playerXTurn) {
            ivDropUpX.setVisibility(View.VISIBLE);
            ivDropUpO.setVisibility(View.INVISIBLE);
        } else {
            ivDropUpX.setVisibility(View.INVISIBLE);
            ivDropUpO.setVisibility(View.VISIBLE);
        }
    }

    private boolean checkForWin() {
        String[][] field = new String[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                field[i][j] = tv[i][j].getText().toString();
            }
        }

        for (int i = 0; i < 3; i++) {
            if (field[i][0].equals(field[i][1]) &&
                    field[i][0].equals(field[i][2]) &&
                    !field[i][0].isEmpty()) return true;

            if (field[0][i].equals(field[1][i]) &&
                    field[0][i].equals(field[2][i]) &&
                    !field[0][i].isEmpty()) return true;
        }

        if (field[0][0].equals(field[1][1]) &&
                field[0][0].equals(field[2][2]) &&
                !field[0][0].isEmpty()) return true;

        if (field[0][2].equals(field[1][1]) &&
                field[0][2].equals(field[2][0]) &&
                !field[0][2].isEmpty()) return true;


        return false;
    }

    private void showWinner(String s) {
        showDialog("Player " + s + " Wins!");
        disableCells();
    }

    private void disableCells() {
        for (TextView[] row: tv) {
            for (TextView cell: row) {
                cell.setEnabled(false);
            }
        }
    }

    private void showDialog(String message) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.layout_dialog);
        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView tvMessage = dialog.findViewById(R.id.tvMessage);
        MaterialButton btnReplay = dialog.findViewById(R.id.btnReplay);
        tvMessage.setText(message);

        btnReplay.setOnClickListener(v -> {
            resetGame();
            dialog.dismiss();
        });
        dialog.show();

    }

    private void resetGame() {
        for (TextView[] row: tv) {
            for (TextView cell: row) {
                cell.setText("");
                cell.setEnabled(true);
            }
        }
        playerXTurn = true;
        roundCount = 0;
        updateTurnIcon();
    }
}