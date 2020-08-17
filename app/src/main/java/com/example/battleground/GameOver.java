package com.example.battleground;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class GameOver extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private TextView tvTap;
    private TextView tvScore;

    private int score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_game_over);

        tvTap = findViewById(R.id.tvTap);
        tvScore = findViewById(R.id.tvScore);

        if(getIntent().hasExtra("score")){
            score = getIntent().getExtras().getInt("score");
        }


        tvScore.setText("Your score: " + score);

        tvTap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GameOver.this, MainActivity.class));
                finish();
            }
        });


    }
}
