package com.example.michael.team3speechtherapy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class InstantFeedbackActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instant_feedback);
        Intent i = getIntent();
        String word = i.getStringExtra("word");
        String vowel = i.getStringExtra("vowel");
        String f1 = i.getStringExtra("F1");
        String f2 = i.getStringExtra("F2");
        String rating = i.getStringExtra("rating");
        String recommend = i.getStringExtra("recommend");

        // get  all text views
        TextView view = findViewById(R.id.wordIFA);
        view.setText(word);
        view = findViewById(R.id.vowelIFA);
        view.setText(vowel);
        view = findViewById(R.id.f1IFA);
        view.setText(f1);
        view = findViewById(R.id.f2IFA);
        view.setText(f2);
        view = findViewById(R.id.ratingIFA);
        view.setText(rating);
        view = findViewById(R.id.recommendationIFA);
        view.setText(recommend);
}
    }

