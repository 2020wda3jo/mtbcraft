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
import com.mtbcraft.Activity.Scrap.MyScrapDetail;
import com.mtbcraft.dto.ScrapStatus;

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
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_couseitem , viewGroup,false);

        return new MyScrapHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyScrapHolder testViewHolder, final int position) {

        testViewHolder.c_rider_name.setText(itemList.get(position).getC_area());
        testViewHolder.c_name.setText(String.valueOf(itemList.get(position).getC_distance()));
        testViewHolder.c_time.setText(String.valueOf(itemList.get(position).getC_level()));

        testViewHolder.mView.setOnClickListener(v -> {
            Context context = v.getContext();
            Toast.makeText(context, position +"번째 아이템 클릭"+itemList.get(position).getC_num(),  Toast.LENGTH_LONG).show();
            Intent intent = new Intent(v.getContext(), MyScrapDetail.class);
            intent.putExtra("ss_rider",itemList.get(position).getSs_rider());
            intent.putExtra("c_num",String.valueOf(itemList.get(position).getC_num()));
            intent.putExtra("c_distance",itemList.get(position).getC_distance());
            intent.putExtra("c_level",itemList.get(position).getC_level());
            intent.putExtra("c_area",itemList.get(position).getC_area());
            intent.putExtra("c_gpx",itemList.get(position).getC_gpx());
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            v.getContext().startActivity(intent);

        });
    }


    class MyScrapHolder extends RecyclerView.ViewHolder {
        public TextView c_rider_name, c_name, c_time, c_avg, c_getgodo;
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
            imageView = itemView.findViewById(R.id.imageView);
            viewClick = itemView.findViewById(R.id.viewClick);

        }

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}