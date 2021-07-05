package com.HashTagApps.WATool.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.HashTagApps.WATool.R;
import com.HashTagApps.WATool.activity.BackupActivity;
import com.HashTagApps.WATool.adapter.BackupHistoryAdapter;
import com.HashTagApps.WATool.model.HistoryFileData;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class BackupHistoryFragment extends Fragment {

    private BackupActivity backupActivity;
    private BackupHistoryAdapter backupHistoryAdapter;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);

        backupActivity.disposables.add(backupActivity.db.userDao().getHistoryFiles()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<HistoryFileData>>() {
                    @Override
                    public void accept(List<HistoryFileData> historyFileData) {

                        ArrayList<HistoryFileData> historyFileData1 = new ArrayList<>();

                        for (HistoryFileData historyFileData2: historyFileData) {
                            File file = new File(historyFileData2.getFilePath());

                            if (file.exists()) {
                                historyFileData1.add(historyFileData2);
                            }
                        }

                        backupHistoryAdapter = new BackupHistoryAdapter(historyFileData1, backupActivity);
                        recyclerView.setLayoutManager(new LinearLayoutManager(backupActivity));
                        recyclerView.setAdapter(backupHistoryAdapter);

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        Log.e("tag error"," = = == = = " + throwable.getMessage());
                    }
                })
        );

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        backupActivity = (BackupActivity) context;
    }
}
