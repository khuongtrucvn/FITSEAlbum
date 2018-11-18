package com.example.a8560p.fitsealbum;

import android.app.Fragment;
import android.content.Intent;
import android.content.res.Resources;
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
    //View
    View pictures;
    //Mảng tĩnh chứa danh sách file
    public static ArrayList<String> images;
    //Trạng thái ẩn/hiện của toolbar, 0 là hiện, 1 là ẩn
    static int hideToolbar = 0;
    //Tạo instance
    public static PicturesActivity newInstance() { return new PicturesActivity(); }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        //Lấy view theo id của activity_pictures
        pictures = (View) inflater.inflate(R.layout.activity_pictures, container,false);
        //Tạo gridview để hiển thị ảnh
        GridView gallery = (GridView) pictures.findViewById(R.id.galleryGridView);
        //Dùng hàm setAdapter
        gallery.setAdapter(new ImageAdapter(this.getActivity()));
        //Xác định số dòng, cột của gridview dựa vào màn hình
        //Màn hình portrait thì 4 cột, màn hình landscape thì 6 cột
        int screenWidth =  Resources.getSystem().getDisplayMetrics().widthPixels;
        int screenHeight =  Resources.getSystem().getDisplayMetrics().heightPixels;
        if (screenWidth > screenHeight) {
            gallery.setNumColumns(6);
        }
        //Gán sự kiện click cho mỗi ảnh => xem ảnh full màn hình
        gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                if (null != images && !images.isEmpty()) {
                    //Tạo intent gửi đến FullImageActivity
                    Intent i = new Intent(PicturesActivity.super.getActivity(), FullImageActivity.class);
                    //Gửi vị trí ảnh hiện tại, tên ảnh và cả mảng file
                    i.putExtra("id", position);
                    i.putExtra("path", images.get(position));
                    i.putExtra("allPath", images);
                    startActivity(i);
                }
            }
        });
        return pictures;
    }
}
