package com.example.a8560p.fitsealbum;

import android.app.Activity;
import android.content.Intent;
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
        if (convertView == null) {
            picturesView = new ImageView(context);
            picturesView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            picturesView
                    .setLayoutParams(new GridView.LayoutParams(230, 230));

        } else {
            picturesView = (ImageView) convertView;
        }

        Glide.with(context).load(PicturesActivity.images.get(position))
                .apply(new RequestOptions()
                        .placeholder(R.mipmap.ic_launcher).centerCrop())
                .into(picturesView);
        return picturesView;
    }

    private ArrayList<String> getAllShownImagesPath(Activity activity) {
        Uri uri;
        Cursor cursor;
        int column_index_data, column_index_folder_name;
        ArrayList<String> listOfAllImages = new ArrayList<String>();
        String absolutePathOfImage = null;

        uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        context.grantUriPermission("id", uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.revokeUriPermission(uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);

        String[] projection = {MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME};

        cursor = activity.getContentResolver().query(uri, projection, null,
                null, null);
        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        column_index_folder_name = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
        while (cursor.moveToNext()) {
            absolutePathOfImage = cursor.getString(column_index_data);
            listOfAllImages.add(absolutePathOfImage);
        }
        return listOfAllImages;
    }
}
