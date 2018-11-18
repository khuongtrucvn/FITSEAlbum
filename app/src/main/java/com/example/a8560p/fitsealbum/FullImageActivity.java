package com.example.a8560p.fitsealbum;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.WallpaperManager;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.media.ExifInterface;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Objects;

public class FullImageActivity extends AppCompatActivity {
    //Toolbar
    Toolbar toolBar;
    //ImageView
    ImageView imageView;
    //TextView hiển thị ngày chỉnh sửa cuối
    TextView txtDateModified;
    //Vị trí hiện tại của ảnh trong danh sách file
    int position;
    //Bottom Navigation View, 4 nút Edit/Crop/Share/Delete
    BottomNavigationView mainNav;
    //Tọa độ trước và sau khi chạm màn hình
    private float x1, x2, y1, y2;
    //Khoảng cách tối thiểu được xem như là cử chỉ vuốt
    static final int MIN_DISTANCE = 150;
    //View
    View decorView;
    //MyPrefs
    MyPrefs myPrefs;
    //Thuộc tính ảnh được yêu thích hay không
    static boolean favoritedImage = false;
    //Firebase
    /*public static FirebaseDatabase database = FirebaseDatabase.getInstance();
    public static DatabaseReference mData ;
    public static FirebaseStorage storage = FirebaseStorage.getInstance();
    public static StorageReference storageRef;*/

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Khởi tạo myprefs
        myPrefs = new MyPrefs(this);
        //Màn hình fullscreen
        decorView = getWindow().getDecorView();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //Set layout chính
        setContentView(R.layout.activity_full_image);
        //Set ActionBar
        toolBar = findViewById(R.id.nav_actionBar);
        setSupportActionBar(toolBar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //Đưa màn hình vào chế dộ Immersive Sticky (ẩn toàn bộ thanh thông báo, thanh điều hướng chỉ hiện lên khi được vuốt lên)
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        //Gán mainNav bằng id của nav_bottom
        mainNav = findViewById(R.id.nav_bottom);
        //Gán txtDateModified bằng id của txtDateModified
        txtDateModified = findViewById(R.id.txtDateModified);
        if (PicturesActivity.hideToolbar == 0) {
            //decorView.setSystemUiVisibility(View.SYSTEM_UI_LAYOUT_FLAGS);
            mainNav.setVisibility(View.VISIBLE);
            txtDateModified.setVisibility(View.VISIBLE);
            getSupportActionBar().show();
        } else {
            getSupportActionBar().hide();
            mainNav.setVisibility(View.GONE);
            txtDateModified.setVisibility(View.GONE);
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }

        Intent inte = getIntent();
        imageView = findViewById(R.id.imageView);

        //Navigation bottom onClickListener
        mainNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_edit: {
                        Intent editIntent = new Intent(Intent.ACTION_EDIT);
                        editIntent.setDataAndType(FileProvider.getUriForFile(getApplicationContext(),"hcmus.mdsd.fitsealbum" , new File(PicturesActivity.images.get(position))), "image/*");
                        editIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        startActivity(Intent.createChooser(editIntent, null));
                        return true;
                    }
                    case R.id.nav_crop: {
                        openCrop();
                        return true;
                    }
                    case R.id.nav_share: {
                        startActivity(Intent.createChooser(emailIntent(), "Share image using"));
                        return true;
                    }
                    case R.id.nav_delete: {
                        Intent i = getIntent(); // Lấy intent
                        final String returnUri = Objects.requireNonNull(i.getExtras()).getString("path"); // Lấy đường dẫn trong intent

                        assert returnUri != null;
                        final File photoFile = new File(returnUri);

                        // Tạo biến builder để tạo dialog để xác nhận có xoá file hay không
                        AlertDialog builder;

                        if (myPrefs.loadNightModeState()) {
                            builder = new AlertDialog.Builder(FullImageActivity.this, android.R.style.Theme_DeviceDefault_Dialog_Alert).create();
                        } else {
                            builder = new AlertDialog.Builder(FullImageActivity.this, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert).create();
                        }

                        builder.setMessage("Are you sure you want to delete this item ?");
                        builder.setButton(Dialog.BUTTON_POSITIVE, "YES", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //---- Dưới đây là bài hướng dẫn xoá ảnh sử dụng ContentResolver trên diễn đàn stackoverflow ----
                                // Nguồn: http://stackoverflow.com/a/20780472#1#L0

                                // Khởi tạo ID
                                String[] projection = {MediaStore.Images.Media._ID};

                                // Lấy thông tin đường dẫn
                                String selection = MediaStore.Images.Media.DATA + " = ?";
                                String[] selectionArgs = new String[]{photoFile.getAbsolutePath()};

                                //
                                Uri queryUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                                ContentResolver contentResolver = getContentResolver();
                                Cursor c = contentResolver.query(queryUri, projection, selection, selectionArgs, null);
                                if (c!=null){
                                    if (c.moveToFirst()) {
                                        // Tìm thấy ID. Xoá ảnh dựa nhờ content provider
                                        long id = c.getLong(c.getColumnIndexOrThrow(MediaStore.Images.Media._ID));
                                        Uri deleteUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);

                                        contentResolver.delete(deleteUri, null, null);
                                    }
//                                else {
//                                    // File không có database
//                                }
                                    c.close();
                                }
                                Toast.makeText(FullImageActivity.this, "Item has been deleted", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                for (int i = position; i < PicturesActivity.images.size() - 1; i++)
                                {
                                    PicturesActivity.images.set(i, PicturesActivity.images.get(i + 1));
                                }
                                PicturesActivity.images.remove(PicturesActivity.images.size() - 1);
                                // Nếu ảnh được yêu thích thì khi xoá ảnh phải xoá trong danh sách các ảnh được yêu thích luôn
                                if (favoritedImage)
                                {
                                    FavoriteActivity.favoriteImages.remove(returnUri);
                                    SharedPreferences sharedPreferences = PreferenceManager
                                            .getDefaultSharedPreferences(getApplicationContext());
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    Gson gson = new Gson();
                                    String json = gson.toJson(FavoriteActivity.favoriteImages);
                                    editor.putString("savedFavoriteImages", json);
                                    //editor.commit();
                                    editor.apply();
                                }
                                int currentNumberOfPictures = PicturesActivity.images.size();
                                if (currentNumberOfPictures == 0) {
                                    finish();
                                } else if (position == currentNumberOfPictures){
                                    finish();
                                    Intent i = new Intent(getApplicationContext(), FullImageActivity.class);
                                    i.putExtra("id", position - 1);
                                    i.putExtra("path", PicturesActivity.images.get(position - 1));
                                    i.putExtra("allPath", PicturesActivity.images);
                                    startActivity(i);
                                }
                                else {
                                    finish();
                                    Intent i = new Intent(getApplicationContext(), FullImageActivity.class);
                                    i.putExtra("id", position);
                                    i.putExtra("path", PicturesActivity.images.get(position));
                                    i.putExtra("allPath", PicturesActivity.images);
                                    startActivity(i);
                                }
                            }
                        });
                        builder.setButton(Dialog.BUTTON_NEGATIVE, "NO", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                        builder.show();
                        return true;
                    }
                }
                return false;
            }
        });

        position = Objects.requireNonNull(inte.getExtras()).getInt("id");
        Glide.with(getApplicationContext()).load(PicturesActivity.images.get(position))
                .apply(new RequestOptions()
                        //.placeholder(R.mipmap.ic_launcher).fitCenter())
                        .placeholder(null).fitCenter())
                .into(imageView);

        String returnUri = inte.getExtras().getString("path"); // Lấy đường dẫn trong intent
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy HH:mm"); // Tạo format date để lưu Date
        assert returnUri != null;
        File file = new File(returnUri);
        txtDateModified.setText(sdf.format(file.lastModified()));

        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        x1 = event.getX();
                        y1 = event.getY();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        x2 = event.getX();
                        y2 = event.getY();
                        float deltaX = x2 - x1;
                        float deltaY = y2 - y1;
                        if (Math.abs(deltaX) >= MIN_DISTANCE && Math.abs(deltaY) <= MIN_DISTANCE/2) {
                            // Left to Right swipe action
                            if (x2 > x1) {
                                if (position > 0) {
                                    finish();
                                    Intent i = new Intent(getApplicationContext(), FullImageActivity.class);
                                    i.putExtra("id", position - 1);
                                    i.putExtra("path", PicturesActivity.images.get(position - 1));
                                    i.putExtra("allPath", PicturesActivity.images);
                                    startActivity(i);
                                }
                            }
                            // Right to left swipe action
                            else if (x2 < x1) {
                                if (position < PicturesActivity.images.size() - 1) {
                                    finish();
                                    Intent i = new Intent(getApplicationContext(), FullImageActivity.class);
                                    i.putExtra("id", position + 1);
                                    i.putExtra("path", PicturesActivity.images.get(position + 1));
                                    i.putExtra("allPath", PicturesActivity.images);
                                    startActivity(i);
                                }
                            }
                        } else if(Math.abs(deltaY) >= MIN_DISTANCE && Math.abs(deltaX) <= MIN_DISTANCE/2 && y2 < y1){
                            finish();
                        }
                        else {
                            // consider as something else - a screen tap for example
                            PicturesActivity.hideToolbar = (PicturesActivity.hideToolbar + 1) % 2;
                            if (PicturesActivity.hideToolbar == 1) {
                                getSupportActionBar().hide();
                                mainNav.setVisibility(View.GONE);
                                txtDateModified.setVisibility(View.GONE);
                                decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                        | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
                            } else {
                                //decorView.setSystemUiVisibility(View.SYSTEM_UI_LAYOUT_FLAGS);
                                mainNav.setVisibility(View.VISIBLE);
                                txtDateModified.setVisibility(View.VISIBLE);
                                getSupportActionBar().show();
                            }
                        }
                        break;
                    }
                }
                if (PicturesActivity.hideToolbar == 0) {
                    //decorView.setSystemUiVisibility(View.SYSTEM_UI_LAYOUT_FLAGS);
                    mainNav.setVisibility(View.VISIBLE);
                    txtDateModified.setVisibility(View.VISIBLE);
                    getSupportActionBar().show();
                } else {
                    getSupportActionBar().hide();
                    mainNav.setVisibility(View.GONE);
                    txtDateModified.setVisibility(View.GONE);
                    decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
                }
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; add items to the action bar
        getMenuInflater().inflate(R.menu.image_main, menu);

        favoritedImage = false;
        Intent i = getIntent();
        String abc = Objects.requireNonNull(i.getExtras()).getString("path"); // Lấy đường dẫn trong intent
        // Nếu tồn tại đường dẫn của ảnh trong favoriteImages
        if (null != FavoriteActivity.favoriteImages && !FavoriteActivity.favoriteImages.isEmpty()) {
            // Nếu ảnh đang chiếu có trong số ảnh được yêu thích thì chuyển tim sang màu đỏ
            if (FavoriteActivity.favoriteImages.contains(abc))
            {
                MenuItem menuItem = menu.findItem(R.id.action_favorite);
                menuItem.setIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.round_favorite_24_clicked)); // đổi màu đỏ
                favoritedImage = true; // Đánh dấu ảnh đang chiếu đã được yêu thích
            }
        }

        return true;
    }


    // return a SHARED intent to deliver an email
    private Intent emailIntent() {

        Intent i = getIntent(); // Lấy intent
        String returnUri = Objects.requireNonNull(i.getExtras()).getString("path"); // Lấy đường dẫn trong intent

        final Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/jpg");
        assert returnUri != null;
        final File photoFile = new File(returnUri);

        shareIntent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(FullImageActivity.this, "hcmus.mdsd.fitsealbum", photoFile));
        //startActivity(Intent.createChooser(shareIntent, "Share image using"));

        return shareIntent;

    }

    @SuppressLint("SetTextI18n")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // user clicked a menu-item from ActionBar

        int id = item.getItemId();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        if (id == R.id.action_favorite) {
            if (favoritedImage)
            {
                MenuView.ItemView favorite_button;
                favorite_button = findViewById(R.id.action_favorite);
                favorite_button.setIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.round_favorite_24));
                FavoriteActivity.favoriteImages.remove(PicturesActivity.images.get(position));
                favoritedImage = false;
            }
            else // Nếu ảnh chưa được yêu thích thì khi bấm vào nút Favorite, đổi tim thành màu đỏ và thêm ảnh vào danh sách ảnh được yêu thích
            {
                MenuView.ItemView favorite_button;
                favorite_button = findViewById(R.id.action_favorite);
                favorite_button.setIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.round_favorite_24_clicked));
                if (null != FavoriteActivity.favoriteImages && !FavoriteActivity.favoriteImages.isEmpty()) {
                    FavoriteActivity.favoriteImages.add(PicturesActivity.images.get(position));
                }
                else
                {
                    FavoriteActivity.favoriteImages = new ArrayList<>();
                    FavoriteActivity.favoriteImages.add(PicturesActivity.images.get(position));
                }
                favoritedImage = true;
            }
            // Cập nhật lại ảnh để lưu vào SharedPreferences
            // (Lưu vào SharedPreferences để có thể lấy được thông tin của những ảnh đã được yêu thích khi thoát ứng dụng và bật lại)
            // Nguồn: https://stackoverflow.com/questions/14981233/android-arraylist-of-custom-objects-save-to-sharedpreferences-serializable/40237149#40237149
            SharedPreferences sharedPreferences = PreferenceManager
                    .getDefaultSharedPreferences(this.getApplicationContext());
            SharedPreferences.Editor editor = sharedPreferences.edit();
            Gson gson = new Gson();
            String json = gson.toJson(FavoriteActivity.favoriteImages);
            editor.putString("savedFavoriteImages", json);
            //editor.commit();
            editor.apply();
            return true;
        } else if (id == R.id.action_upload) {
            /*ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            assert conMgr != null;
            NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();
            if(MainActivity._name_cloud.equals("")){
                Toast.makeText(FullImageActivity.this, "Login to Cloud Storage to upload images", Toast.LENGTH_SHORT).show();
            }
            else{
                if (activeNetwork != null && activeNetwork.isConnected()) {

                    storageRef = storage.getReference(MainActivity._name_cloud);
                    mData = database.getReference(MainActivity._name_cloud);

                    Intent i = getIntent();
                    String filePath = Objects.requireNonNull(i.getExtras()).getString("path");
                    assert filePath != null;
                    final String imgName = new File(filePath).getName();

                    StorageReference mountainsRef = storageRef.child(imgName);

                    imageView.setDrawingCacheEnabled(true);
                    imageView.buildDrawingCache();
                    Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                    byte[] data = baos.toByteArray();
                    UploadTask uploadTask = mountainsRef.putBytes(data);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toast.makeText(FullImageActivity.this, "Lưu ảnh không thành công", Toast.LENGTH_SHORT).show();

                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(FullImageActivity.this, "Lưu ảnh thành công", Toast.LENGTH_SHORT).show();

                            Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!urlTask.isSuccessful());
                            Uri downloadUrl = urlTask.getResult();
                            CloudImage con = new CloudImage(imgName, downloadUrl.toString());
                            mData.push().setValue(con, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                    if(databaseError==null){
                                        Toast.makeText(FullImageActivity.this, "Lưu dữ liệu thành công", Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        Toast.makeText(FullImageActivity.this, "Lưu dữ liệu không thành công", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });
                        }
                    });
                } else {
                    Toast.makeText(FullImageActivity.this, "Kiểm tra kết nối internet của bạn", Toast.LENGTH_SHORT).show();

                }
            }*/
            return true;
        } else if (id == R.id.action_slideshow) {
            // perform SLIDESHOW operations...
            Intent newIntentForSlideShowActivity = new Intent(FullImageActivity.this, SlideShowAcitivity.class);
            newIntentForSlideShowActivity.putExtra("id", position); // Lấy position id và truyền cho SlideShowActivity
            startActivity(newIntentForSlideShowActivity);
            return true;
        } else if (id == R.id.action_rotate) {
            imageView.setRotation(imageView.getRotation() + 90);
        } else if (id == R.id.action_setBackground) {
            // perform SETBACKGROUND operations...
            WallpaperManager myWallpaperManager = WallpaperManager.getInstance(getApplicationContext());
            try {
                myWallpaperManager.setBitmap(((BitmapDrawable) imageView.getDrawable()).getBitmap(), null, false,
                        WallpaperManager.FLAG_SYSTEM | WallpaperManager.FLAG_LOCK);
                Toast.makeText(getApplicationContext(), "Image Successfully Set.", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return true;
        } else if (id == R.id.action_print) {
            // perform PRINT operations...
            return true;
        } else if (id == R.id.action_details) {
            // perform INFORMATION operations...
            Intent i = getIntent(); // Lấy intent
            String returnUri = Objects.requireNonNull(i.getExtras()).getString("path"); // Lấy đường dẫn trong intent
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy HH:mm"); // Tạo format date để lưu Date
            assert returnUri != null;
            File file = new File(returnUri);

            final DecimalFormat format = new DecimalFormat("#.##"); // Tạo format cho size
            final double length = file.length();    // Lấy độ dài file
            String sLength;

            if (length > 1024 * 1024) {
                sLength = format.format(length / (1024 * 1024)) + " MB";
            } else {
                if (length > 1024) {
                    sLength = format.format(length / 1024) + " KB";
                } else {
                    sLength = format.format(length) + " B";
                }
            }

            try {
                ExifInterface exif = new ExifInterface(returnUri);
                String Details = ShowExif(exif);    // Lấy thông tin của ảnh

                Details = "Date: " + sdf.format(file.lastModified()) +
                        "\n\nSize: " + sLength +
                        "\n\nFile path: " + returnUri +
                        Details;

                // -----  Tạo dialog để xuất ra detail -----


                TextView title = new TextView(getApplicationContext());
                title.setPadding(46, 40, 0, 0);
                title.setText("Details");
                title.setTextSize(23.0f);
                title.setTypeface(null, Typeface.BOLD);
                AlertDialog dialog;

                if (myPrefs.loadNightModeState()) {
                    title.setTextColor(Color.WHITE);
                    //dialog = new AlertDialog.Builder(FullImageActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_DARK).create();
                    dialog = new AlertDialog.Builder(FullImageActivity.this, android.R.style.Theme_DeviceDefault_Dialog_Alert).create();
                } else {
                    title.setTextColor(Color.BLACK);
                    //dialog = new AlertDialog.Builder(FullImageActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT).create();
                    dialog = new AlertDialog.Builder(FullImageActivity.this, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert).create();
                }

                dialog.setCustomTitle(title);
                dialog.setMessage(Details);
                dialog.setButton(AlertDialog.BUTTON_POSITIVE, "Close",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                dialog.show();

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return true;
        } else if (id == android.R.id.home) {
            finish();
            return true;
        }
        return false;
    }

    @SuppressLint("DefaultLocale")
    private String ShowExif(ExifInterface exif) {
        String myAttribute = "";

        if (exif.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH, 0) == 0) {
            return myAttribute;
        } else {
            myAttribute += "\n\nResolution: " + exif.getAttribute(ExifInterface.TAG_IMAGE_WIDTH) +
                    "x" + exif.getAttribute(ExifInterface.TAG_IMAGE_LENGTH);

            if (exif.getAttribute(ExifInterface.TAG_MODEL) == null) {
                return myAttribute;
            }
        }

        // Lấy aperture
        final DecimalFormat apertureFormat = new DecimalFormat("#.#"); // Tạo format cho aperture
        String aperture = exif.getAttribute(ExifInterface.TAG_F_NUMBER);
        if (aperture != null) {
            Double aperture_double = Double.parseDouble(aperture);
            apertureFormat.format(aperture_double);
            myAttribute += "\n\nAperture: f/" + aperture_double + "\n\n";
        } else {
            myAttribute += "\n\nAperture: unknown\n\n";
        }

        // Lấy exposure time
        String ExposureTime = exif.getAttribute(ExifInterface.TAG_EXPOSURE_TIME);
        Double ExposureTime_double = Double.parseDouble(ExposureTime);
        Double Denominator = 1 / ExposureTime_double;

        ExposureTime = 1 + "/" + String.format("%.0f", Denominator);

        myAttribute += "Exposure Time: " + ExposureTime + "s\n\n";

        if (exif.getAttributeInt(ExifInterface.TAG_FLASH, 0) == 0) {
            myAttribute += "Flash: Off\n\n";
        } else {
            myAttribute += "Flash: On\n\n";
        }

        myAttribute += "Focal Length: " + exif.getAttributeDouble(ExifInterface.TAG_FOCAL_LENGTH, 0) + "mm\n\n";
        myAttribute += "ISO Value: " + exif.getAttribute(ExifInterface.TAG_ISO_SPEED_RATINGS) + "\n\n";
        myAttribute += "Model: " + exif.getAttribute(ExifInterface.TAG_MODEL);

        return myAttribute;
    }

    String nameImage;

    ///crop
    public void openCrop() {
        Intent i = getIntent();
        String filePath = Objects.requireNonNull(i.getExtras()).getString("path");
        assert filePath != null;
        Uri photoURI = Uri.fromFile(new File(filePath));
        nameImage = new File(filePath).getName();
        if (photoURI != null) {
            CropImage.activity(photoURI)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                assert result != null;
                Uri resultUri = result.getUri();
                imageView.setImageURI(null);
                imageView.setImageURI(resultUri);
                imageView.setDrawingCacheEnabled(true);
                Bitmap b = imageView.getDrawingCache();
                MediaStore.Images.Media.insertImage(getContentResolver(), b, nameImage + "_crop", "");
//                File storageLoc = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES); //context.getExternalFilesDir(null);
//
//                //SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy HH:mm");
//
//
//
//
//                File file = new File(storageLoc, "tan" );
//                try{
//                    FileOutputStream fos = new FileOutputStream(file);
//                    b.compress(Bitmap.CompressFormat.JPEG, 100, fos);
//                    fos.close();
//                    scanFile(this, Uri.fromFile(file));
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }

                PicturesActivity.images.add(nameImage + "_crop");
                Glide.with(getApplicationContext()).load(PicturesActivity.images.get(0))
                        .apply(new RequestOptions()
                                //.placeholder(R.mipmap.ic_launcher).fitCenter())
                                .placeholder(null).fitCenter())
                        .into(imageView);

                // cái đó là gì v load cái ảnh mới cắt vào glide trong giao diện đó ok
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}