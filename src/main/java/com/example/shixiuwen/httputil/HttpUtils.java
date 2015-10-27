package com.example.shixiuwen.httputil;

import com.example.shixiuwen.entities.ChatMessage;
import com.example.shixiuwen.entities.Result;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;

/**
 * Created by shixiuwen on 15-10-27.
 */
public class HttpUtils {

    public static String doGet(String msg) {

        String result = null;

        String APIKEY = "2f047d4718869645aa50acaebf2f8946";
        try {
            String INFO = URLEncoder.encode(msg, "utf-8");
            String getURL = "http://www.tuling123.com/openapi/api?key=" + APIKEY + "&info=" + INFO;
            URL getUrl = new URL(getURL);
            HttpURLConnection connection = (HttpURLConnection) getUrl.openConnection();
            connection.connect();
            connection.setReadTimeout(5 * 1000);
            connection.setConnectTimeout(5 * 1000);
            connection.setRequestMethod("GET");

            //获取输入流
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            reader.close();
            //断开连接
            connection.disconnect();
            System.out.println(sb.toString());

            result = sb.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static ChatMessage sendMessage(String msg) {
        ChatMessage chatMessage = new ChatMessage();

        String s = doGet(msg);

        Result result = null;

        Gson gson = new Gson();
        try {
            result = gson.fromJson(s, Result.class);
            if (result != null) {
                chatMessage.setMsg(result.getText());
            }else{
                chatMessage.setMsg("你不联网，不和你玩了！！！");
            }
        } catch (JsonSyntaxException e) {
            chatMessage.setMsg("我现在很忙，请等会儿啊~~~");
        }

        chatMessage.setDate(new Date());
        chatMessage.setType(ChatMessage.Type.INCOME);
        return chatMessage;
    }


}
