package com.example.a8560p.fitsealbum;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.ArrayList;
public class LogoutCloudActivity extends AppCompatActivity {
    EditText edtname,edtpass, edtpass2;
    Button btnlogout,btnexit;
    ArrayList<CloudUsers> arrayCloudUsers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout_cloud);
        edtname =(EditText) findViewById(R.id.name_Cloud3);
        edtpass =(EditText) findViewById(R.id.pass_Cloud3);
        edtpass2 =(EditText) findViewById(R.id.pass_again);
        btnlogout =(Button) findViewById(R.id.button_Logout);
        btnexit =(Button) findViewById(R.id.button_Exit3);
        arrayCloudUsers=new ArrayList<>();
        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String namecloud = edtname.getText().toString().trim();
                String passcloud = edtpass.getText().toString().trim();
                String passcloud2 = edtpass2.getText().toString().trim();
                if(checkLogout( arrayCloudUsers,namecloud,passcloud,passcloud2)==true){
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("name_cloud", MainActivity._name_cloud);
                    editor.commit();
                    Toast.makeText(LogoutCloudActivity.this,"Đăng xuất thành công",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(LogoutCloudActivity.this,"Đăng xuất không thành công",Toast.LENGTH_SHORT).show();
                }
//
            }
        });
    }
    private boolean checkLogout(ArrayList<CloudUsers> arrayList, String name, String pass , String pass2){
//        int temp=0;
//        for(int i=0;i<arrayList.size();i++){
//            if(arrayList.get(i).NameCloud.equals(name) && arrayList.get(i).PassCloud.equals(pass)&& pass.equals(pass2))
//               LoginCloudActivity._name_cloud="";
//                temp=1;
//        }
//        if(temp==1){
//            return true;
//        }
//        return false;
        //        chưa lấy được usename và password từ database nên tạm thời return true;
        MainActivity._name_cloud="";
        //_pass_cloud=pass;
        return true;
    }
}
