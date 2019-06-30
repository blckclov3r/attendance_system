package com.example.blckclov3r.arduino_firebase_nodemcu.DialogPackage;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.example.blckclov3r.arduino_firebase_nodemcu.R;

public class AccessCustomDialog extends DialogFragment {

    private static final String TAG = AccessCustomDialog.class.getSimpleName();
    private static final String COMMON_TAG = "abrenica_nujla";

    private EditText mPasscode;
    private OnInputSelected mListener;
    private long mLastClickTime = 0;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.access_custom_dialog,container,false);
        TextView ok,cancel;
        ok = view.findViewById(R.id.ok_textView);
        cancel = view.findViewById(R.id.cancel_textView);
        mPasscode = view.findViewById(R.id.input_editText);
        mPasscode.requestFocus();

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                String code = mPasscode.getText().toString().trim();
                if(!code.equals("")){
                    mListener.sendInput(code);
                }else{
                    return;
                }
                getDialog().dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog =  super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }


    public void setOnInputSelected(OnInputSelected listener){
        mListener = listener;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mListener = (OnInputSelected) getTargetFragment();
        }catch (ClassCastException e){
            e.printStackTrace();
            Log.e(COMMON_TAG,TAG+" onAttach: ClassCastException: "+e.getMessage());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
         mListener=null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPasscode = null;
    }
}
