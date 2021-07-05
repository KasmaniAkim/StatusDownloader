package com.HashTagApps.WATool.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.HashTagApps.WATool.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.hbb20.CountryCodePicker;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class DirectChatActivity extends AppCompatActivity {

    EditText phoneNumberField, messageField;
    Button messageButton;
    CountryCodePicker countryCodePicker;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direct_chat);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        setTitle("Direct Chat");
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        phoneNumberField  = findViewById(R.id.input_field);
        messageField      = findViewById(R.id.input_field_message);
        messageButton     = findViewById(R.id.message_button);
        countryCodePicker = findViewById(R.id.cpp);

        messageButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                contactOnWhatsApp();
            }
        });


        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED){

            if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CALL_LOG)) {

                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_CALL_LOG},
                        2);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_CALL_LOG}, 2);

                }

            }

        }

        MobileAds.initialize(this, initializationStatus -> {
        });
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void contactOnWhatsApp() {

        if (phoneNumberField.getText().toString().isEmpty()) {
            Alert_Dialog_Blank_Input();
        } else {

            countryCodePicker.registerCarrierNumberEditText(phoneNumberField);
            String phoneNumber = countryCodePicker.getFullNumber();

            boolean installed = appInstalledOrNot();
            if(installed) {
                try {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://api.whatsapp.com/send?phone=" +
                                    phoneNumber +"&text="+ URLEncoder.encode(
                                            messageField.getText().toString(), "UTF-8")));
                    startActivity(browserIntent);
                } catch (UnsupportedEncodingException e) {
                    Log.e("tag","=== message error == " + e.getMessage());
                }
            } else {
                Toast.makeText(this,"Whatsapp is not installed on your device",Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 2) {
                if (data != null) {
                    phoneNumberField.setText("");
                    phoneNumberField.setText(Objects.requireNonNull(data.getData()).toString());
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    private boolean appInstalledOrNot() {
        PackageManager pm = getPackageManager();
        boolean app_installed;
        try {
            pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
            app_installed = true;
        }
        catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void Alert_Dialog_Blank_Input() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Error");
        builder.setMessage("Please Enter A Number");
        AlertDialog alert = builder.create();
        (Objects.requireNonNull(alert.getWindow())).setGravity(Gravity.CENTER);
        alert.show();
    }

}
