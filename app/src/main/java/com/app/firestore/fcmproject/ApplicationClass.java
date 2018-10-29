package com.app.firestore.fcmproject;

import android.app.Application;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDex;

import com.app.firestore.fcmproject.pushnotification.NotificationSendService;
import com.app.firestore.fcmproject.views.FirestoreActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class ApplicationClass extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        MultiDex.install(getApplicationContext());


    }

    private void getToken(){
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {


                    }
                });
    }
}
