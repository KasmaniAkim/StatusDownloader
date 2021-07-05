package com.HashTagApps.WATool.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.TextView;

import com.HashTagApps.WATool.R;
import com.HashTagApps.WATool.adapter.RSSFeedAdapter;
import com.HashTagApps.WATool.common.HTTPDataHandler;
import com.HashTagApps.WATool.model.RSSObject;
import com.google.gson.Gson;

public class RSSContent extends AppCompatActivity {

    RSSObject rssObject;
    WebView webView;
    TextView title,details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rsscontent);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        setTitle("Latest Offers");
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        webView = findViewById(R.id.webview);
        title = findViewById(R.id.title);
        details = findViewById(R.id.details);

        int position = getIntent().getIntExtra("position", 0);

        loadObject(position);
    }

    private void loadObject(int position) {

        @SuppressLint("StaticFieldLeak")
        AsyncTask<String,String,String> loadRSSAsync = new AsyncTask<String, String, String>() {

            ProgressDialog dialog = new ProgressDialog(RSSContent.this);

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
                String temp = rssObject.getItems().get(position).getCategories().toString();
                String temp2 = temp.replace("[","");
                String temp3 = temp2.replace("]","");
                title.setText(rssObject.getItems().get(position).getTitle());
                Log.e("tag", "   =  = =  = = = = = = " + rssObject.getItems().get(position).getLink());
                details.setText(temp3+" / "+rssObject.getItems().get(position).getAuthor()+" / "+rssObject.getItems().get(position).getPubDate());
                webView.loadData(rssObject.getItems().get(position).getContent(),"text/html; charset=UTF-8", null);
            }
        };

        String rss_to_json_api = "https://api.rss2json.com/v1/api.json?rss_url=";
        String rss_link = "https://hashtagappsnetwork.wordpress.com/category/latest-offers/feed";

        loadRSSAsync.execute(rss_to_json_api + rss_link);

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
