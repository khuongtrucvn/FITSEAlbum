package com.example.a8560p.fitsealbum;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class PicturesActivity extends Fragment{
    View pictures;

    public static PicturesActivity newInstance() {
        PicturesActivity fragment = new PicturesActivity();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        pictures = (View) inflater.inflate(R.layout.activity_pictures, container,false);

        return pictures;
    }
}
