package com.example.duofingo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    Button getStarted;
    TextView catchPhrase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        catchPhrase = findViewById(R.id.catch_phrase);

        String[] catchPhrases = {"World's best way to learn finance", "Learn and Compete",
                "Quick and fun lessons that work"};
        catchPhrase.post(new Runnable() {
            int i = 0;
            @Override
            public void run() {
                catchPhrase.setText(catchPhrases[i]);
                i++;
                if (i == catchPhrases.length)
                    i = 0;
                catchPhrase.postDelayed(this, 2000);
            }
        });

        getStarted = findViewById(R.id.get_started_button);
        getStarted.setOnClickListener(v -> openLoginSignUp());
    }

    public void openLoginSignUp() {
        Intent intent = new Intent(MainActivity.this, LoginSignUp.class);
        startActivity(intent);
    }
}