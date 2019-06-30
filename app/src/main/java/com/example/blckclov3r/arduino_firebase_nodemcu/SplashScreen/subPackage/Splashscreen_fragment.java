package com.example.blckclov3r.arduino_firebase_nodemcu.SplashScreen.subPackage;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.blckclov3r.arduino_firebase_nodemcu.R;
import com.fujiyuu75.sequent.Animation;
import com.fujiyuu75.sequent.Sequent;

public class Splashscreen_fragment extends Fragment {

    private static final String TAG = Splashscreen_fragment.class.getSimpleName();
    private static final String COMMON_TAG = "abrenica_nujla";

    public Splashscreen_fragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.splashscreen_fragment, container, false);
        Log.d(COMMON_TAG,TAG+" onCreateView");
        if(savedInstanceState == null) {
            RelativeLayout header_relativeLayout = (RelativeLayout) view.findViewById(R.id.header_relativeLayout);
            Sequent.origin(header_relativeLayout).anim(getActivity(), Animation.FADE_IN).start();
        }
        return view;
    }


}
