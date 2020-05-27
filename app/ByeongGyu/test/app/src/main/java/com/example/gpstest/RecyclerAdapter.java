package com.example.gpstest;

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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyRecordHolder>{

    public Context mContext;
    public ArrayList<RidingRecord> itemList;
    RidingRecord item;

    public RecyclerAdapter(Context mContext, ArrayList<RidingRecord> itemList) {
        this.mContext = mContext;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public MyRecordHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
       // View baseView = View.inflate(mContext, R.layout.activity_myreportitem, null);
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_myreportitem , viewGroup,false);

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
        String min_s = String.valueOf(min);



        SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String aaa = itemList.get(position).getRr_date();
        Date to;
        try {
            to = transFormat.parse(aaa);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        testViewHolder.textView1.setText(itemList.get(position).getRr_date());
        testViewHolder.textView2.setText(hour_s+":"+min+":"+ sec);
        testViewHolder.textView3.setText(total);
        //testViewHolder.textView4.setText(itemList.get(position).getRr_num());

        testViewHolder.mView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Toast.makeText(context, position +"번째 아이템 클릭"+itemList.get(position).getRr_num(),  Toast.LENGTH_LONG).show();
                Intent intent = new Intent(v.getContext(), DetailActivity.class);
                intent.putExtra("rr_num",itemList.get(position).getRr_num());
                intent.putExtra("rr_rider",itemList.get(position).getRr_rider());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                  v.getContext().startActivity(intent);

            }
        });
    }


    class MyRecordHolder extends RecyclerView.ViewHolder {
        public TextView textView1, textView2, textView3, textView4;
        public ImageView imageView;
        public LinearLayout viewClick;
        public RidingRecord data;
        public final View mView;

        public MyRecordHolder( View itemView) {
            super(itemView);
            mView = itemView;
            textView1 = itemView.findViewById(R.id.textView1);
            textView2 = itemView.findViewById(R.id.textView2);
            textView3 = itemView.findViewById(R.id.textView3);
            imageView = itemView.findViewById(R.id.imageView);
            viewClick = itemView.findViewById(R.id.viewClick);

        }

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}