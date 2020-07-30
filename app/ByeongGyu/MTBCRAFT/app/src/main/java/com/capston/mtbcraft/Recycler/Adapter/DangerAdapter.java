package com.capston.mtbcraft.Recycler.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.capston.mtbcraft.R;
import com.capston.mtbcraft.dto.DangerousArea;

import java.util.ArrayList;

public class DangerAdapter extends RecyclerView.Adapter<DangerAdapter.DangerHolder> {
    public Context mContext;
    public ArrayList<DangerousArea> itemList;

    public DangerAdapter(Context mContext, ArrayList<DangerousArea> itemList) {
        this.mContext = mContext;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public DangerHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_dangeritem , viewGroup,false);

        return new DangerHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DangerHolder holder, int position) {
        String status ="";

        //holder.imageView.setImageBitmap(myBitmap);
        holder.textView1.setText(itemList.get(position).getDa_addr());
        holder.textView2.setText(itemList.get(position).getDa_content());
        if ( itemList.get(position).getDa_status().equals("0"))
            status = "수락";
        if ( itemList.get(position).getDa_status().equals("1"))
            status = "신청중";
        if ( itemList.get(position).getDa_status().equals("2"))
            status = "거절";
        holder.textView3.setText(status);

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    class DangerHolder extends RecyclerView.ViewHolder {
        public TextView textView1, textView2, textView3;
        public LinearLayout viewClick;
        public final View mView;

        public DangerHolder( View itemView) {
            super(itemView);
            mView = itemView;
            textView1 = itemView.findViewById(R.id.textView32);
            textView2 = itemView.findViewById(R.id.textView34);
            textView3 = itemView.findViewById(R.id.textView35);
            viewClick = itemView.findViewById(R.id.viewClick);
        }
    }
}