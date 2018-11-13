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
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AlbumActivity extends Fragment {
    View album;
    public static ArrayList<AlbumFolder> folderAlbum;
    public AlbumActivity(){
    }

    public static AlbumActivity newInstance() {
        return new AlbumActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        album = (View) inflater.inflate(R.layout.activity_album, container, false);
        GridView gallery = (GridView) album.findViewById(R.id.albumFolderGridView);
        //gallery.setAdapter(new AlbumImageAdapter(this.getActivity()));
        gallery.setAdapter(new AlbumImageAdapter(AlbumActivity.super.getActivity()));
        gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                if (null != AlbumActivity.folderAlbum && !AlbumActivity.folderAlbum.isEmpty()) {

                }
            }
        });

        return album;
    }
}