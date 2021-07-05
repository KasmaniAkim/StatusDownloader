package com.HashTagApps.WATool.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.HashTagApps.WATool.R;
import com.HashTagApps.WATool.activity.FileExplorerActivity;
import com.HashTagApps.WATool.adapter.SendCleanerImageAdapter;
import com.HashTagApps.WATool.adapter.SendFileAdapter;
import com.HashTagApps.WATool.model.FileItem;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SendFragment extends Fragment {

    private FileExplorerActivity fileExplorerActivity;
    private RecyclerView recyclerView;
    private Parcelable listState;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);

        getFromSdcard();
        addView(fileExplorerActivity.sendFileItems);

        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
        recyclerView.getLayoutManager().onRestoreInstanceState(listState);
    }

    @Override
    public void onPause() {
        super.onPause();
        listState = recyclerView.getLayoutManager().onSaveInstanceState();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        fileExplorerActivity = (FileExplorerActivity) context;
    }



    @SuppressWarnings("deprecation")
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        FileExplorerActivity.sendFragmentVisible = isVisibleToUser;
    }

    @Override
    public void onStart() {
        super.onStart();
        getFromSdcard();
        addView(fileExplorerActivity.sendFileItems);
    }

    public void getFromSdcard() {
        fileExplorerActivity.sendFileItems = new ArrayList<>();
        getFiles(new File(fileExplorerActivity.watsAppPath + "/Sent"));
    }

    private void getFiles(File dir) {

        ArrayList<File> files = new ArrayList<>();

        File[] listFile = dir.listFiles();
        if (listFile != null && listFile.length > 0) {
            for (File file : listFile) {
                if (! (file.getName().startsWith("com.") || file.getName().startsWith("Android") || file.getName().contains(".nomedia"))) {
                    if (!file.isDirectory()) {
                        files.add(file);
                    }
                }
            }
        }

        Collections.reverse(files);

        if (fileExplorerActivity.sentSort) {
            Collections.sort(files, new Comparator<File>() {
                @Override
                public int compare(File t, File t1) {
                    return (int) (t.length() - t1.length());
                }
            });
            Collections.reverse(files);
        }

        if (fileExplorerActivity.sentSmallSort) {
            Collections.sort(files, new Comparator<File>() {
                @Override
                public int compare(File t, File t1) {
                    return (int) (t.length() - t1.length());
                }
            });

        }

        if (fileExplorerActivity.sentLatestFirst) {
            Collections.sort(files, new Comparator<File>() {
                @Override
                public int compare(File t, File t1) {
                    return Long.compare(t.lastModified(), t1.lastModified());
                }
            });
            Collections.reverse(files);
        }
        if (fileExplorerActivity.sentOldestFirst) {
            Collections.sort(files, new Comparator<File>() {
                @Override
                public int compare(File t, File t1) {
                    return Long.compare(t.lastModified(), t1.lastModified());
                }
            });

        }

        for (File file: files) {
            fileExplorerActivity.sendFileItems.add(new FileItem(file));
        }

    }

    public void addView(ArrayList<FileItem> fileItems) {

        if (fileExplorerActivity.watsAppPath.contains("Images") || fileExplorerActivity.watsAppPath.contains("Video") ) {
            fileExplorerActivity.sendCleanerImageAdapter = new SendCleanerImageAdapter(fileItems, fileExplorerActivity);
            recyclerView.setLayoutManager(new GridLayoutManager(fileExplorerActivity, 2));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(fileExplorerActivity.sendCleanerImageAdapter);

            if (fileExplorerActivity.gallery) {
                fileExplorerActivity.sendCleanerImageAdapter.isVisible(false);
            }
        } else {
            fileExplorerActivity.sendFileAdapter = new SendFileAdapter(fileItems, fileExplorerActivity);
            recyclerView.setLayoutManager((new LinearLayoutManager(fileExplorerActivity)));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(fileExplorerActivity.sendFileAdapter);
        }

    }
}
