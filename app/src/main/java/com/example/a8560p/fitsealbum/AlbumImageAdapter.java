package com.example.a8560p.fitsealbum;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.WorkerThread;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AlbumImageAdapter extends BaseAdapter {

    private class ViewHolder {
        public TextView textView;
        public ImageView imageView;
    }

    private Activity context;
    //private Context context;
    private LayoutInflater mInflater;
    private ViewHolder mViewHolder;

//    public AlbumImageAdapter(Context c) {
//        context = c;
//        mInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        AlbumActivity.folderAlbum = getAllMedia(context);
//    }

    public AlbumImageAdapter(Activity localContext) {
        context = localContext;
        AlbumActivity.folderAlbum = getAllMedia(context);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return AlbumActivity.folderAlbum.size();
    }
    public Object getItem(int position) {
        return position;
    }
    public long getItemId(int position) {
        return position;
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            mViewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.album_folder_image_item, parent, false);
            mViewHolder.textView = (TextView) convertView.findViewById(R.id.nameAlbumFolder);
            mViewHolder.imageView = (ImageView) convertView.findViewById(R.id.imageAlbumFolder);
            mViewHolder.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
            int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
            int sizeOfImage = screenWidth;
            if (screenWidth > screenHeight) {
                sizeOfImage = screenWidth / 2;
            }
            mViewHolder.imageView.setLayoutParams(new RelativeLayout.LayoutParams(sizeOfImage,sizeOfImage/2));
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        Glide.with(context).load(AlbumActivity.folderAlbum.get(position).GetNewestFile().getPath())
                .apply(new RequestOptions().placeholder(null).centerCrop())
                .into(mViewHolder.imageView);

        String title = "";
        title = AlbumActivity.folderAlbum.get(position).getName();
        if (AlbumActivity.folderAlbum.get(position).getAlbumFolderSize()==1)
            title += " \n(1 item)";
        else
            title += " \n(" + String.valueOf(AlbumActivity.folderAlbum.get(position).getAlbumFolderSize()) + " items)";
        mViewHolder.textView.setText(title);
        return convertView;


//        ImageView picturesView;
//        if (convertView == null) {
//            picturesView = new ImageView(context);
//            picturesView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//            int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
//            int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
//            int sizeOfImage = screenWidth;
//            if (screenWidth > screenHeight) {
//                sizeOfImage = screenWidth / 2;
//            }
//            picturesView.setLayoutParams(new GridView.LayoutParams(sizeOfImage, (sizeOfImage / 2)));
//        } else {
//            picturesView = (ImageView) convertView;
//        };
//            Glide.with(context).load(AlbumActivity.folderAlbum.get(position).GetNewestFile().getPath())
//                    .apply(new RequestOptions().placeholder(null).centerCrop())
//                    .into(picturesView);
//        return picturesView;
    }

    private static final String[] IMAGES = {
            MediaStore.MediaColumns.DATA,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.DATE_MODIFIED,
    };

    private ArrayList<AlbumFolder> getAllMedia(Activity activity) {
        Map<String, AlbumFolder> albumFolderMap = new HashMap<>();
        //AlbumFolder allFileFolder = new AlbumFolder();
        //allFileFolder.setName("All images");
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

                //allFileFolder.addAlbumFile(imageFile);
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
        //Collections.sort(allFileFolder.getAlbumFiles());
        //albumFolders.add(allFileFolder);
        for (Map.Entry<String, AlbumFolder> folderEntry : albumFolderMap.entrySet()) {
            AlbumFolder albumFolder = folderEntry.getValue();
            Collections.sort(albumFolder.getAlbumFiles());
            albumFolders.add(albumFolder);
        }
        return albumFolders;
    }
}
