package com.capston.mtbcraft.Recycler.Adapter;

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

import com.capston.mtbcraft.Activity.Mission.MissionDetail;
import com.capston.mtbcraft.R;
import com.capston.mtbcraft.dto.Mission;

import java.io.File;
import java.util.ArrayList;

public class MissionAdapter extends RecyclerView.Adapter<MissionAdapter.MissionHolder> {
    public Context mContext;
    public ArrayList<Mission> itemList;
    Mission item;
    String Save_Path;
    int count = 0;

    public MissionAdapter(Context mContext, ArrayList<Mission> itemList, String Save_Path) {
        this.mContext = mContext;
        this.Save_Path = Save_Path;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public MissionHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_missionitem , viewGroup,false);

        return new MissionHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MissionHolder holder, int position) {

        Bitmap myBitmap = BitmapFactory.decodeFile(new File(Save_Path + "/" + itemList.get(position).getBg_image()).getAbsolutePath());
        holder.imageView.setImageBitmap(myBitmap);
        holder.textView1.setText(itemList.get(position).getM_name());

        holder.mView.setOnClickListener( v -> {
            Context context = v.getContext();
            Intent intent = new Intent(v.getContext(), MissionDetail.class);
            intent.putExtra("bg_image", itemList.get(position).getBg_image());
            intent.putExtra("m_badge", itemList.get(position).getM_badge());
            intent.putExtra("m_content", itemList.get(position).getM_content());
            intent.putExtra("m_name", itemList.get(position).getM_name());
            intent.putExtra("m_type", itemList.get(position).getM_type());
            intent.putExtra("ms_score", itemList.get(position).getMs_score());
            intent.putExtra("m_num", itemList.get(position).getM_num());
            intent.putExtra("bg_name", itemList.get(position).getBg_name());
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            v.getContext().startActivity(intent);

        });


    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    class MissionHolder extends RecyclerView.ViewHolder {
        public TextView textView1;
        public ImageView imageView;
        public LinearLayout viewClick;
        public final View mView;

        public MissionHolder( View itemView) {
            super(itemView);
            mView = itemView;
            textView1 = itemView.findViewById(R.id.textView11);
            imageView = itemView.findViewById(R.id.imageView7);
            viewClick = itemView.findViewById(R.id.viewClick);
        }
    }

}
