package com.app.firestore.fcmproject.helper;

import android.support.annotation.NonNull;

import com.app.firestore.fcmproject.interfaces.IResultListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

import org.json.JSONArray;

public class ResultHelper {

    private final String EMAIL = "a.sajen10491@gmail.com";
    private final String PASSWORD = "1234567890";

    public final static String BASE_NODE ="Sa_Tech";
    public final static String USER ="user";
    public final static String NAME ="Name";
    public final static String DESIGNATION ="Designation";
    public final static String PHONE ="Phone";


    public void createFirebaseUser(final IResultListener iResultListener){
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(EMAIL, PASSWORD)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task1) {

                        if (!task1.isSuccessful())
                            if (task1.getException() instanceof FirebaseAuthUserCollisionException)
                                signFirebaseUser(iResultListener);
                        else
                            iResultListener.onAuthCompleteListener(task1);

                    }
                });
    }

    public void signFirebaseUser(final IResultListener iResultListener){
        FirebaseAuth.getInstance().signInWithEmailAndPassword(EMAIL, PASSWORD)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        iResultListener.onAuthSuccessListener(authResult);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        iResultListener.onAuthFailureListener(e);
                    }
                });
    }

    public void sendMessage(final JSONArray recipients) {
        new NotifyAsync(recipients).execute();

    }
}
