package com.example.a8560p.fitsealbum;

import android.app.Fragment;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.WorkerThread;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AlbumActivity extends Fragment{
    View album;

    public static AlbumActivity newInstance() {
        AlbumActivity fragment = new AlbumActivity();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        album = (View) inflater.inflate(R.layout.activity_album,container,false);

        ArrayList<AlbumFolder> folderName = getAllMedia();
        /*for(int i=0;i< folderName.size();i++)
        {
            Toast.makeText(AlbumActivity.super.getActivity(),folderName.get(i).getName(), Toast.LENGTH_SHORT).show();
        }*/

        return album;
    }

    /**
     * Image attribute.
     */
    private static final String[] IMAGES = {
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.MIME_TYPE,
            MediaStore.Images.Media.DATE_ADDED,
            MediaStore.Images.Media.LATITUDE,
            MediaStore.Images.Media.LONGITUDE,
            MediaStore.Images.Media.SIZE
    };

    @WorkerThread
    private void scanImageFile(Map<String, AlbumFolder> albumFolderMap, AlbumFolder allFileFolder) {
        ContentResolver contentResolver = this.getContext().getContentResolver();
        Cursor cursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                IMAGES,
                null,
                null,
                null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String path = cursor.getString(0);
                String bucketName = cursor.getString(1);
                String mimeType = cursor.getString(2);
                long addDate = cursor.getLong(3);
                float latitude = cursor.getFloat(4);
                float longitude = cursor.getFloat(5);
                long size = cursor.getLong(6);
                AlbumFile imageFile = new AlbumFile();
                imageFile.setMediaType(AlbumFile.TYPE_IMAGE);
                imageFile.setPath(path);
                imageFile.setBucketName(bucketName);
                imageFile.setMimeType(mimeType);
                imageFile.setAddDate(addDate);
                imageFile.setLatitude(latitude);
                imageFile.setLongitude(longitude);
                imageFile.setSize(size);

                //if (mSizeFilter != null && mSizeFilter.filter(size)) {
                //if (!mFilterVisibility) continue;
                //imageFile.setDisable(true);
                //}
                // if (mMimeFilter != null && mMimeFilter.filter(mimeType)) {
                //if (!mFilterVisibility) continue;
                //imageFile.setDisable(true);
                //}

                allFileFolder.addAlbumFile(imageFile);
                AlbumFolder albumFolder = albumFolderMap.get(bucketName);
                if (albumFolder != null) {
                    albumFolder.addAlbumFile(imageFile);
                }
                else {
                    albumFolder = new AlbumFolder();
                    albumFolder.setName(bucketName);
                    albumFolder.addAlbumFile(imageFile);
                    albumFolderMap.put(bucketName, albumFolder);
                }
            }
            cursor.close();
        }
    }

    @WorkerThread
    public ArrayList<AlbumFolder> getAllMedia() {
        Map<String, AlbumFolder> albumFolderMap = new HashMap<>();
        AlbumFolder allFileFolder = new AlbumFolder();
        allFileFolder.setName("All images");

        scanImageFile(albumFolderMap, allFileFolder);

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
