package com.capston.mtbcraft.Recycler.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.capston.mtbcraft.R;
import com.capston.mtbcraft.dto.MissionRanking;

import java.io.File;
import java.util.ArrayList;

public class MissionRankingAdapter extends RecyclerView.Adapter<MissionRankingAdapter.MissionRankingHolder> {
    public Context mContext;
    public ArrayList<MissionRanking> itemList;
    String Save_Path;
    int allCount;

    public MissionRankingAdapter(Context mContext, ArrayList<MissionRanking> itemList, String save_path, int allCount) {
        this.mContext = mContext;
        this.itemList = itemList;
        this.Save_Path = save_path;
        this.allCount = allCount;
    }

    @NonNull
    @Override
    public MissionRankingHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_missionrankingitem , viewGroup,false);

        return new MissionRankingHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MissionRankingHolder holder, int position) {
        Bitmap myBitmap;
        //Bitmap myBitmap = BitmapFactory.decodeFile(new File(Save_Path + "/" + File_Name).getAbsolutePath());
        myBitmap = BitmapFactory.decodeFile(String.valueOf(new File(Save_Path + "/" + itemList.get(position).getR_image())));
        if ( itemList.get(position).getR_image() == null)
            myBitmap = BitmapFactory.decodeFile(String.valueOf(new File(Save_Path + "/" + "noImage.jpg")));

        if ( itemList.get(position).getR_image() == null)
            Log.e("이미지", "널");
        else
            Log.e("이미지", itemList.get(position).getR_image());

        //holder.imageView.setImageBitmap(myBitmap);
        holder.textView1.setText(itemList.get(position).getR_nickname());
        holder.textView2.setText(itemList.get(position).getHow() + " / " + allCount);
        holder.textView3.setText( ( position+1 ) + "위" );
        holder.imageView.setImageBitmap(myBitmap);

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    class MissionRankingHolder extends RecyclerView.ViewHolder {
        public TextView textView1, textView2, textView3;
        public ImageView imageView;
        public LinearLayout viewClick;
        public final View mView;

        public MissionRankingHolder( View itemView) {
            super(itemView);
            mView = itemView;
            textView1 = itemView.findViewById(R.id.textView11);
            textView2 = itemView.findViewById(R.id.textView13);
            textView3 = itemView.findViewById(R.id.textView16);
            imageView = itemView.findViewById(R.id.imageView7);
            viewClick = itemView.findViewById(R.id.viewClick);
        }
    }
}