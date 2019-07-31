package com.aizenangel.myawesomequizz;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_QUIZ = 1;
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String KEY_HIGHSCORE = "keyHighscore";

    private TextView textViewHighScore;
    private int highScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewHighScore = findViewById(R.id.text_view_highscore);
        loadHighscore();

        Button startQuiz = findViewById(R.id.button_start_quizz);
        startQuiz.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
              startQuiz();
            }
        });
    }

    private void startQuiz(){
        Intent intent = new Intent(MainActivity.this, QuizzActivity.class);
        startActivityForResult(intent, REQUEST_CODE_QUIZ);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE_QUIZ){
            if(resultCode == RESULT_OK){
              int score = data.getIntExtra(QuizzActivity.EXTRA_SCORE, 0);
              if(score > highScore){
                updateHighScore(score);
              }
            }
        }
    }

    private void loadHighscore(){
        SharedPreferences prefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        highScore = prefs.getInt(KEY_HIGHSCORE, 0);
        textViewHighScore.setText("Highscore: " + highScore);
    }

    private void updateHighScore(int highScoreNew){
       highScore = highScoreNew;
       textViewHighScore.setText("Highscore: " + highScoreNew);

        SharedPreferences prefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(KEY_HIGHSCORE, highScore);
        editor.apply();
    }

}
