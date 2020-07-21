package com.capston.mtbcraft.Recycler.Adapter;

import android.content.Context;
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
import com.capston.mtbcraft.dto.CompScore;

import java.io.File;
import java.util.ArrayList;

public class CompScoreAdapter extends RecyclerView.Adapter<CompScoreAdapter.CompClubHolder> {
    public Context mContext;
    public ArrayList<CompScore> itemList;
    String Save_Path;

    public CompScoreAdapter(Context mContext, ArrayList<CompScore> itemList, String save_path) {
        this.mContext = mContext;
        this.itemList = itemList;
        this.Save_Path = save_path;
    }

    @NonNull
    @Override
    public CompClubHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_competitionclubitem , viewGroup,false);

        return new CompClubHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CompClubHolder holder, int position) {

        String name = itemList.get(position).getR_nickname();
        int score = itemList.get(position).getRr_avgspeed();
        String mem_image = itemList.get(position).getR_image();

        Bitmap myBitmap = BitmapFactory.decodeFile(new File(Save_Path + "/" + mem_image).getAbsolutePath());

        //holder.imageView.setImageBitmap(myBitmap);
        holder.textView1.setText((position+1) + "등");
        holder.textView2.setText(name);
        holder.textView3.setText(String.valueOf(score));
        holder.imageView.setImageBitmap(myBitmap);

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    class CompClubHolder extends RecyclerView.ViewHolder {
        public TextView textView1, textView2, textView3;
        public ImageView imageView;
        public LinearLayout viewClick;
        public final View mView;

        public CompClubHolder( View itemView) {
            super(itemView);
            mView = itemView;
            textView1 = itemView.findViewById(R.id.textView10);
            textView2 = itemView.findViewById(R.id.textView11);
            textView3 = itemView.findViewById(R.id.textView12);
            imageView = itemView.findViewById(R.id.imageView7);
            viewClick = itemView.findViewById(R.id.viewClick);
        }
    }
}