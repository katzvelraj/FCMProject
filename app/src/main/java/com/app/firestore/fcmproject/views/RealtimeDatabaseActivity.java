package com.app.firestore.fcmproject.views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.app.firestore.fcmproject.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import static com.app.firestore.fcmproject.helper.ResultHelper.DESIGNATION;
import static com.app.firestore.fcmproject.helper.ResultHelper.NAME;
import static com.app.firestore.fcmproject.helper.ResultHelper.PHONE;

public class RealtimeDatabaseActivity extends AppCompatActivity {

    private EditText valueEditTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realtime_database);

        valueEditTxt = findViewById(R.id.value_txt);

        findViewById(R.id.btn_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (valueEditTxt.getText().toString().trim().length() > 0) {
                    addData();
                    valueListner();
                    childListener();
                }
            }
        });


    }

    private void addData(){

        Map<String, Object> userData = new HashMap<>();
        userData.put(NAME, valueEditTxt.getText().toString());
        userData.put(DESIGNATION, "Android");
        userData.put(PHONE, "9941837090");

        FirebaseDatabase.getInstance().getReference("Users/"+valueEditTxt.getText().toString())
                .setValue(userData);
    }

    private void valueListner(){

        FirebaseDatabase.getInstance().getReference("Users/"+valueEditTxt.getText().toString())
                .addValueEventListener(new ValueEventListener() { //attach listener

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) { //something changed!
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String values = snapshot.getValue()!=null?snapshot.getValue().toString():"";
                    System.out.println("addValueEventListener values = " + values);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { //update UI here if error occurred.

            }
        });
    }

    private void childListener(){

        FirebaseDatabase.getInstance().getReference("Users/"+valueEditTxt.getText().toString())
                .addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                System.out.println("dataSnapshot onChildAdded = " + dataSnapshot);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                System.out.println("dataSnapshot onChildChanged = " + dataSnapshot);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                System.out.println("dataSnapshot onChildRemoved = " + dataSnapshot);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                System.out.println("dataSnapshot onChildMoved = " + dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("dataSnapshot onCancelled = " + databaseError);
            }
        });

    }

}
