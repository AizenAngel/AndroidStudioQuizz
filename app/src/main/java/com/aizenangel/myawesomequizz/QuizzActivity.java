package com.aizenangel.myawesomequizz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class QuizzActivity extends AppCompatActivity {
    public static final String EXTRA_SCORE = "extraScore";
    private static final long COUNTDOWN_IN_MILIS = 30000;

    private static final String KEY_SCORE = "keyScore";
    private static final String KEY_QUESTION_COUNT = "keyQuestionCount";
    private static final String KEY_MILIS_LEFT = "keyMilisLeft";
    private static final String KEY_ANSWERED = "keyAnswered";
    private static final String KEY_QUESTION_LIST = "keyQuestionList";



    private TextView textViewQuestion;
    private TextView textViewScore;
    private TextView textViewQuestionCount;
    private TextView textViewCountDown;
    private RadioGroup rbGroup;
    private RadioButton rb1;
    private RadioButton rb2;
    private RadioButton rb3;
    private RadioButton rb4;
    private Button buttonConfirmNext;
    private long backPressedTime = 0;

    private ColorStateList textColorDefaultRb;
    private ColorStateList textColorDefaultCd;

    private CountDownTimer countDownTimer;
    private long timeLeftInMilis;

    private ArrayList<Question> questionList;
    private int questionCounter;
    private int questionCountTotal;
    private Question currentQuestion;

    private int score;
    private boolean answered;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quizz);

        textViewQuestion = findViewById(R.id.text_view_question);
        textViewScore = findViewById(R.id.text_view_score);
        textViewQuestionCount = findViewById(R.id.text_view_question_count);
        textViewCountDown = findViewById(R.id.text_view_countdown);
        rbGroup = findViewById(R.id.radio_group);
        rb1 = findViewById(R.id.radio_button1);
        rb2 = findViewById(R.id.radio_button2);
        rb3 = findViewById(R.id.radio_button3);
        rb4 = findViewById(R.id.radio_button4);
        buttonConfirmNext = findViewById(R.id.text_confirm_id);

        textColorDefaultRb = rb1.getTextColors();
        textColorDefaultCd = textViewCountDown.getTextColors();

        if(savedInstanceState == null) {
            QuizDBHelper dbHelper = new QuizDBHelper(this);
            questionList = dbHelper.getAllQuestions();
            questionCountTotal = questionList.size();
            Collections.shuffle(questionList);
            showNextQuestion();
        }else{
            questionList = savedInstanceState.getParcelableArrayList(KEY_QUESTION_LIST);
            questionCounter = savedInstanceState.getInt(KEY_QUESTION_COUNT);
            timeLeftInMilis = savedInstanceState.getLong(KEY_MILIS_LEFT);
            answered = savedInstanceState.getBoolean(KEY_ANSWERED);
            score = savedInstanceState.getInt(KEY_SCORE);
            currentQuestion = questionList.get(questionCounter - 1);
            questionCountTotal = questionList.size();

            if(!answered){
                startCountdown();
            }else{
                updateCountDownText();
                showSolution();
            }
        }
        buttonConfirmNext.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
              if(!answered){
                  if(rb1.isChecked() || rb2.isChecked() || rb3.isChecked() || rb4.isChecked()){
                      checkAnswer();
                      rbGroup.clearCheck();
                  }else{
                      Toast.makeText(QuizzActivity.this, "Odaberi odgovor!", Toast.LENGTH_SHORT).show();
                  }
              }else{
                  showNextQuestion();
              }
            }
        });
    }

    private void showNextQuestion(){
        rb1.setTextColor(textColorDefaultRb);
        rb2.setTextColor(textColorDefaultRb);
        rb3.setTextColor(textColorDefaultRb);
        rb4.setTextColor(textColorDefaultRb);
        rbGroup.clearCheck();

        if(questionCounter < questionCountTotal){
            currentQuestion = questionList.get(questionCounter);

            textViewQuestion.setText(currentQuestion.getQuestion());
            rb1.setText(currentQuestion.getOption1());
            rb2.setText(currentQuestion.getOption2());
            rb3.setText(currentQuestion.getOption3());
            rb4.setText(currentQuestion.getOption4());

            questionCounter++;
            textViewQuestionCount.setText("Question: " + questionCounter + "/"+questionCountTotal);
            answered = false;
            buttonConfirmNext.setText("Confirm");

            timeLeftInMilis = COUNTDOWN_IN_MILIS;
            startCountdown();
        }else{
            finishQuiz();
        }
    }

    private void startCountdown(){
        countDownTimer = new CountDownTimer(timeLeftInMilis, 1000) {
            @Override
            public void onTick(long l) {
              timeLeftInMilis = l;
              updateCountDownText();
            }

            @Override
            public void onFinish() {
              timeLeftInMilis = 0;
              updateCountDownText();
              checkAnswer();
            }
        }.start();
    }

    private void updateCountDownText(){
        int minutes = (int)((timeLeftInMilis/1000)/60);
        int seconds = (int)((timeLeftInMilis/1000)%60);

        String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        textViewCountDown.setText(timeFormatted);

        if(timeLeftInMilis < 10000){
            textViewCountDown.setTextColor(Color.RED);
        }else{
            textViewCountDown.setTextColor(textColorDefaultCd);
        }
    }

    private void checkAnswer(){
       answered = true;
       countDownTimer.cancel();
       RadioButton rbSelected = findViewById(rbGroup.getCheckedRadioButtonId());
       int answerNr = rbGroup.indexOfChild(rbSelected) + 1;

       if(answerNr == currentQuestion.getAnswerNr()){
           textViewQuestion.setText("Bravo, bravo, ucilo se...");
           score++;
           textViewScore.setText("Score: " + score);
       }else{
           textViewQuestion.setText("E moja ti...");
       }

       showSolution();
    }

    private void showSolution(){
        rb1.setTextColor(Color.RED);
        rb2.setTextColor(Color.RED);
        rb3.setTextColor(Color.RED);
        rb4.setTextColor(Color.RED);

        switch(currentQuestion.getAnswerNr()){
            case 1: rb1.setTextColor(Color.GREEN);break;
            case 2: rb2.setTextColor(Color.GREEN);break;
            case 3: rb3.setTextColor(Color.GREEN);break;
            case 4: rb4.setTextColor(Color.GREEN);break;
        }

        if(questionCounter < questionCountTotal){
            buttonConfirmNext.setText("Next");
        }else{
            buttonConfirmNext.setText("Finish");
        }
    }

    private void finishQuiz(){
        Intent resultIntent = new Intent();
        resultIntent.putExtra(EXTRA_SCORE, score);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    @Override
    public void onBackPressed() {
        if(backPressedTime + 2000 > System.currentTimeMillis()){
            finishQuiz();
        }else{
            Toast.makeText(this, "Press back again to finish", Toast.LENGTH_SHORT).show();
        }
        backPressedTime = System.currentTimeMillis();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(countDownTimer != null){
            countDownTimer.cancel();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(KEY_SCORE, score);
        outState.putInt(KEY_QUESTION_COUNT, questionCounter);
        outState.putLong(KEY_MILIS_LEFT, timeLeftInMilis);
        System.out.println("onSaveInstanceState: " + timeLeftInMilis);
        outState.putBoolean(KEY_ANSWERED, answered);
        outState.putParcelableArrayList(KEY_QUESTION_LIST, questionList);
    }
}
