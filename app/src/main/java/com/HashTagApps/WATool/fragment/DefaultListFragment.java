package com.HashTagApps.WATool.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;

import com.HashTagApps.WATool.R;
import com.HashTagApps.WATool.activity.BackupActivity;
import com.HashTagApps.WATool.adapter.BackupHistoryAdapter;
import com.HashTagApps.WATool.model.HistoryFileData;
import com.google.android.gms.dynamic.IFragmentWrapper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DefaultListFragment extends Fragment {

    private BackupActivity backupActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_default_list, container, false);

        String path = Environment.getExternalStorageDirectory().toString()+"/Download";
        File directory = new File(path);
        File[] files = directory.listFiles();
        for (File file : files) {
            Uri file2 = Uri.fromFile(file);
            if (MimeTypeMap.getFileExtensionFromUrl(file2.toString()).equals("txt")){
                if (file.getName().toLowerCase().contains("whatsapp")){
                    Log.e("/////","//////////"+file.getName());
                }
            }

        }
        return view;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        backupActivity = (BackupActivity) context;
    }



}
