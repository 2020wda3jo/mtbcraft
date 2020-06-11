package com.mtbcraft.Activity.Main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.PermissionChecker;
import androidx.fragment.app.FragmentManager;

import com.capston.mtbcraft.R;
import com.google.android.material.tabs.TabLayout;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.overlay.LocationOverlay;
import com.naver.maps.map.overlay.PathOverlay;
import com.naver.maps.map.util.FusedLocationSource;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;


public class StartActivity extends AppCompatActivity implements LocationListener{
    /*GPS관련 설정 들 */
    private NaverMap map;
    private FusedLocationSource locationSource;
    private static final int PERMISSION_REQUEST_CODE = 100;
    private static final String[] PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    @Nullable
    private LocationManager locationManager;

    private Location mLastlocation = null;
    private Boolean isRunning = true;

    /*레이아웃 관련*/
    //뷰에서 상단정보
    TextView m_speed, m_distance, m_time ,m_t, m_rest;

    //속도계탭에서 보는 정보
    TextView reststatus, nowspeed, avgspeed, maxspeed, godo, dis, timeView, getGodo, resttime, courseinfo;

    Thread timeThread = null;
    LinearLayout layout, layout2, layout3, layout4, layout5, layout6, layout7, layout8, m_status, layout9, layout10, map_layout;

    //각종 변수
    double latitude, lonngitude, getgodo, getSpeed=0, hap = 0, getgodoval = 0, intime=0, avg=0, maX=0, maxLat = 0, maxLon = 0, minLat = 1000, minLon = 1000, total_time;
    int cnt=0, restcnt = 0;
    ArrayList<Double> witch_lat = new ArrayList<>();
    ArrayList<Double> witch_lon = new ArrayList<>();
    ArrayList<Double> ele = new ArrayList<>();
    ArrayList<Float> godoArray = new ArrayList<>();

    //휴식시간 계산
    int hour, min, sec;


    //형변환용변수
    String cha_dis="", cha_max="", cha_avg="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.riding_start);

        // ...

