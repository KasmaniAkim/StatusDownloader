package com.HashTagApps.WATool.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.media.FaceDetector;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.HashTagApps.WATool.R;
import com.HashTagApps.WATool.helperclass.DatabaseHelper;
import com.HashTagApps.WATool.model.ReplyMessageDataItem;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.Objects;

public class AddReplyActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private ReplyMessageDataItem replyMessageDataItem;
    EditText readMessage, replyMessage;
    private TextView contactEditText, ignoreContact;
    private String checkMessage;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reply);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        db = DatabaseHelper.getDatabase(this);
        readMessage   = findViewById(R.id.read_message);
        replyMessage  = findViewById(R.id.reply_message);
        contactEditText  = findViewById(R.id.contact_edit_text);
        ignoreContact  = findViewById(R.id.ignore_edit_text);
        Button button = findViewById(R.id.done_button);

        contactEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(contactPickerIntent,1);
            }
        });

        ignoreContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(contactPickerIntent,2);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (contactEditText.getText().toString().isEmpty()) {
                    replyMessageDataItem = new ReplyMessageDataItem(readMessage.getText().toString(),
                            replyMessage.getText().toString(), "All",
                            ignoreContact.getText().toString(),1);
                } else {
                    replyMessageDataItem = new ReplyMessageDataItem(readMessage.getText().toString(),
                            replyMessage.getText().toString(), contactEditText.getText().toString(),
                            ignoreContact.getText().toString(),1);
                }
                checkMessage = readMessage.getText().toString();
                new BackTask().execute();
            }
        });

        MobileAds.initialize(this, initializationStatus -> {
        });
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }


    @SuppressLint("StaticFieldLeak")
    private class BackTask extends AsyncTask<Boolean, Boolean, Boolean> {

        @SuppressLint("WrongThread")
        @Override
        protected Boolean doInBackground(Boolean... booleans) {
            if (!db.userDao().checkMessageText(checkMessage)) {
                db.userDao().insertReplyMessage(replyMessageDataItem);
                readMessage.setText("");
                replyMessage.setText("");
            } else {
                return true;
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (aBoolean) {
                Toast.makeText(AddReplyActivity.this, "Message Already Executed", Toast.LENGTH_SHORT).show();
            } else {
                finish();
            }
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                Uri contactData = data.getData();

                Cursor cur = getContentResolver().query(Objects.requireNonNull(contactData), null, null, null, null);
                if (Objects.requireNonNull(cur).getCount() > 0) {
                    if (cur.moveToNext()) {
                        String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        Log.e("Names", name);
                        contactEditText.setText(contactEditText.getText() + name + ",");
                    }
                }
                cur.close();
            }
        } else if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {
                Uri contactData = data.getData();

                Cursor cur = getContentResolver().query(Objects.requireNonNull(contactData), null, null, null, null);
                if (Objects.requireNonNull(cur).getCount() > 0) {
                    if (cur.moveToNext()) {
                        String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        Log.e("Names", name);
                        ignoreContact.setText(ignoreContact.getText() + name + ",");
                    }
                }
                cur.close();
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



}
