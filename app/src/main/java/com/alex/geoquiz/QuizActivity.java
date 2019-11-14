package com.alex.geoquiz;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.widget.Toast.makeText;

public class QuizActivity extends AppCompatActivity {
    private Button mTrueButton;
    private Button mFalseButton;
    private Button mCheatButton;
    private ImageButton mNextButton;
    private ImageButton mPreviousButton;
    private TextView mQuestionTextView;
    private static final String KEY_INDEX = "index";
    private static final String ANSWER_STATE = "answer_state";
    private static final String ANSWERS_COUNT = "answers_count";
    private static final String RIGHT_ANSWERS_COUNTER = "right_answers_counter";
    private static final String IS_HE_A_CHEATER = "is_he_a_cheater";
    private static final String HOW_MANY_CHEATS = "how_many_cheats";
    private static final String CHEATER_ARRAY = "cheater_array";
    private ArrayList<Integer> mAnswersArrayList;
    private ArrayList<Integer> mCheaterArrayList;
    private int questionQuantity = 0;
    private float rightAnswerCounter = 0;
    private int thisIsTheEnd = 0;
    private int cheatsCounter = 0;
    private static final int REQUEST_CODE_CHEAT = 0;
    private boolean mIsCheater;
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
        mCheatButton = findViewById(R.id.quiz_cheat_button);
        questionQuantity = mQuestionBank.length;
        mAnswersArrayList = new ArrayList<>(questionQuantity);
        for (int i = 0; i < questionQuantity; i++) {
            mAnswersArrayList.add(i, 0);
        }
        mCheaterArrayList = new ArrayList<>(questionQuantity);
        for (int i = 0; i < questionQuantity; i++) {
            mCheaterArrayList.add(i, 0);
        }

        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            mAnswersArrayList = savedInstanceState.getIntegerArrayList(ANSWER_STATE);
            mCheaterArrayList = savedInstanceState.getIntegerArrayList(CHEATER_ARRAY);
            thisIsTheEnd = savedInstanceState.getInt(ANSWERS_COUNT, 0);
            rightAnswerCounter = savedInstanceState.getFloat(RIGHT_ANSWERS_COUNTER);
            mIsCheater = savedInstanceState.getBoolean(IS_HE_A_CHEATER);
            cheatsCounter = savedInstanceState.getInt(HOW_MANY_CHEATS);
        }

        updateQuestion();
    }

    @Override
    protected void onStart() {
        super.onStart();
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
                increaseIndex();
                updateQuestion();
                mIsCheater = false;

            }
        });
        mPreviousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decreaseIndex();
                updateQuestion();
                mIsCheater = false;

            }
        });
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
                Intent intent = CheatActivity.newIntent(QuizActivity.this, answerIsTrue, cheatsCounter);
                startActivityForResult(intent, REQUEST_CODE_CHEAT);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_INDEX, mCurrentIndex);
        outState.putInt(ANSWERS_COUNT, thisIsTheEnd);
        outState.putIntegerArrayList(ANSWER_STATE, mAnswersArrayList);
        outState.putIntegerArrayList(CHEATER_ARRAY, mCheaterArrayList);
        outState.putFloat(RIGHT_ANSWERS_COUNTER, rightAnswerCounter);
        outState.putBoolean(IS_HE_A_CHEATER, mIsCheater);
        outState.putInt(HOW_MANY_CHEATS, cheatsCounter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_CHEAT) {
            if (data == null) {
                return;
            }
            mIsCheater = CheatActivity.wasAnswerShown(data);
            mCheaterArrayList.set(mCurrentIndex, 1);
            mCheatButton.setEnabled(false);
            cheatsCounter = CheatActivity.counterAnswerShown(data);
        }
    }

    private void increaseIndex() {
        mCurrentIndex = (mCurrentIndex + 1) % questionQuantity;

    }

    private void decreaseIndex() {
        if (mCurrentIndex == 0) {
            mCurrentIndex = 6;
        }
        mCurrentIndex = (mCurrentIndex - 1);
    }

    private void updateQuestion() {
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
        if (mCheaterArrayList.get(mCurrentIndex) == 1){
            mCheatButton.setEnabled(false);
            if(mAnswersArrayList.get(mCurrentIndex)!=1) {
                mNextButton.setEnabled(false);
                mPreviousButton.setEnabled(false);
            }
        }else if(cheatsCounter>=3)mCheatButton.setEnabled(false);
        else mCheatButton.setEnabled(true);
        if (mAnswersArrayList.get(mCurrentIndex) == 1) {
            buttonEnable(false);
        } else buttonEnable(true);
    }

    private void checkAnswer(boolean userPressedTrue) {
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
        int messageResId = 0;
        if (mIsCheater || (mCheaterArrayList.get(mCurrentIndex) == 1)) {
            messageResId = R.string.judgment_toast;
            mPreviousButton.setEnabled(true);
            mNextButton.setEnabled(true);
        } else {
            if (userPressedTrue == answerIsTrue) {
                messageResId = R.string.correct_toast;
                rightAnswerCounter++;
            } else {
                messageResId = R.string.incorrect_toast;
            }
        }
        makeText(this, messageResId, Toast.LENGTH_LONG).show();
        buttonEnable(false);
        mAnswersArrayList.set(mCurrentIndex, 1);
        thisIsTheEnd++;
        if (thisIsTheEnd == mQuestionBank.length) {
            newGame();
            rightAnswerCounter = 0;
        }
    }

    private void newGame() {
        for (int i = 0; i < questionQuantity; i++) {
            mAnswersArrayList.set(i, 0);
        }
        for (int i = 0; i < questionQuantity; i++) {
            mCheaterArrayList.set(i, 0);
        }
        rightAnswerCounter = rightAnswerCounter * 100 / questionQuantity;
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_item, (ViewGroup) findViewById(R.id.llToast));
        TextView tv = layout.findViewById(R.id.tvToast);
        String s = "Ваш результат: " + (int) rightAnswerCounter + "%";
        tv.setText(s);
        if (rightAnswerCounter == 100)
            layout.setBackgroundColor(Color.GREEN);
        Toast toast = new Toast(this);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setView(layout);
        toast.show();
        thisIsTheEnd = 0;
        cheatsCounter = 0;
    }

    private void buttonEnable(boolean b) {
        mTrueButton.setEnabled(b);
        mFalseButton.setEnabled(b);
    }
}
