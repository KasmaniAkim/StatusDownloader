package com.HashTagApps.WATool.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.HashTagApps.WATool.R;
import com.HashTagApps.WATool.adapter.RSSFeedAdapter;
import com.HashTagApps.WATool.common.HTTPDataHandler;
import com.HashTagApps.WATool.model.RSSObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class RSSFeedActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView recyclerView;
    RSSObject rssObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rssfeed);


        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        setTitle("Latest Offers");
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.rss_recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getBaseContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        
        loadRSS();
    }

    private void loadRSS() {
        @SuppressLint("StaticFieldLeak")
        AsyncTask<String,String,String> loadRSSAsync = new AsyncTask<String, String, String>() {

            ProgressDialog dialog = new ProgressDialog(RSSFeedActivity.this);

            @Override
            protected void onPreExecute() {
                dialog.setMessage("Please Wait");
                dialog.show();
            }

            @Override
            protected String doInBackground(String... strings) {
                String result;
                HTTPDataHandler http = new HTTPDataHandler();
                result= http.GetHTTPData(strings[0]);
                return result;
            }

            @Override
            protected void onPostExecute(String s) {
                dialog.dismiss();
                rssObject = new Gson().fromJson(s,RSSObject.class);
                RSSFeedAdapter rssFeedAdapter = new RSSFeedAdapter(rssObject,RSSFeedActivity.this);
                recyclerView.setAdapter(rssFeedAdapter);
                rssFeedAdapter.notifyDataSetChanged();
            }
        };
        String rss_to_json_api = "https://api.rss2json.com/v1/api.json?rss_url=";
        String rss_link = "https://hashtagappsnetwork.wordpress.com/category/latest-offers/feed";



        if (isNetworkAvailable(this)) {
            loadRSSAsync.execute(rss_to_json_api + rss_link);
        } else {
            Toast.makeText(this, "Internet Connection Required", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressWarnings("deprecation")
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = null;
        if (connectivityManager != null) {
            activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        }
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.rss_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(item.getItemId()==R.id.refresh_item){
            loadRSS();
        }else if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
