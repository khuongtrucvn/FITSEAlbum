package com.example.a8560p.fitsealbum;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

public class MainActivity extends AppCompatActivity {
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // setup ActionBar
        actionBar = getSupportActionBar();

        actionBar.setTitle("  FIT SE Album");
        actionBar.setSubtitle("  Version 1.0");
        actionBar.setIcon(R.drawable.ic_action_logo);

        actionBar.setDisplayShowCustomEnabled(true);            // allow custom views to be shown
        actionBar.setDisplayShowHomeEnabled(true);              // allow app icon – logo to be shown
        actionBar.setHomeButtonEnabled(true);                   // needed for API14 or greater

        GridView gridView = (GridView) findViewById(R.id.gridView);
        gridView.setAdapter(new ImageAdapter(this));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getApplicationContext(), FullImageActivity.class);
                i.putExtra("id", position);
                startActivity(i);
            }
        });
    }

    @Override  public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; add items to the action bar
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    // user clicked a menu-item from ActionBar
        int id = item.getItemId();
        if (id == R.id.action_search) {
            // perform SEARCH operations...
            return true;
        }
        else if (id == R.id.action_about) {
            // perform ABOUT operations...
            return true;
        }
        else if (id == R.id.action_settings) {
            // perform SETTING operations...
            return true;
        }

        return false;
    }
}
