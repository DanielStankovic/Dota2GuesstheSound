package com.dsapps2018.dota2guessthesound;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.media.AudioManager;

import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;


public class OptionsActivity extends ToastActivity {

    SeekBar seekBar = null;
    private AudioManager audioManager = null;
    TextView volumeText;
    TextView watchVideoText;
    TextView rateAppText;
    TextView emailText;
    SharedPreferences settings;
    private RewardedAd rewardedAd;
    private boolean isLoading;

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
        volumeText = (TextView) findViewById(R.id.volumeText);
        watchVideoText = (TextView) findViewById(R.id.watch_video_textView);
        rateAppText = (TextView) findViewById(R.id.rate_app_textView);
        emailText = (TextView) findViewById(R.id.email_textView);
        settings = this.getSharedPreferences("com.example.daniel.dota2guessthesound",
                Context.MODE_PRIVATE);

        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        loadRewardedAd();

        setFonts();
        volumeControls();
    }

    private void volumeControls() {

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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showAdVideo(View view) {
      showRewardedVideo();
    }

    private void setFonts() {
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/score_font.ttf");

        volumeText.setTypeface(font);
        watchVideoText.setTypeface(font);
        rateAppText.setTypeface(font);
        emailText.setTypeface(font);

    }

    public void sendEmail(View view) {

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("plain/text");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"dsapps2018@gmail.com"});
        startActivity(Intent.createChooser(intent, "Select Action"));
    }

    private void loadRewardedAd() {
        if (rewardedAd == null) {
            isLoading = true;
            AdRequest adRequest = new AdRequest.Builder().build();
            RewardedAd.load(
                    this,
                    getResources().getString(R.string.admob_rewarded_ad),
                    adRequest,
                    new RewardedAdLoadCallback() {
                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            // Handle the error.
                            rewardedAd = null;
                            OptionsActivity.this.isLoading = false;
                            Toast.makeText(OptionsActivity.this,
                                    "onAdFailedToLoad",
                                    Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                            OptionsActivity.this.rewardedAd = rewardedAd;
                            OptionsActivity.this.isLoading = false;
                        }
                    });
        }
    }

    private void showRewardedVideo() {
        if (rewardedAd == null) {
            Toast.makeText(OptionsActivity.this,
                    "Video is not ready yet.",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        rewardedAd.setFullScreenContentCallback(
                new FullScreenContentCallback() {
                    @Override
                    public void onAdShowedFullScreenContent() {
                        // Called when ad is shown.
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                        // Called when ad fails to show.

                        // Don't forget to set the ad reference to null so you
                        // don't show the ad a second time.
                        rewardedAd = null;
                        Toast.makeText(OptionsActivity.this,
                                "Video is not ready yet.",
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onAdDismissedFullScreenContent() {
                        // Called when ad is dismissed.
                        // Don't forget to set the ad reference to null so you
                        // don't show the ad a second time.
                        rewardedAd = null;
                        // Preload the next rewarded ad.
                        OptionsActivity.this.loadRewardedAd();

                    }
                });
        rewardedAd.show(
                OptionsActivity.this,
                new OnUserEarnedRewardListener() {
                    @Override
                    public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                        showCoinRewardToast(R.drawable.twenty_coin);
                        setCoinValue(settings, 20);
                    }
                });
    }
}
