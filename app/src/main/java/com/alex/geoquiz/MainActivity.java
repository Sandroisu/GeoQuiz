package com.alex.geoquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import static android.view.Gravity.TOP;

public class MainActivity extends AppCompatActivity {
private Button mTrueButton;
private Button mFalseButton;
Toast toast;

    @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_quiz);
                mTrueButton = findViewById(R.id.true_button);
                mFalseButton = findViewById(R.id.false_button);

                mTrueButton.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v) {
                        toast = Toast.makeText(MainActivity.this, R.string.correct_toast,Toast.LENGTH_LONG);
                        toast.setGravity(TOP,0,0);
                        toast.show();

                    }
        });
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, R.string.incorrect_toast, Toast.LENGTH_SHORT).show();
            }
        });
    }
    
}
