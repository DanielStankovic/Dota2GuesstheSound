package com.dsapps.dota2guessthesound;


import android.content.SharedPreferences;

import android.graphics.Color;
import android.graphics.Typeface;

import android.support.v4.content.ContextCompat;

import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

public class ToastActivity extends AppCompatActivity {
    Toast toast = null;

    public void showInfoToast(CharSequence message){

        LayoutInflater layoutInflater = getLayoutInflater();
        View layout = layoutInflater.inflate(R.layout.custome_toast, (ViewGroup)findViewById(R.id.custom_toast_layout));
        layout.setBackgroundColor(ContextCompat.getColor(this, R.color.lightGrayToast));

        TextView textView = (TextView)layout.findViewById(R.id.toast_message);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textView.setText(message);

        if(toast != null){
            toast.cancel();
        }

        toast = new Toast(this);
        toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, -430);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    public void showCheckAnswerToast(CharSequence message, int colorId, int YTextPosition ){
        LayoutInflater layoutInflater = getLayoutInflater();
        View layout = layoutInflater.inflate(R.layout.custome_toast, (ViewGroup)findViewById(R.id.custom_toast_layout));
        layout.setBackgroundColor(ContextCompat.getColor(this, R.color.answersBackgroundColor));

        TextView textView = (TextView)layout.findViewById(R.id.toast_message);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
        textView.setTypeface(null, Typeface.BOLD);
        textView.setTextColor(colorId);
        textView.setText(message);

        if(toast != null){
            toast.cancel();
        }

        toast = new Toast(this);
        toast.setGravity(Gravity.CENTER, 0, YTextPosition);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();



    }

    public void showCoinRewardToast(int imageResource){
        LayoutInflater layoutInflater = getLayoutInflater();
        View layout = layoutInflater.inflate(R.layout.coin_layout, (ViewGroup)findViewById(R.id.coin_reward_layout));
        layout.setBackgroundColor(ContextCompat.getColor(this, R.color.answersBackgroundColor));

        ImageView imageView = (ImageView)layout.findViewById(R.id.coinImageView);
        imageView.setImageResource(imageResource);

        if(toast != null){
            toast.cancel();
        }


        toast = new Toast(this);
        toast.setGravity(Gravity.CENTER, 0, -50);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();

    }

    public void showInterstitialAd (final InterstitialAd mInterstitialAd){

        if(mInterstitialAd.isLoaded()){
            mInterstitialAd.show();
        } else{


            Log.i("TAG ADD", "Add not loaded yet.");
        }

        mInterstitialAd.setAdListener(new AdListener(){

            @Override
            public void onAdClosed() {
                mInterstitialAd.loadAd(new AdRequest.Builder().build());

            }

        });
    }

    public void setHighScore(int score, int highScore, SharedPreferences settings, TextView highScoreTextView, String highscoreMode){

        if(score > highScore){
            highScore = score;


            settings.edit().putString(highscoreMode,Integer.toString(highScore)).apply();

        }

        String oldHighScore = settings.getString(highscoreMode, Integer.toString(0));

        highScoreTextView.setText("Highscore: " + oldHighScore);


    }

    public void setCoinValue(SharedPreferences settings, int value){
        int currentCoinValue = settings.getInt("coinValue", 0);

        settings.edit().putInt("coinValue", currentCoinValue + value).apply();
    }

    public void getCoinValue(SharedPreferences settings, TextView coinTextView){

        int currentCoinValue = settings.getInt("coinValue", 0);
        coinTextView.setText(String.valueOf(currentCoinValue));
    }

    public boolean checkCoinValue(SharedPreferences settings){
        int value = settings.getInt("coinValue", 0);

        if(value < 50){
            showCheckAnswerToast("50 coins needed", Color.RED, 100);
            return false;
        } else{
            value = value - 50;
            settings.edit().putInt("coinValue",value).apply();
            return true;
        }

    }



}
