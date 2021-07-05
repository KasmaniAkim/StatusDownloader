package com.HashTagApps.WATool.fragment;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.HashTagApps.WATool.R;
import com.HashTagApps.WATool.activity.IntroActivity;
import com.HashTagApps.WATool.adapter.AppAdapter;
import com.HashTagApps.WATool.model.AppItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class NotificationReadFragment extends Fragment {

    private IntroActivity introActivity;
    public NotificationReadFragment(IntroActivity introActivity) {
        this.introActivity = introActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification_read, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.app_recycler_view);


        ArrayList<AppItem> appItems = new ArrayList<>();

        int flags = PackageManager.GET_META_DATA |
                PackageManager.GET_SHARED_LIBRARY_FILES |
                PackageManager.GET_UNINSTALLED_PACKAGES;

        PackageManager pm = Objects.requireNonNull(getContext()).getPackageManager();
        List<ApplicationInfo> applications = pm.getInstalledApplications(flags);
        for (ApplicationInfo appInfo : applications) {
            if ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 1) {
                String appname = pm.getApplicationLabel(appInfo).toString();
                if (!appname.equals("")) {
                    if ("WhatsApp".equals(appname) || "Telegram".contains(appname)
                            || "Hike".equals(appname) || "Viber".equals(appname)
                            || "Instagram".equals(appname) || "Facebook".equals(appname)
                            || "Messenger".equals(appname) || "Imo".equals(appname)
                            || "Line".equals(appname) || "Sharechat".equals(appname)
                            || "Twitter".equals(appname) || "Linkedin".equals(appname)
                            || "Snapchat".equals(appname) || "Tumblr".equals(appname)
                            || "Tinder".equals(appname)) {

                        try {
                            appItems.add(new AppItem(getContext().getPackageManager().getApplicationIcon(appInfo.packageName), appname));
                        } catch (PackageManager.NameNotFoundException e) {
                            Log.e("tag", " =  = error =  = =  = " + e.getMessage());

                        }
                    }
                }
            }
        }


        AppAdapter appAdapter = new AppAdapter(appItems, getContext(),introActivity);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(appAdapter);

        return view;
    }

}
