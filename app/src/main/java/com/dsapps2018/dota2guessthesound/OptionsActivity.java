package com.dsapps2018.dota2guessthesound;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.media.AudioManager;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;



public class OptionsActivity extends ToastActivity {

    SeekBar seekBar = null;
    private AudioManager audioManager = null;
    TextView volumeText;
    TextView watchVideoText;
    TextView rateAppText;
    TextView emailText;
    RewardedVideoAd mRewardedVideoAd;
    SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Options");
        }
        volumeText  = (TextView)findViewById(R.id.volumeText);
        watchVideoText = (TextView)findViewById(R.id.watch_video_textView);
        rateAppText = (TextView)findViewById(R.id.rate_app_textView);
        emailText = (TextView)findViewById(R.id.email_textView);
        settings = this.getSharedPreferences("com.example.daniel.dota2guessthesound", Context.MODE_PRIVATE);
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {
            @Override
            public void onRewardedVideoAdLoaded() {

            }

            @Override
            public void onRewardedVideoAdOpened() {

            }

            @Override
            public void onRewardedVideoStarted() {

            }

            @Override
            public void onRewardedVideoAdClosed() {

                mRewardedVideoAd.loadAd("ca-app-pub-3940256099942544/5224354917", new AdRequest.Builder().build());
            }

            @Override
            public void onRewarded(RewardItem rewardItem) {

                showCoinRewardToast(R.drawable.twenty_coin);
                setCoinValue(settings, 20);

            }

            @Override
            public void onRewardedVideoAdLeftApplication() {

            }

            @Override
            public void onRewardedVideoAdFailedToLoad(int i) {

                Toast.makeText(OptionsActivity.this, "Video is not ready yet.", Toast.LENGTH_SHORT).show();

            }
        });
        mRewardedVideoAd.loadAd("ca-app-pub-3940256099942544/5224354917", new AdRequest.Builder().build());

        setVolumeControlStream(AudioManager.STREAM_MUSIC);


        setFonts();
        volumeControls();
    }

    private void volumeControls(){

        try {

            seekBar = (SeekBar) findViewById(R.id.seekBar);
            audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            if (audioManager != null) {
                seekBar.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
                seekBar.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
            }

            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void showAdVideo(View view){

        if(mRewardedVideoAd.isLoaded()){
            mRewardedVideoAd.show();
        }

    }

    private void setFonts(){
        Typeface font = Typeface.createFromAsset(getAssets(),"fonts/score_font.ttf" );

        volumeText.setTypeface(font);
        watchVideoText.setTypeface(font);
        rateAppText.setTypeface(font);
        emailText.setTypeface(font);

    }

    public void sendEmail(View view){

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("plain/text");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"dsapps2018@gmail.com"});
        startActivity(Intent.createChooser(intent, "Select Action"));
    }
}
