package com.example.a8560p.fitsealbum;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

public class CreateCloudActivity extends AppCompatActivity {
    EditText edtname,edtpass;
    Button btncreate,btnexit;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mData = database.getReference("InfoUsers");
    //DatabaseReference InfoUsers = database.getReference("InfoUsers");
    String namecloud ;
    String passcloud ;
    ArrayList<CloudUsers> arrayCloudUsers ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_cloud);
        edtname =(EditText) findViewById(R.id.name_Cloud);
        edtpass =(EditText) findViewById(R.id.pass_Cloud);
        btncreate =(Button) findViewById(R.id.button_Create);
        btnexit =(Button) findViewById(R.id.button_Exit);
        arrayCloudUsers=new ArrayList<>();
        btncreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                namecloud = edtname.getText().toString().trim();
                passcloud = edtpass.getText().toString().trim();
//                    loadInfoUsers();
//                CloudUsers cloudUsers = new CloudUsers(namecloud,passcloud);
//                arrayCloudUsers.add(cloudUsers);
//                Toast.makeText(CreateCloudActivity.this, arrayCloudUsers.get(0).NameCloud, Toast.LENGTH_SHORT).show();
                if (checkCloud(namecloud) == true) {
                    CloudUsers item = new CloudUsers(namecloud, passcloud);
                    mData.push().setValue(item, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                            if (databaseError == null) {
                                Toast.makeText(CreateCloudActivity.this, "Tạo Cloud thành công", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(CreateCloudActivity.this, "Quá trình tạo bị lỗi, xin thử lại ", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(CreateCloudActivity.this, "Có thể tên Cloud đã trùng, xin kiểm tra lại", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void loadInfoUsers(){
//        mData.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                arrayCloudUsers.clear();
//                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
//                    CloudUsers cloudUsers=new CloudUsers();
//                    cloudUsers = postSnapshot.getValue(CloudUsers.class);
//                    arrayCloudUsers.add(cloudUsers);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
        mData.keepSynced(true);
        mData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> dataSnapshots = dataSnapshot.getChildren().iterator();
                while (dataSnapshots.hasNext()) {
                    DataSnapshot dataSnapshotChild = dataSnapshots.next();
                    CloudUsers cloudUsers=new CloudUsers();
                    cloudUsers = dataSnapshotChild.getValue(CloudUsers.class);
                    arrayCloudUsers.add(cloudUsers);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
    public boolean checkCloud(String name) {
//        loadInfoUsers();
//        for(int i=0;i<arrayCloudUsers.size();i++){
//            if(arrayCloudUsers.get(i).NameCloud==name){
//                Toast.makeText(CreateCloudActivity.this, arrayCloudUsers.get(i).NameCloud, Toast.LENGTH_SHORT).show();
//
//                return false;
//            }
//        }
//        return true;
        //  }
        for (int i = 0; i < arrayCloudUsers.size(); i++) {
            if (arrayCloudUsers.get(i).NameCloud == name) {
                return false;
            }
        }
        CloudUsers cloudUsers = new CloudUsers(namecloud, passcloud);
        arrayCloudUsers.add(cloudUsers);
        return true;
    }
}
