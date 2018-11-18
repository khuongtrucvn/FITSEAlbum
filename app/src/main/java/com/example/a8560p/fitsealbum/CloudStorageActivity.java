package com.example.a8560p.fitsealbum;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

public class CloudStorageActivity extends Fragment{

    View cloud;
    ListView lvCloudImage;
    ArrayList<CloudImage> arrayCloudImage;
    CloudImageAdapter adapter=null;

    public static String nameCloud;
    public  static String nameData;

    //FirebaseDatabase database = FirebaseDatabase.getInstance();
    //DatabaseReference mData = database.getReference("CloudImage");


    public static CloudStorageActivity newInstance() {
        CloudStorageActivity fragment = new CloudStorageActivity();
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        cloud =(View) inflater.inflate(R.layout.activity_cloud_storage,container, false);

        lvCloudImage =(ListView) cloud.findViewById(R.id.listViewCloudImage);
        arrayCloudImage = new ArrayList<>();

        adapter= new CloudImageAdapter(CloudStorageActivity.super.getActivity(),R.layout.image_items,arrayCloudImage);
        lvCloudImage.setAdapter(adapter);

        ConnectivityManager conMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();

        if (activeNetwork != null && activeNetwork.isConnected()) {
            if(MainActivity._name_cloud==""){
                Toast.makeText(getContext(), "Đăng nhập để xem ảnh", Toast.LENGTH_SHORT).show();
            }
            else{
                //loadData();
            }
        } else {
            Toast.makeText(getContext(), "Check your internet connection and try again.", Toast.LENGTH_SHORT).show();
        }


        return cloud;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_cloud, menu);
        //super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_Create){
            openCreateCloudActivity();
            return true;
        }
        if(id == R.id.action_Login){
            if(MainActivity._name_cloud==""){
                openLoginCloudActivity();
            }
            else {
                Toast.makeText(getContext(),"Bạn đã đăng nhập, vui lòng đăng xuất để đăng nhập tài khoản khác",Toast.LENGTH_SHORT).show();

            }
            return true;
        }
        if(id == R.id.action_Out){
            if(MainActivity._name_cloud==""){
                Toast.makeText(getContext(),"Bạn chưa đăng nhập Cloud Storage",Toast.LENGTH_SHORT).show();
            }
            else {
                openLogoutCloudActivity();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /*private  void loadData(){
        FullImageActivity.mData.keepSynced(true);
        FullImageActivity.mData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> dataSnapshots = dataSnapshot.getChildren().iterator();
                while (dataSnapshots.hasNext()) {
                    DataSnapshot dataSnapshotChild = dataSnapshots.next();
                    CloudImage item = dataSnapshotChild.getValue(CloudImage.class);
                    arrayCloudImage.add(item);
                    adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }*/

    public void openCreateCloudActivity() {
        //Intent intent = new Intent(this,CloudStorageActivity.class);
        Intent intent = new Intent(this.getActivity(), CreateCloudActivity.class);
        startActivity(intent);
    }

    public void openLoginCloudActivity() {
        //Intent intent = new Intent(this,CloudStorageActivity.class);
        Intent intent = new Intent(this.getActivity(), LoginCloudActivity.class);
        startActivity(intent);
    }

    public void openLogoutCloudActivity() {
        //Intent intent = new Intent(this,CloudStorageActivity.class);
        Intent intent = new Intent(this.getActivity(), LogoutCloudActivity.class);
        startActivity(intent);
    }

}
