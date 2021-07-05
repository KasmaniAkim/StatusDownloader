//package com.HashTagApps.WATool.helperclass;
//
//import android.app.Service;
//import android.content.Intent;
//import android.os.Environment;
//import android.os.FileObserver;
//import android.os.IBinder;
//import android.util.Log;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.InputStream;
//import java.io.OutputStream;
//
//public class FileObserverService extends Service {
//
//    FileObserver fileObserver;
//    @Override
//    public IBinder onBind(Intent intent) {
//        // TODO: Return the communication channel to the service.
//        throw new UnsupportedOperationException("Not yet implemented");
//    }
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        String pathToWatch = Environment.getExternalStorageDirectory().getPath()+"/WhatsApp/Media/WhatsApp Images/";
//        fileObserver = new FileObserver(pathToWatch) {
//
//            @Override
//            public void onEvent(int event, String file) {
//
//
//                String outputPath = Environment.getExternalStorageDirectory().getPath()+"/WATools Deleted Media/";
//
//                if (file!=null && !file.equals("")){
//                    copyFile(pathToWatch,file,outputPath);
//                }
//
//            }
//        };
//        fileObserver.startWatching();
//        return super.onStartCommand(intent, flags, startId);
//    }
//
//    private void copyFile(String inputPath, String inputFile, String outputPath) {
//
//        InputStream in = null;
//        OutputStream out = null;
//        try {
//
//            //create output directory if it doesn't exist
//            File dir = new File (outputPath);
//            if (!dir.exists())
//            {
//                dir.mkdirs();
//            }
//
//
//            in = new FileInputStream(inputPath + inputFile);
//            out = new FileOutputStream(outputPath + inputFile);
//
//            byte[] buffer = new byte[1024];
//            int read;
//            while ((read = in.read(buffer)) != -1) {
//                out.write(buffer, 0, read);
//            }
//            in.close();
//            in = null;
//
//            // write the output file (You have now copied the file)
//            out.flush();
//            out.close();
//            out = null;
//
//        } catch (Exception fnfe1) {
//            Log.e("tag", ""+fnfe1.getMessage());
//        }
//
//    }
//}