package com.example.testapplication.ui.recycler;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testapplication.R;
import com.example.testapplication.dto.RidingRecord;
import com.example.testapplication.ui.records.DetailActivity;
import com.example.testapplication.ui.records.MyDetailFragment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Callback;

public class MyReportAdapter extends RecyclerView.Adapter<MyReportAdapter.MyRecordHolder>{
    public interface OnItemClick{
        void onItemClick(int position, RidingRecord record);
    }

    private List<RidingRecord> record;
    private OnItemClick listener;
    public MyReportAdapter(OnItemClick listener) {
        this.listener=listener;
    }

    public void setData(List<RidingRecord> data) { this.record=data; notifyDataSetChanged();}

    public void setData(ArrayList<RidingRecord> record) { this.record=record; notifyDataSetChanged();}

    @NonNull
    @Override
    public MyRecordHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.myreport_item , viewGroup,false);

        return new MyRecordHolder(v);
    }

    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(@NonNull final MyRecordHolder testViewHolder, final int position) {
        //item = itemList.get(position);
        int hour;
        int min;
        int sec;
        sec = (itemList.get(position).getRr_time());

        min = sec/60;
        hour = min/60;
        sec = sec % 60;
        min = min % 60;
        if(hour == 0){
            hour=00;
        }
        if(min==0){
            min=00;
        }

        int des = (itemList.get(position).getRr_distance());
        float km = (float) (des/1000.0);
        String total = String.valueOf(km)+"Km";

        String hour_s = String.valueOf(hour);

        SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Log.d("dfdf",itemList.get(position).getRr_rider());

        Bitmap user_image = BitmapFactory.decodeFile(new File(mContext.getFilesDir().getPath() + "/" + r_image).getAbsolutePath());
        testViewHolder.my_image.setImageBitmap(user_image);




        testViewHolder.record_name.setText(itemList.get(position).getRr_name());
        testViewHolder.record_date.setText(itemList.get(position).getRr_date());
        testViewHolder.record_adress.setText(itemList.get(position).getRr_area());
        testViewHolder.my_dis.setText(total);
        testViewHolder.my_get.setText(String.valueOf(itemList.get(position).getRr_high())+"m");
        testViewHolder.my_time.setText(hour_s+"시간 "+min+"분 "+ sec+"초");


        testViewHolder.mView.setOnClickListener(v -> {
            RidingRecord record = itemList.get(position);
            listener.onItemClick(position, record);
        });
    }


    class MyRecordHolder extends RecyclerView.ViewHolder {
        public TextView record_name, record_date,record_adress, my_dis, my_get, my_time;
        public ImageView my_image;
        public LinearLayout viewClick;
        public final View mView;
        public MyRecordHolder( View itemView) {
            super(itemView);
            mView = itemView;
            record_name = itemView.findViewById(R.id.record_name);
            record_date = itemView.findViewById(R.id.record_date);
            record_adress = itemView.findViewById(R.id.record_adress);
            my_dis = itemView.findViewById(R.id.my_dis);
            my_get = itemView.findViewById(R.id.my_get);
            my_time = itemView.findViewById(R.id.my_time);
            my_image = itemView.findViewById(R.id.myimage);
            viewClick = itemView.findViewById(R.id.viewClick);
        }

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}