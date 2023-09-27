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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;



import okhttp3.Call;
import okhttp3.Callback;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    //存放json檔的資料
    ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();

    //Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        catch_data();

    }

    private void catch_data(){
        String data = "https://jsonplaceholder.typicode.com/posts";
        new Thread(()->{

                OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                        .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
                        .build();

                Request request = new Request.Builder()
                        .url(data)
                        .build();

                Call call = okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        System.out.println("okhttp傳送失敗"+e);
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
//                        System.out.println("回傳"+response.body().string());   //OK
                        assert response.body() != null;
                        String allData = response.body().string();

                        try {
                            JSONArray jsonArray = new JSONArray(allData);
                            for (int i=0; i<jsonArray.length(); i++){
                                JSONObject obj = jsonArray.getJSONObject(i);
                                String title = obj.getString("title");
                                StringBuilder titleName= new StringBuilder();
                                titleName.append(title).append("\n");

                                runOnUiThread(()->{
                                    binding.textView.setText(title);
                                });
                            }
//


                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });




        }).start();
    }


//    public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder>{
//
//        ArrayList<HashMap<String, String>> arrayList;
//        LayoutInflater inflater;
//
//        public class ViewHolder extends RecyclerView.ViewHolder{
//            TextView txtUserID, txtAlbumTitle, textView3, textView4;
//            public ViewHolder(@NonNull View itemView) {
//                super(itemView);
//                txtUserID = itemView.findViewById(R.id.txtUserID);
//                txtAlbumTitle = itemView.findViewById(R.id.txtAlbumTitle);
//                textView3 = itemView.findViewById(R.id.textView3);
//                textView4 = itemView.findViewById(R.id.textView4);
//            }
//        }
//
//        public Adapter(ArrayList<HashMap<String, String>> arrayList, Context context) {
//            this.arrayList = arrayList;
//            inflater = LayoutInflater.from(context);
//        }
//
//        @NonNull
//        @Override
//        public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.data_item, parent, false);
//            return new ViewHolder(view);
//        }
//
//        @Override
//        public void onBindViewHolder(@NonNull Adapter.ViewHolder holder, int position) {
//            holder.txtUserID.setText(arrayList.get(position).get("userId"));
//            holder.txtAlbumTitle.setText(arrayList.get(position).get("title"));
//            holder.textView3.setText("UserID:");
//        }
//
//        @Override
//        public int getItemCount() {
//            return arrayList.size();
//        }
//    }

}