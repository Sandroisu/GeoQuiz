package com.alex.geoquiz;

public class Question {
    private int mTextResId;
    private boolean mAnswerTrue;

    public Question(int TextResId, boolean AnswerTrue) {
        mTextResId = TextResId;
        mAnswerTrue = AnswerTrue;
    }

    public boolean isAnswerTrue() {
        return mAnswerTrue;
    }

    public void setAnswerTrue(boolean answerTrue) {
        mAnswerTrue = answerTrue;
    }

    public int getTextResId() {
        return mTextResId;
    }

    public void setTextResId(int textResId) {
        mTextResId = textResId;
    }
}
