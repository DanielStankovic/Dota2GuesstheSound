package com.example.daniel.dota2guessthesound;



import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;

import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;



import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class FastFingerActivity extends ToastActivity implements SimpleDialogFragment.NoticeDialogListener {

    TextView timerTextView;
    TextView scoreTextView;
    TextView highScoreTextView;
    TextView resultTextView;

    DialogFragment newFragment;

    int score = 0;
    int numberOfQuestionsTextView = 0;

    long timer;

    int chosenSound;

    long coinChance;

    MediaPlayer mediaPlayer;

    int locationOfCorrectAnswer = 0;
    String[] answers = new String[4];

    ImageView image;

    Button button0;
    Button button1;
    Button button2;
    Button button3;
    Button playAgainButton;

    RelativeLayout playAgainLayout;
    RelativeLayout soundAndScoreLayout;
    LinearLayout buttonsLayout;

    InterstitialAd mInterstitialAd;

    CountDownTimer countDownTimer;
    SharedPreferences settings;


    ArrayList<Integer> sounds = new ArrayList<Integer>(Arrays.<Integer>asList(R.raw.astral_spirit, R.raw.charge_of_darkness,
            R.raw.counter_helix, R.raw.curse_of_the_silent, R.raw.dark_pact, R.raw.death_ward, R.raw.dismember,
            R.raw.dragon_slave, R.raw.duel, R.raw.earth_splitter, R.raw.eye_of_the_storm, R.raw.fiends_grip,
            R.raw.fire_remnant, R.raw.glimpse, R.raw.heat_seeking_missile, R.raw.hoof_stomp, R.raw.howl,
            R.raw.ice_blast, R.raw.ice_path, R.raw.invoke, R.raw.leap, R.raw.leech_seed, R.raw.life_break,
            R.raw.light_strike_array, R.raw.malefice, R.raw.mana_burn, R.raw.meat_hook, R.raw.natures_call,
            R.raw.nether_strike, R.raw.nether_swap, R.raw.omnislash, R.raw.open_wounds, R.raw.overgrowth,
            R.raw.paralyzing_cask, R.raw.penitence,R.raw.phantasm, R.raw.plasma_field, R.raw.poof, R.raw.pounce,
            R.raw.powershot, R.raw.press_the_attack, R.raw.primal_roar, R.raw.psionic_trap, R.raw.quill_spray,
            R.raw.rabid, R.raw.reality_rift, R.raw.reapers_scythe, R.raw.rearm, R.raw.reverse_polarity, R.raw.rip_tide,
            R.raw.rupture, R.raw.sacrifice, R.raw.scorched_earth, R.raw.searing_chains, R.raw.shackleshot,
            R.raw.shadow_poison, R.raw.shadow_wave, R.raw.shockwave, R.raw.shuriken_toss, R.raw.silence, R.raw.skewer,
            R.raw.snowball, R.raw.spell_steal, R.raw.spirit_lance, R.raw.stampede, R.raw.sticky_napalm,
            R.raw.stifling_dagger, R.raw.sun_ray, R.raw.supernova, R.raw.surge, R.raw.telekinesis, R.raw.teleportation,
            R.raw.thunder_clap, R.raw.thundergods_wrath, R.raw.timber_chain, R.raw.time_lock, R.raw.time_walk,
            R.raw.torrent, R.raw.unstable_concoction, R.raw.vacuum, R.raw.venomous_gale, R.raw.viper_strike,
            R.raw.walrus_punch, R.raw.whirling_death, R.raw.wild_axes, R.raw.winters_curse, R.raw.wrath_of_nature,
            R.raw.x_marks_the_spot));
    ArrayList<String> names = new ArrayList<String>(Arrays.<String>asList("astral spirit","Charge of Darkness",
            "Counter Helix", "Curse of the Silent", "Dark Pact", "Death Ward", "Dismember",
            "Dragon Slave", "Duel", "Earth Splitter", "Eye of the Storm", "Fiend's Grip",
            "Fire Remnant", "Glimpse","heat-seeking missile","hoof stomp","howl",
            "ice blast","ice path", "invoke","leap","leech seed","life break",
            "light strike array","malefice","mana burn", "meat hook","nature's call",
            "nether strike","nether swap","omnislash","open wounds", "overgrowth",
            "paralyzing cask","penitence","phantasm", "plasma field","poof", "pounce",
            "powershot","press the attack","primal roar", "psionic trap","quill spray",
            "rabid","reality rift", "reaper's scythe","rearm", "reverse polarity","rip tide",
            "rupture","sacrifice","scorched earth","searing chains","shackleshot",
            "shadow poison","shadow wave","shockwave","shuriken toss","silence","skewer",
            "snowball","spell steal","spirit lance","stampede","sticky napalm",
            "stifling dagger","sun ray","supernova", "surge","telekinesis","teleportation",
            "thunder clap","thundergod's wrath", "timber chain","time lock","time walk",
            "torrent","unstable concoction","vacuum","venomous gale", "viper strike",
            "walrus punch","whirling death","wild axes","winter's curse","wrath of nature",
            "x marks the spot"));
    ArrayList<String> alreadyUsedSounds = new ArrayList<String>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fast_finger);

        timerTextView = (TextView)findViewById(R.id.timerTextView);
        scoreTextView = (TextView)findViewById(R.id.scoreTextView);
        resultTextView = (TextView)findViewById(R.id.invokerResultTextView);

        button0 = (Button)findViewById(R.id.button0);
        button1 = (Button)findViewById(R.id.button1);
        button2 = (Button)findViewById(R.id.button2);
        button3 = (Button)findViewById(R.id.button3);
        image = (ImageView)findViewById(R.id.imageView);

        playAgainButton = (Button)findViewById(R.id.playAgainButton);
        highScoreTextView = (TextView)findViewById(R.id.highScoreTextView);

        playAgainLayout = (RelativeLayout)findViewById(R.id.playAgainLayout);
        soundAndScoreLayout= (RelativeLayout)findViewById(R.id.relativeLayout1);
        buttonsLayout = (LinearLayout)findViewById(R.id.linearLayout1);
        settings = this.getSharedPreferences("com.example.daniel.dota2guessthesound", Context.MODE_PRIVATE);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setFonts();

        mInterstitialAd = new InterstitialAd(getApplicationContext());
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        newFragment = new SimpleDialogFragment();
        newFragment.setCancelable(false);
        newFragment.show(ft, "tag");


    }

    @Override
    protected void onStop() {
        super.onStop();
        countDownTimer.cancel();
        finish();
    }



    public  void playSound(View view){

        if(!mediaPlayer.isPlaying()){
            mediaPlayer.start();
        }
    }

    public void generateQuestion() {

        Random random = new Random();
        chosenSound = random.nextInt(sounds.size());

        if (alreadyUsedSounds.size() == names.size()) {

            showNoMoreQuestionsDialog();



        } else {

            while (alreadyUsedSounds.contains(names.get(chosenSound))) {
                chosenSound = random.nextInt(sounds.size());
            }

            locationOfCorrectAnswer = random.nextInt(4);
            int incorrectAnswerLocation;

            for (int i = 0; i < 4; i++) {
                if (i == locationOfCorrectAnswer) {

                    answers[i] = names.get(chosenSound);

                } else {

                    incorrectAnswerLocation = random.nextInt(sounds.size());

                    while (incorrectAnswerLocation == chosenSound || Arrays.asList(answers).contains(names.get(incorrectAnswerLocation))) {
                        incorrectAnswerLocation = random.nextInt(sounds.size());
                    }


                    answers[i] = names.get(incorrectAnswerLocation);
                }
            }
            button0.setText(answers[0]);
            button1.setText(answers[1]);
            button2.setText(answers[2]);
            button3.setText(answers[3]);
            mediaPlayer = MediaPlayer.create(this, sounds.get(chosenSound));
            mediaPlayer.start();

        }
    }

    public void chooseSound(View view) throws InterruptedException {
        Random random = new Random();
        coinChance = random.nextInt(100);

        if(view.getTag().toString().equals(Integer.toString(locationOfCorrectAnswer))){

            showCheckAnswerToast("CORRECT!", Color.GREEN, -50 );
            if(coinChance <= 10){
                showCoinRewardToast(R.drawable.one_coin);
                setCoinValue(settings, 1);
            }
            alreadyUsedSounds.add(names.get(chosenSound));
            score++;




        } else{
            showCheckAnswerToast("WRONG!", Color.RED, -50 );


        }

        numberOfQuestionsTextView++;
        try {

                mediaPlayer.stop();
                mediaPlayer.release();



        } catch (Exception e){


        }
        scoreTextView.setText("Score: " + Integer.toString(score) + "/" + Integer.toString(numberOfQuestionsTextView));
        generateQuestion();

    }

    public void playAgain(View view){
        soundAndScoreLayout.setVisibility(View.VISIBLE);
        buttonsLayout.setVisibility(View.VISIBLE);
        playAgainLayout.setVisibility(View.GONE);
        score = 0;
        numberOfQuestionsTextView = 0;
        scoreTextView.setText("Score: " + Integer.toString(score) + "/" + Integer.toString(numberOfQuestionsTextView));
        alreadyUsedSounds.clear();
        generateQuestion();
       countDownTimer = new CountDownTimer(timer, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                timerTextView.setText(String.valueOf(millisUntilFinished / 1000) + "s");
            }

            @Override
            public void onFinish() {

                timerTextView.setText("0s");
                mediaPlayer.release();
                showInterstitialAd(mInterstitialAd);

                if(timer == 30200){
                    showCoinRewardToast(R.drawable.five_coin);
                    setCoinValue(settings, 5);
                }else if(timer == 60200){
                    showCoinRewardToast(R.drawable.ten_coin);
                    setCoinValue(settings, 10);
                }else if(timer == 90200){
                    showCoinRewardToast(R.drawable.fifteen_coin);
                    setCoinValue(settings, 15);
                }
                showGameOverScreen();
                alreadyUsedSounds.clear();

            }
        }.start();

    }

    public void showGameOverScreen(){

        soundAndScoreLayout.setVisibility(View.GONE);
        buttonsLayout.setVisibility(View.GONE);
        playAgainLayout.setVisibility(View.VISIBLE);
        resultTextView.setText("Your score is: " + score + "/" + numberOfQuestionsTextView);


    }

