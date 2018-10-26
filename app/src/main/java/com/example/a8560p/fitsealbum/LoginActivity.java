package com.example.a8560p.fitsealbum;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class LoginActivity extends AppCompatActivity {
    String password, inputPassword;
    Button btnConfirm;
    TextInputLayout txtPassword;
    private final static String FILE_PASSWORD = "password.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtPassword = (TextInputLayout)findViewById(R.id.txt_Password);

        btnConfirm = (Button)findViewById(R.id.btn_confirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkPassword()){
                    finish();
                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                }
            }
        });
    }

    boolean checkPassword(){
        inputPassword=txtPassword.getEditText().getText().toString();

        try {
            InputStream inputStream = openFileInput(FILE_PASSWORD);
            if (inputStream != null) {
                BufferedReader reader = new BufferedReader(new
                        InputStreamReader(inputStream));
                password = reader.readLine();
                inputStream.close();
            }
        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }

        if(!inputPassword.matches(password)){
            txtPassword.setError("Password is not correct");
            return false;
        }
        else{
            txtPassword.setError(null);
            txtPassword.setErrorEnabled(false);
            return true;
        }
    }
}
