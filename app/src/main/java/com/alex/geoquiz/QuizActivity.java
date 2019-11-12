package com.alex.geoquiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.view.Gravity.BOTTOM;
import static android.view.Gravity.CENTER_VERTICAL;
import static android.view.Gravity.TOP;
import static android.widget.Toast.makeText;

public class QuizActivity extends AppCompatActivity {
    private static final String TAG = "QuizActivity";
    private Button mTrueButton;
    private Button mFalseButton;
    private ImageButton mNextButton;
    private ImageButton mPreviousButton;
    private TextView mQuestionTextView;
    private static final String KEY_INDEX = "index";
    private ArrayList<Integer> mArrayList;
    private float counter = 0;
    private Question[] mQuestionBank = {
            new Question(R.string.question_australia, true),
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true)
    };
    private int mCurrentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        mTrueButton = findViewById(R.id.true_button);
        mFalseButton = findViewById(R.id.false_button);
        mNextButton = findViewById(R.id.next_button);
        mQuestionTextView = findViewById(R.id.question_text_view);
        mPreviousButton = findViewById(R.id.previous_button);
        mArrayList = new ArrayList<>(mQuestionBank.length);
        setZero();
        newGame();
        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
        }
        Log.d(TAG, "onCreate(Bundle) called");
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);
            }
        });
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);
            }
        });
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPreviousButton.setEnabled(true);
                increaseIndex();
                updateQuestion();
            }
        });
        mPreviousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decreaseIndex();
                updateQuestion();
            }
        });
        updateQuestion();
    }

    private void increaseIndex() {
        mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
        if ((mCurrentIndex + 1) % mQuestionBank.length == 0) {
            newGame();
        }
    }

    private void decreaseIndex() {
        if (mCurrentIndex == 0)
            mPreviousButton.setEnabled(false);
        else
            mCurrentIndex = (mCurrentIndex - 1);
    }

    private void updateQuestion() {
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
        if (mArrayList.get(mCurrentIndex) == 1) {
            buttonEnable(false);
        } else buttonEnable(true);
        if(counter!=0&&mCurrentIndex==0) {
            counter = counter * 100 / mQuestionBank.length;
            Toast.makeText(QuizActivity.this, "Ваш результат: " + (int)counter + "%", Toast.LENGTH_SHORT).show();
            counter = 0;
        }
    }

    private void checkAnswer(boolean userPressedTrue) {
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
        int messageResId = 0;
        if (userPressedTrue == answerIsTrue) {
            messageResId = R.string.correct_toast;
            counter++;
        } else messageResId = R.string.incorrect_toast;
        makeText(this, messageResId, Toast.LENGTH_LONG).show();
        buttonEnable(false);
        mArrayList.set(mCurrentIndex, 1);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_INDEX, mCurrentIndex);
    }

    private void setZero() {
        for (int i = 0; i < mQuestionBank.length; i++) {
            mArrayList.add(i, 0);
        }
    }

    private void newGame() {
        mPreviousButton.setEnabled(false);
        for (int i = 0; i < mQuestionBank.length; i++) {
            mArrayList.set(i, 0);
        }


    }

    private void buttonEnable(boolean b) {
        mTrueButton.setEnabled(b);
        mFalseButton.setEnabled(b);
    }
}
