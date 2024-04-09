package com.example.xo_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.google.android.material.switchmaterial.SwitchMaterial;
import java.util.ArrayList;
import java.util.List;


public class SecondActivity extends AppCompatActivity {
    // Se declară un obiect Logic care gestionează logica jocului
    private final Logic logic = new Logic();
    // Lista de butoane din interfața grafică
    private final List<Button> buttons = new ArrayList<>();
    // Switch-ul pentru a schimba partea/jucătorul care începe
    private SwitchMaterial switchSide;
    // Switch-ul pentru a alege adversarul (umanoizid sau AI)
    private SwitchMaterial switchOpponent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_activity);

        Button backButton = findViewById(R.id.Back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Inchiderea activitatii curente (SecondActivity) pentru a reveni la MainActivity
                finish();
            }
        });

    // referința către layout-ul care conține butoanele
    LinearLayout layoutForButtons = findViewById(R.id.LayoutForButtons);
    // Se adaugă butoanele din layout în lista de butoane
    for (int i = 0; i < layoutForButtons.getChildCount(); i++) {
        LinearLayout layout = findViewById(layoutForButtons.getChildAt(i).getId());
        for (int j = 0; j < layout.getChildCount(); j++) {
            Button button = findViewById(layout.getChildAt(j).getId());
            buttons.add(button);
        }
    }

    // Se inițializează switch-urile
    switchSide = findViewById(R.id.switchSide);
    switchSide.setOnClickListener(this::clickSwitchSide);
    switchOpponent = findViewById(R.id.switchOpponent);

    // Se inițializează butonul de restart și se setează un text personalizat
    Button restart = findViewById(R.id.restart);
    restart.setOnClickListener(this::clickOnRestart);
    restart.setText("Restart");
}

    // Metoda care verifică dacă jocul s-a încheiat și afișează mesaje corespunzătoare
    public boolean isWin() {
        // Se inițializează un toast pentru afișarea mesajelor
        Toast toast = Toast.makeText(
                getApplicationContext(), "",
                Toast.LENGTH_SHORT
        );
        toast.setGravity(Gravity.CENTER, 0, 0);
        // Se verifică dacă unul dintre jucători a câștigat sau dacă s-a terminat remiza
        if (this.logic.checkWin("X")) {
            toast.setText("X wins! Please restart the game!");
            toast.show();
            return true;
        } else if (this.logic.checkWin("O")) {
            toast.setText("O wins! Please restart the game!");
            toast.show();
            return true;
        } else if (this.logic.isFilled()) {
            toast.setText("It's a draw! Start a new Game!");
            toast.show();
            return true;
        }
        return false;
    }

    // Metoda apelată la click pe un buton din interfață
    public void clickOnButton(View view) {
        Button clicked = findViewById(view.getId());
        // Se verifică dacă butonul apăsat nu a fost deja selectat și dacă jocul nu s-a încheiat
        if (clicked.getText() != Logic.firstMark && clicked.getText() != Logic.secondMark && !isWin()) {
            // Se actualizează textul butonului cu simbolul corespunzător
            clicked.setText(logic.getValue());
            // Se apelează logica jocului pentru a actualiza starea jocului
            logic.clickOnButton((String) clicked.getTag());
            // Dacă adversarul este AI, acesta va face o mutare
            if (!switchOpponent.isChecked() && !logic.isFilled() && !logic.checkWin(Logic.firstMark) && logic.getTurn() % 2 == 1) {
                Button button = buttons.get(logic.clickOnButtonWithAI());
                button.setText(logic.getValue());
                logic.setTurn(logic.getTurn() + 1);
            }
            // Se verifică dacă jocul s-a încheiat
            isWin();
        }
    }

    // Metoda apelată la click pe butonul de restart
    public void clickOnRestart(View view) {
        // Se resetează textul butoanelor și matricea jocului
        for (Button v : buttons) {
            v.setText("");
        }
        logic.clearMatrix();
        // Se schimbă părțile/jucătorii dacă switch-ul este activat
        if (switchSide.isChecked()) {
            logic.changeSide("O");
        } else logic.changeSide("X");
    }

    // Metoda apelată la schimbarea switch-ului pentru părțile/jucătorii
    public void clickSwitchSide(View view) {
        logic.changeSide();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // Se salvează starea butoanelor și a matricei jocului
        for (Button v : buttons) {
            outState.putString("buttons" + v.getId(), (String) v.getText());
        }
        for (int i = 0; i < logic.getMatrix().length; i++) {
            for (int j = 0; j < logic.getMatrix().length; j++) {
                outState.putString("matrix" + ((i * logic.SIZE) + j), logic.getMatrix()[i][j]);
            }
        }
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Se restaurează starea butoanelor și a matricei jocului
        for (Button v : buttons) {
            v.setText(savedInstanceState.getString("buttons" + v.getId()));
        }
        for (int i = 0; i < logic.getMatrix().length; i++) {
            for (int j = 0; j < logic.getMatrix().length; j++) {
                logic.getMatrix()[i][j] = savedInstanceState.getString("matrix" + ((i * logic.SIZE) + j));
            }
        }
    }
}
