package com.example.a8560p.fitsealbum;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

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

    /*FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mData = database.getReference("CloudImage");*/

    public static CloudStorageActivity newInstance() {
        CloudStorageActivity fragment = new CloudStorageActivity();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        cloud =(View) inflater.inflate(R.layout.activity_cloud_storage,container, false);

        /*lvCloudImage =(ListView) cloud.findViewById(R.id.listViewCloudImage);
        arrayCloudImage = new ArrayList<>();

        adapter= new CloudImageAdapter(CloudStorageActivity.super.getActivity(),R.layout.image_items,arrayCloudImage);
        lvCloudImage.setAdapter(adapter);
        loadData();*/

        return cloud;
    }

    /*private  void loadData(){
        mData.keepSynced(true);
        mData.addValueEventListener(new ValueEventListener() {
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
}
