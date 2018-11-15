package com.example.a8560p.fitsealbum;

import android.app.Fragment;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;

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
        final GridView gallery = (GridView) album.findViewById(R.id.albumFolderGridView);
        gallery.setAdapter(new AlbumImageAdapter(AlbumActivity.super.getActivity()));

        int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
        if (screenWidth > screenHeight)
            gallery.setNumColumns(2);
        else
            gallery.setNumColumns(1);
        gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                if (null != AlbumActivity.folderAlbum && !AlbumActivity.folderAlbum.isEmpty()) {
                    Toast.makeText(getContext(),AlbumActivity.folderAlbum.get(position).getName(), Toast.LENGTH_SHORT).show();

                }
            }
        });


        return album;
    }
}