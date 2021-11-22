package com.example.covidtracker;

import android.content.Context;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class ProvinceAdapter extends RecyclerView.Adapter<ProvinceAdapter.ProvinceViewHolder> {

    private Context context;
    private List<ProvinceData> list;

    public ProvinceAdapter(Context context, List<ProvinceData> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ProvinceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.province_layout, parent, false);
        return new ProvinceViewHolder(view);
    }

    public void filterList(List<ProvinceData> filterList)
    {
        list = filterList;
        notifyDataSetChanged();
    }
    @Override
    public void onBindViewHolder(@NonNull ProvinceViewHolder holder, int position) {

        ProvinceData data = list.get(position);
        holder.provinceCases.setText(NumberFormat.getInstance().format(Integer.parseInt(data.getCases())));
        holder.provinceName.setText(data.getProvince());
        holder.number.setText(String.valueOf(position+1));

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, MainActivity.class);
            intent.putExtra("province",data.getProvince());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ProvinceViewHolder extends RecyclerView.ViewHolder {

        private TextView number, provinceName, provinceCases;

        public ProvinceViewHolder(@NonNull View itemView) {
            super(itemView);

            number = itemView.findViewById(R.id.number);
            provinceName = itemView.findViewById(R.id.provinceName);
            provinceCases = itemView.findViewById(R.id.provinceCases);

        }
    }
}
