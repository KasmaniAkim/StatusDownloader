package com.HashTagApps.WATool.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.HashTagApps.WATool.R;

public class Disclaimer extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_disclaimer, container, false);
        TextView textView = view.findViewById(R.id.dis2);


        String str_links = "By using this app, you agree to our <b><u><a href='https://hashtagappsnetwork.wordpress.com/privacy-policy-eu-gdpr-policy/'>privacy policy terms of use</a></u></b>";

        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setText( Html.fromHtml( str_links ) );
        textView.setLinksClickable(true);
        return view;
    }
}
