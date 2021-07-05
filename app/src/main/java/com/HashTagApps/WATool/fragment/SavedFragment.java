package com.HashTagApps.WATool.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.HashTagApps.WATool.R;
import com.HashTagApps.WATool.activity.StatusSaverActivity;
import com.HashTagApps.WATool.adapter.SavedFileAdapter;
import com.HashTagApps.WATool.model.FileItem;

import java.io.File;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SavedFragment extends Fragment {

    private StatusSaverActivity statusSaverActivity;
    private RecyclerView recyclerView;
    private TextView noStatus;
    private Button downloadImageButton, downloadVideoButton;
    private String type = "jpg";
    private String mainType = "image";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_saved, container, false);

        downloadImageButton = view.findViewById(R.id.downloads_image_button);
        downloadVideoButton = view.findViewById(R.id.downloads_video_button);
        noStatus = view.findViewById(R.id.no_downloads);
        recyclerView = view.findViewById(R.id.downloads_recycler_view);

        addView(getFromSdcard("jpg"), "image");

        StatusSaverActivity.savedImageFragmentVisible = true;
        StatusSaverActivity.savedVideoFragmentVisible = false;

        downloadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                type = "jpg";
                mainType = "image";
                addView(getFromSdcard(type), mainType);
                statusSaverActivity.isSelected = false;
                statusSaverActivity.itemDownload.setVisible(false);
                statusSaverActivity.itemDelete.setVisible(false);
                statusSaverActivity.itemShare.setVisible(false);
                statusSaverActivity.itemSelect.setVisible(true);
                statusSaverActivity.howToUse.setVisible(true);
                statusSaverActivity.fileAdapter.setChecked(false);
                StatusSaverActivity.savedImageFragmentVisible = true;
                StatusSaverActivity.savedVideoFragmentVisible = false;
                downloadImageButton.setBackgroundResource(R.color.colorPrimary);
                downloadImageButton.setTextColor(Color.parseColor("#FFFFFF"));
                downloadVideoButton.setBackgroundResource(R.color.white);
                downloadVideoButton.setTextColor(Color.parseColor("#000000"));
            }
        });

        downloadVideoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type = "mp4";
                mainType = "video";
                addView(getFromSdcard(type), mainType);
                statusSaverActivity.isSelected = false;
                statusSaverActivity.itemDownload.setVisible(false);
                statusSaverActivity.itemDelete.setVisible(false);
                statusSaverActivity.itemShare.setVisible(false);
                statusSaverActivity.itemSelect.setVisible(true);
                statusSaverActivity.howToUse.setVisible(true);
                statusSaverActivity.fileAdapter.setChecked(false);
                StatusSaverActivity.savedImageFragmentVisible = false;
                StatusSaverActivity.savedVideoFragmentVisible = true;
                downloadImageButton.setBackgroundResource(R.color.white);
                downloadImageButton.setTextColor(Color.parseColor("#000000"));
                downloadVideoButton.setBackgroundResource(R.color.colorPrimary);
                downloadVideoButton.setTextColor(Color.parseColor("#FFFFFF"));

            }
        });
        return view;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        StatusSaverActivity.savedFragmentVisible = isVisibleToUser;
        if (isVisibleToUser) {
            try {
                Log.e("tag","= = = = =  = " + statusSaverActivity.itemDownload.getItemId());
                statusSaverActivity.itemDownload.setVisible(false);
                statusSaverActivity.itemDelete.setVisible(false);
                statusSaverActivity.itemShare.setVisible(false);
                statusSaverActivity.itemSelect.setVisible(true);
                statusSaverActivity.howToUse.setVisible(true);
            } catch (Exception e) {
                Log.e("tag","= = = = =  = " + e.getMessage());
            }

        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        statusSaverActivity = (StatusSaverActivity) context;
    }
    public void onStart() {
        super.onStart();
        addView(getFromSdcard(type), mainType);
        if (type.equals("mp4")) {
            downloadImageButton.setBackgroundResource(R.color.white);
            downloadImageButton.setTextColor(Color.parseColor("#000000"));
            downloadVideoButton.setBackgroundResource(R.color.colorPrimary);
            downloadVideoButton.setTextColor(Color.parseColor("#FFFFFF"));
        }
    }


    public ArrayList<FileItem> getFromSdcard(String type) {
    ArrayList<FileItem> fileItems = new ArrayList<>();
    File file= new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/WATool Saved Status");

    if (file.isDirectory()) {

        File[] listFile = file.listFiles();

        assert listFile != null;
        for (File aListFile : listFile) {

            String[] filenameArray = aListFile.getAbsolutePath().split("\\.");
            String extension = filenameArray[filenameArray.length - 1];

            if (extension.equals(type)) {

                fileItems.add(new FileItem(aListFile));

            }
        }
    }
    return fileItems;
}

    public void addView(ArrayList<FileItem> fileItems, String type) {

        if (fileItems.size() == 0 ) {

            noStatus.setVisibility(View.VISIBLE);

        }else {

            noStatus.setVisibility(View.GONE);

        }

        statusSaverActivity.fileAdapter = new SavedFileAdapter(fileItems, statusSaverActivity, type);
        recyclerView.setLayoutManager((new GridLayoutManager(getContext(),2)));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(statusSaverActivity.fileAdapter);

    }

}
