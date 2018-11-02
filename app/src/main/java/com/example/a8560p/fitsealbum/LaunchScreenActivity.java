package com.example.a8560p.fitsealbum;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class LaunchScreenActivity extends AppCompatActivity{
    MyPrefs myPrefs;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        myPrefs = new MyPrefs(this);
        password = myPrefs.getPassword();
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
                myPrefs.setPassMode(0);
                startActivity(new Intent(LaunchScreenActivity.this,PasswordActivity.class));
            }

            finish();
        }
    }
}
