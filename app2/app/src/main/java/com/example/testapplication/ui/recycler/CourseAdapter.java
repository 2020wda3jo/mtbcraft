package com.example.testapplication.ui.recycler;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testapplication.MainViewModel;
import com.example.testapplication.R;
import com.example.testapplication.dto.RidingRecord;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

import java.io.File;
import java.util.ArrayList;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseHolder> implements OnMapReadyCallback {

    public Context mContext;
    public ArrayList<RidingRecord> itemList;
    public int count = 5;
    private String r_image;
    private NavController controller;
    public MainViewModel model;


    public CourseAdapter(Context mContext, ArrayList<RidingRecord> itemList, String r_image, NavController controller, MainViewModel model) {
        this.mContext = mContext;
        this.itemList = itemList;
        this.r_image = r_image;
        this.controller = controller;
        this.model = model;
    }

    @NonNull
    @Override
    public CourseHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.course_item , viewGroup,false);

        return new CourseHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final CourseHolder testViewHolder, final int position) {


        int hour;
        int min;
        int sec;
        sec = (itemList.get(position).getRr_time());

        min = sec/60;
        hour = min/60;
        sec = sec % 60;
        min = min % 60;
        if(hour == 0){
            hour=00;
        }
        if(min==0){
            min=00;
        }
        String hour_s = String.valueOf(hour);
        int des = (itemList.get(position).getRr_distance());
        float km = (float) (des/1000.0);
        String total = String.valueOf(km)+"Km";

        Bitmap user_image = BitmapFactory.decodeFile(new File(mContext.getFilesDir().getPath() + "/" + r_image).getAbsolutePath());
        testViewHolder.imageView.setImageBitmap(user_image);

        testViewHolder.c_rider_name.setText(itemList.get(position).getRr_rider());
        testViewHolder.c_name.setText(itemList.get(position).getRr_name());
        testViewHolder.c_time.setText(hour_s+":"+min+":"+ sec);
        testViewHolder.c_avg.setText(String.valueOf(itemList.get(position).getRr_avgspeed())+"km");
        testViewHolder.c_getgodo.setText(String.valueOf(itemList.get(position).getRr_high())+"m");
        testViewHolder.c_dis.setText(total);
        testViewHolder.c_date.setText(itemList.get(position).getRr_date());
        testViewHolder.like_count.setText(String.valueOf(itemList.get(position).getRr_like()));
        testViewHolder.webview.loadUrl("http://13.209.229.237:8080/app/riding/course_view/"+itemList.get(position).getRr_num());
        testViewHolder.webview.getSettings().setJavaScriptEnabled(true);
        testViewHolder.webview.getSettings().setLoadWithOverviewMode(true);
        testViewHolder.webview.getSettings().setUseWideViewPort(true);
        testViewHolder.webview.getSettings().setSupportZoom(true);
        testViewHolder.webview.getSettings().setBuiltInZoomControls(true);
        testViewHolder.webview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        testViewHolder.webview.getSettings().setDomStorageEnabled(true);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            testViewHolder.webview.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }else{
            testViewHolder.webview.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        testViewHolder.mView.setOnClickListener(v -> {

            controller.navigate(R.id.action_nav_course_view_to_nav_course_detail);

            model.my_rec_time.setValue(String.valueOf(itemList.get(position).getRr_time()));
            model.my_rec_rest.setValue(String.valueOf(itemList.get(position).getRr_breaktime()));
            model.my_rec_dis.setValue(String.valueOf(itemList.get(position).getRr_distance()));
            model.my_rec_max.setValue(String.valueOf(itemList.get(position).getRr_topspeed()));
            model.my_rec_avg.setValue(String.valueOf(itemList.get(position).getRr_avgspeed()));
            model.my_rec_get.setValue(String.valueOf(itemList.get(position).getRr_high()));
            model.my_rec_adress.setValue(String.valueOf(itemList.get(position).getRr_area()));
            model.my_rec_name.setValue(String.valueOf(itemList.get(position).getRr_name()));
            model.CourseRider.setValue(String.valueOf(itemList.get(position).getRr_rider()));
            model.my_rec_date.setValue(String.valueOf(itemList.get(position).getRr_date()));
            model.like_count.setValue(itemList.get(position).getRr_like());
            model.my_rec_gpx.setValue(itemList.get(position).getRr_gpx());
            model.CourseR_image.setValue(itemList.get(position).getR_image());
        });



        /*testViewHolder.mView.setOnClickListener(v -> {
            Context context = v.getContext();
            Intent intent = new Intent(v.getContext(), CourseDetail.class);
            intent.putExtra("c_num",String.valueOf(itemList.get(position).getRr_num()));
            intent.putExtra("c_distance",itemList.get(position).getRr_distance());
            intent.putExtra("c_area",itemList.get(position).getRr_area());
            intent.putExtra("c_gpx",itemList.get(position).getRr_gpx());
            intent.putExtra("c_name",itemList.get(position).getRr_name());
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            v.getContext().startActivity(intent);

        });*/
    }

    @Override
    public int getItemCount() {
        return count;
    }

    class CourseHolder extends RecyclerView.ViewHolder {
        public TextView textView1, textView2, textView3, c_rider_name, c_name, c_time, c_avg, c_getgodo, c_dis, c_date, like_count;
        public ImageView imageView;
        public LinearLayout viewClick;
        public final View mView;
        public WebView webview;

        public CourseHolder( View itemView) {
            super(itemView);
            mView = itemView;
            textView1 = itemView.findViewById(R.id.textView10);
            textView2 = itemView.findViewById(R.id.textView11);
            textView3 = itemView.findViewById(R.id.textView12);
            imageView = itemView.findViewById(R.id.userimg);
            viewClick = itemView.findViewById(R.id.viewClick);
            webview = itemView.findViewById(R.id.course_view);
            c_rider_name = itemView.findViewById(R.id.c_rider_name);
            c_name = itemView.findViewById(R.id.c_name);
            c_time = itemView.findViewById(R.id.c_time);
            c_avg = itemView.findViewById(R.id.c_avg);
            c_getgodo = itemView.findViewById(R.id.c_getgodo);
            c_dis = itemView.findViewById(R.id.c_dis);
            c_date = itemView.findViewById(R.id.c_date);
            like_count = itemView.findViewById(R.id.like_count);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {



    }
}