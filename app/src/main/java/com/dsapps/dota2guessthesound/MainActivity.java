package com.dsapps.dota2guessthesound;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;

import android.graphics.Typeface;

import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

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

    public GamesClient getGamesClient(Activity activity) {
        return gamesClient;
    }

    private GoogleSignInClient mGoogleSignInClient;

    private AchievementsClient mAchievementsClient;

    //achievements pending to be pushed on the cloud when the user logs in.
 //   private final AccomplishmentsOutbox mOutbox = new AccomplishmentsOutbox();


    private static final String TAG = "TanC";

    private static final int RC_UNUSED = 5001;
    private static final int RC_SIGN_IN = 9001;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startQuizTextView = (TextView)findViewById(R.id.playGameTextView);
        fastFingersTextView = (TextView)findViewById(R.id.fastFingerTextView);
        invokerTextView = (TextView)findViewById(R.id.invokerTextView);
        coinTextView = (TextView)findViewById(R.id.coinTextView);
        signSwitch = (SwitchCompat)findViewById(R.id.signSwitch);
        signTextView = (TextView)findViewById(R.id.signTextView);



        mGoogleSignInClient = GoogleSignIn.getClient(this,
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN).build());


        leftArrow = (ImageView)findViewById(R.id.leftArrow);
        rightArrow = (ImageView)findViewById(R.id.rightArrow);

       setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Typeface font = Typeface.createFromAsset(getAssets(),"fonts/score_font.ttf" );

        startQuizTextView.setTypeface(font);
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

    private boolean isSignedIn() {
        return GoogleSignIn.getLastSignedInAccount(this) != null;
    }

    private void signInSilently() {
        Log.d(TAG, "signInSilently()");

        mGoogleSignInClient.silentSignIn().addOnCompleteListener(this,
                new OnCompleteListener<GoogleSignInAccount>() {
                    @Override
                    public void onComplete(@NonNull Task<GoogleSignInAccount> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInSilently(): success");
                            onConnected(task.getResult());
                        } else {
                            Log.d(TAG, "signInSilently(): failure", task.getException());
                           onDisconnected();
                        }
                    }
                });
    }

    private void startSignInIntent() {
        startActivityForResult(mGoogleSignInClient.getSignInIntent(), RC_SIGN_IN);
    }

    private void signOut() {
        Log.d(TAG, "signOut()");

        if (!isSignedIn()) {
            Log.w(TAG, "signOut() called, but was not signed in!");
            return;
        }

        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        boolean successful = task.isSuccessful();
                        Log.d(TAG, "signOut(): " + (successful ? "success" : "failed"));

                       onDisconnected();
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if(requestCode == RC_SIGN_IN && resultCode == GamesActivityResultCodes.RESULT_RECONNECT_REQUIRED){
            onDisconnected();
        }
        if (requestCode == RC_SIGN_IN) {
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

    private void onConnected(GoogleSignInAccount googleSignInAccount){

        mAchievementsClient = Games.getAchievementsClient(this, googleSignInAccount);
        gamesClient = Games.getGamesClient(MainActivity.this, GoogleSignIn.getLastSignedInAccount(getApplicationContext()));
        gamesClient.setViewForPopups(findViewById(R.id.gps_popup));
        signSwitch.setChecked(true);
    }

    private void onDisconnected() {

        mAchievementsClient = null;
        signSwitch.setChecked(false);
        signTextView.setText("Signed out");
    }

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

    private void handleException(String details) {

        new AlertDialog.Builder(MainActivity.this)
                .setMessage(details)
                .setNeutralButton(android.R.string.ok, null)
                .show();
    }
    @Override
    protected void onResume() {
        super.onResume();
        getCoinValue(settings, coinTextView);
        signInSilently();
        if(!isSignedIn()){
            onDisconnected();
        }
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

    public void aboutActivity(View view){

       Intent aboutIntent = new Intent(this, AboutActivity.class);
       startActivity(aboutIntent);
    }



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
}
