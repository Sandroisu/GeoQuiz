package com.alex.geoquiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {
    private static final String EXTRA_ANSWER_IS_TRUE = "com.alex.geoquiz.answer_is_true";
    private static final String EXTRA_ANSWER_SHOWN = "com.alex.geoquiz.answer_shown";
    private static final String WAS_ANSWER_SHOWN = "was_answer_shown";
    private static final String ANSWER = "answer";
    private static final String HE_CHEATS_TOO_MUCH = "he_cheats_too_much";
    private static final String HE_CHEATS_TOO_MUCH_1 = "he_cheats_too_much_1";
    private static final String IS_HE_CHEATS_TOO_MUCH = "is_he_cheats_too_much";
    private boolean mAnswerWasShown = false;
    private boolean mAnswerIsTrue;
    private String answer = null;
    private TextView mAnswerTextView;
    private TextView mAPILevel;
    private Button mShowAnswerButton;
    private static int cheatsCounter = 0;
    private int maximumCheats = 3;
    private String APIAndTryCounter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);
        mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);
        cheatsCounter = getIntent().getIntExtra(HE_CHEATS_TOO_MUCH, 0);
        mAnswerTextView = findViewById(R.id.cheat_tvAnswer);
        mShowAnswerButton = findViewById(R.id.cheat_btnShowAnswer);
        mAPILevel = findViewById(R.id.cheat_tvAPILevel);
        if (cheatsCounter == maximumCheats) mShowAnswerButton.setEnabled(false);

        if (savedInstanceState != null) {
            cheatsCounter = savedInstanceState.getInt(HE_CHEATS_TOO_MUCH_1);
            if (cheatsCounter == maximumCheats) mShowAnswerButton.setEnabled(false);
            mAPILevel.setText(howManyTrysLeft(cheatsCounter));
            setAnswerShownResult(savedInstanceState.getBoolean(WAS_ANSWER_SHOWN), cheatsCounter);
            answer = savedInstanceState.getString(ANSWER);
            if (answer != null) {
                mAnswerTextView.setText(answer);
                mShowAnswerButton.setVisibility(View.INVISIBLE);
            }
        }else mAPILevel.setText(howManyTrysLeft(cheatsCounter));
        mShowAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAnswerTextView.setText(String.valueOf(mAnswerIsTrue));
                cheatsCounter++;
                mAPILevel.setText(howManyTrysLeft(cheatsCounter));
                setAnswerShownResult(true, cheatsCounter);

                mAnswerWasShown = true;
                answer = String.valueOf(mAnswerIsTrue);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    int cx = mShowAnswerButton.getWidth() / 2;
                    int cy = mShowAnswerButton.getHeight() / 2;
                    float radius = mShowAnswerButton.getWidth();
                    Animator anim = ViewAnimationUtils
                            .createCircularReveal(mShowAnswerButton, cx, cy, radius, 0);
                    anim.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            mShowAnswerButton.setVisibility(View.INVISIBLE);
                        }
                    });
                    anim.start();
                } else {
                    mShowAnswerButton.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(WAS_ANSWER_SHOWN, mAnswerWasShown);
        if (answer != null) {
            outState.putString(ANSWER, answer);
        }
        outState.putInt(HE_CHEATS_TOO_MUCH_1, cheatsCounter);
    }

    public static Intent newIntent(Context packageContext, boolean answerIsTrue, int cheatsCounter) {
        Intent intent = new Intent(packageContext, CheatActivity.class);
        intent.putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue);
        intent.putExtra(HE_CHEATS_TOO_MUCH, cheatsCounter);
        return intent;
    }

    private void setAnswerShownResult(boolean isAnswerShown, int cheatsCounter) {
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown);
        data.putExtra(IS_HE_CHEATS_TOO_MUCH, cheatsCounter);
        setResult(RESULT_OK, data);
    }

    public static int counterAnswerShown(Intent result) {
        return result.getIntExtra(IS_HE_CHEATS_TOO_MUCH, 0);
    }

    public static boolean wasAnswerShown(Intent result) {
        return result.getBooleanExtra(EXTRA_ANSWER_SHOWN, false);
    }
    private String howManyTrysLeft(Integer cheatsCounter){
        APIAndTryCounter  = "your API level is " + Build.VERSION.SDK_INT + "\n" + "Осталось " + (maximumCheats - cheatsCounter) + " попыток читер!";
        return APIAndTryCounter;
    }
}