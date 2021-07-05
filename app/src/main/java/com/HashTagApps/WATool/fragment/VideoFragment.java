package com.HashTagApps.WATool.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.HashTagApps.WATool.R;
import com.HashTagApps.WATool.activity.StatusSaverActivity;
import com.HashTagApps.WATool.adapter.VideoFileAdapter;
import com.HashTagApps.WATool.model.FileItem;

import java.io.File;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class VideoFragment extends Fragment {

    private StatusSaverActivity statusSaverActivity;
    private RecyclerView recyclerView;
    private TextView noStatus;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image, container, false);

        noStatus = view.findViewById(R.id.no_status);
        recyclerView = view.findViewById(R.id.recycler_view);

        addView(getFromSdcard());

        return view;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        StatusSaverActivity.videoFragmentVisible = isVisibleToUser;
        if (isVisibleToUser) {
            addView(getFromSdcard());
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        statusSaverActivity = (StatusSaverActivity) context;
    }
    public void onStart() {
        super.onStart();
        addView(getFromSdcard());
    }

    public ArrayList<FileItem> getFromSdcard() {

        ArrayList<FileItem> fileItems = new ArrayList<>();

        File file= new File(Environment.getExternalStorageDirectory(),"WhatsApp/Media/.Statuses");

        if (file.isDirectory()) {

            File[] listFile = file.listFiles();

            assert listFile != null;
            for (File aListFile : listFile) {

                String[] filenameArray = aListFile.getAbsolutePath().split("\\.");
                String extension = filenameArray[filenameArray.length - 1];

                if (extension.equals("mp4")) {

                    fileItems.add(new FileItem(aListFile));

                }
            }
        }
        return fileItems;
    }

    public void addView(ArrayList<FileItem> fileItems) {

        if (fileItems.size() == 0 ) {

            noStatus.setVisibility(View.VISIBLE);

        }else {

            noStatus.setVisibility(View.GONE);

        }

        statusSaverActivity.videoFileAdapter = new VideoFileAdapter(fileItems, statusSaverActivity);
        recyclerView.setLayoutManager((new GridLayoutManager(getContext(),2)));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(statusSaverActivity.videoFileAdapter);

    }


}
