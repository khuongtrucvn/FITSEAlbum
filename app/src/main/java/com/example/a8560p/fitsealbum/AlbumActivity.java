package com.example.a8560p.fitsealbum;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.WorkerThread;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
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

        ArrayList<String> folderName = getAllMedia();
        for(int i=0;i< folderName.size();i++)
        {
            Toast.makeText(AlbumActivity.super.getActivity(),folderName.get(i), Toast.LENGTH_SHORT).show();
        }

        return album;
    }

    @WorkerThread
    public ArrayList<String> getAllMedia() {
        Map<String, String> albumFolderMap = new HashMap<>();
        String allFileFolder = "All images";
        ArrayList<String> albumFolders = new ArrayList<>();
        albumFolders.add(allFileFolder);

        for (Map.Entry<String, String> folderEntry : albumFolderMap.entrySet()) {
            String albumFolder = folderEntry.getValue();
            albumFolders.add(albumFolder);
        }

        return albumFolders;
    }
}
