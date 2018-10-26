package com.example.a8560p.fitsealbum;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.OutputStreamWriter;

public class SetPasswordActivity extends AppCompatActivity{
    private final static String FILE_PASSWORD = "password.txt";
    Button btnConfirm, btnCancel;
    TextInputLayout txtPassword, txtRetypePassword;
    String password, retypePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_password);

        txtPassword = (TextInputLayout)findViewById(R.id.txt_Password);
        txtRetypePassword = (TextInputLayout)findViewById(R.id.txt_RetypePassword);

        btnConfirm = (Button)findViewById(R.id.btn_confirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(confirmPassword()){
                    finish();
                }
            }
        });

        btnCancel = (Button)findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            //Truy cập và lưu password vào file password.txt
            try {
                OutputStreamWriter out = new OutputStreamWriter(
                        openFileOutput(FILE_PASSWORD, 0));
                out.write(password);
                out.close();
            } catch (Throwable t) {
                Toast.makeText(this, t.getMessage(), Toast.LENGTH_LONG).show();
            }

            Toast.makeText(this, "Password has been set", Toast.LENGTH_SHORT).show();
            return true;
        }
        else{
            return false;
        }
    }
}
