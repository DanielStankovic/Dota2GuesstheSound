package com.example.daniel.dota2guessthesound;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class ToastActivity extends AppCompatActivity {
    Toast toast = null;

    public void showInfoToast(CharSequence message){

        LayoutInflater layoutInflater = getLayoutInflater();
        View layout = layoutInflater.inflate(R.layout.custome_toast, (ViewGroup)findViewById(R.id.custom_toast_layout));
        layout.setBackgroundColor(ContextCompat.getColor(this, R.color.lightGrayToast));

        TextView textView = (TextView)layout.findViewById(R.id.toast_message);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textView.setText(message);

        if(toast != null){
            toast.cancel();
        }

        toast = new Toast(this);
        toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, -430);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    public void showCheckAnswerToast(CharSequence message, int colorId ){
        LayoutInflater layoutInflater = getLayoutInflater();
        View layout = layoutInflater.inflate(R.layout.custome_toast, (ViewGroup)findViewById(R.id.custom_toast_layout));
        layout.setBackgroundColor(ContextCompat.getColor(this, R.color.answersBackgroundColor));

        TextView textView = (TextView)layout.findViewById(R.id.toast_message);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
        textView.setTypeface(null, Typeface.BOLD);
        textView.setTextColor(colorId);
        textView.setText(message);

        if(toast != null){
            toast.cancel();
        }

        toast = new Toast(this);
        toast.setGravity(Gravity.CENTER, 0, -50);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();



    }

}
