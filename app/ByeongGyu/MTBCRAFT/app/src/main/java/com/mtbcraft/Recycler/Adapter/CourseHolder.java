package com.mtbcraft.Recycler.Adapter;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.capston.mtbcraft.R;

import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapPolyline;
import net.daum.mf.map.api.MapReverseGeoCoder;
import net.daum.mf.map.api.MapView;

public class CourseHolder extends RecyclerView.ViewHolder implements
        LocationListener, MapView.CurrentLocationEventListener, MapReverseGeoCoder.ReverseGeoCodingResultListener{

    public TextView c_rider_name, c_name, c_time, c_avg, c_getgodo, c_dis;
    public ImageView imageView;
    public LinearLayout viewClick;
    public View mView;
     MapView mapView1;
    MapPolyline polyline;
    ViewGroup mapViewContainer;
    public CourseHolder(MapView mapView){
        super(mapView);
        mapView1 = new MapView(mapView.getContext());
        mapViewContainer = (ViewGroup) mapView.findViewById(R.id.map_view);

    }
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

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onReverseGeoCoderFoundAddress(MapReverseGeoCoder mapReverseGeoCoder, String s) {

    }

    @Override
    public void onReverseGeoCoderFailedToFindAddress(MapReverseGeoCoder mapReverseGeoCoder) {

    }

    @Override
    public void onCurrentLocationUpdate(MapView mapView, MapPoint mapPoint, float v) {

    }

    @Override
    public void onCurrentLocationDeviceHeadingUpdate(MapView mapView, float v) {

    }

    @Override
    public void onCurrentLocationUpdateFailed(MapView mapView) {

    }

    @Override
    public void onCurrentLocationUpdateCancelled(MapView mapView) {

    }
}
