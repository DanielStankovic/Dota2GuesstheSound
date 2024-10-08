package com.dsapps2018.dota2guessthesound;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;

import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;

import android.view.View;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

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
import java.util.Random;





public class StartQuizActivity extends MainActivity {

    int chosenSound;
    int score = 0;
    int coinChance;

    MediaPlayer mediaPlayer;

    TextView scoreTextView;
    TextView resultTextView;
    TextView highScoreTextView;

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

    SharedPreferences settings;

    InterstitialAd mInterstitialAd;
    private boolean adIsLoading;

    AchievementsClient mAchievementClient;


    int achievementTotalGuessedSounds;


    ArrayList<Integer> sounds = new ArrayList<>(Arrays.<Integer>asList(R.raw.astral_spirit, R.raw.charge_of_darkness,
            R.raw.chemical_rage, R.raw.concussive_shot,
            R.raw.counter_helix, R.raw.curse_of_the_silent, R.raw.dark_pact, R.raw.death_ward, R.raw.decrepify,R.raw.dismember,
            R.raw.dragon_slave, R.raw.drunken_brawler, R.raw.duel, R.raw.earth_splitter, R.raw.electric_vortex, R.raw.epicenter,
            R.raw.exorcism,R.raw.eye_of_the_storm, R.raw.fade_bolt, R.raw.fiends_grip,
            R.raw.fire_remnant, R.raw.fissure, R.raw.frost_arrows, R.raw.ghost_shroud, R.raw.glimpse, R.raw.heat_seeking_missile,
            R.raw.hex_lion, R.raw.hoof_stomp, R.raw.howl, R.raw.icarus_dive,
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
    ArrayList<String> names = new ArrayList<>(Arrays.<String>asList("astral spirit","Charge of Darkness","chemical rage",
            "concussive shot", "Counter Helix", "Curse of the Silent", "Dark Pact", "Death Ward","decrepify", "Dismember",
            "Dragon Slave","drunken brawler", "Duel", "Earth Splitter","electric vortex","epicenter","exorcism", "Eye of the Storm",
            "fade bolt","Fiend's Grip", "Fire Remnant","fissure","frost arrows", "ghost shroud",
            "Glimpse","heat-seeking missile","hex","hoof stomp","howl","icarus dive",
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
            "walrus punch", "whirling death", "wild axes", "winter's curse", "wrath of nature",
            "x marks the spot"));
    ArrayList<String> alreadyUsedSounds = new ArrayList<>();
    ArrayList<String> achievementTotalDifferentSounds = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_quiz);

        button0 = findViewById(R.id.button0);
        button1 =  findViewById(R.id.button1);
        button2 =  findViewById(R.id.button2);
        button3 =  findViewById(R.id.button3);
        image =  findViewById(R.id.imageView);
        scoreTextView =  findViewById(R.id.scoreTextView);
        resultTextView =  findViewById(R.id.invokerResultTextView);
        playAgainButton =  findViewById(R.id.playAgainButton);
        highScoreTextView =  findViewById(R.id.highScoreTextView);

        playAgainLayout =  findViewById(R.id.playAgainLayout);
        soundAndScoreLayout = findViewById(R.id.relativeLayout1);
        buttonsLayout =  findViewById(R.id.linearLayout1);

        settings = this.getSharedPreferences("com.example.daniel.dota2guessthesound", Context.MODE_PRIVATE);





        //OVDE DA SE DOBAVI LISTA ZVUKOVA KOJI SU ISKORISCENI ZA ACHIVMENT PREKO SETTINGS I DA SE DODA
        // U  achievementTotalDifferentSounds LISTU.

        loadListOfSounds();
       achievementTotalGuessedSounds = loadAllGuessedSoundNumber(settings);

        getGoogleSignInAccount();

        loadInterstitialAd();

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setFonts();


        playAgain(findViewById(R.id.playAgainLayout));



    }

