package com.example.daniel.dota2guessthesound;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;


import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InvokerActivity extends AppCompatActivity {

    ImageView buttonQ;
    ImageView buttonW;
    ImageView buttonE;
    ImageView buttonInvoke;



    private ArrayList<ImageView> orbs;




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
        ImageView   orb1 = (ImageView)findViewById(R.id.orb1);
        ImageView  orb2 = (ImageView)findViewById(R.id.orb2);
        ImageView   orb3 =(ImageView) findViewById(R.id.orb3);
        buttonInvoke = (ImageView) findViewById(R.id.buttonInvoke);
        orbs = new ArrayList<>(Arrays.asList(orb1, orb2,orb3));
        for (int i = 0; i < 3 ; i++) {
            spells.add(null);

        }
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
                spell = "Cold Snap";
            } else if (quasOrbNumber == 2 && wexOrbNumber == 1) {

                spell = "Ghost Walk";

            } else if (quasOrbNumber == 2 && exortOrbNumber == 1) {

                spell = "Ice Wall";

            } else if (wexOrbNumber == 3) {

                spell = "EMP";

            } else if (wexOrbNumber == 2 && quasOrbNumber == 1) {

                spell = "Tornado";
            } else if (wexOrbNumber == 2 && exortOrbNumber == 1) {
                spell = "Alactrity";
            } else if (exortOrbNumber == 3) {

                spell = "Sun Strike";

            } else if (exortOrbNumber == 2 && quasOrbNumber == 1) {


                spell = "Forge Spirit";
            } else if (exortOrbNumber == 2 && wexOrbNumber == 1) {

                spell = "Chaos Meteor";
            } else if (quasOrbNumber == 1 && wexOrbNumber == 1 && exortOrbNumber == 1) {

                spell = "Deafening Blast";

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
}
