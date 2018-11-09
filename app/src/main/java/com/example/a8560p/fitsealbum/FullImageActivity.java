package com.example.a8560p.fitsealbum;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import static android.view.View.VISIBLE;

public class FullImageActivity extends AppCompatActivity {

    ActionBar actionBar;
    ImageView imageView;
    BottomNavigationView mainNav;
    int position;
    private float x1,x2;
    static final int MIN_DISTANCE = 150;
    View decorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Màn hình fullscreen
        decorView = getWindow().getDecorView();
        requestWindowFeature( Window.FEATURE_NO_TITLE );
        getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN );
        setContentView(R.layout.activity_full_image);

        // setup ActionBar
        actionBar = getSupportActionBar();

        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        mainNav=(BottomNavigationView)findViewById(R.id.nav_bottom);

        if (PicturesActivity.hideToolbar == 0) {
            actionBar.show();
            mainNav.setVisibility(View.VISIBLE);
            decorView.setSystemUiVisibility(View.SYSTEM_UI_LAYOUT_FLAGS);
        } else {
            actionBar.hide();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            mainNav.setVisibility(View.GONE);
        }

        Intent i = getIntent();
        imageView = (ImageView) findViewById(R.id.imageView);

        position = i.getExtras().getInt("id");
        Glide.with(getApplicationContext()).load(PicturesActivity.images.get(position))
                .apply(new RequestOptions()
                        .placeholder(R.mipmap.ic_launcher).fitCenter())
                .into(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //                hideToolbar = (hideToolbar + 1) % 2;
//                if (hideToolbar==0) {
//                    actionBar.show();
//                }
//                else {
//                    actionBar.hide();
//                }
            }
        });

        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction())
                {
                    case MotionEvent.ACTION_DOWN: {
                        x1 = event.getX();
                        break;
                    }
                    case MotionEvent.ACTION_SCROLL: {
                        Toast.makeText(getApplicationContext(), "action scroll", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        x2 = event.getX();
                        float deltaX = x2 - x1;
                        if (Math.abs(deltaX) > MIN_DISTANCE) {
                            // Left to Right swipe action
                            if (x2 > x1) {
                                if (position>0) {
                                    //Toast.makeText(getApplicationContext(), "Left to Right swipe [Next]", Toast.LENGTH_SHORT).show();
                                    finish();
                                    Intent i = new Intent(getApplicationContext(), FullImageActivity.class);
                                    i.putExtra("id", position - 1);
                                    startActivity(i);
                                }
                            }
                            // Right to left swipe action
                            else {
                                //Toast.makeText(getApplicationContext(), "Right to Left swipe [Previous]", Toast.LENGTH_SHORT).show();
                                if (position<PicturesActivity.images.size()-1) {
                                    finish();
                                    Intent i = new Intent(getApplicationContext(), FullImageActivity.class);
                                    i.putExtra("id", position + 1);
                                    startActivity(i);
                                }
                            }
                        } else {
                            // consider as something else - a screen tap for example
                            PicturesActivity.hideToolbar = (PicturesActivity.hideToolbar + 1) % 2;
                            if (PicturesActivity.hideToolbar == 1) {
                                decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                        | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
                                mainNav.setVisibility(View.GONE);
                            }
                            else {
                                decorView.setSystemUiVisibility(View.SYSTEM_UI_LAYOUT_FLAGS);
                                mainNav.setVisibility(View.VISIBLE);
                            }
                        }
                        break;
                    }
                }
                if (PicturesActivity.hideToolbar == 0) {
                    actionBar.show();
                    decorView.setSystemUiVisibility(View.SYSTEM_UI_LAYOUT_FLAGS);
                    mainNav.setVisibility(View.VISIBLE);
                }
                else {
                    actionBar.hide();
                    decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
                    mainNav.setVisibility(View.GONE);
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
                    startActivity( Intent.createChooser( emailIntent(), "Send EMAIL Using...") );
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

    @Override  public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; add items to the action bar
        getMenuInflater().inflate(R.menu.image_main, menu);
        return true;
    }

    // return a SHARED intent to deliver an email
    private Intent emailIntent() {

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] { "v.matos@csuohio.edu" });
        intent.putExtra(Intent.EXTRA_SUBJECT, "subject here...");
        intent.putExtra(Intent.EXTRA_TEXT, "this is the email-text to be sent...");

        return intent;

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
            return true;
        }
        else if (id == R.id.action_print) {
            // perform PRINT operations...
            return true;
        }
        else if (id == R.id.action_information) {
            // perform INFORMATION operations...
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
}
