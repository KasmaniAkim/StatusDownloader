package com.HashTagApps.WATool.activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.HashTagApps.WATool.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class ChatScreenActivity extends AppCompatActivity {

    LinearLayout m_ll;
    Menu menu;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_screen);
        final ScrollView scrollView = findViewById(R.id.scrollView);

        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(View.FOCUS_DOWN);
            }
        });

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        setTitle("Chat Screen");
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        m_ll = findViewById(R.id.llMain);

        Intent intent = getIntent();
        String path = intent.getStringExtra("path");
        Uri uri = getUriForFile();



        BufferedReader br = null;
        if (uri!=null){

            InputStream inputStream = null;

            try {
                inputStream = getContentResolver().openInputStream(uri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
           br = new BufferedReader(new InputStreamReader(inputStream));

        }else {
            File file = new File(path);
            try {
                br = new BufferedReader(new FileReader(file));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

            String line = null;
            boolean left = false;
            String mainName = "";
            while (true) {
                try {
                    if (!((line = br.readLine()) != null)) break;
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (!line.contains("Messages to this chat and calls are now secured with end")) {

                    String[] strings = line.split("-");
                    String message;
                    String name = "";
                    String date = "";

                    if (strings.length == 1) {
                        message = strings[0];
                    } else {

                        date = strings[0];
                        String s = strings[1];
                        String[] messages = s.split(":");
                        name = messages[0];
                        message = line.replace( date + "-" + name +":","");

                        if (!mainName.equals(name)) {
                            mainName = name;
                            left = !left;
                        } else {
                            mainName = name;
                        }

                    }

                    if (!left) {


                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        params.setMargins(0,6,15,6);

                        LinearLayout mainLinearLayout = new LinearLayout(ChatScreenActivity.this);
                        mainLinearLayout.setOrientation(LinearLayout.VERTICAL);
                        mainLinearLayout.setLayoutParams(params);
                        mainLinearLayout.setGravity(Gravity.END);

                        LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        params3.setMargins(0,0,25,0);
                        TextView textView = new TextView(this);
                        textView.setLayoutParams(params3);
                        textView.setTextColor(Color.parseColor("#000000"));
                        textView.setText(name);

                        TextView textView2 = new TextView(this);
                        textView2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                        textView2.setText(date);

                        LinearLayout linearLayout = new LinearLayout(ChatScreenActivity.this);
                        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                        linearLayout.setGravity(Gravity.END);
                        linearLayout.addView(textView);
                        linearLayout.addView(textView2);


                        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        params2.setMargins(0,10,0,25);
                        TextView textView1 = new TextView(this);
                        textView1.setLayoutParams(params2);
                        textView1.setTextColor(Color.parseColor("#000000"));
                        textView1.setText(message);
                        textView1.setTextSize(20);

                        textView1.setBackgroundResource(R.drawable.rectangle2);
                        mainLinearLayout.addView(linearLayout);
                        mainLinearLayout.addView(textView1);

                        m_ll.addView(mainLinearLayout);
                    } else {


                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        params.setMargins(15,6,0,6);

                        LinearLayout mainLinearLayout = new LinearLayout(ChatScreenActivity.this);
                        mainLinearLayout.setOrientation(LinearLayout.VERTICAL);
                        mainLinearLayout.setLayoutParams(params);
                        mainLinearLayout.setGravity(Gravity.START);

                        LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        params3.setMargins(0,0,25,0);
                        TextView textView1 = new TextView(this);
                        textView1.setLayoutParams(params3);
                        textView1.setGravity(Gravity.START);
                        textView1.setTextColor(Color.parseColor("#000000"));
                        textView1.setText(name);

                        TextView textView3 = new TextView(this);
                        textView3.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                        textView3.setGravity(Gravity.START);
                        textView3.setText(date);

                        LinearLayout linearLayout = new LinearLayout(ChatScreenActivity.this);
                        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                        linearLayout.setGravity(Gravity.START);
                        linearLayout.addView(textView1);
                        linearLayout.addView(textView3);

                        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        params2.setMargins(0,10,0,25);
                        TextView textView2 = new TextView(this);
                        textView2.setLayoutParams(params2);
                        textView2.setGravity(Gravity.START);
                        textView2.setBackgroundResource(R.drawable.rectangle2);
                        textView2.setText(message);
                        textView2.setTextSize(20);
                        textView2.setTextColor(Color.parseColor("#000000"));
                        mainLinearLayout.addView(linearLayout);
                        mainLinearLayout.addView(textView2);

                        m_ll.addView(mainLinearLayout);
                    }
                }
            }
        try {
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        MobileAds.initialize(this, initializationStatus -> {
        });
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main, menu);
        this.menu = menu;
        return true;
    }

    private Uri getUriForFile() {
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        if (TextUtils.equals(Intent.ACTION_SEND, action) && !TextUtils.isEmpty(type)) {

            Uri uri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
            if (uri != null) {
                Log.e("uri",uri.toString());
                return  uri;
            }
        }
        return null;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
