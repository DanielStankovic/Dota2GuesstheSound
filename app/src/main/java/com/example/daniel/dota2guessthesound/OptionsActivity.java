package com.example.daniel.dota2guessthesound;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;

import org.w3c.dom.Text;

public class OptionsActivity extends AppCompatActivity {

    private SeekBar seekBar = null;
    private AudioManager audioManager = null;
    private TextView volumeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        volumeText  = (TextView)findViewById(R.id.volumeText);

        setVolumeControlStream(AudioManager.STREAM_MUSIC);


        Typeface font = Typeface.createFromAsset(getAssets(),"fonts/score_font.ttf" );

        volumeText.setTypeface(font);
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
}
