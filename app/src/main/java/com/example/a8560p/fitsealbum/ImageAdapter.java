package com.example.a8560p.fitsealbum;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class ImageAdapter extends BaseAdapter {

    private Activity context;
    public ImageAdapter(Activity localContext) {
        context = localContext;
        PicturesActivity.images = getAllShownImagesPath(context);
    }

    public int getCount() {
        return PicturesActivity.images.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView,
                        ViewGroup parent) {
        ImageView picturesView;

        //GridView grdView = (GridView)findViewById(R.id.galleryGridView);
        //int column = grdView.getNumColumns();
        int column = 4;
        int screenWidth =  Resources.getSystem().getDisplayMetrics().widthPixels;
        int screenHeight =  Resources.getSystem().getDisplayMetrics().heightPixels;

        if (screenWidth > screenHeight) {
            column = 6;
        }

        int sizeOfImage = (screenWidth - (column + 1) * 8) / column;

        if (convertView == null) {
            picturesView = new ImageView(context);
            picturesView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            picturesView
                    .setLayoutParams(new GridView.LayoutParams(sizeOfImage, sizeOfImage));

        }
        else {
            picturesView = (ImageView) convertView;
        }

        Glide.with(context).load(PicturesActivity.images.get(position))
                .apply(new RequestOptions()
                        .placeholder(null).centerCrop())
                .into(picturesView);

        return picturesView;
    }

    private ArrayList<String> getAllShownImagesPath(Activity activity)
    {
        Uri uri;
        Cursor cursor;
        int column_index_data;
        ArrayList<String> listOfAllImages = new ArrayList<String>();
        String absolutePathOfImage = null;

        String[] projection = {MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.Images.Media.DATE_MODIFIED};

        cursor = activity.getContentResolver().query(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection, null, null, "DATE_MODIFIED DESC");
        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        while (cursor.moveToNext()) {
            absolutePathOfImage = cursor.getString(column_index_data);
            listOfAllImages.add(absolutePathOfImage);
        }
        return listOfAllImages;
    }
}
