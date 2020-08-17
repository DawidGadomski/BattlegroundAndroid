package com.example.battleground;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private TextView tvPlay;
    private TextView tvHighscore;
    private int highscore;

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
                finish();
            }
        });
        sharedPreferences = getSharedPreferences("game", MODE_PRIVATE);
        highscore = sharedPreferences.getInt("highscore", MODE_PRIVATE);
        tvHighscore.setText("HighScore: " + highscore);

    }
}
