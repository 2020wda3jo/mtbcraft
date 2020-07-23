package com.capston.mtbcraft.Recycler.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.capston.mtbcraft.Activity.Course.CourseDetail;
import com.capston.mtbcraft.R;
import com.capston.mtbcraft.dto.RidingRecord;

import java.util.ArrayList;

public class CourseAdapter extends RecyclerView.Adapter<CourseHolder> {

    public Context mContext;
    public ArrayList<RidingRecord> itemList;
    RidingRecord item;
    public int count = 5;

    public CourseAdapter(Context mContext, ArrayList<RidingRecord> itemList) {
        this.mContext = mContext;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public CourseHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.course_item , viewGroup,false);

        return new CourseHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final CourseHolder testViewHolder, final int position) {

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
        String hour_s = String.valueOf(hour);
        int des = (itemList.get(position).getRr_distance());
        float km = (float) (des/1000.0);
        String total = String.valueOf(km)+"Km";
        testViewHolder.c_rider_name.setText(itemList.get(position).getRr_rider());
        testViewHolder.c_name.setText(itemList.get(position).getRr_name());
        testViewHolder.c_time.setText(hour_s+":"+min+":"+ sec);
        testViewHolder.c_avg.setText(String.valueOf(itemList.get(position).getRr_avgspeed())+"km");
        testViewHolder.c_getgodo.setText(String.valueOf(itemList.get(position).getRr_high())+"m");
        testViewHolder.c_dis.setText(total);
        testViewHolder.c_date.setText(itemList.get(position).getRr_date());
        testViewHolder.like_count.setText(String.valueOf(itemList.get(position).getRr_like()));
        testViewHolder.mView.setOnClickListener(v -> {
            Context context = v.getContext();
            Toast.makeText(context, position +"번째 아이템 클릭"+itemList.get(position).getRr_num(),  Toast.LENGTH_LONG).show();
            Intent intent = new Intent(v.getContext(), CourseDetail.class);
            intent.putExtra("c_num",String.valueOf(itemList.get(position).getRr_num()));
            intent.putExtra("c_distance",itemList.get(position).getRr_distance());
            intent.putExtra("c_area",itemList.get(position).getRr_area());
            intent.putExtra("c_gpx",itemList.get(position).getRr_gpx());
            intent.putExtra("c_name",itemList.get(position).getRr_name());
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
              v.getContext().startActivity(intent);

        });
    }

    @Override
    public int getItemCount() {
            return count;
    }
}