    private void getGoogleSignInAccount(){
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this,
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN).build());

        mGoogleSignInClient.silentSignIn().addOnCompleteListener(this, new OnCompleteListener<GoogleSignInAccount>() {
            @Override
            public void onComplete(@NonNull Task<GoogleSignInAccount> task) {

                if(task.isSuccessful()){
                  GoogleSignInAccount  mGoogleSignInAccount = task.getResult();
                    getAchievementClientAndToast(mGoogleSignInAccount);

                } else{

                    new android.app.AlertDialog.Builder(StartQuizActivity.this)
                            .setMessage(getString(R.string.start_quiz_sign_in_failed))
                            .setNeutralButton(android.R.string.ok, null)
                            .show();
                }

            }
        });

    }

    private void getAchievementClientAndToast(GoogleSignInAccount googleSignInAccount){
        mAchievementClient = Games.getAchievementsClient(StartQuizActivity.this, googleSignInAccount);
       Games.getGamesClient(this, GoogleSignIn.getLastSignedInAccount(StartQuizActivity.this) ).setViewForPopups(findViewById(R.id.gps_popup));

    }
    public void playSound(View view) {

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
    public void chooseSound(View view)  {

        Random random = new Random();
        coinChance = random.nextInt(100);
        if(view.getTag().toString().equals(Integer.toString(locationOfCorrectAnswer))){

            showCheckAnswerToast("CORRECT!", Color.GREEN, -50 );

             if(coinChance <= 10) {
                showCoinRewardToast(R.drawable.two_coin);
                setCoinValue(settings, 2);

            }

            alreadyUsedSounds.add(names.get(chosenSound));

           checkDifferentSoundsAchievement();

           achievementTotalGuessedSounds++;

           checkAllGuessedSoundAchievement(achievementTotalGuessedSounds, mAchievementClient);


            score++;
            mediaPlayer.stop();
            mediaPlayer.release();
            generateQuestion();
            scoreTextView.setText("Score: " + Integer.toString(score));
        } else{

            showInterstitial();
            mediaPlayer.stop();
            mediaPlayer.release();
            showCheckAnswerToast("WRONG!", Color.RED, -50 );
            showGameOverScreen();


        }




    }

    public void playAgain(View view){
        soundAndScoreLayout.setVisibility(View.VISIBLE);
        buttonsLayout.setVisibility(View.VISIBLE);
        playAgainLayout.setVisibility(View.GONE);
        score = 0;
        scoreTextView.setText("Score: " + Integer.toString(score));
        alreadyUsedSounds.clear();
        generateQuestion();


    }

    public void showGameOverScreen(){


        soundAndScoreLayout.setVisibility(View.GONE);
        buttonsLayout.setVisibility(View.GONE);
        playAgainLayout.setVisibility(View.VISIBLE);
        saveListOfSounds();
        saveAllGuessedSoundNumber(achievementTotalGuessedSounds, settings);
        resultTextView.setText("Your score is: " + score);

        setHighScore(score, settings, highScoreTextView, "startQuizHighScore", "");

    }



    public void backToMenu(View view){
        StartQuizActivity.this.finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
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

    private void saveListOfSounds(){

        StringBuilder stringBuilder = new StringBuilder();
        for(String s:achievementTotalDifferentSounds){
            if(!achievementTotalDifferentSounds.isEmpty()){
            stringBuilder.append(s);
            stringBuilder.append(",");
        }
        }
        settings.edit().putString(Constants.DIFFERENT_SOUNDS, stringBuilder.toString()).apply();

    }

    private void loadListOfSounds(){

        String differentSounds = settings.getString(Constants.DIFFERENT_SOUNDS, "");
        String[] arrayOfDifferentSounds = differentSounds.split(",");
        achievementTotalDifferentSounds.addAll(Arrays.asList(arrayOfDifferentSounds));

    }





    private void setFonts(){

        Typeface buttonFont = Typeface.createFromAsset(getAssets(), "fonts/font.ttf");
        Typeface scoreFont = Typeface.createFromAsset(getAssets(), "fonts/score_font.ttf");
        scoreTextView.setTypeface(scoreFont);

        button0.setTypeface(buttonFont);
        button1.setTypeface(buttonFont);
        button2.setTypeface(buttonFont);
        button3.setTypeface(buttonFont);
        playAgainButton.setTypeface(scoreFont);
        resultTextView.setTypeface(scoreFont);
        highScoreTextView.setTypeface(scoreFont);
    }


    //OVA METODA PROVERAVA ACHIVMENTE ZA OVAJ MODE, I ONLINE I OFFLINE
    private void checkDifferentSoundsAchievement(){

        //PROVERAVA DA LI TAJ ZVUK VEC POSTOJI U LISTI, AKO NE DODAJE GA
        if(!(achievementTotalDifferentSounds.contains(names.get(chosenSound)))){
            achievementTotalDifferentSounds.add(names.get(chosenSound));

            if (achievementTotalDifferentSounds.size() == 11) {
               checkIfSignedInForAchievements(mAchievementClient, R.string.achievement_novice_listener, Constants.NOVICE_LISTENER);
            } else if(achievementTotalDifferentSounds.size() == 26){
                checkIfSignedInForAchievements(mAchievementClient, R.string.achievement_apprentice_listener, Constants.APPRENTICE_LISTENER);
            }else if(achievementTotalDifferentSounds.size() == 51){
                checkIfSignedInForAchievements(mAchievementClient, R.string.achievement_journeyman_listener, Constants.JOURNEYMAN_LISTENER);
            }else if(achievementTotalDifferentSounds.size() == 101){
                checkIfSignedInForAchievements(mAchievementClient, R.string.achievement_master_listener, Constants.MASTER_LISTENER);
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
                        StartQuizActivity.this.mInterstitialAd = interstitialAd;
                        adIsLoading = false;
                        interstitialAd.setFullScreenContentCallback(
                                new FullScreenContentCallback() {
                                    @Override
                                    public void onAdDismissedFullScreenContent() {
                                        // Called when fullscreen content is dismissed.
                                        // Make sure to set your reference to null so you don't
                                        // show it a second time.
                                        StartQuizActivity.this.mInterstitialAd = null;
                                    }

                                    @Override
                                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                                        // Called when fullscreen content failed to show.
                                        // Make sure to set your reference to null so you don't
                                        // show it a second time.
                                        StartQuizActivity.this.mInterstitialAd = null;

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
