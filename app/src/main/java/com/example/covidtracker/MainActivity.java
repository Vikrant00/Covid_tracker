package com.example.covidtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.text.NumberFormat;
import java.util.ArrayList;

import io.alterac.blurkit.BlurLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import org.json.JSONArray;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {

    private TextView totalConfirm, totalActive, totalRecovered, totalDeath, todayConfirm, todayRecovered, todayDeath, date;
    private PieChart pieChart;
    String province = "BC";
    BlurLayout blurLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getIntent().getStringExtra("province") != null) {
            province = getIntent().getStringExtra("province");
        }
        TextView provinceButton = findViewById(R.id.provinceButton);
        provinceButton.setText(province);
        findViews();
        provinceButton.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, ProvinceActivity.class)));
        getJson();


    }


    private void getJson() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.opencovid.ca/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RequestInterface request = retrofit.create(RequestInterface.class);
        Call<JsonObject> call = request.getJson();
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonElement ar = response.body().get("summary");
                JsonArray ox = (JsonArray) ar;
//                for(int i =0;i<1;i++)
//                {
//                    JsonObject bh = (JsonObject)ox.get(i);
//                    System.out.println(bh.get("cases"));
//                }
                for (int i = 0; i < ox.size(); i++) {
                    JsonObject bh = (JsonObject) ox.get(i);
                    if (bh.get("province").toString().replaceAll("[\"]", "").equals(province)) {
                        int confirm = Integer.parseInt(bh.get("cumulative_cases").toString().replaceAll("[\"]", ""));
                        int active = Integer.parseInt(bh.get("active_cases").toString().replaceAll("[\"]", ""));
                        int recovered = Double.valueOf(bh.get("cumulative_recovered").toString().replaceAll("[\"]", "")).intValue();
                        int death = Double.valueOf(bh.get("cumulative_deaths").toString().replaceAll("[\"]", "")).intValue();
                        int todaydeath = Double.valueOf(bh.get("deaths").toString().replaceAll("[\"]", "")).intValue();
                        int todayconfirm = Double.valueOf(bh.get("cases").toString().replaceAll("[\"]", "")).intValue();
                        int todayrecovered = Double.valueOf(bh.get("recovered").toString().replaceAll("[\"]", "")).intValue();
                        String datea = bh.get("date").toString().replaceAll("[\"]", "");

                        totalConfirm.setText(NumberFormat.getInstance().format(confirm));
                        totalActive.setText(NumberFormat.getInstance().format(active));
                        totalRecovered.setText(NumberFormat.getInstance().format(recovered));
                        totalDeath.setText(NumberFormat.getInstance().format(death));
                        date.setText("Updated on: " + datea);

                        String td = NumberFormat.getInstance().format(todaydeath);
                        String rd = "( +" + td + " )";

                        todayDeath.setText(rd);
                        td = NumberFormat.getInstance().format(todayconfirm);
                        rd = "( +" + td + " )";
                        todayConfirm.setText(rd);
                        td = NumberFormat.getInstance().format(todayrecovered);
                        rd = "( +" + td + " )";
                        todayRecovered.setText(rd);


                        pieChart.addPieSlice(new PieModel("Confirm", confirm, getResources().getColor(R.color.yellow)));
                        pieChart.addPieSlice(new PieModel("Active", active, getResources().getColor(R.color.blue_pie)));
                        pieChart.addPieSlice(new PieModel("Recovered", recovered, getResources().getColor(R.color.green_pie)));
                        pieChart.addPieSlice(new PieModel("Death", death, getResources().getColor(R.color.red_pie)));

                        pieChart.startAnimation();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }


    private void findViews() {
        totalConfirm = findViewById(R.id.totalConfirm);
        totalActive = findViewById(R.id.totalActive);
        totalRecovered = findViewById(R.id.totalRecovered);
        totalDeath = findViewById(R.id.totalDeath);
        todayConfirm = findViewById(R.id.todayConfirm);
        todayRecovered = findViewById(R.id.todayRecovered);
        todayDeath = findViewById(R.id.todayDeath);
        date = findViewById(R.id.date);
        pieChart = findViewById(R.id.pieChart);
    }
}