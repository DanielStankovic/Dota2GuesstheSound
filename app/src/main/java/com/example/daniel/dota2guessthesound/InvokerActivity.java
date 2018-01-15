package com.example.daniel.dota2guessthesound;

import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class InvokerActivity extends AppCompatActivity {

    ImageView buttonQ;
    ImageView buttonW;
    ImageView buttonE;
    ImageView buttonInvoke;

    ImageView heart1;
    ImageView heart2;
    ImageView heart3;

    TextView timerTextView;
    TextView welcomeTextView;

    LinearLayout gameStartContainer;
    LinearLayout playGameContainer;

    MediaPlayer mediaPlayer;
    int chosenSound;
    Random random;

    long playRate = 5000;
    int soundRateCounter =1;
    int numberOfHearts = 2;

    Timer timer;


    private ArrayList<ImageView> orbs;

    private ArrayList<String> soundOrder =  new ArrayList<>();

    private ArrayList<Integer> sounds = new ArrayList<>(Arrays.asList(R.raw.invoker_mode_alacrity, R.raw.invoker_mode_chaos_meteor, R.raw.invoker_mode_cold_snap, R.raw.invoker_mode_deafening_blast, R.raw.invoker_mode_emp, R.raw.invoker_mode_forge_spirit, R.raw.invoker_mode_ghost_walk, R.raw.invoker_mode_ice_wall, R.raw.invoker_mode_sun_strike, R.raw.invoker_mode_tornado));

    private ArrayList<String> names = new ArrayList<>(Arrays.asList("alacrity", "chaos meteor", "cold snap" ,"deafening blast", "emp", "forge spirit", "ghost walk", "ice wall", "sun strike", "tornado"));

    String spell;

    List<String> spells = new ArrayList<>(3);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoker);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        buttonQ =(ImageView) findViewById(R.id.buttonQ);
        buttonW = (ImageView)findViewById(R.id.buttonW);
        buttonE =(ImageView) findViewById(R.id.buttonE);
        buttonInvoke = (ImageView) findViewById(R.id.buttonInvoke);

        ImageView   orb1 = (ImageView)findViewById(R.id.orb1);
        ImageView  orb2 = (ImageView)findViewById(R.id.orb2);
        ImageView   orb3 =(ImageView) findViewById(R.id.orb3);

        heart1 = (ImageView)findViewById(R.id.heart1Image);
        heart2 = (ImageView)findViewById(R.id.heart2Image);
        heart3 = (ImageView)findViewById(R.id.heart3Image);

        gameStartContainer = (LinearLayout)findViewById(R.id.gameStartContainer);
        playGameContainer = (LinearLayout)findViewById(R.id.playGameContainer);

        timerTextView = (TextView)findViewById(R.id.timerTextView);
        welcomeTextView = (TextView)findViewById(R.id.welcomeTextView);

        random = new Random();

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setFonts();

        orbs = new ArrayList<>(Arrays.asList(orb1, orb2,orb3));

        for (int i = 0; i < 3 ; i++) {
            spells.add(null);

                    }

        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.invoker_mode_alacrity);
    }

    @Override
    protected void onStop() {
        super.onStop();
        timer.cancel();
        timer.purge();

    }

    public void addQ (View view){

        if(spells.size() >= 3){
            spells.remove(0);
        }
        spells.add("Q");
        setOrbs();

    }
    public void addW (View view){
        if(spells.size() >= 3){
            spells.remove(0);
        }
        spells.add("W");
        setOrbs();
    }
    public void addE (View view){
        if(spells.size() >= 3){
            spells.remove(0);
        }
        spells.add("E");
        setOrbs();
    }
    public void invokeSpell (View view){
        if(spells.size() == 3) {

            int quasOrbNumber = 0;
            int wexOrbNumber = 0;
            int exortOrbNumber = 0;

            for (int i = 0; i < 3; i++) {

                if (spells.get(i).equals("Q")) {
                    quasOrbNumber = quasOrbNumber + 1;
                } else if (spells.get(i).equals("W")) {
                    wexOrbNumber = wexOrbNumber + 1;
                } else if (spells.get(i).equals("E")) {
                    exortOrbNumber = exortOrbNumber + 1;
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

                Toast.makeText(getApplicationContext(), "CORRECT", Toast.LENGTH_SHORT).show();
            } else {
                if (numberOfHearts > 0) {
                    if(numberOfHearts == 2){
                        numberOfHearts--;
                        heart3.setVisibility(View.INVISIBLE);
                    }else if(numberOfHearts == 1){
                        numberOfHearts--;
                        heart2.setVisibility(View.INVISIBLE);
                    }

            }
            else{
                    Toast.makeText(getApplicationContext(), "Game Over", Toast.LENGTH_SHORT).show();
                 InvokerActivity.this.finish();
                }
      }

        }

    }

    private void addQuasOrb(ImageView view){
        Glide.with(this).load(R.drawable.quas_icon).into(view);
    }
    private void addWexOrb(ImageView view){
        Glide.with(this).load(R.drawable.wex_icon).into(view);
    }
    private void addExortOrb(ImageView view){
        Glide.with(this).load(R.drawable.exort_icon).into(view);
    }

    private void setOrbs(){


        for (int i = 0; i < spells.size(); i++) {

            if(spells.get(i)!= null) {
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



    private void setFonts(){

        Typeface scoreFont = Typeface.createFromAsset(getAssets(),"fonts/score_font.ttf" );
        timerTextView.setTypeface(scoreFont);
        welcomeTextView.setTypeface(scoreFont);
        timerTextView.setTypeface(scoreFont);
    }

    public void playSound(){

        chosenSound = random.nextInt(sounds.size());
        mediaPlayer = MediaPlayer.create(getApplicationContext(), sounds.get(chosenSound));
        mediaPlayer.start();
        soundOrder.add(names.get(chosenSound));
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {

                if(soundRateCounter == 5){
                    Log.i("AAAAAA", " soundcounter je 5");
                    timer.cancel();
                    timer.purge();
                    playRate = 6000;
                    doSound(playRate);
                }
                if(soundRateCounter == 10){
                    Log.i("AAAAAA", " soundcounter je 10");
                    timer.cancel();
                    timer.purge();
                    playRate = 5000;
                    doSound(playRate);

                }

                if(soundRateCounter == 15){
                    Log.i("AAAAAA", " soundcounter je 15");
                    timer.cancel();
                    timer.purge();
                    playRate = 4000;
                    doSound(playRate);

                }

                if(soundRateCounter == 20){
                    Log.i("AAAAAA", " soundcounter je 20");
                    timer.cancel();
                    timer.purge();
                    playRate = 3000;
                    doSound(playRate);

                }

                if(soundRateCounter == 25){
                    Log.i("AAAAAA", " soundcounter je 25");
                    timer.cancel();
                    timer.purge();
                    playRate = 2000;
                    doSound(playRate);

                }

                if(soundRateCounter == 30){
                    Log.i("AAAAAA", " soundcounter je 30");
                    timer.cancel();
                    timer.purge();
                    playRate = 1000;
                    doSound(playRate);

                }

                if(soundRateCounter == 35){
                    Log.i("AAAAAA", " soundcounter je 35");
                    timer.cancel();
                    timer.purge();
                    playRate = 500;
                    doSound(playRate);

                }
                if(soundRateCounter == 40){
                    Log.i("AAAAAA", " soundcounter je 40");
                    timer.cancel();
                    timer.purge();
                    playRate = 250;
                    doSound(playRate);

                }

                soundRateCounter++;

            }
        });

    }

    public void doSound(long playRate){
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (!mediaPlayer.isPlaying()) {
                    mediaPlayer.release();
                    playSound();
                    if(numberOfHearts < 0 ) {
                        this.cancel();
                    }
                }
            }
        }, 0, playRate);

    }

    public void goInvoker(View view){
        gameStartContainer.setVisibility(View.GONE);
        playGameContainer.setVisibility(View.VISIBLE);
        doSound(playRate);

    }
}
