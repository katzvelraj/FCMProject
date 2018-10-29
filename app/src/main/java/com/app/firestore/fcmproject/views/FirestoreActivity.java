package com.app.firestore.fcmproject.views;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.firestore.fcmproject.R;
import com.app.firestore.fcmproject.pushnotification.NotificationSendService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.HashMap;
import java.util.Map;

import static com.app.firestore.fcmproject.helper.ResultHelper.BASE_NODE;
import static com.app.firestore.fcmproject.helper.ResultHelper.DESIGNATION;
import static com.app.firestore.fcmproject.helper.ResultHelper.NAME;
import static com.app.firestore.fcmproject.helper.ResultHelper.PHONE;

public class FirestoreActivity extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ListenerRegistration listenerRegistration;
    private EditText addUserEditTxt, updateUserEditTxt, searchUserEditTxt;
    private TextView labelListenerTxt;
    private Button notifyUser;

    private String oldText = "UpdateTable", searchUserToken = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firestore);

        addUserEditTxt = findViewById(R.id.value_txt);
        updateUserEditTxt = findViewById(R.id.value_update);
        labelListenerTxt = findViewById(R.id.label_listener);
        searchUserEditTxt = findViewById(R.id.value_search);


        notifyUser = findViewById(R.id.btn_notify);

        addNewUser(oldText);
        addRealtimeUpdate(oldText);

        updateUserEditTxt.setText(oldText);
        labelListenerTxt.setText(oldText);

        findViewById(R.id.btn_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addUserEditTxt.getText().toString().trim().length() > 0) {
                    addNewUser(addUserEditTxt.getText().toString());
                }
            }
        });

        findViewById(R.id.btn_update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (updateUserEditTxt.getText().toString().trim().length() > 0) {
                    updateUser(oldText, updateUserEditTxt.getText().toString());
                    //oldText = updateUserEditTxt.getText().toString();
                }
            }
        });

        findViewById(R.id.btn_seach).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchUserEditTxt.getText().toString().trim().length() > 0) {
                    readSingleUser(searchUserEditTxt.getText().toString());
                }
            }
        });

        notifyUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendNotify();
            }
        });
    }

    private void addNewUser(final String name) {
        Map<String, Object> newUser = new HashMap<>();
        newUser.put(NAME, name);
        newUser.put(DESIGNATION, "Android");
        newUser.put(PHONE, "9941837090");

        db.collection(BASE_NODE).document(name).set(newUser)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        updateFCMToken(name);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                    }
                });
    }

    private void updateFCMToken(final String name) {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {

                        if (task.getResult() == null)
                            return;

                        Map<String, Object> tokenObj = new HashMap<>();
                        tokenObj.put("fcmtoken", task.getResult().getToken());

                        db.collection(BASE_NODE).document(name)
                                .update(tokenObj);
                    }
                });
    }


    private void sendNotify() {

        Intent intent = new Intent(FirestoreActivity.this, NotificationSendService.class);
        intent.putExtra("ID", searchUserToken);
        intent.putExtra("MSG", searchUserEditTxt.getText().toString() + " Send u Message");

        startService(intent);
    }

    private void updateUser(String userName, String newName) {
        Map<String, Object> updateUser = new HashMap<>();
        updateUser.put(NAME, newName);

        db.collection(BASE_NODE)
                .document(userName)
                .update(updateUser)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        e.printStackTrace();
                    }
                });
    }

    private void readSingleUser(String name) {

        Query query = db.collection(BASE_NODE).whereEqualTo("Name", name);

        query.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots == null || queryDocumentSnapshots.size() == 0) {
                            Toast.makeText(FirestoreActivity.this, "No Search Element Found", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        DocumentChange snapshot = queryDocumentSnapshots.getDocumentChanges().get(0);

                        searchUserToken = (String) snapshot.getDocument().get("fcmtoken");

                        notifyUser.setEnabled(true);


                    }
                });

    }

    private void addRealtimeUpdate(String name) {

        if (listenerRegistration != null)
            listenerRegistration.remove();

        DocumentReference contactListener =
                db.collection(BASE_NODE).document(name);
        listenerRegistration = contactListener.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                if (e != null) {
                    return;
                }
                if (documentSnapshot != null
                        && documentSnapshot.exists()
                        && documentSnapshot.getData() != null) {

                    labelListenerTxt.setText((String) documentSnapshot.getData().get("Name"));

                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        listenerRegistration.remove();
    }
}
