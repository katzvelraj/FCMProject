package com.app.firestore.fcmproject.views;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.app.firestore.fcmproject.R;

import java.util.ArrayList;

public class FcmStartActivity extends AppCompatActivity {

    private TextView notifyTextview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fcm_start);

        notifyTextview = findViewById(R.id.notify_txt);

        String notifyTxt = "Notification Received";

        notifyTxt = getIntent().getExtras()!=null?getIntent().getExtras().getString("MSG",""):notifyTxt;

        notifyTextview.setText(notifyTxt);
    }
}
