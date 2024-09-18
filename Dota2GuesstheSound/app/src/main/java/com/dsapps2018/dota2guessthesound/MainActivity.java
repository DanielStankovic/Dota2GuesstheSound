package com.dsapps2018.dota2guessthesound;



import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;

import android.graphics.Typeface;

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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.games.AchievementsClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesActivityResultCodes;
import com.google.android.gms.games.GamesClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class MainActivity extends ToastActivity {

    TextView startQuizTextView;

    TextView fastFingersTextView;
    TextView invokerTextView;
    TextView coinTextView;

    AdView mAdView;

    String visibleView = "startQuiz";

    Animation animation;

    ImageView leftArrow;
    ImageView rightArrow;

    SwitchCompat signSwitch;
    TextView signTextView;

    SharedPreferences settings;

    GamesClient gamesClient;



    private GoogleSignInClient mGoogleSignInClient;


    boolean signedInForOnResume = false;


    private AchievementsClient mAchievementsClient;

   private static final int RC_UNUSED = 5001;
  private  static final int RC_SIGN_IN = 9001;


    //ARRAY SVIH IMENA ACHIVMENTA, POTREBNO ZA OTKLJUCAVANJE PRILIKOM SIGN IN-A, KORISTI SE KAO KLJUC ZA UPISIVANJE U SETTINGS
 String[] allAchievementsNames = {Constants.NOVICE_LISTENER, Constants.APPRENTICE_LISTENER, Constants.JOURNEYMAN_LISTENER, Constants.MASTER_LISTENER,
    Constants.TRY_IT_ALL, Constants.HASTY_TRIER, Constants.HASTY_REPEATER, Constants.PATIENT_TRIER, Constants.PATIENT_REPEATER,
    Constants.UNHURRIED_TRIER, Constants.UNHURRIED_REPEATER, Constants.SWIFT_FINGERS, Constants.HASTY_TOUCH, Constants.RAPID_MIND,
    Constants.INVO_NEWBIE, Constants.INVO_BEGINNER, Constants.INVO_TRAINED, Constants.INVO_EXPERT, Constants.INVO_GOD,
    Constants.SQUISHY, Constants.TOUGH, Constants.RESILIENT, Constants.TANKY, Constants.DIVERSE_SKILLSET, Constants.FLAWLESS_SKILLSET,
    Constants.ATTRACTED, Constants.INTERESTED, Constants.IMPRESSED, Constants.HOOKED, Constants.ABSORBED, Constants.IMMERSED};

 //ARRAY SVIH IDIJEVA ACHIVMENTA, POTREBNO ZA OTKLJUCAVANJE PRILIKOM SIGN IN-A
 int[] allAchievementsIDs = {R.string.achievement_novice_listener, R.string.achievement_apprentice_listener,
         R.string.achievement_journeyman_listener, R.string.achievement_master_listener,
 R.string.achievement_tryitall, R.string.achievement_hasty_trier, R.string.achievement_hasty_repeater
         , R.string.achievement_patient_trier, R.string.achievement_patient_repeater, R.string.achievement_unhurried_trier
         , R.string.achievement_unhurried_repeater, R.string.achievement_swift_fingers, R.string.achievement_hasty_touch
         , R.string.achievement_rapid_mind, R.string.achievement_invonewbie, R.string.achievement_invobeginner
         , R.string.achievement_invotrained, R.string.achievement_invoexpert, R.string.achievement_invogod
         , R.string.achievement_squishy, R.string.achievement_tough, R.string.achievement_resilient, R.string.achievement_tanky
         , R.string.achievement_diverse_skillset, R.string.achievement_flawless_skillset, R.string.achievement_attracted
         , R.string.achievement_interested, R.string.achievement_impressed, R.string.achievement_hooked
         , R.string.achievement_absorbed, R.string.achievement_immersed};






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startQuizTextView = findViewById(R.id.playGameTextView);
        fastFingersTextView = findViewById(R.id.fastFingerTextView);
        invokerTextView = findViewById(R.id.invokerTextView);
        coinTextView = findViewById(R.id.coinTextView);
       signSwitch = findViewById(R.id.signSwitch);
       signSwitch.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               onSwitchButtonClicked(view);
           }
       });
        signTextView = findViewById(R.id.signTextView);
        leftArrow = findViewById(R.id.leftArrow);
        rightArrow = findViewById(R.id.rightArrow);


        mGoogleSignInClient = GoogleSignIn.getClient(this,
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN).build());





       setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setFonts();

        //DOBIJANJE SETTINGS OBJEKTA
        settings = this.getSharedPreferences("com.example.daniel.dota2guessthesound", Context.MODE_PRIVATE);

        //OVDE SE DOBIJA SACUVANA TRENUTNA VREDNOST COINA
        getCoinValue(settings, coinTextView);

        //OVDE SE LOADUJE REKLAME
        MobileAds.initialize(this);
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


    }

    //METODA KOJA PROVERAVA DA LI JE USER SIGN IN-OVAN I VRACA BOOLEAN VREDNOST
    public boolean isSignedIn() {
        return GoogleSignIn.getLastSignedInAccount(this) != null;
    }

    //METODA ZA AUTOMATSKO SIGN IN-OVANJE, POZIVA SE U ONRESUME, KADA SE APLIKACIJA UPALI
    private void signInSilently() {

                mGoogleSignInClient.silentSignIn().addOnCompleteListener(this,
                new OnCompleteListener<GoogleSignInAccount>() {
                    @Override
                    public void onComplete(@NonNull Task<GoogleSignInAccount> task) {
                        if (task.isSuccessful()) {
                            onConnected(task.getResult());
                        } else {
                           onDisconnected();
                        }
                    }
                });
    }

    //METODA KOJA SIGN IN-UJE, POZIVA SE KADA SE KLIKNE NA SWTICH DUGME
    private void startSignInIntent() {
        startActivityForResult(mGoogleSignInClient.getSignInIntent(),RC_SIGN_IN);
    }


    //METODA KOJA SIGN OUT-UJE, POZIVA SE KADA SE KLIKNE NA SWITCH DUGME
    private void signOut() {

        if (!isSignedIn()) {
            return;
        }
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        onDisconnected();
                    }
                });
    }


    //METODA KOJA VRACA REZULTAT KADA SE POZOVE INTENT DA SE PRIKAZE ACHIEVEMENT UI
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if(requestCode == RC_UNUSED && resultCode == GamesActivityResultCodes.RESULT_RECONNECT_REQUIRED){
            onDisconnected();
            signedInForOnResume = true;

        }

        if(requestCode == RC_SIGN_IN ){


            Task<GoogleSignInAccount> task =
                    GoogleSignIn.getSignedInAccountFromIntent(intent);

            try {

                GoogleSignInAccount account = task.getResult(ApiException.class);
                onConnected(account);
            } catch (ApiException apiException) {
                onDisconnected();
                new AlertDialog.Builder(this)
                        .setMessage(getString(R.string.signin_other_error))
                        .setNeutralButton(android.R.string.ok, null)
                        .show();
            }
        }
    }

    //METODA KOJA RADI STVARI KADA SE KONEKTUJEM NA GOOGLE
    private void onConnected(GoogleSignInAccount googleSignInAccount){
        mAchievementsClient = Games.getAchievementsClient(this, googleSignInAccount);
        gamesClient = Games.getGamesClient(MainActivity.this, GoogleSignIn.getLastSignedInAccount(getApplicationContext()));
        gamesClient.setViewForPopups(findViewById(R.id.gps_popup));
        signSwitch.setChecked(true);
        signTextView.setText("Signed in with Google");
        for(int i = 0; i<31; i++){
            boolean achievement = settings.getBoolean(allAchievementsNames[i], false);
            if(achievement){
                mAchievementsClient.unlock(getString(allAchievementsIDs[i]));
            }
        }
    }

    //METODA KOJA RADI STVARI UKOLIKO SE DISKONEKTUJEM SA GOOGLA
    public void onDisconnected() {
        mGoogleSignInClient.signOut();
        mAchievementsClient = null;
        signSwitch.setChecked(false);
      signTextView.setText("Signed Out");
    }

    //METODA KOJA PRIKAZUJE ACHIEVEMENT UI UKOLIKO SAM ULOGOVAN, AKO NE ONDA ALERTDIALOG
    public void showAchievementUi(View view) {
        if (mAchievementsClient != null) {
            mAchievementsClient.getAchievementsIntent()
                    .addOnSuccessListener(new OnSuccessListener<Intent>() {
                        @Override
                        public void onSuccess(Intent intent) {
                            startActivityForResult(intent, RC_UNUSED);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            handleException( getString(R.string.achievements_exception));
                        }
                    });
        } else{

            handleException(getString(R.string.achievements_exception));
        }
    }

    //METODA KOJA PRIIKAZUJE ALERTDIALOG AKO DODJE DO EXCEPTIONA
    private void handleException(String details) {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(details)
                .setNeutralButton(android.R.string.ok, null)
                .show();
    }

    //METODA AKTIVNOSTI ONRESUME, TU SE SIGNINUJEM I DISKONEKTUJEM PO POTREBI
    @Override
    protected void onResume() {
        super.onResume();
        getCoinValue(settings, coinTextView);
        if(!signedInForOnResume) {
            signInSilently();
        } else {
            if (!isSignedIn()) {
                onDisconnected();
            }
        }



    }

    //METODA KOJA KREIRA CUSTOME ACTION BAR
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_actionbar, menu);
        return super.onCreateOptionsMenu(menu);
    }



    //METODA KOJA POKAZUJE TOAST KADA SE KLIKNE NA DOTA IKONICU U ACTION BARU
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.actionbar_icon :
              int spannableColor = ContextCompat.getColor(this, R.color.infoToastTitleColor);
              String infoText = "SPELL SOUND QUIZ FOR DOTA 2\nDeveloper: DS-Apps\nSerbia 2018";
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

    //METODA KOJA POKRECE START QUIZ AKTIVNOST
    public void playGameActivity(View view){
        Intent playGameIntent = new Intent(this, StartQuizActivity.class);
        startActivity(playGameIntent);
    }

    //METODA KOJA POKRECE FAST FINGER AKTIVNOST
    public void fastFingerActivity(View view){
        Intent playGameIntent = new Intent(this, FastFingerActivity.class);
        startActivity(playGameIntent);
    }

    //METODA KOJA POKRECE INVOKER AKTIVNOST
    public void invokerActivity(View view){
       if(checkCoinValue(settings)) {
            Intent optionsIntent = new Intent(this, InvokerActivity.class);
            startActivity(optionsIntent);
       }
    }

    //METODA KOJA POKRECE OPTIONS AKTIVNOST
    public void optionsActivity(View view){
        Intent optionsIntent = new Intent(this, OptionsActivity.class);
        startActivity(optionsIntent);
    }

    //METODA KOJA POKRECE ABOUT AKTIVNOST
    public void aboutActivity(View view){
       Intent aboutIntent = new Intent(this, AboutActivity.class);
       startActivity(aboutIntent);
    }




    //METODA KOJA IMA LOGIKU ZA PROMENU MODA NA LEVO
    public void goLeft(View view){
        if(visibleView.equals("fastFinger")){
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
    }

    //METODA KOJA IMA LOGIKU ZA PROMENU MODA NA DESNO
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
    }

    //METODA KOJA ANIMIRA NA LEVO
    private void animateToLeft(View view1, View view2){

        animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.view1toleft);
        view1.startAnimation(animation);
        animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.view2toleft);
        view2.startAnimation(animation);
        animationListener(animation);

    }

    //METODA KOJA ANIMIRA NA DESNO
    private void animateToRight(View view1, View view2) {
        animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.view1toright);
        view1.startAnimation(animation);
        animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.view2toright);
        view2.startAnimation(animation);
        animationListener(animation);

    }

    //METODA KOJA NE DOZVOLJAVA DA SE DVA PUTA KLIKNE NA STRELICU ZA ANIMIRANJE
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

    //POKAZUJE TOAST KADA SE KLIKNE NA COIN
    public void showCoinToast(View view){

        String coinToast = "Win coins by:\n-Playing the Quiz\n-Playing Fast Finger mode\n-Watching videos inside Options";
        showInfoToast(coinToast);
    }



    //METODA KOJA RADI NESTO KADA SE KLIKNE NA SIGN IN GOOGLE DUGME
    public void onSwitchButtonClicked (View view){
        boolean checked = ((SwitchCompat)view).isChecked();
        if(checked){
            signTextView.setText("Signed in with Google");
            startSignInIntent();
        }else{
            signTextView.setText("Signed out");
            signOut();
        }


    }

    //METODA KOJA POSTAVLJA FONTOVE NA TEXTVIEW ELEMENTE
    private void setFonts(){
        Typeface font = Typeface.createFromAsset(getAssets(),"fonts/score_font.ttf" );
        startQuizTextView.setTypeface(font);
        fastFingersTextView.setTypeface(font);
        invokerTextView.setTypeface(font);
        coinTextView.setTypeface(font);
    }


    //OVA METODA DOBAVLJA UKUPAN BROJ SVIH POGODJENIH ZVUKOVA
    public int loadAllGuessedSoundNumber(SharedPreferences settings){

        return settings.getInt(Constants.TOTAL_GUESSED_SOUNDS,0);
    }

    //OVA METODA SACUVAVA UKUPAN BROJ SVIH POGODJENIH ZVUKOVA
    public void saveAllGuessedSoundNumber(int achievementTotalGuessedSounds, SharedPreferences settings){
        settings.edit().putInt(Constants.TOTAL_GUESSED_SOUNDS, achievementTotalGuessedSounds).apply();
    }


    //OVA METODA PROVERAVA DA LI JE ISPUNJEN USLOV DA SE PRIKAZE ACHIVMENT ZA GUESS -NUMBER- SOUNDS IN TOTAL
    public void checkAllGuessedSoundAchievement(int achievementTotalGuessedSounds, AchievementsClient mAchievementClient) {



        if (isSignedIn()) {
            if (achievementTotalGuessedSounds == 100) {
                mAchievementClient.unlock(getString(R.string.achievement_attracted));
            }
            if (achievementTotalGuessedSounds == 250) {
                mAchievementClient.unlock(getString(R.string.achievement_interested));
            }
            if (achievementTotalGuessedSounds == 500) {
                mAchievementClient.unlock(getString(R.string.achievement_impressed));
            }
            if (achievementTotalGuessedSounds == 1000) {
                mAchievementClient.unlock(getString(R.string.achievement_hooked));
            }
            if (achievementTotalGuessedSounds == 2500) {
                mAchievementClient.unlock(getString(R.string.achievement_absorbed));
            }
            if (achievementTotalGuessedSounds == 5000) {
                mAchievementClient.unlock(getString(R.string.achievement_immersed));
            }
        }


        else{

            if(achievementTotalGuessedSounds == 100){
                saveBoolForAchievementsAndShowToast(Constants.ATTRACTED);
            }
            if(achievementTotalGuessedSounds == 250){
                saveBoolForAchievementsAndShowToast(Constants.INTERESTED);
            }
            if(achievementTotalGuessedSounds == 500){
                saveBoolForAchievementsAndShowToast(Constants.IMPRESSED);
            }
            if(achievementTotalGuessedSounds == 1000){
                saveBoolForAchievementsAndShowToast(Constants.HOOKED);
            }
            if(achievementTotalGuessedSounds == 2500){
                saveBoolForAchievementsAndShowToast(Constants.ABSORBED);
            }
            if(achievementTotalGuessedSounds == 5000){
                saveBoolForAchievementsAndShowToast(Constants.IMMERSED);
            }

        }
    }

    //METODA KOJA UPISUJE BOOLEAN VREDNOST ZA ACHIVMENTE KADA SAM OFFLINE I POKAZUJE TOAST ZA ACHIEVEMENT

    public void saveBoolForAchievementsAndShowToast(String constant){
        Toast.makeText(getApplicationContext(), "Achievement: " + constant + " unlocked", Toast.LENGTH_SHORT).show();

        settings.edit().putBoolean(constant, true).apply();
    }


    //OVA METODA PROVERAVA DA LI JE USER SIGN INOVAN I PRIKAZUJE ACHIEVEMNT ILI TOAST U ZAVISNOSTI
    public void checkIfSignedInForAchievements(AchievementsClient mAchievementClient, int id, String achievementText){

        if(isSignedIn()){
            mAchievementClient.unlock(getString(id));

        } else{

            saveBoolForAchievementsAndShowToast(achievementText);
        }
    }


}
