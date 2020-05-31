package com.mtbcraft.Recycler;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gpstest.R;
import com.mtbcraft.dto.RidingRecord;

public class MyRecordHolder extends RecyclerView.ViewHolder {
    public TextView textView1, textView2, textView3, textView4;
    public ImageView imageView;
    public LinearLayout viewClick;
    public RidingRecord data;
    public MyRecordHolder(@NonNull View itemView) {
        super(itemView);

        textView1 = itemView.findViewById(R.id.textView1);
        textView2 = itemView.findViewById(R.id.textView2);
        textView3 = itemView.findViewById(R.id.textView3);
        imageView = itemView.findViewById(R.id.imageView);
        viewClick = itemView.findViewById(R.id.viewClick);

    }



}
