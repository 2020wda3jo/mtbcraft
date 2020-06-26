package com.capston.mtbcraft.Activity.Main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.capston.mtbcraft.R;
import com.capston.mtbcraft.network.HttpClient;
import com.google.android.material.tabs.TabLayout;
import net.daum.mf.map.api.CalloutBalloonAdapter;
import net.daum.mf.map.api.CameraUpdateFactory;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapPointBounds;
import net.daum.mf.map.api.MapPolyline;
import net.daum.mf.map.api.MapReverseGeoCoder;
import net.daum.mf.map.api.MapView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@SuppressLint("HandlerLeak")
public class StartActivity extends FragmentActivity
        implements LocationListener, MapView.CurrentLocationEventListener, MapReverseGeoCoder.ReverseGeoCodingResultListener, MapView.MapViewEventListener, MapView.POIItemEventListener, TextToSpeech.OnInitListener {
    private static final MapPoint CUSTOM_MARKER_POINT = MapPoint.mapPointWithGeoCoord(37.537229, 127.005515);
    private MapView mMapView;
    private MapPOIItem mCustomMarker;
    private LocationManager locationManager;
    private Location mLastlocation = null;
    private Boolean isRunning = true;
    private Boolean isRunning2 = true;
    private TextToSpeech textToSpeech;
    String meter;
    String value;
    float distance;
    JSONArray jarray;
    JSONObject jObject;

    //뷰에서 상단정보
    TextView m_speed, m_distance, m_time, m_t, m_rest;

    //속도계탭에서 보는 정보
    TextView reststatus, nowspeed, avgspeed, maxspeed, godo, dis, timeView, getGodo, resttime, courseinfo;

    Thread timeThread = null;
    Thread restTime = null;
    LinearLayout map_layout, speed_tap, status_layout;
    GridLayout speed_pre;
    String test, test2, test3;
    //각종 변수
    double latitude, lonngitude, getgodo, getSpeed = 0, hap = 0, getgodoval = 0, intime = 0, avg = 0, maX = 0, maxLat = 0, maxLon = 0, minLat = 1000, minLon = 1000;
    int cnt = 0, restcnt = 0, total_time=0;
    ArrayList<Double> witch_lat = new ArrayList<>();
    ArrayList<Double> witch_lon = new ArrayList<>();
    ArrayList<Double> ele = new ArrayList<>();
    ArrayList<Float> godoArray = new ArrayList<>();
    private TextToSpeech tts;

    //형변환용변수
    String cha_dis = "0", cha_max = "0", cha_avg = "0", adress_value = "";

    List<Address> addr = null;
    Geocoder gCoder;

    //이동시간 핸들러
    long BaseTime, RestBase;
    final static int Init = 0;
    final static int Run = 1;
    final static int Pause = 2;
    int cur_status = Init;
    long PauseTime;
    private long RestTime;

    // CalloutBalloonAdapter 인터페이스 구현
    class CustomCalloutBalloonAdapter implements CalloutBalloonAdapter {
        private final View mCalloutBalloon;

        public CustomCalloutBalloonAdapter() {
            mCalloutBalloon = getLayoutInflater().inflate(R.layout.custom_callout_balloon, null);
        }

        @Override
        public View getCalloutBalloon(MapPOIItem poiItem) {
            ((ImageView) mCalloutBalloon.findViewById(R.id.badge)).setImageResource(R.drawable.danger_icon);
            ((TextView) mCalloutBalloon.findViewById(R.id.title)).setText(poiItem.getItemName());
            return mCalloutBalloon;
        }

        @Override
        public View getPressedCalloutBalloon(MapPOIItem poiItem) {
            return null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.riding_start);


        // MapLayout mapLayout = new MapLayout(this);
        mMapView = new MapView(this);
        ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.map_view);
        mapViewContainer.addView(mMapView);
        mMapView.setCurrentLocationEventListener(this);
        mMapView.setShowCurrentLocationMarker(true);
        mMapView.setMapViewEventListener(this);
        mMapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);

        // 구현한 CalloutBalloonAdapter 등록
        mMapView.setCalloutBalloonAdapter(new CustomCalloutBalloonAdapter());
        //createCustomMarker(mMapView);
        //뷰에서 상단에 위치
        m_speed = (TextView) findViewById(R.id.m_speed); //현재속도
        m_distance = (TextView) findViewById(R.id.m_distance); //이동거리
        m_time = (TextView) findViewById(R.id.m_time); //라이딩 시간
        m_t = (TextView) findViewById(R.id.m_t);  //라이딩 시간(초)
        m_rest = (TextView) findViewById(R.id.m_rest); //휴식시간(초)

        //속도계탭에서 보는 정보
        nowspeed = (TextView) findViewById(R.id.nowspeed); //현재속도
        avgspeed = (TextView) findViewById(R.id.avgspeed); //평균속도
        maxspeed = (TextView) findViewById(R.id.maxspeed); //최대속도
        godo = (TextView) findViewById(R.id.Nowgodo); //현재고도
        dis = (TextView) findViewById(R.id.dis); //이동거리
        timeView = (TextView) findViewById(R.id.ingtime); //주행시간
        reststatus = (TextView) findViewById(R.id.restview); //현재상태
        getGodo = (TextView) findViewById(R.id.getgodo); //획득고도
        resttime = (TextView) findViewById(R.id.resttime);//휴식시간
        courseinfo = (TextView) findViewById(R.id.couse_info);//코스정보
        Button button2 = (Button) findViewById(R.id.endriding);
        Button button3 = (Button) findViewById(R.id.pausebt);
        //이동시간 핸들러
        BaseTime = SystemClock.elapsedRealtime();
        RidingTimer.sendEmptyMessage(0);
        cur_status = Run; //현재상태를 런상태로 변경



        speed_pre = (GridLayout) findViewById(R.id.speed_pre);
        map_layout = (LinearLayout) findViewById(R.id.map_layout);
        speed_tap = (LinearLayout) findViewById(R.id.speed_tap);
        status_layout = (LinearLayout) findViewById(R.id.status_layout);

        map_layout.setVisibility(View.VISIBLE); //지도
        speed_pre.setVisibility(View.VISIBLE); //지도탭에서 보여지는거
        speed_tap.setVisibility(View.GONE); //속도계
        status_layout.setVisibility(View.GONE);

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
            if (hap == 0) {
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

            } else {
                Intent intent = new Intent(StartActivity.this, endActivity.class);
                intent.putExtra("cha_dis", cha_dis); //이동거리(소수점X)
                intent.putExtra("cha_max", cha_max); //최대속도(소수점X)
                intent.putExtra("cha_avg", cha_avg); //평균속도(소수점X)
                intent.putExtra("distence", String.valueOf(dis.getText())); //이동거리
                intent.putExtra("endmax", String.valueOf(maxspeed.getText())); //최대속도
                intent.putExtra("endavg", String.valueOf(avgspeed.getText())); //평균속도
                intent.putExtra("getgodo", String.valueOf(getGodo.getText())); //획득고도
                intent.putExtra("resttime", String.valueOf(resttime.getText())); //휴식시간
                intent.putExtra("ingtime", String.valueOf(timeView.getText())); //경과시간
                intent.putExtra("addr", adress_value);
                intent.putExtra("endsec", total_time); //라이딩 시간(초)
                intent.putExtra("restsectime", String.valueOf(m_rest.getText())); //휴식시간(초)
                intent.putExtra("witch_lat", witch_lat); //위도
                intent.putExtra("witch_lon", witch_lon); //경도
                intent.putExtra("godoarray", godoArray);
                intent.putExtra("ele", ele); //고도
                intent.putExtra("maxLat", maxLat); //최대위도
                intent.putExtra("minLat", minLat); //최소위도
                intent.putExtra("maxLon", maxLon); //최대경도
                intent.putExtra("minLon", minLon); //최소경도
                intent.putExtra("wido", witch_lat); //위도(지도보여줄거
                intent.putExtra("kyun", witch_lon); //경도(지도보여줄거)
                intent.putExtra("rr_comp", "null");
                startActivity(intent);
                finish();
                mapViewContainer.removeAllViews();
            }
        });



        //위험지역 가져오기
        try {

            GetTask getTask = new GetTask();
            getTask.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void changeView(int index) {
        switch (index) {
            case 0:
                map_layout.setVisibility(View.VISIBLE);
                speed_pre.setVisibility(View.VISIBLE);
                speed_tap.setVisibility(View.GONE);
                break;
            case 1:
                map_layout.setVisibility(View.GONE);
                speed_pre.setVisibility(View.GONE);
                speed_tap.setVisibility(View.VISIBLE);
                break;
        }

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
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
        Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (lastKnownLocation != null) {
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 5, this);
    }

    public void myOnClick(View v){
        switch (v.getId()) {
            case R.id.pausebt: //시작버튼을 클릭했을때 현재 상태값에 따라 다른 동작을 할수있게끔 구현.
                switch (cur_status) {
                    case Init:
                        BaseTime = SystemClock.elapsedRealtime();
                        System.out.println(BaseTime);
                        //myTimer이라는 핸들러를 빈 메세지를 보내서 호출
                        RidingTimer.sendEmptyMessage(0);
                        cur_status = Run; //현재상태를 런상태로 변경
                        break;
                    case Run:
                        RidingTimer.removeMessages(0); //핸들러 메세지 제거
                        PauseTime = SystemClock.elapsedRealtime();
                        cur_status = Pause;
                        break;
                    case Pause:
                        long now = SystemClock.elapsedRealtime();
                        RidingTimer.sendEmptyMessage(0);
                        BaseTime += (now - PauseTime);
                        cur_status = Run;
                        break;
                }
                break;
        }
    }

    Handler RidingTimer = new Handler() {
        public void handleMessage(Message msg) {
            timeView.setText(getTimeout());
            m_time.setText(getTimeout());
            total_time = getTimeout2();

            //sendEmptyMessage는 비어있는 메세제를 핸들러에게 전송함
            RidingTimer.sendEmptyMessage(0);
        }

        //현재시간을 계속구해서 출력하는 메소드
        int getTimeout2() {
            long now = SystemClock.elapsedRealtime(); //애플리케이션이 실행되고나서 실제로 경과된 시간(??)^^;
            long outTime = now - BaseTime;

            int easy_outTime2 = (int)outTime/1000;
           // Log.d("로그", String.valueOf(easy_outTime2));
            return easy_outTime2;
        }

        //현재시간을 계속구해서 출력하는 메소드
        String getTimeout() {
            long now = SystemClock.elapsedRealtime(); //애플리케이션이 실행되고나서 실제로 경과된 시간(??)^^;
            long outTime = now - BaseTime;

           int sec = (int) ((outTime / 1000) % 60);
            int min = (int) ((outTime / 1000) / 60);
            int hour = (int) ((outTime / 1000) / 3600);

            String easy_outTime = String.format("%02d:%02d:%02d", hour, min, sec);
            //Log.d("로그", String.valueOf(outTime / 1000));
            return easy_outTime;
        }
    };
    
    public class GetTask extends AsyncTask<Map<String, String>, Integer, String> {
        @Override
        protected String doInBackground(Map<String, String>... maps) {
            // Http 요청 준비 작업
            //URL은 현재 자기 아이피번호를 입력해야합니다.
            HttpClient.Builder http = new HttpClient.Builder("GET", "http://13.209.229.237:8080/app/riding/danger");

            //Http 요청 전송
            HttpClient post = http.create();
            post.request();

            // 응답 상태코드 가져오기
            int statusCode = post.getHttpStatusCode();

            // 응답 본문 가져오기
            String body = post.getBody();
            return body;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                Log.d("JSON_RESULT", s);
                String tempData = s;

                jarray = new JSONArray(tempData);
                for (int i = 0; i < jarray.length(); i++) {
                    jObject = jarray.getJSONObject(i);
                    test = jObject.getString("da_latitude");
                    test2 = jObject.getString("da_longitude");
                    test3 = jObject.getString("da_content");
                    Log.d("위험지역", test + " " + test2 + test3);

                    mCustomMarker = new MapPOIItem();
                    String name = "Custom Marker";
                    mCustomMarker.setItemName(test3);
                    mCustomMarker.setTag(1);
                    mCustomMarker.setMapPoint(MapPoint.mapPointWithGeoCoord(Double.parseDouble(test), Double.parseDouble(test2)));

                    mCustomMarker.setMarkerType(MapPOIItem.MarkerType.CustomImage);

                    mCustomMarker.setCustomImageResourceId(R.drawable.custom_marker_red);
                    mCustomMarker.setCustomImageAutoscale(false);
                    mCustomMarker.setCustomImageAnchor(0.5f, 1.0f);

                    mMapView.addPOIItem(mCustomMarker);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }




    @Override
    public void onMapViewInitialized(MapView mapView) {

    }


    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int i) {

    }

    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {

    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {
        Toast.makeText(this, "Clicked " + mapPOIItem.getItemName() + " Callout Balloon", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {

    }

    @Override
    public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {
    }


    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        lonngitude = location.getLongitude();

        Location A = new Location("pointA");
        A.setLatitude(latitude);
        A.setLongitude(lonngitude);

        /*
        try {
            for (int i = 0; i < jarray.length(); i++) {
                jObject = jarray.getJSONObject(i);
                test = jObject.getString("da_latitude");
                test2 = jObject.getString("da_longitude");
                test3 = jObject.getString("da_content");
                //Log.d("위험지역", test + " " + test2 + test3);

                Location B = new Location("pointA");
                B.setLatitude(Double.parseDouble(test));
                B.setLongitude(Double.parseDouble(test2));
                distance = A.distanceTo(B);
               // Log.d(test3 + "거리는", String.valueOf(distance));
                //float를 정수로
                int a = (int) distance;
                //정수로바꾼거를 문자열로
                String cut_format = String.valueOf(a);
                //문자열의 길이를 구한다
                int leget = cut_format.length();
                //

                if (leget == 2) {
                    String get = cut_format.substring(cut_format.length() - 2);
                    String get2 = get.substring(0, 1);
                    if (get2.equals("7")) {
                        value = test3 + "위험구간이 " + get + "미터 앞입니다. 주의하세요";
                        textToSpeech = new TextToSpeech(this, this);
                       // Log.d("십의자리숫자", "70m 전방에 주의구간입니동");
                    }
                }
                if (leget == 3) {
                    String get = cut_format.substring(cut_format.length() - 3);
                    String get2 = get.substring(0, 1);
                    if (get2.equals("3")) {
                        value = "300m앞에 위험구간입니다";
                        textToSpeech = new TextToSpeech(this, this);
                       // Log.d("십의자리숫자", "300m 전방에 주의구간입니동");
                    }
                }


            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
*/
        // 위도 경도 정보 배열 저장
        witch_lat.add(latitude);
        witch_lon.add(lonngitude);
        godoArray.add((float) location.getAltitude());

        // 최대 최소 위도 경도 계산
        if (maxLat < latitude) maxLat = latitude;
        if (minLat > latitude) minLat = latitude;
        if (maxLon < lonngitude) maxLon = lonngitude;
        if (minLon > lonngitude) minLon = lonngitude;

        // 고도 정보 저장
        ele.add(location.getAltitude());

        //현재속도
        getSpeed = Double.parseDouble(String.format("%.1f", location.getSpeed() * 3600 / 1000));
        nowspeed.setText(String.format("%.1f", getSpeed));  //Get Speed
        m_speed.setText(String.format("%.1f", getSpeed) + "km/h");


        //만약 속도가 0.1미만이라면 휴식시간이 늘어남(뭔가 이상함)

        MapPolyline polyline = new MapPolyline();
        polyline.setLineColor(Color.argb(128, 255, 51, 0));

        polyline.addPoint(MapPoint.mapPointWithGeoCoord(latitude, lonngitude));
        mMapView.addPolyline(polyline);
        mMapView.isShowingCurrentLocationMarker();

        // 지도뷰의 중심좌표와 줌레벨을 Polyline이 모두 나오도록 조정.
        MapPointBounds mapPointBounds = new MapPointBounds(polyline.getMapPoints());
        int padding = 50; // px
        mMapView.moveCamera(CameraUpdateFactory.newMapPointBounds(mapPointBounds, padding, 2, 3));

        gCoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            addr = gCoder.getFromLocation(latitude, lonngitude, 1);
            Address a = addr.get(0);

            adress_value = a.getAddressLine(0);

            Log.d("주소", adress_value + "\n");
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (getSpeed==0.0) {
            RestTimer.removeMessages(0);
            long PauseTime = SystemClock.elapsedRealtime();
            long now = SystemClock.elapsedRealtime();
            RestTime += (now-PauseTime);


        } else {
            RestTime = SystemClock.elapsedRealtime();
            RestTimer.sendEmptyMessage(0);
            cur_status = Run;

            reststatus.setText("열심히^^");
        }
        if (mLastlocation != null) {


            /*폴리라인 그리기 */
            MapPolyline polyline2 = new MapPolyline();
            polyline.setLineColor(Color.argb(128, 255, 51, 0));
            polyline.addPoint(MapPoint.mapPointWithGeoCoord(mLastlocation.getLatitude(), mLastlocation.getLongitude()));
            mMapView.addPolyline(polyline);

            // 지도뷰의 중심좌표와 줌레벨을 Polyline이 모두 나오도록 조정.
            MapPointBounds mapPointBounds2 = new MapPointBounds(polyline.getMapPoints());
            int padding2 = 50; // px
            mMapView.moveCamera(CameraUpdateFactory.newMapPointBounds(mapPointBounds2, padding, 2, 3));

            if (getSpeed > maX) {
                maX = getSpeed;
                maxspeed.setText(String.format("%.1f", maX));
                cha_max = String.format("%.0f", maX);
            }


            //이동거리
            hap = hap + mLastlocation.distanceTo(location);
            cha_dis = String.format("%.0f", hap);

            int testhap = 0;
            dis.setText(String.format("%.2f", hap));
            m_distance.setText(String.format("%.1f", hap) + "m");

            double killlo;
            if (hap >= 1000) {
                killlo = hap / 1000.0;
                dis.setText(String.format("%.2f", killlo));
                m_distance.setText(String.format("%.1f", killlo) + "km");
            }

            //평균속도
            avg = hap /total_time;
            avgspeed.setText(String.format("%.1f", Double.parseDouble(String.valueOf(avg))));
            cha_avg = String.format("%.0f", (avg));

            //획득고도
            getgodoval = (location.getAltitude() - mLastlocation.getAltitude());
            if (getgodoval > 0) {
                getgodo += getgodoval;
                getGodo.setText(String.format("%.0f", getgodo));
            }

            //고도
            godo.setText(String.format("%.0f", location.getAltitude()) + "m");
        }
        // 현재위치를 지난 위치로 변경
        mLastlocation = location;
    }

    Handler RestTimer = new Handler(){
        public void handleMessage(Message msg){
            resttime.setText(getTimeRest());

            //sendEmptyMessage 는 비어있는 메세지를 Handler 에게 전송하는겁니다.
            RestTimer.sendEmptyMessage(0);
        }
    };
    //현재시간을 계속 구해서 출력하는 메소드
    String getTimeRest(){
        long now = SystemClock.elapsedRealtime(); //애플리케이션이 실행되고나서 실제로 경과된 시간(??)^^;
        long outTime = now - RestTime;


        int sec  = (int) ((outTime/1000) % 60);
        int min = (int) ((outTime/1000) / 60);
        int hour = (int) ((outTime/1000) / 3600);

        String easy_outTime = String.format("%02d:%02d:%02d", hour, min, sec);

       // Log.d("로그", String.valueOf(outTime/1000));
        return easy_outTime;

    }


    @Override
    public void onInit(int i) {
        if (i == TextToSpeech.SUCCESS) {
            textToSpeech.setLanguage(Locale.KOREAN);
            textToSpeech.setPitch(0.6f);
            textToSpeech.setSpeechRate(1f);
            textToSpeech.speak(value, TextToSpeech.QUEUE_FLUSH, null);
        }
    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
// 위치정보 업데이트
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
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 5, this);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("onDestroy","onDestroy");

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("onPause","onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("onResume","OnResume");
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        Log.d("다른화면임","다른화면임");
    }
}