package com.example.testapplication.ui.home;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.testapplication.R;
import com.example.testapplication.dto.RidingRecord;
import com.example.testapplication.ui.BaseFragment;


import java.util.List;

import retrofit2.Call;

import java.text.SimpleDateFormat;
import java.util.Date;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends BaseFragment {
    private Call<List<RidingRecord>> MyInfo;
    private Button ridingstart;
    private TextView mainTime, mainKm, rider;
    public static HomeFragment newInstance(){
        return new HomeFragment();
    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_home, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        String Save_Path = requireContext().getFilesDir().getPath();

        model.message.observe(getViewLifecycleOwner(), message->{
            Log.i("Home", message);
        });

        SharedPreferences auto = requireContext().getSharedPreferences("auto", Activity.MODE_PRIVATE);
        String r_image = auto.getString("r_image", "");

        model.message.observe(getViewLifecycleOwner(), message -> {
            Log.i("Home", message);
        });

        mainTime = (TextView) view.findViewById(R.id.main_time);
        mainKm = (TextView) view.findViewById(R.id.main_km);
        rider = (TextView) view.findViewById(R.id.idinfo);

        

        MyInfo = serverApi.getMyRecord("345");
        MyInfo.enqueue(new Callback<List<RidingRecord>>() {
            @Override
            public void onResponse(Call<List<RidingRecord>> call, Response<List<RidingRecord>> response) {
                if(response.code()==200){
                    String oTime="";//현재날짜

                    SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date currentTime = new Date();
                    oTime = mSimpleDateFormat.format(currentTime);

                    try{
                        List<RidingRecord> record = response.body();
                        int dis = 0;
                        int riding_time = 0;
                        int killlo=0;
                        String date = "";
                        for(RidingRecord test : record) {
                            dis += test.getRr_distance();
                            riding_time += test.getRr_time();
                            date = test.getRr_date();

                            //오늘 주행한거
                                int hour;
                                int min;
                                int sec = riding_time;

                                min = sec / 60;
                                hour = min / 60;
                                sec = sec % 60;
                                min = min % 60;
                                if (hour == 0) {
                                    hour = 0;
                                }
                                if (min == 0) {
                                    min = 0;
                                }
                                mainTime.setText(hour + "시간 " + min + "분 " + sec + "초");

                                killlo = (int) (dis / 1000.0);

                                if (dis >= 1000) {
                                    mainKm.setText(killlo + "km");
                                } else {
                                    mainKm.setText(dis + "m");
                                }


                            }

                        Log.d("합한거는?",dis+" " + riding_time+" " +date);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<RidingRecord>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}