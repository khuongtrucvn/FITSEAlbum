package com.example.a8560p.fitsealbum;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;

public class PicturesActivity extends Fragment{
    View pictures;
    public static ArrayList<String> images;

    public static PicturesActivity newInstance() {
        PicturesActivity fragment = new PicturesActivity();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        pictures = (View) inflater.inflate(R.layout.activity_pictures, container,false);

        GridView gallery = (GridView) pictures.findViewById(R.id.galleryGridView);
        gallery.setAdapter(new ImageAdapter(PicturesActivity.super.getActivity()));
        gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                if (null != images && !images.isEmpty())
                    Toast.makeText(
                            PicturesActivity.super.getActivity(),
                            "position " + position + " " + images.get(position), Toast.LENGTH_SHORT).show();
            }
        });

        return pictures;
    }
}
