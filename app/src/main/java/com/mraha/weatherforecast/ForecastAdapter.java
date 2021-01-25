package com.mraha.weatherforecast;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.mraha.weatherforecast.database.AppData;
import com.mraha.weatherforecast.pojo.CurrentMain;
import com.mraha.weatherforecast.util.LocationHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.MViewHolder> {
    private Context context;

    List<AppData> appDataList;

    public ForecastAdapter(Context context, List<AppData> appDataList) {
        this.context = context;
        this.appDataList = appDataList;
    }

    @NonNull
    @Override
    public MViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_view, parent, false);
        return new MViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MViewHolder holder, int position) {
        CurrentMain currentMain=new Gson().fromJson(appDataList.get(position).data,CurrentMain.class);

        holder.textTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openInfoDialog(currentMain.toString(),position);
            }
        });
        holder.textTv.setText("id: "+appDataList.get(position).uid+"\ntime: " + findTime(currentMain.getDt())+" "
                + LocationHelper.generateAddressFromCoords(context, currentMain.getCoord().getLat(),
                currentMain.getCoord().getLon()));


    }

    private void openInfoDialog(String info,int position) {
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setMessage("id: "+appDataList.get(position).uid+"\n"+info).create().show();
    }

    @Override
    public int getItemCount() {
        return appDataList.size();
    }

    public class MViewHolder extends RecyclerView.ViewHolder {
        public TextView textTv;

        public MViewHolder(@NonNull View itemView) {
            super(itemView);
            textTv = itemView.findViewById(R.id.item_view_text);
        }
    }
    public String findTime(long time){
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss.SSS");
        return formatter.format( new Date(time*1000));

    }
}
