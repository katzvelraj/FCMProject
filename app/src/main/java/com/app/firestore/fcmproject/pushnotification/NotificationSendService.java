package com.app.firestore.fcmproject.pushnotification;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import static android.content.ContentValues.TAG;

public class NotificationSendService extends IntentService {

    private final static String SERVER_KEY= "AAAAXnMw8js:APA91bERAG_diLZyToW6hGm_DuSPpcq8RW6yBlHGQYZyXLHn6iyGqA31Cu3Fe6cNrUmeigs4Tbkzo-mf0kDt1VOXneimhAQnzEb25GYxx7azyQCofL9gwu-bXDuowcWdpocGonQBoYro";
    private JSONArray recipients;
    private static final String FCM_MESSAGE_URL = "https://fcm.googleapis.com/fcm/send";
    private OkHttpClient mClient = new OkHttpClient();

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     */
    public NotificationSendService() {
        super(NotificationSendService.class.getName());
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        try {

            if (intent == null || intent.getExtras() == null)
                return;

            String recipient = intent.getExtras().getString("ID","");

            String notifyTxt = "Notification Received";

            notifyTxt = intent.getExtras()!=null?intent.getExtras().getString("MSG",""):notifyTxt;

            System.out.println("Inside Service Class "+recipient);
            System.out.println("Inside Service Class "+notifyTxt);

            JSONObject notification = new JSONObject();
            String body = "Notification Message";
            notification.put("body", body);
            String title = "FCM Notify";
            notification.put("title", title);

            JSONObject data = new JSONObject();
            String message = notifyTxt;
            data.put("message", message);

            JSONObject root = new JSONObject();
            //root.put("notification", notification);
            root.put("data", data);
            root.put("to", recipient);

            System.out.println("root = " + root.toString());

            String result = postToFCM(root.toString());
            Log.d(TAG, "Result: " + result);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    private String postToFCM(String bodyString) throws IOException {

        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        RequestBody body = RequestBody.create(JSON, bodyString);
        Request request = new Request.Builder()
                .url(FCM_MESSAGE_URL)
                .post(body)
                .addHeader("Authorization", "key=" + SERVER_KEY)
                .build();
        Response response = mClient.newCall(request).execute();
        return response.body().string();
    }

}