        FragmentManager fm = getSupportFragmentManager();
        MapFragment mapFragment = (MapFragment) fm.findFragmentById(R.id.map_view);
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            fm.beginTransaction().add(R.id.map_view, mapFragment).commit();
        }
        mapFragment.getMapAsync(naverMap -> map = naverMap);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        SimpleDateFormat format = new SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분");
        Date time = new Date();
        String time2 = format.format(time);


        //뷰에서 상단에 위치
        m_speed = (TextView)findViewById(R.id.m_speed); //현재속도
        m_distance = (TextView)findViewById(R.id.m_distance); //이동거리
        m_time = (TextView)findViewById(R.id.m_time); //라이딩 시간
        m_t = (TextView)findViewById(R.id.m_t);  //라이딩 시간(초)
        m_rest = (TextView)findViewById(R.id.m_rest); //휴식시간(초)

        //속도계탭에서 보는 정보
        nowspeed = (TextView)findViewById(R.id.nowspeed); //현재속도
        avgspeed = (TextView) findViewById(R.id.avgspeed); //평균속도
        maxspeed = (TextView) findViewById(R.id.maxspeed); //최대속도
        godo = (TextView) findViewById(R.id.Nowgodo); //현재고도
        dis = (TextView)findViewById(R.id.dis); //이동거리
        timeView = (TextView)findViewById(R.id.ingtime); //주행시간
        reststatus = (TextView)findViewById(R.id.restview); //현재상태
        getGodo = (TextView)findViewById(R.id.getgodo); //획득고도
        resttime = (TextView)findViewById(R.id.resttime);//휴식시간
        courseinfo = (TextView)findViewById(R.id.couse_info);//코스정보
        Button button2 = (Button)findViewById(R.id.endriding);

        timeThread = new Thread(new timeThread());
        timeThread.start();

        layout = (LinearLayout)findViewById(R.id.speedlayout);
        layout2 = (LinearLayout)findViewById(R.id.restlayout);
        layout3 = (LinearLayout)findViewById(R.id.statuslayout1);
        layout4 = (LinearLayout)findViewById(R.id.statusvaluelayout1);
        layout5 = (LinearLayout)findViewById(R.id.statuslayout2);
        layout6 = (LinearLayout)findViewById(R.id.statusvaluelayout2);
        layout7 = (LinearLayout)findViewById(R.id.statuslayout3);
        layout8 = (LinearLayout)findViewById(R.id.statusvaluelayout3);
        layout9 = (LinearLayout)findViewById(R.id.statuslayout4);
        layout10 = (LinearLayout)findViewById(R.id.statusvaluelayout5);
        m_status = (LinearLayout) findViewById(R.id.m_status);
        map_layout = (LinearLayout) findViewById(R.id.map_layout);
        layout.setVisibility(View.INVISIBLE);
        layout2.setVisibility(View.INVISIBLE);
        layout3.setVisibility(View.INVISIBLE);
        layout4.setVisibility(View.INVISIBLE);
        layout5.setVisibility(View.INVISIBLE);
        layout6.setVisibility(View.INVISIBLE);
        layout7.setVisibility(View.INVISIBLE);
        layout8.setVisibility(View.INVISIBLE);
        layout9.setVisibility(View.INVISIBLE);
        layout10.setVisibility(View.INVISIBLE);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // TODO : process tab selection event.
                int pos = tab.getPosition();
                changeView(pos);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // do nothing
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // do nothing
            }
        });

        button2.setOnClickListener(v -> {
            //형변환한거
            if(cha_dis.equals("")){
                /*이동거리가 0이면 라이딩 기록 종료시키면 액티비티 종료 */
                AlertDialog.Builder alert_confirm = new AlertDialog.Builder(this);
                alert_confirm.setMessage("수집 데이터가 너무 적어 저장하지 않습니다");
                alert_confirm.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                alert_confirm.setNegativeButton("아니오", null);
                AlertDialog alert = alert_confirm.create();
                alert.setTitle("라이딩 기록 실패");
                alert.show();

            }else{
            Intent intent = new Intent(StartActivity.this, endActivity.class);
            intent.putExtra("cha_dis",cha_dis); //이동거리(소수점X)
            intent.putExtra("cha_max",cha_max); //최대속도(소수점X)
            intent.putExtra("cha_avg",cha_avg); //평균속도(소수점X)

            intent.putExtra("distence",String.valueOf(dis.getText())); //이동거리
            intent.putExtra("endmax",String.valueOf(maxspeed.getText())); //최대속도
            intent.putExtra("endavg",String.valueOf(avgspeed.getText())); //평균속도
            intent.putExtra("getgodo",String.valueOf(getGodo.getText())); //획득고도
            intent.putExtra("resttime",String.valueOf(resttime.getText())); //휴식시간
            intent.putExtra("ingtime",String.valueOf(timeView.getText())); //경과시간
            intent.putExtra("endsec",String.valueOf(m_t.getText())); //라이딩 시간(초)
            intent.putExtra("restsectime",String.valueOf(m_rest.getText())); //휴식시간(초)
            intent.putExtra("witch_lat", witch_lat); //위도
            intent.putExtra("witch_lon", witch_lon); //경도
            intent.putExtra("godoarray", godoArray);
            intent.putExtra("ele", ele); //고도
            intent.putExtra("maxLat", maxLat); //최대위도
            intent.putExtra("minLat", minLat); //최소위도
            intent.putExtra("maxLon", maxLon); //최대경도
            intent.putExtra("minLon", minLon); //최소경도
            intent.putExtra("wido",witch_lat); //위도(지도보여줄거
            intent.putExtra("kyun",witch_lon); //경도(지도보여줄거)
            startActivity(intent);
            finish();
            }
        });

        //권한 체크
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (lastKnownLocation != null) {
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000,10, this);
    }


    private void changeView(int index) {
        switch (index) {
            case 0 :
                map_layout.setVisibility(View.VISIBLE);
                m_status.setVisibility(View.VISIBLE);
                m_time.setVisibility(View.VISIBLE);
                layout.setVisibility(View.INVISIBLE);
                layout2.setVisibility(View.INVISIBLE);
                layout3.setVisibility(View.INVISIBLE);
                layout4.setVisibility(View.INVISIBLE);
                layout5.setVisibility(View.INVISIBLE);
                layout6.setVisibility(View.INVISIBLE);
                layout7.setVisibility(View.INVISIBLE);
                layout8.setVisibility(View.INVISIBLE);
                layout9.setVisibility(View.INVISIBLE);
                break ;
            case 1 :
                map_layout.setVisibility(View.GONE);
                m_status.setVisibility(View.INVISIBLE);
                m_time.setVisibility(View.INVISIBLE);
                layout.setVisibility(View.VISIBLE);
                layout2.setVisibility(View.VISIBLE);
                layout3.setVisibility(View.VISIBLE);
                layout4.setVisibility(View.VISIBLE);
                layout5.setVisibility(View.VISIBLE);
                layout6.setVisibility(View.VISIBLE);
                layout7.setVisibility(View.VISIBLE);
                layout8.setVisibility(View.VISIBLE);
                layout9.setVisibility(View.VISIBLE);
                break ;
        }
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int sectotal = (msg.arg1 / 100);
            int mSec = msg.arg1 % 100;
            int sec = (msg.arg1 / 100) % 60;
            int min = (msg.arg1 / 100) / 60;
            int hour = (msg.arg1 / 100) / 360;
            //1000이 1초 1000*60 은 1분 1000*60*10은 10분 1000*60*60은 한시간

            @SuppressLint("DefaultLocale") String result = String.format("%02d:%02d:%02d", hour, min, sec);
            @SuppressLint("DefaultLocale") String result2 =  String.format("%02d",sectotal);
            total_time = Double.parseDouble(result2);
            timeView.setText(result); //현재시간
            m_time.setText(result); //현재시간(상단뷰)
            m_t.setText(result2); //초만표시
        }
    };


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    public class timeThread implements Runnable {
        @Override
        public void run() {
            int i = 0;

            while (true) {
                while (isRunning) { //일시정지를 누르면 멈춤
                    Message msg = new Message();
                    msg.arg1 = i++;
                    handler.sendMessage(msg);

                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //timeView.setText("");
                                //timeView.setText("00:00:00:00");
                            }
                        });
                        return; // 인터럽트 받을 경우 return
                    }
                }
            }
        }
    }


    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        lonngitude = location.getLongitude();

        LatLng coord = new LatLng(location);
        LocationOverlay locationOverlay = map.getLocationOverlay();
        locationOverlay.setVisible(true);
        locationOverlay.setPosition(coord);
        locationOverlay.setBearing(location.getBearing());
        map.moveCamera(CameraUpdate.scrollTo(coord));

        // 위도 경도 정보 배열 저장
        witch_lat.add(latitude);
        witch_lon.add(lonngitude);
        godoArray.add((float) location.getAltitude());

        // 최대 최소 위도 경도 계산
        if ( maxLat <  latitude ) maxLat =  latitude;
        if ( minLat >  latitude ) minLat =  latitude;
        if ( maxLon < lonngitude ) maxLon = lonngitude;
        if ( minLon > lonngitude ) minLon = lonngitude;

        // 고도 정보 저장
        ele.add(location.getAltitude());
        cnt = cnt + 1;
        //km당 알림주는거

        //현재속도
        getSpeed = Double.parseDouble(String.format("%.1f", location.getSpeed() * 3600 / 1000));
        nowspeed.setText(String.format("%.1f", getSpeed));  //Get Speed
        m_speed.setText(String.format("%.1f", getSpeed)+"km/h");

        //만약 속도가 0.1미만이라면 휴식시간이 늘어남(뭔가 이상함)
        if(getSpeed < 0.0){
            restcnt = restcnt + 1;
            reststatus.setText("일시정지");
            sec=restcnt;
            min = sec/60; hour = min/60; sec = sec % 60; min = min % 60;
            resttime.setText(String.valueOf(hour+":"+min+":"+sec));
            m_rest.setText(String.valueOf(restcnt));
        }else{ //그게 아니면 경과시간이 늘어남
            intime = intime+1;
            reststatus.setText("열심히^^");
        }

        if(mLastlocation != null) {
            /*폴리라인 그리기 */
            PathOverlay path = new PathOverlay();
            path.setCoords(Arrays.asList(
                    new LatLng(location.getLatitude(), location.getLongitude()),
                    new LatLng(mLastlocation.getLatitude(), mLastlocation.getLongitude())
            ));
            path.setMap(map);
            path.setWidth(30);
            path.setColor(Color.RED);
            //최대속도(5.12 값 틀림)
            if(getSpeed > maX){
                maX = getSpeed;
                maxspeed.setText(String.format("%.1f", maX));
                cha_max = String.format("%.0f", maX);
            }


            //이동거리
            hap = hap+mLastlocation.distanceTo(location);
            cha_dis = String.format("%.0f",hap);

            int testhap=0;
            dis.setText(String.format("%.2f",hap));

            //평균속도
            avg= hap/total_time;
            avgspeed.setText(String.format("%.1f", Double.parseDouble(String.valueOf(avg))));
            cha_avg = String.format("%.0f", (avg));

            //획득고도
            getgodoval = (location.getAltitude()-mLastlocation.getAltitude());
            if(getgodoval>0){
                getgodo+=getgodoval;
                getGodo.setText(String.format("%.0f",getgodo));
            }

            //고도
            godo.setText(String.format("%.0f",location.getAltitude())+"m");
        }
        // 현재위치를 지난 위치로 변경
        mLastlocation = location;
    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }
    @Override
    public void onProviderEnabled(String provider) {
        //권한 체크
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        // 위치정보 업데이트
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000,5, this);
    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        //권한 체크
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        // 위치정보 업데이트
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000,5, this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 위치정보 가져오기 제거
        locationManager.removeUpdates(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (hasPermission()) {
            if (locationManager != null) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER, 1000, 2, this);
            }
        } else {
            ActivityCompat.requestPermissions(
                    this, PERMISSIONS, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (hasPermission() && locationManager != null) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER, 1000, 10, this);
            }
            return;
        }
    }

    private boolean hasPermission() {
        return PermissionChecker.checkSelfPermission(this, PERMISSIONS[0])
                == PermissionChecker.PERMISSION_GRANTED
                && PermissionChecker.checkSelfPermission(this, PERMISSIONS[1])
                == PermissionChecker.PERMISSION_GRANTED;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (locationManager != null) {
            locationManager.removeUpdates(this);
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }
}