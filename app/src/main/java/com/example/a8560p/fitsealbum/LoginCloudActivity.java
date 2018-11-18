package com.example.a8560p.fitsealbum;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class LoginCloudActivity extends AppCompatActivity {
    EditText edtname,edtpass;
    Button btnlogin,btnexit;
    //public static String _name_cloud ="";
    //public static String _pass_cloud;
    ArrayList<CloudUsers> arrayCloudUsers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_cloud);
        edtname =(EditText) findViewById(R.id.name_Cloud2);
        edtpass =(EditText) findViewById(R.id.pass_Cloud2);
        btnlogin =(Button) findViewById(R.id.button_Login);
        btnexit =(Button) findViewById(R.id.button_Exit2);
        arrayCloudUsers=new ArrayList<>();
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String namecloud = edtname.getText().toString().trim();
                String passcloud = edtpass.getText().toString().trim();
                if(checkLogin( arrayCloudUsers,namecloud,passcloud)==true){
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("name_cloud", MainActivity._name_cloud);
                    editor.commit();
                    Toast.makeText(LoginCloudActivity.this,"Đăng nhập thành công",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(LoginCloudActivity.this,"Đăng nhập thất bại",Toast.LENGTH_SHORT).show();
                }
//
            }
        });
    }
    private boolean checkLogin(ArrayList<CloudUsers>  arrayList, String name, String pass ){
//        int temp=0;
//        for(int i=0;i<arrayList.size();i++){
//            if(arrayList.get(i).NameCloud.equals(name) && arrayList.get(i).PassCloud.equals(pass))
//                _name_cloud=name;
//                _pass_cloud=pass;
//                temp=1;
//        }
//        if(temp==1){
//            return true;
//        }
//        return false;
        // chưa lấy được usename và password từ database nên tạm thời return true;
        MainActivity._name_cloud=name;
        //_pass_cloud=pass;
        return true;
    }
}
