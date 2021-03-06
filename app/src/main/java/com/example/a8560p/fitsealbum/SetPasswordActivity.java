package com.example.a8560p.fitsealbum;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Calendar;

public class SetPasswordActivity extends AppCompatActivity{
    MyPrefs myPrefs;
    Button btnConfirm, btnCancel;
    TextInputLayout txtPassword, txtRetypePassword;
    String password, retypePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        myPrefs = new MyPrefs(this);
        setNightmode();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_password);

        txtPassword = (TextInputLayout)findViewById(R.id.txt_Password);
        txtRetypePassword = (TextInputLayout)findViewById(R.id.txt_RetypePassword);

        btnConfirm = (Button)findViewById(R.id.btn_confirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(confirmPassword()){
                    startActivity(new Intent(SetPasswordActivity.this,MainActivity.class));
                    startActivity(new Intent(SetPasswordActivity.this,SettingsActivity.class));
                    finish();
                }
            }
        });

        btnCancel = (Button)findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SetPasswordActivity.this,MainActivity.class));
                startActivity(new Intent(SetPasswordActivity.this,SettingsActivity.class));
                finish();
            }
        });
    }

    boolean validatePassword(){
        password = txtPassword.getEditText().getText().toString().trim();
        retypePassword = txtRetypePassword.getEditText().getText().toString().trim();

        if(password.isEmpty()){
            txtPassword.setError("Password cannot be blank");
            return false;
        }
        else if(password.length()<6){
            txtPassword.setError("Password must be at least 6 characters long");
            return false;
        }
        else if(retypePassword.isEmpty()) {
            txtRetypePassword.setError("Password cannot be blank");
            return false;
        }
        else if(!password.matches(retypePassword)){
            txtRetypePassword.setError("Password and Retype password do not match");
            return false;
        }
        else{
            txtPassword.setError(null);
            txtPassword.setErrorEnabled(false);

            txtRetypePassword.setError(null);
            txtRetypePassword.setErrorEnabled(false);
            return true;
        }
    }

    boolean confirmPassword(){
        if(validatePassword()){
            //Truy cập và lưu password vào file data
            myPrefs.setPassword(password);

            Toast.makeText(this, "Password has been set", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    public boolean CheckTime(int curHour,int curMinute, int hourStart, int minuteStart, int hourEnd, int minuteEnd) {
        boolean nightmode = true;

        if(hourStart < hourEnd){
            if(hourStart <= curHour && curHour <= hourEnd){
                if(hourStart == curHour){
                    if(minuteStart > curMinute){
                        nightmode = false;
                    }
                }
                if(hourEnd == curHour){
                    if(curMinute > minuteEnd){
                        nightmode = false;
                    }
                }
            }
            else{
                nightmode = false;
            }
        }
        else if(hourStart == hourEnd){
            if(hourStart == curHour){
                if(minuteStart<minuteEnd){
                    if(minuteStart > curMinute || curMinute > minuteEnd){
                        nightmode = false;
                    }
                }
                else if(minuteStart>minuteEnd){
                    if(minuteEnd <= curMinute && curMinute <= minuteStart){
                        nightmode = false;
                    }
                }
            }
            else{
                nightmode = false;
            }
        }
        else{
            if(hourEnd >= curHour || curHour >= hourStart){
                if(hourStart == curHour){
                    if(minuteStart > curMinute){
                        nightmode = false;
                    }
                }
                if(hourEnd == curHour){
                    if(curMinute > minuteEnd){
                        nightmode = false;
                    }
                }
            }
        }
        return nightmode;
    }

    public void setNightmode(){
        Calendar c = Calendar.getInstance();
        int hour, minute;

        if(myPrefs.loadNightModeState() == 0){
            setTheme(R.style.DayNoActionBarTheme);
        }
        else if(myPrefs.loadNightModeState() == 1){
            setTheme(R.style.NightNoActionBarTheme);
        }
        else if(myPrefs.loadNightModeState() == 2) {
            hour = c.get(Calendar.HOUR_OF_DAY);

            if(6 <= hour && hour <= 17){
                setTheme(R.style.DayNoActionBarTheme);
            }
            else{
                setTheme(R.style.NightNoActionBarTheme);
            }
        }
        else{
            hour = c.get(Calendar.HOUR_OF_DAY);
            minute = c.get(Calendar.MINUTE);
            boolean nightmode = CheckTime(hour,minute,myPrefs.loadStartHour(),myPrefs.loadStartMinute(),myPrefs.loadEndHour(),myPrefs.loadEndMinute());
            if(true == nightmode){
                setTheme(R.style.NightNoActionBarTheme);
            }
            else{
                setTheme(R.style.DayNoActionBarTheme);
            }
        }
    }
}
