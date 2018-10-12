package com.example.a8560p.fitsealbum;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

public class FullImageActivity extends AppCompatActivity {
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);

        // setup ActionBar
        actionBar = getSupportActionBar();

        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent i = getIntent();

        int position = i.getExtras().getInt("id");
        ImageAdapter adapter = new ImageAdapter(this);
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setImageResource(adapter.images[position]);
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
        if (id == R.id.action_share) {
            startActivity( Intent.createChooser( emailIntent(), "Send EMAIL Using...") );
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
