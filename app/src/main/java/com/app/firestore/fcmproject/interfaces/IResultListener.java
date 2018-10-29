package com.app.firestore.fcmproject.interfaces;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public interface IResultListener {

    void onAuthSuccessListener(AuthResult authResult);
    void onAuthFailureListener(Exception e);
    void onAuthCompleteListener(Task<AuthResult> task1);



}
