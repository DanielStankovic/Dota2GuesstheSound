package com.dsapps.dota2guessthesound;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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


import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class MainActivity extends ToastActivity {

    TextView startQuizTextView;
    TextView optionsTextView;
    TextView fastFingersTextView;
    TextView invokerTextView;
    TextView coinTextView;

    AdView mAdView;

    String visibleView = "startQuiz";

    Animation animation;

    ImageView leftArrow;
    ImageView rightArrow;

    SharedPreferences settings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startQuizTextView = (TextView)findViewById(R.id.playGameTextView);
        optionsTextView = (TextView)findViewById(R.id.optionsTextView);
        fastFingersTextView = (TextView)findViewById(R.id.fastFingerTextView);
        invokerTextView = (TextView)findViewById(R.id.invokerTextView);
        coinTextView = (TextView)findViewById(R.id.coinTextView);

        leftArrow = (ImageView)findViewById(R.id.leftArrow);
        rightArrow = (ImageView)findViewById(R.id.rightArrow);

       setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Typeface font = Typeface.createFromAsset(getAssets(),"fonts/score_font.ttf" );

        startQuizTextView.setTypeface(font);
        optionsTextView.setTypeface(font);
        fastFingersTextView.setTypeface(font);
        invokerTextView.setTypeface(font);
        coinTextView.setTypeface(font);

        settings = this.getSharedPreferences("com.example.daniel.dota2guessthesound", Context.MODE_PRIVATE);

        getCoinValue(settings, coinTextView);

        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");
        mAdView = (AdView)findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


    }

    @Override
    protected void onResume() {
        super.onResume();
        getCoinValue(settings, coinTextView);
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

    public void invokerActivity(View view){
        if(checkCoinValue(settings)) {
            Intent optionsIntent = new Intent(this, InvokerActivity.class);
            startActivity(optionsIntent);
        }
    }
    public void optionsActivity(View view){

        Intent optionsIntent = new Intent(this, OptionsActivity.class);
        startActivity(optionsIntent);
    }



    public void goLeft(View view){


        if(visibleView.equals("startQuiz")){

        } else if(visibleView.equals("fastFinger")){

            visibleView = "startQuiz";
        animateToRight(fastFingersTextView, startQuizTextView);
            fastFingersTextView.setVisibility(View.GONE);
            startQuizTextView.setVisibility(View.VISIBLE);
        }
        else if(visibleView.equals("invokerMode")){

            visibleView = "fastFinger";
           animateToRight(invokerTextView, fastFingersTextView);
            invokerTextView.setVisibility(View.GONE);
            fastFingersTextView.setVisibility(View.VISIBLE);

        }
        else if(visibleView.equals("options")){
            visibleView = "invokerMode";
            animateToRight(optionsTextView, invokerTextView);
            optionsTextView.setVisibility(View.GONE);
            invokerTextView.setVisibility(View.VISIBLE);
        }

    }

    public void goRight(View view){
        if(visibleView.equals("startQuiz")){
            visibleView = "fastFinger";
            animateToLeft(startQuizTextView, fastFingersTextView);
            startQuizTextView.setVisibility(View.GONE);
            fastFingersTextView.setVisibility(View.VISIBLE);
        }
        else if(visibleView.equals("fastFinger")){
            visibleView = "invokerMode";
            animateToLeft(fastFingersTextView, invokerTextView);
            fastFingersTextView.setVisibility(View.GONE);
            invokerTextView.setVisibility(View.VISIBLE);

        }
        else if(visibleView.equals("invokerMode")){
            visibleView = "options";
            animateToLeft(invokerTextView, optionsTextView);
            invokerTextView.setVisibility(View.GONE);
            optionsTextView.setVisibility(View.VISIBLE);
        }

        else if(visibleView.equals("options")){

        }

    }

    private void animateToLeft(View view1, View view2){

        animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.view1toleft);
        view1.startAnimation(animation);
        animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.view2toleft);
        view2.startAnimation(animation);
        animationListener(animation);

    }

    private void animateToRight(View view1, View view2) {
        animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.view1toright);
        view1.startAnimation(animation);
        animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.view2toright);
        view2.startAnimation(animation);
        animationListener(animation);

    }

    public void animationListener(Animation animation){

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                leftArrow.setClickable(false);
                rightArrow.setClickable(false);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                leftArrow.setClickable(true);
                rightArrow.setClickable(true);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public void showCoinToast(View view){

        String coinToast = "Win coins by:\n-Playing the Quiz\n-Playing Fast Finger mode\n-Watching videos inside Options";
        showInfoToast(coinToast);
    }
}
