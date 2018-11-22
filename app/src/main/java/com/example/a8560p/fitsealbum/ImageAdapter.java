package com.example.a8560p.fitsealbum;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class ImageAdapter extends BaseAdapter {

    //Activity
    private Activity context;

    /**
     * Constructor 1 tham số
     * @param localContext activity
     */
    ImageAdapter(Activity localContext) {
        context = localContext;
        PicturesActivity.images = getAllShownImagesPath(context);
    }
    /**
     * Constructor 2 tham số
     * @param localContext activity
     * @param arrayList danh sách file
     */
    ImageAdapter(Activity localContext, ArrayList<String> arrayList){
        context = localContext;
        PicturesActivity.images = arrayList;
    }
    //Hàm lấy số lượng ảnh
    public int getCount() { return PicturesActivity.images.size(); }
    public Object getItem(int position) { return position; }
    public long getItemId(int position) { return position; }

    public View getView(final int position, View convertView, ViewGroup parent) {
        //ImageView
        ImageView picturesView;
        if (convertView == null) {
            //Đoạn code kiểm tra tình trạng màn hình để hiển thị số cột ảnh

            //Nếu màn hình portrait thì hiển thị 4 cột (mặc định trong layout xml), nếu màn hình landscape hiển thị 6 cột
            GridView gridView = context.findViewById(R.id.galleryGridView);
            int column = gridView.getNumColumns();
            int screenWidth =  Resources.getSystem().getDisplayMetrics().widthPixels;

            //Kích thước ảnh hiển thị
            int sizeOfImage = (screenWidth - (column + 1) * 8) / column;
            //Khởi tạo picturesView
            picturesView = new ImageView(context);
            //Đặt thuộc tính hiển thị FIT_CENTER
            picturesView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            //Đặt tham số param
            picturesView.setLayoutParams(new GridView.LayoutParams(sizeOfImage, sizeOfImage));
        } else {
            picturesView = (ImageView) convertView;
        }
        //Dùng Glide để load ảnh lên gridview
        Glide.with(context).load(PicturesActivity.images.get(position))
                .apply(new RequestOptions().placeholder(null).centerCrop())
                .into(picturesView);
        return picturesView;
    }

    /**
     * Hàm load tất cả các file ảnh trong Media
     * @param activity activity
     * @return mảng file
     */
    private ArrayList<String> getAllShownImagesPath(Activity activity) {
        //Khởi tạo mảng
        ArrayList<String> listOfAllImages = new ArrayList<>();
        //Khởi tạo con trỏ
        //Đọc các file trong Media, lấy dữ liệu về DATA, sắp xếp theo ngày chỉnh sửa giảm dần
        Cursor cursor = activity.getContentResolver().query(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.MediaColumns.DATA}, null, null, "DATE_MODIFIED DESC");
        //Nếu con trỏ không rỗng
        if (cursor!=null) {
            //Duyệt qua từng file
            while (cursor.moveToNext()) {
                //Thêm file vào mảng
                listOfAllImages.add(cursor.getString(0));
            }
            cursor.close();
        }
        return listOfAllImages;
    }
}
