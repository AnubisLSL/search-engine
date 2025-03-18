package com.anubis.li.searchengine.deepseek.service;

import com.anubis.li.searchengine.deepseek.request.DeepSeekRequest;
import com.anubis.li.searchengine.deepseek.response.DeepSeekResponse;
import com.google.gson.Gson;
import okhttp3.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class DeepseekService {
    public static void main(String[] args) throws IOException {
        deepseekChat();
    }

    public static String deepseekChat() throws IOException {
        // DeepSeek本地地址
        String url = "http://127.0.0.1:11434/api/chat";
        DeepSeekRequest requestObject = new DeepSeekRequest();
        List<DeepSeekRequest.Message> messages = new ArrayList<>();
        // 添加消息 根据自身情况调整
        messages.add(new DeepSeekRequest.Message("如何评价百融云创", "user"));
        requestObject.setMessages(messages);
        // 模型 根据自身情况调整
        requestObject.setModel("deepseek-r1:7b");
        requestObject.setFrequency_penalty(0);
        requestObject.setMax_tokens(2048);
        requestObject.setPresence_penalty(0);
        requestObject.setResponse_format(new DeepSeekRequest.ResponseFormat("text"));
        requestObject.setStop(null);
        requestObject.setStream(false);
        requestObject.setStream_options(null);
        requestObject.setTemperature(1);
        requestObject.setTop_p(1);
        requestObject.setTools(null);
        requestObject.setTool_choice("none");
        requestObject.setLogprobs(false);
        requestObject.setTop_logprobs(null);


        // 使用Gson将请求对象转换为JSON字符串
        Gson  gson = new Gson();
        String jsonBody = gson.toJson(requestObject);
        // 创建OkHttpClient实例，并设置超时时间
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(300, java.util.concurrent.TimeUnit.SECONDS)
                .readTimeout(300, java.util.concurrent.TimeUnit.SECONDS)
                .writeTimeout(300, java.util.concurrent.TimeUnit.SECONDS)
                .build();
        // 设置请求体的媒体类型为JSON
        MediaType mediaType = MediaType.parse("application/json");
        // 创建请求体，包含JSON字符串
        RequestBody body = RequestBody.create(mediaType, jsonBody);
        // 创建HTTP POST请求
        Request request = new Request.Builder()
                .url(url)
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .build();
        // 发送请求并获取响应
        Response response = client.newCall(request).execute();
        DeepSeekResponse deepSeekResponse = gson.fromJson(response.body().string(), DeepSeekResponse.class);
        return deepSeekResponse.getMessage().getContent();
    }
}

