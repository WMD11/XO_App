package com.example.xo_app;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button newGameButton = findViewById(R.id.NewGame);
        newGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crearea unei intentii pentru a deschide SecondActivity
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                // Pornirea activității SecondActivity
                startActivity(intent);
            }
        });


        Button exitButton = findViewById(R.id.Exit_Game);

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Închiderea activității curente, ceea ce duce la închiderea aplicației
                finish();
            }
        });
    }
}
