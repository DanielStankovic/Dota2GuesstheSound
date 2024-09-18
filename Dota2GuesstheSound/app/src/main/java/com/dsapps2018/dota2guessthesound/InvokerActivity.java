package com.dsapps2018.dota2guessthesound;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Handler;

import android.os.Message;

import android.os.Bundle;


import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.games.AchievementsClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class InvokerActivity extends MainActivity {

    ImageView buttonQ;
    ImageView buttonW;
    ImageView buttonE;
    ImageView buttonInvoke;

    ImageView heart1;
    ImageView heart2;
    ImageView heart3;

    TextView timerTextView;
    TextView welcomeTextView;

    TextView highScoreTextView;
    TextView resultTextView;


    LinearLayout gameStartContainer;
    LinearLayout playGameContainer;
    RelativeLayout gameOverContainer;

    MediaPlayer mediaPlayer;
    int chosenSound;
    Random random;

    long playRate = 7000;
    int soundRateCounter = 1;
    int numberOfHearts = 2;


    int timePassed = 0;


    SharedPreferences settings;

    Timer timer;
    Timer uITimer;

    CountDownTimer countDownTimer;

    boolean invokedSound = false;

    InterstitialAd mInterstitialAd;
    private boolean adIsLoading;

    private ArrayList<ImageView> orbs;

    private ArrayList<String> soundOrder = new ArrayList<>();

    private ArrayList<Integer> sounds = new ArrayList<>(Arrays.asList(R.raw.invoker_mode_alacrity,
            R.raw.invoker_mode_chaos_meteor,
            R.raw.invoker_mode_cold_snap,
            R.raw.invoker_mode_deafening_blast,
            R.raw.invoker_mode_emp,
            R.raw.invoker_mode_forge_spirit,
            R.raw.invoker_mode_ghost_walk,
            R.raw.invoker_mode_ice_wall,
            R.raw.invoker_mode_sun_strike,
            R.raw.invoker_mode_tornado));

    private ArrayList<String> names = new ArrayList<>(Arrays.asList("alacrity",
            "chaos meteor",
            "cold snap",
            "deafening blast",
            "emp",
            "forge spirit",
            "ghost walk",
            "ice wall",
            "sun strike",
            "tornado"));

    String spell;

    List<String> spells = new ArrayList<>(3);

    // ACHIEVEMENTS VARIABLES

    AchievementsClient mAchievementClient;

    short soundsInARow = 0;

    List<String> invokeAllSpellsAchievementList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoker);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        buttonQ = findViewById(R.id.buttonQ);
        buttonW = findViewById(R.id.buttonW);
        buttonE = findViewById(R.id.buttonE);
        buttonInvoke = findViewById(R.id.buttonInvoke);

        ImageView orb1 = findViewById(R.id.orb1);
        ImageView orb2 = findViewById(R.id.orb2);
        ImageView orb3 = findViewById(R.id.orb3);

        heart1 = findViewById(R.id.heart1Image);
        heart2 = findViewById(R.id.heart2Image);
        heart3 = findViewById(R.id.heart3Image);

        gameStartContainer = findViewById(R.id.gameStartContainer);
        playGameContainer = findViewById(R.id.playGameContainer);
        gameOverContainer = findViewById(R.id.gameOverContainer);

        timerTextView = findViewById(R.id.timerTextView);
        welcomeTextView = findViewById(R.id.welcomeTextView);
        highScoreTextView = findViewById(R.id.invokerHighScoreTextView);
        resultTextView = findViewById(R.id.invokerResultTextView);

        random = new Random();

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setFonts();

        settings = this.getSharedPreferences("com.example.daniel.dota2guessthesound",
                Context.MODE_PRIVATE);

        getGoogleSignInAccount();

        orbs = new ArrayList<>(Arrays.asList(orb1, orb2, orb3));

        for (int i = 0; i < 3; i++) {
            spells.add(null);

        }

        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.invoker_mode_alacrity);

        loadInterstitialAd();

    }


    private void getGoogleSignInAccount() {
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this,
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN).build());

        mGoogleSignInClient.silentSignIn()
                .addOnCompleteListener(this, new OnCompleteListener<GoogleSignInAccount>() {
                    @Override
                    public void onComplete(@NonNull Task<GoogleSignInAccount> task) {

                        if (task.isSuccessful()) {
                            GoogleSignInAccount mGoogleSignInAccount = task.getResult();
                            getAchievementClientAndToast(mGoogleSignInAccount);

                        } else {

                            new android.app.AlertDialog.Builder(InvokerActivity.this)
                                    .setMessage(getString(R.string.start_quiz_sign_in_failed))
                                    .setNeutralButton(android.R.string.ok, null)
                                    .show();
                        }

                    }
                });

    }

    private void getAchievementClientAndToast(GoogleSignInAccount googleSignInAccount) {
        mAchievementClient = Games.getAchievementsClient(InvokerActivity.this, googleSignInAccount);
        Games.getGamesClient(this, GoogleSignIn.getLastSignedInAccount(InvokerActivity.this))
                .setViewForPopups(findViewById(R.id.gps_popup));

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (timer != null) {
            timer.cancel();
            timer.purge();
        }
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        if (uITimer != null) {
            uITimer.cancel();
            uITimer.purge();
        }


    }

    public void addQ(View view) {

        if (spells.size() >= 3) {
            spells.remove(0);
        }
        spells.add("Q");
        setOrbs();

    }

    public void addW(View view) {
        if (spells.size() >= 3) {
            spells.remove(0);
        }
        spells.add("W");
        setOrbs();
    }

    public void addE(View view) {
        if (spells.size() >= 3) {
            spells.remove(0);
        }
        spells.add("E");
        setOrbs();
    }

    public void invokeSpell(View view) {
        if (spells.size() == 3) {

            int quasOrbNumber = 0;
            int wexOrbNumber = 0;
            int exortOrbNumber = 0;

            for (int i = 0; i < 3; i++) {
                if (spells.get(i) != null) {

                    if (spells.get(i).equals("Q")) {
                        quasOrbNumber = quasOrbNumber + 1;
                    } else if (spells.get(i).equals("W")) {
                        wexOrbNumber = wexOrbNumber + 1;
                    } else if (spells.get(i).equals("E")) {
                        exortOrbNumber = exortOrbNumber + 1;
                    }

                }

            }


            if (quasOrbNumber == 3) {
                spell = "cold snap";
            } else if (quasOrbNumber == 2 && wexOrbNumber == 1) {

                spell = "ghost walk";

            } else if (quasOrbNumber == 2 && exortOrbNumber == 1) {

                spell = "ice wall";

            } else if (wexOrbNumber == 3) {

                spell = "emp";

            } else if (wexOrbNumber == 2 && quasOrbNumber == 1) {

                spell = "tornado";
            } else if (wexOrbNumber == 2 && exortOrbNumber == 1) {
                spell = "alacrity";
            } else if (exortOrbNumber == 3) {

                spell = "sun strike";

            } else if (exortOrbNumber == 2 && quasOrbNumber == 1) {


                spell = "forge spirit";
            } else if (exortOrbNumber == 2 && wexOrbNumber == 1) {

                spell = "chaos meteor";
            } else if (quasOrbNumber == 1 && wexOrbNumber == 1 && exortOrbNumber == 1) {

                spell = "deafening blast";

            }


            if (soundOrder.get(soundOrder.size() - 1).equals(spell)) {

                showCheckAnswerToast("CORRECT!", Color.GREEN, 50);
                checkInvokeAllSpellsAchievement();

                soundsInARow++;
                checkSoundsInARowAchievement();
                invokedSound = true;
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }

            } else {

                showCheckAnswerToast("WRONG!", Color.RED, 50);
                soundsInARow = 0;
                checkHearts();

                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }

            }

        }

    }

    private void addQuasOrb(ImageView view) {
        Glide.with(this).load(R.drawable.quas_icon).into(view);
    }

    private void addWexOrb(ImageView view) {
        Glide.with(this).load(R.drawable.wex_icon).into(view);
    }

    private void addExortOrb(ImageView view) {
        Glide.with(this).load(R.drawable.exort_icon).into(view);
    }

    private void setOrbs() {


        for (int i = 0; i < spells.size(); i++) {

            if (spells.get(i) != null) {
                if (spells.get(i).equals("Q")) {
                    addQuasOrb(orbs.get(i));

                } else if (spells.get(i).equals("W")) {
                    addWexOrb(orbs.get(i));
                } else if (spells.get(i).equals("E")) {

                    addExortOrb(orbs.get(i));
                }
            }
        }
    }

    private void setFonts() {

        Typeface scoreFont = Typeface.createFromAsset(getAssets(), "fonts/score_font.ttf");
        timerTextView.setTypeface(scoreFont);
        welcomeTextView.setTypeface(scoreFont);
        timerTextView.setTypeface(scoreFont);
        highScoreTextView.setTypeface(scoreFont);
        resultTextView.setTypeface(scoreFont);
    }

    public void playSound() {

        chosenSound = random.nextInt(sounds.size());
        mediaPlayer = MediaPlayer.create(getApplicationContext(), sounds.get(chosenSound));
        mediaPlayer.start();

        initiateCountDownHeartTimer();
        soundOrder.add(names.get(chosenSound));

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {


                if (soundRateCounter == 5) {

                    timer.cancel();
                    timer.purge();
                    playRate = 6000;
                    doSound(playRate);
                }
                if (soundRateCounter == 10) {

                    timer.cancel();
                    timer.purge();
                    playRate = 5000;
                    doSound(playRate);

                }

                if (soundRateCounter == 15) {

                    timer.cancel();
                    timer.purge();
                    playRate = 4000;
                    doSound(playRate);

                }

                if (soundRateCounter == 20) {

                    timer.cancel();
                    timer.purge();
                    playRate = 3000;
                    doSound(playRate);

                }

                if (soundRateCounter == 25) {

                    timer.cancel();
                    timer.purge();
                    playRate = 2000;
                    doSound(playRate);

                }

                if (soundRateCounter == 30) {

                    timer.cancel();
                    timer.purge();
                    playRate = 1000;
                    doSound(playRate);

                }

                if (soundRateCounter == 35) {

                    timer.cancel();
                    timer.purge();
                    playRate = 500;
                    doSound(playRate);

                }
                if (soundRateCounter == 40) {

                    timer.cancel();
                    timer.purge();
                    playRate = 250;
                    doSound(playRate);

                }

                soundRateCounter++;

            }
        });

    }

    public void doSound(long playRate) {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (!mediaPlayer.isPlaying()) {
                    mediaPlayer.release();
                    invokedSound = false;
                    playSound();
                    if (numberOfHearts < 0) {
                        this.cancel();

                    }
                }

            }
        }, 2000, playRate);

    }

    public void goInvoker(View view) {
        gameStartContainer.setVisibility(View.GONE);
        playGameContainer.setVisibility(View.VISIBLE);
        doSound(playRate);
        startTimer();


    }

    private void checkHearts() {

        if (numberOfHearts > 0) {
            if (numberOfHearts == 2) {
                numberOfHearts--;
                heart3.setVisibility(View.INVISIBLE);
            } else if (numberOfHearts == 1) {
                numberOfHearts--;
                heart2.setVisibility(View.INVISIBLE);
            }

        } else {
            timer.cancel();
            showInterstitial();
            showGameOverScreen();


        }
    }


    private void startTimer() {

        uITimer = new Timer();
        uITimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                timePassed++;

                Message msg = Message.obtain();
                msg.obj = String.valueOf(timePassed + "s");
                handler.sendMessage(msg);
            }
        }, 1000, 1000);

    }

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            timerTextView.setText(msg.obj.toString());

            checkSurviveForAchievement();

            return true;
        }
    });


    private void initiateCountDownHeartTimer() {

        InvokerActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {


                long totalTime = mediaPlayer.getDuration() + 2500L;
                countDownTimer = new CountDownTimer(totalTime, 1000) {

                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {
                        if (!invokedSound) {
                            checkHearts();
                        }
                        this.cancel();

                    }
                }.start();

            }
        });


    }

    public void backToMenu(View view) {
        InvokerActivity.this.finish();
    }

    private void showGameOverScreen() {

        countDownTimer.cancel();
        uITimer.cancel();
        uITimer.purge();
        handler.removeCallbacksAndMessages(null);
        mediaPlayer.release();

        playGameContainer.setVisibility(View.GONE);
        gameOverContainer.setVisibility(View.VISIBLE);
        resultTextView.setText("You survived for " + timePassed + " seconds");


        setHighScore(timePassed, settings, highScoreTextView, "invokerModeHighScore", "");

    }


    private void checkSoundsInARowAchievement() {

        if (soundsInARow == 5) {
            checkIfSignedInForAchievements(mAchievementClient,
                    R.string.achievement_invonewbie,
                    Constants.INVO_NEWBIE);
        }
        if (soundsInARow == 10) {
            checkIfSignedInForAchievements(mAchievementClient,
                    R.string.achievement_invobeginner,
                    Constants.INVO_BEGINNER);
        }
        if (soundsInARow == 25) {
            checkIfSignedInForAchievements(mAchievementClient,
                    R.string.achievement_invotrained,
                    Constants.INVO_TRAINED);
        }
        if (soundsInARow == 50) {
            checkIfSignedInForAchievements(mAchievementClient,
                    R.string.achievement_invoexpert,
                    Constants.INVO_EXPERT);
        }
        if (soundsInARow == 100) {
            checkIfSignedInForAchievements(mAchievementClient,
                    R.string.achievement_invogod,
                    Constants.INVO_GOD);
        }

    }

    private void checkSurviveForAchievement() {

        if (timePassed == 60) {

            checkIfSignedInForAchievements(mAchievementClient,
                    R.string.achievement_squishy,
                    Constants.SQUISHY);

        } else if (timePassed == 120) {

            checkIfSignedInForAchievements(mAchievementClient,
                    R.string.achievement_tough,
                    Constants.TOUGH);

        } else if (timePassed == 180) {

            checkIfSignedInForAchievements(mAchievementClient,
                    R.string.achievement_resilient,
                    Constants.RESILIENT);
        } else if (timePassed == 300) {

            checkIfSignedInForAchievements(mAchievementClient,
                    R.string.achievement_tanky,
                    Constants.TANKY);
        }

    }

    private void checkInvokeAllSpellsAchievement() {

        if (!(invokeAllSpellsAchievementList.contains(spell))) {
            invokeAllSpellsAchievementList.add(spell);
            if (invokeAllSpellsAchievementList.size() == 10) {
                if (numberOfHearts < 2) {
                    checkIfSignedInForAchievements(mAchievementClient,
                            R.string.achievement_diverse_skillset,
                            Constants.DIVERSE_SKILLSET);
                } else {
                    checkIfSignedInForAchievements(mAchievementClient,
                            R.string.achievement_flawless_skillset,
                            Constants.FLAWLESS_SKILLSET);

                }
            }
        }
    }

    private void loadInterstitialAd() {
        // Request a new ad if one isn't already loaded.
        if (adIsLoading || mInterstitialAd != null) {
            return;
        }
        adIsLoading = true;
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(
                this,
                getResources().getString(R.string.interstitial_ad_id),
                adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@androidx.annotation.NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        InvokerActivity.this.mInterstitialAd = interstitialAd;
                        adIsLoading = false;
                        interstitialAd.setFullScreenContentCallback(
                                new FullScreenContentCallback() {
                                    @Override
                                    public void onAdDismissedFullScreenContent() {
                                        // Called when fullscreen content is dismissed.
                                        // Make sure to set your reference to null so you don't
                                        // show it a second time.
                                        InvokerActivity.this.mInterstitialAd = null;
                                    }

                                    @Override
                                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                                        // Called when fullscreen content failed to show.
                                        // Make sure to set your reference to null so you don't
                                        // show it a second time.
                                        InvokerActivity.this.mInterstitialAd = null;

                                    }

                                    @Override
                                    public void onAdShowedFullScreenContent() {
                                        // Called when fullscreen content is shown.
                                    }
                                });
                    }

                    @Override
                    public void onAdFailedToLoad(@androidx.annotation.NonNull LoadAdError loadAdError) {
                        // Handle the error
                        mInterstitialAd = null;
                        adIsLoading = false;
                    }
                });
    }

    private void showInterstitial() {
        // Show the ad if it's ready. Otherwise restart the game.
        if (mInterstitialAd != null) {
            mInterstitialAd.show(this);
        } else {
            Toast.makeText(this, "Add not loaded yet", Toast.LENGTH_SHORT).show();
        }

        loadInterstitialAd();
    }
}
