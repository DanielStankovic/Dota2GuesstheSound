package com.dsapps2018.dota2guessthesound;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {
    TextView creditsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        creditsTextView = (TextView)findViewById(R.id.creditsTextView);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("About the Game");
        }

        int spannableColor = ContextCompat.getColor(this, R.color.toastText);
        String infoText = getResources().getString(R.string.credits);
        Spannable spannable = new SpannableString(infoText);
        spannable.setSpan(new ForegroundColorSpan(spannableColor), 47, 65, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        spannable.setSpan(new ForegroundColorSpan(spannableColor), 126, 146, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        creditsTextView.setText(spannable);
    }
}
