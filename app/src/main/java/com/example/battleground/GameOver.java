package com.example.battleground;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class GameOver extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private TextView tvPlay;
    private TextView tvHighscore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_game_start);

        tvPlay = findViewById(R.id.tvPlay);
        tvHighscore = findViewById(R.id.tvHighscore);

        tvPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, GameActivity.class));
            }
        });
        sharedPreferences = getSharedPreferences("game", MODE_PRIVATE);
        tvHighscore.setText("HighScore: " + sharedPreferences.getInt("highscore", 0));

    }
}
