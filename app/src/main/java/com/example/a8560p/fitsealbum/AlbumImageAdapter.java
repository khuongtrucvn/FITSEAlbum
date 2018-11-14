package com.example.a8560p.fitsealbum;

import android.app.Activity;
import android.content.res.Resources;
import android.database.Cursor;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AlbumImageAdapter extends BaseAdapter {

    private Activity context;
    public AlbumImageAdapter(Activity localContext) {
        context = localContext;
        AlbumActivity.folderAlbum = getAllMedia(context);
    }

    public int getCount() {return AlbumActivity.folderAlbum.size(); }
    public Object getItem(int position) {
        return position;
    }
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView picturesView;
        //TextView textView = (TextView) convertView.findViewById(R.id.txtFolderName);

        if (convertView == null) {
            picturesView = new ImageView(context);
            picturesView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            int column = 1;
            int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
            int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

            if (screenWidth > screenHeight) {
                column = 2;
            }

            int sizeOfImage = screenWidth / column;
            picturesView.setLayoutParams(new GridView.LayoutParams(sizeOfImage, sizeOfImage / 2));
        }
        else {
            picturesView = (ImageView) convertView;
        };

        Glide.with(context).load(AlbumActivity.folderAlbum.get(position).GetNewestFile().getPath())
                .apply(new RequestOptions()
                        //.placeholder(R.mipmap.ic_launcher).centerCrop())
                        .placeholder(null).centerCrop())
                .into(picturesView);

        //textView.setText(AlbumActivity.folderAlbum.get(position).getName());

        return picturesView;
    }

    private static final String[] IMAGES = {
            MediaStore.MediaColumns.DATA,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.DATE_MODIFIED,
    };

    private ArrayList<AlbumFolder> getAllMedia(Activity activity) {
        Map<String, AlbumFolder> albumFolderMap = new HashMap<>();
        AlbumFolder allFileFolder = new AlbumFolder();
        allFileFolder.setName("All images");
        Cursor cursor = activity.getContentResolver().query(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                IMAGES, null, null, "DATE_MODIFIED DESC");
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String path = cursor.getString(0);
                String bucketName = cursor.getString(1);
                long modDate = cursor.getLong(2);

                AlbumFile imageFile = new AlbumFile();
                imageFile.setPath(path);
                imageFile.setBucketName(bucketName);
                imageFile.setDateModified(modDate);

                allFileFolder.addAlbumFile(imageFile);
                AlbumFolder albumFolder = albumFolderMap.get(bucketName);
                if (albumFolder != null)
                    albumFolder.addAlbumFile(imageFile);
                else {
                    albumFolder = new AlbumFolder();
                    albumFolder.setName(bucketName);
                    albumFolder.addAlbumFile(imageFile);
                    albumFolderMap.put(bucketName, albumFolder);
                }
            }
            cursor.close();
        }
        ArrayList<AlbumFolder> albumFolders = new ArrayList<>();
        Collections.sort(allFileFolder.getAlbumFiles());
        albumFolders.add(allFileFolder);
        for (Map.Entry<String, AlbumFolder> folderEntry : albumFolderMap.entrySet()) {
            AlbumFolder albumFolder = folderEntry.getValue();
            Collections.sort(albumFolder.getAlbumFiles());
            albumFolders.add(albumFolder);
        }
        return albumFolders;
    }
}
