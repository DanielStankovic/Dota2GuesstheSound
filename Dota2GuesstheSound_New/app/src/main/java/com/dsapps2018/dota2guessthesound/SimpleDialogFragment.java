package com.dsapps2018.dota2guessthesound;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;


public class SimpleDialogFragment extends DialogFragment {

    NoticeDialogListener mListener;

    RadioGroup radioGroup;
    RadioButton thirtyButton;
    RadioButton sixtyButton;
    RadioButton ninetyButton;
    long timer;




    public interface NoticeDialogListener{

        void onGoButtonClick(long time);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

     try{   mListener = (NoticeDialogListener)context;
     } catch (ClassCastException e){

         throw new ClassCastException(context.toString() + "must implement NoticeDialogListener");
     }


    }



    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {




        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View custom_dialog = inflater.inflate(R.layout.custome_dialog, null);


        radioGroup =(RadioGroup) custom_dialog.findViewById(R.id.radioGroup);
        thirtyButton = (RadioButton)custom_dialog.findViewById(R.id.thirtyTimer);
        sixtyButton = (RadioButton)custom_dialog.findViewById(R.id.sixtyTimer);
        ninetyButton = (RadioButton)custom_dialog.findViewById(R.id.ninetyTimer);


        Button button = (Button) custom_dialog.findViewById(R.id.goButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTimer();
                mListener.onGoButtonClick(timer);
            }
        });
        builder.setView(custom_dialog);
        return builder.create();
    }


    public long setTimer(){
        int selectedTimer = radioGroup.getCheckedRadioButtonId();

        if(selectedTimer == R.id.thirtyTimer){
            timer = 30200;
        }else if(selectedTimer == R.id.sixtyTimer){
            timer = 30200;
        }else if(selectedTimer == R.id.ninetyTimer){
            timer = 90200;
        }else{
            timer = 30200;
        }

       return timer;
    }
}
