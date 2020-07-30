package com.capston.mtbcraft.Recycler.Adapter;

import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.capston.mtbcraft.R;
public class CourseHolder extends RecyclerView.ViewHolder {

    public TextView c_rider_name, c_name, c_time, c_avg, c_getgodo, c_dis, c_date, like_count;
    public ImageView likebt, imageView;
    public LinearLayout viewClick;
    public View mView;
    public WebView webview;


    public CourseHolder(View itemView) {
        super(itemView);

        mView = itemView;
        c_rider_name = itemView.findViewById(R.id.c_rider_name);
        c_name = itemView.findViewById(R.id.c_name);
        c_time = itemView.findViewById(R.id.c_time);
        c_avg = itemView.findViewById(R.id.c_avg);
        c_getgodo = itemView.findViewById(R.id.c_getgodo);
        c_dis = itemView.findViewById(R.id.c_dis);
        c_date = itemView.findViewById(R.id.c_date);
        imageView = itemView.findViewById(R.id.userimg);
        likebt = itemView.findViewById(R.id.likeimg);
        like_count = itemView.findViewById(R.id.like_count);
        viewClick = itemView.findViewById(R.id.viewClick);
        webview = itemView.findViewById(R.id.course_view);


    }
}
