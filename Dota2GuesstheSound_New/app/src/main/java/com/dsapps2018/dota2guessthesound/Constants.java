package com.dsapps2018.dota2guessthesound;


public final class Constants {


     static final int RC_UNUSED = 5001;
     static final int RC_SIGN_IN = 9001;




    //ACHIEVEMENT OFFLINE CHECK CONSTANTS FOR QUIZ MODE

     static final String DIFFERENT_SOUNDS = "differentSounds";

     static final String NOVICE_LISTENER = "Novice Listener";
     static final String APPRENTICE_LISTENER = "Apprentice Listener";
     static final String JOURNEYMAN_LISTENER = "Journeyman Listener";
     static final String MASTER_LISTENER = "Master Listener";


    //CONSTANT FOR SAVING NUMBER OF ALL GUESSED SOUNDS

    static final String TOTAL_GUESSED_SOUNDS = "Total Guessed Sounds";

    static final String ATTRACTED = "Attracted";
    static final String INTERESTED = "Interested";
    static final String IMPRESSED = "Impressed";
    static final String HOOKED = "Hooked";
    static final String ABSORBED = "Absorbed";
    static final String IMMERSED = "Immersed";


    private Constants(){

        throw new AssertionError();
    }

}
