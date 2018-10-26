package com.example.a8560p.fitsealbum;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FavoriteActivity extends Fragment{
    View favorite;

    public static FavoriteActivity newInstance() {
        FavoriteActivity fragment = new FavoriteActivity();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        favorite =(View) inflater.inflate(R.layout.activity_favorite,container, false);

        return favorite;
    }
}

