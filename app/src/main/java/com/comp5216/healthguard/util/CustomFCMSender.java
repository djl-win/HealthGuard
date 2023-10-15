package com.comp5216.healthguard.util;

import android.content.Context;
import android.os.BatteryManager;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * 消息通知服务器
 * <p>
 * 消息通知服务器
 * </p>
 *
 * @author Jiale Dong
 * @version 1.0
 * @since 2023-10-10
 */
public class CustomFCMSender {

    private static final String FCM_API = "https://fcm.googleapis.com/fcm/send";
    private static final String serverKey = "key=" + "\tAAAAMkqgtN8:APA91bHUfLbpfJ1xNG1Kqf65PW_TQatGSo61AH2bzrlPLwX5rZIGiiPsuoN9kY_fKTvN8r6r2m4klG1aSS6KndkTSO7AjDn8z24Kv0CYBX7OAPYAYv75uRdsIk9oD__ta2ogVrSlGDgE";
    private static final String contentType = "application/json";


    public static void sendFCMMessage(Context context, String toToken, String title, String body) {

        BatteryManager batteryManager = (BatteryManager) context.getSystemService(Context.BATTERY_SERVICE);
        int battery = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);

        if (battery > 50) {
            RequestQueue requestQueue = Volley.newRequestQueue(context);

            JSONObject notification = new JSONObject();
            JSONObject notificationBody = new JSONObject();

            try {
                notificationBody.put("title", title);
                notificationBody.put("body", body);
                notification.put("to", toToken);
                notification.put("notification", notificationBody);
            } catch (JSONException e) {
                Log.e("djl", "sendFCMMessage: " + e.getMessage());
            }

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.POST, FCM_API, notification,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.i("djl", "onResponse: " + response);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(context, "Request error", Toast.LENGTH_LONG).show();
                            Log.i("djl", "onErrorResponse: Didn't work");
                            error.printStackTrace();
                            Log.e("djl", "Error: " + error.getMessage());
                        }
                    }
            ) {
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> params = new HashMap<>();
                    params.put("Authorization", serverKey);
                    params.put("Content-Type", contentType);
                    return params;
                }
            };

            requestQueue.add(jsonObjectRequest);
        }
    }
}
