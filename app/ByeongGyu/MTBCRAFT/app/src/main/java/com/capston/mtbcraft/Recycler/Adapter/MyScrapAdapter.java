package com.capston.mtbcraft.Recycler.Adapter;

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

import com.capston.mtbcraft.Activity.Scrap.MyScrapDetail;
import com.capston.mtbcraft.R;
import com.capston.mtbcraft.dto.ScrapStatus;

import java.util.ArrayList;

public class MyScrapAdapter extends RecyclerView.Adapter<MyScrapAdapter.MyScrapHolder>{

    public Context mContext;
    public ArrayList<ScrapStatus> itemList;

    public MyScrapAdapter(Context mContext, ArrayList<ScrapStatus> itemList) {
        this.mContext = mContext;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public MyScrapHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.course_item , viewGroup,false);

        return new MyScrapHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyScrapHolder testViewHolder, final int position) {
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
        testViewHolder.c_name.setText(String.valueOf(itemList.get(position).getRr_name()));
        testViewHolder.c_time.setText(hour_s+":"+min+":"+ sec);
        testViewHolder.c_avg.setText(String.valueOf(itemList.get(position).getRr_avgspeed())+"km");
        testViewHolder.c_getgodo.setText(String.valueOf(itemList.get(position).getRr_high())+"m");
        testViewHolder.c_distance.setText(total);

        testViewHolder.mView.setOnClickListener(v -> {
            Context context = v.getContext();
            Toast.makeText(context, position +"번째 아이템 클릭"+itemList.get(position).getSs_rnum(),  Toast.LENGTH_LONG).show();
            Intent intent = new Intent(v.getContext(), MyScrapDetail.class);
            intent.putExtra("ss_rider",itemList.get(position).getSs_rider());
            intent.putExtra("c_num",String.valueOf(itemList.get(position).getSs_rnum()));
            intent.putExtra("c_distance",itemList.get(position).getRr_distance());
            intent.putExtra("c_area",itemList.get(position).getRr_area());
            intent.putExtra("c_gpx",itemList.get(position).getRr_gpx());
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            v.getContext().startActivity(intent);

        });
    }


    class MyScrapHolder extends RecyclerView.ViewHolder {
        public TextView c_rider_name, c_name, c_time, c_avg, c_getgodo, c_distance;
        public ImageView imageView;
        public LinearLayout viewClick;
        public final View mView;

        public MyScrapHolder( View itemView) {
            super(itemView);
            mView = itemView;
            c_rider_name = itemView.findViewById(R.id.c_rider_name);
            c_name = itemView.findViewById(R.id.c_name);
            c_time = itemView.findViewById(R.id.c_time);
            c_avg = itemView.findViewById(R.id.c_avg);
            c_getgodo = itemView.findViewById(R.id.c_getgodo);
            c_distance = itemView.findViewById(R.id.c_dis);
            viewClick = itemView.findViewById(R.id.viewClick);

        }

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}