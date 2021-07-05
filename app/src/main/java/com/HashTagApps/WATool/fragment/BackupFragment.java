package com.HashTagApps.WATool.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.HashTagApps.WATool.R;
import com.HashTagApps.WATool.activity.BackupActivity;
import com.HashTagApps.WATool.activity.ChatScreenActivity;
import com.HashTagApps.WATool.model.HistoryFileData;

public class BackupFragment extends Fragment {

    private int STORAGE_PERMISSION_CODE=1;
    private BackupActivity backupActivity;
    public Button convertButton;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_backup, container, false);

        view.findViewById(R.id.button).setOnClickListener(view1 -> {

            if (ContextCompat.checkSelfPermission(backupActivity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(backupActivity,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                    ActivityCompat.requestPermissions(backupActivity, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            STORAGE_PERMISSION_CODE);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                        ActivityCompat.requestPermissions(backupActivity, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);

                    }

                }

            } else {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("text/*");
                intent = Intent.createChooser(intent, "Choose a file");
                backupActivity.startActivityForResult(intent, 100);
            }
        });

        convertButton = view.findViewById(R.id.convert_button);
        convertButton.setOnClickListener(view12 -> {
            if (backupActivity.path.equals("")) {
                Toast.makeText(backupActivity, "Please Select File", Toast.LENGTH_SHORT).show();
            } else {
                new BackTask().execute();
                Intent intent = new Intent(backupActivity, ChatScreenActivity.class);
                intent.putExtra("path", backupActivity.path);
                backupActivity.startActivity(intent);
                convertButton.setBackgroundResource(R.drawable.custom_button);
            }

        });

        return view;
    }


    @SuppressLint("StaticFieldLeak")
    public class BackTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            HistoryFileData historyFileData = new HistoryFileData(backupActivity.path);
            backupActivity.db.userDao().insertHistoryData(historyFileData);
            return null;
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        backupActivity = (BackupActivity) context;
    }
}
