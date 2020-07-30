package com.capston.mtbcraft.Recycler.Adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.capston.mtbcraft.R;

public class ScrapHolder extends RecyclerView.ViewHolder{
    public ImageView my_image;
    public LinearLayout viewClick;
    public final View mView;
    public TextView  record_name, record_date, record_adress, my_dis, my_get, my_time;

    public ScrapHolder(View itemView){
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
