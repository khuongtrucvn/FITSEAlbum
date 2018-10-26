package com.example.a8560p.fitsealbum;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class LaunchScreenActivity extends AppCompatActivity{
    private final static String FILE_PASSWORD = "password.txt";
    String password="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launch_screen);
        new BackgroundTask().execute();
    }

    public void onStart(){
        super.onStart();


    }

    private class BackgroundTask extends AsyncTask {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] params) {

            /*  Use this method to load background
             * data that your app needs. */
            try {
                InputStream inputStream = openFileInput(FILE_PASSWORD);
                if (inputStream != null) {
                    BufferedReader reader = new BufferedReader(new
                            InputStreamReader(inputStream));
                    password = reader.readLine();
                    inputStream.close();
                }
            } catch (Exception ex) {}

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
//            Pass your loaded data here using Intent

//            intent.putExtra("data_key", "");
            if(password.matches("")){
                startActivity(new Intent(LaunchScreenActivity.this,MainActivity.class));
            }
            else{
                startActivity(new Intent(LaunchScreenActivity.this,LoginActivity.class));
            }

            finish();
        }
    }
}
