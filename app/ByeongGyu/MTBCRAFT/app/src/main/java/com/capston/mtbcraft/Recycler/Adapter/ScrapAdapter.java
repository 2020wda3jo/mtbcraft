package com.capston.mtbcraft.Recycler.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.capston.mtbcraft.Activity.Riding.DetailActivity;
import com.capston.mtbcraft.R;
import com.capston.mtbcraft.dto.RidingRecord;
import com.capston.mtbcraft.dto.ScrapStatus;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ScrapAdapter extends RecyclerView.Adapter<ScrapAdapter.MyRecordHolder>{

    public Context mContext;
    public ArrayList<ScrapStatus> itemList;


    public ScrapAdapter(Context mContext, ArrayList<ScrapStatus> itemList) {
        this.mContext = mContext;
        this.itemList = itemList;
    }

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
        switch(itemList.get(position).getRr_rider()){
            case "1401287":
                testViewHolder.my_image.setImageDrawable(mContext.getResources().getDrawable(R.drawable.peo1));
                break;
            case "2병규":
                testViewHolder.my_image.setImageDrawable(mContext.getResources().getDrawable(R.drawable.peo2));
                break;
            case "괴물쥐":
                testViewHolder.my_image.setImageDrawable(mContext.getResources().getDrawable(R.drawable.peo3));
                break;
            default:
                testViewHolder.my_image.setImageDrawable(mContext.getResources().getDrawable(R.drawable.common_google_signin_btn_icon_light));
                break;
        }




        testViewHolder.record_name.setText(itemList.get(position).getRr_name());
        testViewHolder.record_date.setText(itemList.get(position).getRr_date());
        testViewHolder.record_adress.setText(itemList.get(position).getRr_area());
        testViewHolder.my_dis.setText(total);
        testViewHolder.my_get.setText(String.valueOf(itemList.get(position).getRr_high())+"m");
        testViewHolder.my_time.setText(hour_s+"시간 "+min+"분 "+ sec+"초");


        testViewHolder.mView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), DetailActivity.class);
            intent.putExtra("rr_num",itemList.get(position).getSs_rnum());
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            v.getContext().startActivity(intent);
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