public void setFonts(){

    Typeface buttonFont = Typeface.createFromAsset(getAssets(), "fonts/font.ttf");
    Typeface scoreFont = Typeface.createFromAsset(getAssets(),"fonts/score_font.ttf" );
    scoreTextView.setTypeface(scoreFont);

    button0.setTypeface(buttonFont);
    button1.setTypeface(buttonFont);
    button2.setTypeface(buttonFont);
    button3.setTypeface(buttonFont);
    playAgainButton.setTypeface(scoreFont);
    scoreTextView.setTypeface(scoreFont);
    timerTextView.setTypeface(scoreFont);
    resultTextView.setTypeface(scoreFont);
    highScoreTextView.setTypeface(scoreFont);

}


    @Override
    public void onGoButtonClick(long time) {
        timer = time;
        playAgain(findViewById(R.id.playAgainLayout));
        newFragment.dismiss();
    }

    public void backToMenu(View view){
        FastFingerActivity.this.finish();
    }

    private  void showNoMoreQuestionsDialog(){

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Congratulations!");
        alertDialogBuilder.setMessage("You have answered all the sounds!\nSoon we will add more sounds and new modes!\nStay tuned!");
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("Play Again", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                playAgain(findViewById(R.id.playAgainButton));
            }
        });

        alertDialogBuilder.setNeutralButton("Back to Menu", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                backToMenu(findViewById(R.id.goBackImageView));
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

}
