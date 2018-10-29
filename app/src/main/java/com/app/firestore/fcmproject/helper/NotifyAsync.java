package com.app.firestore.fcmproject.helper;

import android.os.AsyncTask;
import android.util.Log;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import static android.content.ContentValues.TAG;

public class NotifyAsync extends AsyncTask<String, String, String> {


    private final static String SERVER_KEY= "AAAAXnMw8js:APA91bERAG_diLZyToW6hGm_DuSPpcq8RW6yBlHGQYZyXLHn6iyGqA31Cu3Fe6cNrUmeigs4Tbkzo-mf0kDt1VOXneimhAQnzEb25GYxx7azyQCofL9gwu-bXDuowcWdpocGonQBoYro";
    private JSONArray recipients;
    private OkHttpClient mClient = new OkHttpClient();
    private static final String FCM_MESSAGE_URL = "https://fcm.googleapis.com/fcm/send";

    NotifyAsync(final JSONArray recipients){
        this.recipients = recipients;
    }

    @Override
    protected String doInBackground(String... params) {
        try {

            JSONObject notification = new JSONObject();
            String body = "Notification Message";
            notification.put("body", body);
            String title = "FCM Notify";
            notification.put("title", title);

            JSONObject data = new JSONObject();
            String message = "Notification Send Successfull";
            data.put("message", message);

            JSONObject root = new JSONObject();
            root.put("notification", notification);
            root.put("data", data);
            root.put("registration_ids", recipients);

            String result = postToFCM(root.toString());
            Log.d(TAG, "Result: " + result);
            return result;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        try {
            JSONObject resultJson = new JSONObject(result);
            int success, failure;
            success = resultJson.getInt("success");
            failure = resultJson.getInt("failure");
        } catch (JSONException e) {
            e.printStackTrace();
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
