package com.example.blckclov3r.arduino_firebase_nodemcu.NavFragments.FloatingActionButton;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import com.example.blckclov3r.arduino_firebase_nodemcu.R;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import hari.bounceview.BounceView;

public class AboutDialog extends DialogFragment {

    private static final String TAG = AboutDialog.class.getSimpleName();
    private static final String COMMON_TAG = "abrenica_nujla";

    private CircleImageView mCircleImageView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.about_dialog_fab,container,false);
        mCircleImageView = (CircleImageView) view.findViewById(R.id.miss);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mCircleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toasty.info(Objects.requireNonNull(getActivity()),"Stephanie Grace Villarubia", Toast.LENGTH_SHORT).show();
            }
        });

        BounceView.addAnimTo(mCircleImageView);

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mCircleImageView = null;
    }
}
