package com.mtbcraft.Recycler.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gpstest.R;
import com.mtbcraft.Activity.Riding.RidingRecordAll_Detail;
import com.mtbcraft.dto.RidingRecord;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RidingRecordListAdapter extends RecyclerView.Adapter<RidingRecordListAdapter.MyRecordHolder>{

    public Context mContext;
    public ArrayList<RidingRecord> itemList;


    public RidingRecordListAdapter(Context mContext, ArrayList<RidingRecord> itemList) {
        this.mContext = mContext;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public MyRecordHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.ridingrecorall_listitem , viewGroup,false);

        return new MyRecordHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyRecordHolder testViewHolder, final int position) {
        //item = itemList.get(position);
        int hour;
        int min;
        int sec;
        sec = Integer.parseInt(itemList.get(position).getRr_time());

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

        int des = Integer.parseInt(itemList.get(position).getRr_distance());
        float km = (float) (des/1000.0);
        String total = String.valueOf(km)+"Km";

        String hour_s = String.valueOf(hour);

        SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String aaa = itemList.get(position).getRr_date();
        Date to;
        try {
            to = transFormat.parse(aaa);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        testViewHolder.riding_date.setText(itemList.get(position).getRr_date());
        testViewHolder.rider_name.setText(itemList.get(position).getRr_rider());
        testViewHolder.riding_time.setText(hour_s+":"+min+":"+ sec);
        testViewHolder.riding_distance.setText(total);

        testViewHolder.mView.setOnClickListener(v -> {
            Context context = v.getContext();
            Toast.makeText(context, position +"번째 아이템 클릭"+itemList.get(position).getRr_num(),  Toast.LENGTH_LONG).show();
            Intent intent = new Intent(v.getContext(), RidingRecordAll_Detail.class);
            intent.putExtra("rr_num",itemList.get(position).getRr_num());
            intent.putExtra("rr_gpx",itemList.get(position).getRr_gpx());
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
              v.getContext().startActivity(intent);
        });
    }


    class MyRecordHolder extends RecyclerView.ViewHolder {
        public TextView riding_date, rider_name, riding_time, riding_distance;
        public ImageView imageView;
        public LinearLayout viewClick;
        public final View mView;
        public MyRecordHolder( View itemView) {
            super(itemView);
            mView = itemView;
            riding_date = itemView.findViewById(R.id.riding_date);
            rider_name = itemView.findViewById(R.id.rider_name);
            riding_time = itemView.findViewById(R.id.riding_time);
            riding_distance = itemView.findViewById(R.id.riding_distance);
            imageView = itemView.findViewById(R.id.imageView);
            viewClick = itemView.findViewById(R.id.viewClick);

        }

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}