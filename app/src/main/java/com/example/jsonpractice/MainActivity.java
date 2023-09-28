package com.example.jsonpractice;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.example.jsonpractice.databinding.ActivityMainBinding;
import com.example.jsonpractice.model.myData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.IOException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import okhttp3.Call;
import okhttp3.Callback;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.btnTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                catch_data();
            }
        });

        binding.btnGson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                catch_data2();
            }
        });


    }

    private void catch_data(){
        String data = "https://jsonplaceholder.typicode.com/posts";

        //網路接收資料是耗時操作
        //
        //因此在程式碼內必須寫入一些耗時執行緒的操作
        //
        //像是繼承Thread，或是Handler，或者是AsyneTask
        //這裡是Thread搭配runOnUIThread
        new Thread(()->{
            //建立連線
            OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                    //加入攔截器 可以印出request的訊息                        //level.BASIC = 印出request和response
                    .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
                    .build();
            //設定傳送request
            Request request = new Request.Builder()
                    .url(data)
                    .build();
            //設定回傳
            Call call = okHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    System.out.println("okhttp傳送失敗"+e);
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
//                        System.out.println("回傳"+response.body().string());   //測試抓資料OK
                    assert response.body() != null;
                    String allData = response.body().string();

                    try {
                        //把String轉成JSONArray
                        JSONArray jsonArray = new JSONArray(allData);
                        //建一個StringBuilder的物件(SB相較於String更靈活)
                        StringBuilder titleName= new StringBuilder();

                        //用迴圈取出jsonArray的值
                        for (int i=0; i<jsonArray.length(); i++){
                            //把JSONArray裡每個JSONObject取出來
                            JSONObject obj = jsonArray.getJSONObject(i);
                                            //用key取得JSONObject的值
                            String title = obj.getString("title");

                            titleName.append(title).append("\n");
                        }

                        runOnUiThread(()->{
                            binding.textView.setText(titleName);
                        });

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            });

        }).start();
    }

    private void catch_data2(){
        String data = "https://jsonplaceholder.typicode.com/posts";
        new Thread(()->{
            //建立連線
            OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                    //加入攔截器 可以印出request的訊息                        //level.BASIC = 印出request和response
                    .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
                    .build();
            //設定傳送request
            Request request = new Request.Builder()
                    .url(data)
                    .build();
            //設定回傳
            Call call = okHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    System.out.println("okhttp傳送失敗"+e);
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
//                        System.out.println("回傳"+response.body().string());   //測試抓資料OK
                    assert response.body() != null;
                    String allData = response.body().string();

                    //建一個gson物件
                    Gson gson = new Gson();
                                        //TypeToken: gson的資料類型轉換器, 先用TypeToken<>得到匿名內部類別, 再由該匿名內部類別成員使用.getType()得到要轉換的Type
                                        //
                                        //解釋By: https://blog.csdn.net/jiangyu1013/article/details/56489412
                    Type myDataType = new TypeToken<List<myData>>() {}.getType();
                                            //把JSON字串轉成Type(aka List<myData>)
                    List<myData> dataList = gson.fromJson(allData, myDataType);

                    System.out.println(dataList);
                    StringBuilder bodyName= new StringBuilder();
                    for (myData myData: dataList){
                        String body = myData.getBody();
                        bodyName.append(body).append("\n");
                    }

                    runOnUiThread(()->{
                        binding.textView.setText(bodyName);

                    });


                }
            });

        }).start();
    }


}