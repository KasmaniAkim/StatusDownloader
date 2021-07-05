package com.HashTagApps.WATool.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.HashTagApps.WATool.R;
import com.HashTagApps.WATool.helperclass.TouchImageView;
import com.HashTagApps.WATool.model.FileItem;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class ImageDetailAdapter extends PagerAdapter {

    private Activity activity;
    private View viewLayout;
    private ArrayList<FileItem> fileItems;

    public ImageDetailAdapter(Activity activity, ArrayList<FileItem> fileItems) {
        this.activity = activity;
        this.fileItems = fileItems;
    }

    @Override
    public int getCount() {
        return this.fileItems.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        TouchImageView imgDisplay;
        LayoutInflater inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        viewLayout = inflater.inflate(R.layout.fullscreen_image, container, false);

        imgDisplay = viewLayout.findViewById(R.id.imgDisplay);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeFile(fileItems.get(position).getFile().getAbsolutePath(), options);
        imgDisplay.setImageBitmap(bitmap);

        container.addView(viewLayout);

        return viewLayout;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    public void removeView(ViewGroup viewGroup, int position) {
        destroyItem(viewGroup, position, viewLayout);
    }

}
