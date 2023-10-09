package com.comp5216.healthguard.util;

import android.util.Log;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

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
    private static final String FCM_URL = "https://fcm.googleapis.com/fcm/send";
    private static final String SERVER_KEY = "pl2uZ8llIFeY8COpiWvUm5kSmbSe5GDxJRp0Albwdc8";

    public static void sendFCMMessage(String toToken, String title, String body) {
        try {

            // 创建连接对象
            URL url = new URL(FCM_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // 设置请求头
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "key=" + SERVER_KEY);
            connection.setRequestProperty("Content-Type", "application/json");

            // 构建消息体
            String jsonMessage = String.format(
                    "{\"to\":\"%s\",\"notification\":{\"title\":\"%s\",\"body\":\"%s\"}}",
                    toToken, title, body
            );

            // 发送请求
            connection.setDoOutput(true);
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(jsonMessage.getBytes());
            outputStream.flush();
            outputStream.close();

            // 检查响应
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                System.out.println("Message sent successfully");
            } else {
                System.out.println("Failed to send message: " + connection.getResponseMessage());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
