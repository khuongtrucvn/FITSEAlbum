package com.example.a8560p.fitsealbum;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.WallpaperManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.media.ExifInterface;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
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

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

public class FullImageActivity extends AppCompatActivity
{
    MyPrefs myPrefs;
    Toolbar toolBar;
    ImageView imageView;
    BottomNavigationView mainNav;
    int position;
    private float x1,x2;
    static final int MIN_DISTANCE = 150;
    View decorView;
    AlertDialog dialog;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        myPrefs = new MyPrefs(this);
        super.onCreate(savedInstanceState);

        //Màn hình fullscreen
        decorView = getWindow().getDecorView();
        requestWindowFeature( Window.FEATURE_NO_TITLE );
        getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN );
        setContentView(R.layout.activity_full_image);

        // setup ActionBar
        toolBar = (Toolbar) findViewById(R.id.nav_actionBar);
        setSupportActionBar(toolBar);

        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        mainNav=(BottomNavigationView)findViewById(R.id.nav_bottom);

        if (PicturesActivity.hideToolbar == 0) {
            mainNav.setVisibility(View.VISIBLE);
            //decorView.setSystemUiVisibility(View.SYSTEM_UI_LAYOUT_FLAGS);
            getSupportActionBar().show();
        }
        else {
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            mainNav.setVisibility(View.GONE);
            getSupportActionBar().hide();
        }

        Intent i = getIntent();
        imageView = (ImageView) findViewById(R.id.imageView);

        position = i.getExtras().getInt("id");
        Glide.with(getApplicationContext()).load(PicturesActivity.images.get(position))
                .apply(new RequestOptions()
                        .placeholder(R.mipmap.ic_launcher).fitCenter())
                .into(imageView);
imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction())
                {
                    case MotionEvent.ACTION_DOWN: {
                        x1 = event.getX();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        x2 = event.getX();
                        float deltaX = x2 - x1;
                        if (Math.abs(deltaX) > MIN_DISTANCE) {
                            // Left to Right swipe action
                            if (x2 > x1) {
                                if (position>0) {
                                    finish();
                                    Intent i = new Intent(getApplicationContext(), FullImageActivity.class);
                                    i.putExtra("id", position - 1);
                                    i.putExtra("path", PicturesActivity.images.get(position-1));
                                    i.putExtra("allPath", PicturesActivity.images);
                                    startActivity(i);
                                }
                            }
                            // Right to left swipe action
                            else {
                                if (position<PicturesActivity.images.size()-1) {
                                    finish();
                                    Intent i = new Intent(getApplicationContext(), FullImageActivity.class);
                                    i.putExtra("id", position + 1);
                                    i.putExtra("path", PicturesActivity.images.get(position + 1));
                                    i.putExtra("allPath", PicturesActivity.images);
                                    startActivity(i);
                                }
                            }
                        } else {
                            // consider as something else - a screen tap for example
                            PicturesActivity.hideToolbar = (PicturesActivity.hideToolbar + 1) % 2;
                            if (PicturesActivity.hideToolbar == 1) {
                                getSupportActionBar().hide();
                                decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                        | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
                                mainNav.setVisibility(View.GONE);
                            }
                            else {
                                getSupportActionBar().show();
                                //decorView.setSystemUiVisibility(View.SYSTEM_UI_LAYOUT_FLAGS);
                                mainNav.setVisibility(View.VISIBLE);
                            }
                        }
                        break;
                    }
                }

                if (PicturesActivity.hideToolbar == 0) {
                    //decorView.setSystemUiVisibility(View.SYSTEM_UI_LAYOUT_FLAGS);
                    mainNav.setVisibility(View.VISIBLE);
                    getSupportActionBar().show();
                }
                else {
                    decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
                    mainNav.setVisibility(View.GONE);
                    getSupportActionBar().hide();
                }

                return false;
            }
        });

        //Navigation bottom onClickListener
        mainNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();

                if (id == R.id.nav_edit) {
                    Toast.makeText(getApplicationContext(),"Edit Image",Toast.LENGTH_SHORT).show();
                    return true;
                }
                if (id == R.id.nav_crop) {
                    Toast.makeText(getApplicationContext(),"Crop Image",Toast.LENGTH_SHORT).show();
                    return true;
                }
                if (id == R.id.nav_share) {
                    startActivity( Intent.createChooser( emailIntent(), "Send image Using...") );
                    return true;
                }
                if (id == R.id.nav_delete) {
                    Toast.makeText(getApplicationContext(),"Delete",Toast.LENGTH_SHORT).show();
                    return true;
                }

                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; add items to the action bar
        getMenuInflater().inflate(R.menu.image_main, menu);
        return true;
    }

    // return a SHARED intent to deliver an email
    private Intent emailIntent() {

        Intent i = getIntent(); // Lấy intent
        String returnUri = i.getExtras().getString("path"); // Lấy đường dẫn trong intent
        final Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/jpg");
        final File photoFile = new File(returnUri);

        shareIntent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(FullImageActivity.this,"hcmus.mdsd.fitsealbum", photoFile));

        return shareIntent;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // user clicked a menu-item from ActionBar
        int id = item.getItemId();

        if (id == R.id.action_favorite) {
            // perform FAVORITE operations...
            return true;
        }
        else if (id == R.id.action_slideshow) {
            // perform SLIDESHOW operations...
            return true;
        }
        else if (id == R.id.action_setBackground) {
            // perform SETBACKGROUND operations...
            WallpaperManager myWallpaperManager = WallpaperManager.getInstance(getApplicationContext());
            try {
                myWallpaperManager.setBitmap(((BitmapDrawable)imageView.getDrawable()).getBitmap(), null, false,
                        WallpaperManager.FLAG_SYSTEM | WallpaperManager.FLAG_LOCK);
                Toast.makeText(getApplicationContext(), "Image Successfully Set.", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return true;
        }
        else if (id == R.id.action_print) {
            // perform PRINT operations...
            return true;
        }
        else if (id == R.id.action_details) {
            // perform INFORMATION operations...
            Intent i = getIntent(); // Lấy intent
            String returnUri = i.getExtras().getString("path"); // Lấy đường dẫn trong intent
            SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy HH:mm"); // Tạo format date để lưu Date
            File file = new File(returnUri);
            final DecimalFormat format = new DecimalFormat("#.##"); // Tạo format cho size
            final double length = file.length();    // Lấy độ dài file
            String sLength;

            if (length > 1024 * 1024) {
                sLength = format.format(length / (1024 * 1024)) + " MB";
            }
            else {
                if (length > 1024) {
                    sLength = format.format(length / 1024) + " KB";
                }
                else {
                    sLength = format.format(length) + " B";
                }
            }
            try {
                ExifInterface exif = new ExifInterface(returnUri);
                String Details = ShowExif(exif); // Lấy thông tin của ảnh
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

                if(myPrefs.loadNightModeState() == true){
                    title.setTextColor(Color.WHITE);
                    dialog = new AlertDialog.Builder(FullImageActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_DARK).create();
                }
                else{
                    title.setTextColor(Color.BLACK);
                    dialog = new AlertDialog.Builder(FullImageActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT).create();
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
        }
        else if (id == R.id.action_delete) {
            // perform DELETE operations...
            return true;
        }
        else if(id == android.R.id.home){
            finish();
            return true;
        }
        return false;
    }

    private String ShowExif(ExifInterface exif)
    {
        String myAttribute="";

        if(exif.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH, 0) == 0)
        {
            return myAttribute;
        }
        else
        {
            myAttribute += "\n\nResolution: " + exif.getAttribute(ExifInterface.TAG_IMAGE_WIDTH) +
                    "x" + exif.getAttribute(ExifInterface.TAG_IMAGE_LENGTH);
            if (exif.getAttribute(ExifInterface.TAG_MODEL) == null)
            {
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
        }
        else {
            myAttribute += "\n\nAperture: unknown\n\n";
        }

        // Lấy exposure time
        String ExposureTime = exif.getAttribute(ExifInterface.TAG_EXPOSURE_TIME);
        Double ExposureTime_double = Double.parseDouble(ExposureTime);
        Double Denominator = 1 / ExposureTime_double;
        ExposureTime = 1 + "/" + String.format("%.0f", Denominator);
        myAttribute += "Exposure Time: " + ExposureTime + "s\n\n";

        if (exif.getAttributeInt(ExifInterface.TAG_FLASH, 0) == 0){
            myAttribute += "Flash: Off\n\n";
        }
        else {
            myAttribute += "Flash: On\n\n";
        }

        myAttribute += "Focal Length: " + exif.getAttributeDouble(ExifInterface.TAG_FOCAL_LENGTH, 0) + "mm\n\n";
        myAttribute += "ISO Value: " + exif.getAttribute(ExifInterface.TAG_ISO_SPEED_RATINGS) + "\n\n";
        myAttribute += "Model: " + exif.getAttribute(ExifInterface.TAG_MODEL);

        return myAttribute;
    }

    private String getTagString(String tag, ExifInterface exif)
    {
        return(tag + " : " + exif.getAttribute(tag) + "\n");
    }
}
