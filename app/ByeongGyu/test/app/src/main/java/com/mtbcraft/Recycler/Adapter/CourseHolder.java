package com.mtbcraft.Recycler.Adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.gpstest.R;

import net.daum.mf.map.api.MapView;

public class CourseHolder extends RecyclerView.ViewHolder{

    public TextView textView1, textView2, textView3;
    public ImageView imageView;
    public LinearLayout viewClick;
    public View mView;


    public CourseHolder(MapView mapView){
        super(mapView);

    }
    public CourseHolder(View itemView) {
        super(itemView);

        mView = itemView;
        textView1 = itemView.findViewById(R.id.textView1);
        textView2 = itemView.findViewById(R.id.textView2);
        textView3 = itemView.findViewById(R.id.textView3);
        imageView = itemView.findViewById(R.id.imageView);
        viewClick = itemView.findViewById(R.id.viewClick);

    }
}
