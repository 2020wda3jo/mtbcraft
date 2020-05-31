package com.mtbcraft.Recycler;

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
import com.mtbcraft.Activity.CourseDetail;
import com.mtbcraft.dto.Course;

import java.util.ArrayList;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseHolder>{

    public Context mContext;
    public ArrayList<Course> itemList;
    Course item;

    public CourseAdapter(Context mContext, ArrayList<Course> itemList) {
        this.mContext = mContext;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public CourseHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_couseitem , viewGroup,false);

        return new CourseHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final CourseHolder testViewHolder, final int position) {

        testViewHolder.textView1.setText(itemList.get(position).getC_area());
        testViewHolder.textView2.setText(String.valueOf(itemList.get(position).getC_distance()));
        testViewHolder.textView3.setText(String.valueOf(itemList.get(position).getC_level()));

        testViewHolder.mView.setOnClickListener(v -> {
            Context context = v.getContext();
            Toast.makeText(context, position +"번째 아이템 클릭"+itemList.get(position).getC_num(),  Toast.LENGTH_LONG).show();
            Intent intent = new Intent(v.getContext(), CourseDetail.class);
            intent.putExtra("c_num",String.valueOf(itemList.get(position).getC_num()));
            intent.putExtra("c_distance",itemList.get(position).getC_distance());
            intent.putExtra("c_level",itemList.get(position).getC_level());
            intent.putExtra("c_area",itemList.get(position).getC_area());
            intent.putExtra("c_gpx",itemList.get(position).getC_gpx());
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
              v.getContext().startActivity(intent);

        });
    }


    class CourseHolder extends RecyclerView.ViewHolder {
        public TextView textView1, textView2, textView3;
        public ImageView imageView;
        public LinearLayout viewClick;
        public final View mView;

        public CourseHolder( View itemView) {
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