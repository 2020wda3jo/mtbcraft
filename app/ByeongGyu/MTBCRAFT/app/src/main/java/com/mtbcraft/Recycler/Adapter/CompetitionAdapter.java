package com.mtbcraft.Recycler.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.capston.mtbcraft.R;
import com.mtbcraft.Activity.Competition.CompetitionDetail;
import com.mtbcraft.dto.Competition;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CompetitionAdapter extends RecyclerView.Adapter<CompetitionAdapter.CompetitionHolder> {
    public Context mContext;
    public ArrayList<Competition> itemList;
    Competition item;
    String Save_Path;
    int count = 0;

    public CompetitionAdapter(Context mContext, ArrayList<Competition> itemList, String Save_Path) {
        this.mContext = mContext;
        this.Save_Path = Save_Path;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public CompetitionHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_competitionitem , viewGroup,false);

        return new CompetitionHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CompetitionHolder holder, int position) {
        String fullDay = itemList.get(position).getComp_period();
        String startDay = fullDay.substring(0,4) + "-" + fullDay.substring(4,6) + "-" + fullDay.substring(6,8);
        String endDay = fullDay.substring(8,12) + "-" + fullDay.substring(12,14) + "-" + fullDay.substring(14,16);
        String day = startDay + " ~ " + endDay;
        String File_Name = itemList.get(position).getComp_image();
        Bitmap myBitmap = BitmapFactory.decodeFile(new File(Save_Path + "/" + File_Name).getAbsolutePath());

        long howDay = 0;
        try {
            howDay = getDate(fullDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if ( howDay < 0)
            holder.textView2.setText("D - " + Math.abs(howDay));
        else
            holder.textView2.setText(day);


        holder.textView1.setText(itemList.get(position).getComp_name());
        holder.imageView.setImageBitmap(myBitmap);

        holder.mView.setOnClickListener( v -> {
            Context context = v.getContext();
            Intent intent = new Intent(v.getContext(), CompetitionDetail.class);
            intent.putExtra("comp_num",String.valueOf(itemList.get(position).getComp_num()));
            intent.putExtra("comp_period",day);
            intent.putExtra("comp_badge",String.valueOf(itemList.get(position).getComp_badge()));
            intent.putExtra("comp_course",String.valueOf(itemList.get(position).getComp_course()));
            intent.putExtra("comp_image",itemList.get(position).getComp_image());
            intent.putExtra("comp_content",itemList.get(position).getComp_content());
            intent.putExtra("comp_name",itemList.get(position).getComp_name());
            intent.putExtra("save_path",Save_Path);
            intent.putExtra("c_gpx",itemList.get(position).getC_gpx());
            intent.putExtra("c_name",itemList.get(position).getC_name());
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            v.getContext().startActivity(intent);

        });


    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    class CompetitionHolder extends RecyclerView.ViewHolder {
        public TextView textView1, textView2;
        public ImageView imageView;
        public LinearLayout viewClick;
        public final View mView;

        public CompetitionHolder( View itemView) {
            super(itemView);
            mView = itemView;
            textView1 = itemView.findViewById(R.id.comp_name);
            textView2 = itemView.findViewById(R.id.comp_day);
            imageView = itemView.findViewById(R.id.imageView);
            viewClick = itemView.findViewById(R.id.viewClick);
        }
    }

    public long getDate(String getPeriod) throws ParseException {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyyMMdd");
        String temp = sdfNow.format(date);
        Date nowTime = sdfNow.parse(temp);
        Date periodTime = sdfNow.parse(String.valueOf(getPeriod.substring(8,16)));

/*            SimpleDateFormat sdfNow2 = new SimpleDateFormat("yyyyMMdd");
            int day1 = Integer.parseInt(sdfNow2.format(date));
            int day2 =  Integer.parseInt(getPeriod.substring(8,16));*/

        return (nowTime.getTime() - periodTime.getTime()) / (24*60*60*1000);

    }
}
