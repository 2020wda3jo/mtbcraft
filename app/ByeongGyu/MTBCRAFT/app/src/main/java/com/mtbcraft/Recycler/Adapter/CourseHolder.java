package com.mtbcraft.Recycler.Adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.capston.mtbcraft.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

public class CourseHolder extends RecyclerView.ViewHolder implements OnMapReadyCallback {

    public TextView c_rider_name, c_name, c_time, c_avg, c_getgodo, c_dis;
    public ImageView imageView;
    public LinearLayout viewClick;
    public View mView;
    public GoogleMap map;
    MapView mapView;
    private GoogleMap googleMap;

    public CourseHolder(View itemView) {
        super(itemView);

        mView = itemView;
        c_rider_name = itemView.findViewById(R.id.c_rider_name);
        c_name = itemView.findViewById(R.id.c_name);
        c_time = itemView.findViewById(R.id.c_time);
        c_avg = itemView.findViewById(R.id.c_avg);
        c_getgodo = itemView.findViewById(R.id.c_getgodo);
        c_dis = itemView.findViewById(R.id.c_dis);
        imageView = itemView.findViewById(R.id.imageView);
        viewClick = itemView.findViewById(R.id.viewClick);
        mapView = itemView.findViewById(R.id.map);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        //지도타입 - 일반
        this.googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        //기본위치(63빌딩)
        LatLng position = new LatLng(37.5197889 , 126.9403083);
        //화면중앙의 위치와 카메라 줌비율
        this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15));
    }
}
