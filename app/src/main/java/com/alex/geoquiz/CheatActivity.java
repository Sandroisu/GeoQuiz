package com.alex.geoquiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {
    private static final String EXTRA_ANSWER_IS_TRUE = "com.alex.geoquiz.answer_is_true";
    private static final String EXTRA_ANSWER_SHOWN = "com.alex.geoquiz.answer_shown";
    private static final String WAS_ANSWER_SHOWN = "was_answer_shown";
    private static final String ANSWER= "answer";
    private boolean mAnswerWasShown = false;
    private boolean mAnswerIsTrue;
    private String answer = null;
    private TextView mAnswerTextView;
    private Button mShowAnswerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);
        mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);
        mAnswerTextView = findViewById(R.id.cheat_tvAnswer);
        mShowAnswerButton = findViewById(R.id.cheat_btnShowAnswer);
        if (savedInstanceState != null){
            setAnswerShownResult(savedInstanceState.getBoolean(WAS_ANSWER_SHOWN));
            answer = savedInstanceState.getString(ANSWER);
                    if(answer!=null){
             mAnswerTextView.setText(answer);
            }
        }
        mShowAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAnswerTextView.setText(String.valueOf(mAnswerIsTrue));
                setAnswerShownResult(true);
                mAnswerWasShown = true;
                answer = String.valueOf(mAnswerIsTrue);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(WAS_ANSWER_SHOWN, mAnswerWasShown);
        if (answer!= null){
            outState.putString(ANSWER, answer);
        }
    }

    public static Intent newIntent(Context packageContext, boolean answerIsTrue) {
        Intent intent = new Intent(packageContext, CheatActivity.class);
        intent.putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue);
        return intent;
    }

    private void setAnswerShownResult(boolean isAnswerShown) {
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown);
        setResult(RESULT_OK, data);
    }
    public static boolean wasAnswerShown(Intent result) {
        return result.getBooleanExtra(EXTRA_ANSWER_SHOWN, false);
    }
}