package com.example.daniel.dota2guessthesound;

import android.content.Intent;
import android.content.pm.ActivityInfo;

import android.graphics.Typeface;

import android.support.v4.content.ContextCompat;

import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import android.widget.TextView;

public class MainActivity extends ToastActivity {

    TextView startQuizTextView;
    TextView optionsTextView;
    TextView fastFingersTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startQuizTextView = (TextView)findViewById(R.id.playGameTextView);
        optionsTextView = (TextView)findViewById(R.id.optionsTextView);
        fastFingersTextView = (TextView)findViewById(R.id.fastFingerTextView);

       setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Typeface font = Typeface.createFromAsset(getAssets(),"fonts/score_font.ttf" );

        startQuizTextView.setTypeface(font);
        optionsTextView.setTypeface(font);
        fastFingersTextView.setTypeface(font);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_actionbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()){

            case R.id.actionbar_icon :

              int spannableColor = ContextCompat.getColor(this, R.color.textMenuInfo);

              String infoText = "DOTA 2 SOUND QUIZ\nDeveloper: Daniel Stanković\nContent Assistant: Željko Stanković\nSerbia 2017";
               Spannable spannable = new SpannableString(infoText);

                spannable.setSpan(new ForegroundColorSpan(spannableColor), 0, infoText.indexOf("\n"), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannable.setSpan(new android.text.style.StyleSpan(Typeface.BOLD), 0, infoText.indexOf("\n"), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                spannable.setSpan(new RelativeSizeSpan(0.7f), infoText.indexOf("\n"), infoText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
               showInfoToast(spannable);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void playGameActivity(View view){

        Intent playGameIntent = new Intent(this, StartQuizActivity.class);
        startActivity(playGameIntent);
    }

    public void fastFingerActivity(View view){

        Intent playGameIntent = new Intent(this, FastFingerActivity.class);
        startActivity(playGameIntent);
    }

    public void optionsActivity(View view){

        Intent optionsIntent = new Intent(this, OptionsActivity.class);
        startActivity(optionsIntent);
    }
}
