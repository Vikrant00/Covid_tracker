package com.example.covidtracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProvinceActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<ProvinceData> list;
    private ProgressDialog dialog;
    private EditText searchBar;
    private ProvinceAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_province);

        recyclerView = findViewById(R.id.province);
        list = new ArrayList<>();
        searchBar = findViewById(R.id.searchBar);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        adapter = new ProvinceAdapter(this, list);
        recyclerView.setAdapter(adapter);



        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading....");
        dialog.setCancelable(false);
        dialog.show();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.opencovid.ca/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RequestInterface request = retrofit.create(RequestInterface.class);
        Call<JsonObject> call=request.getJson();
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonElement ar = response.body().get("summary");
                JsonArray ox = (JsonArray) ar;

                for(int i = 0; i <ox.size();i++)
                {
                    JsonObject bh = (JsonObject)ox.get(i);
                    ProvinceData newob = new ProvinceData(bh.get("province").toString().replaceAll("[\"]",""),bh.get("active_cases").toString().replaceAll("[\"]",""));
                    list.add(newob);
                }
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(ProvinceActivity.this, "Something went wrong: "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                filter(s.toString());
            }
        });

    }

    private void filter(String result)
    {
        List<ProvinceData> filterList = new ArrayList<>();
        for(ProvinceData items : list)
        {
            if(items.getProvince().toLowerCase().contains(result.toLowerCase()))
            {
                filterList.add(items);
            }
        }

        adapter.filterList(filterList);


    }
}