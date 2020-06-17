package com.mtbcraft.Activity.Riding;

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
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.google.android.material.tabs.TabLayout;
import com.mtbcraft.Activity.Main.endActivity;
import com.mtbcraft.gpxparser.GPXParser;
import com.mtbcraft.gpxparser.Gpx;
import com.mtbcraft.gpxparser.Track;
import com.mtbcraft.gpxparser.TrackPoint;
import com.mtbcraft.gpxparser.TrackSegment;
import com.mtbcraft.network.HttpClient;

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
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class FollowStart extends FragmentActivity
        implements LocationListener, MapView.CurrentLocationEventListener, MapReverseGeoCoder.ReverseGeoCodingResultListener, MapView.MapViewEventListener, MapView.POIItemEventListener,TextToSpeech.OnInitListener{


    private static final MapPoint CUSTOM_MARKER_POINT = MapPoint.mapPointWithGeoCoord(37.537229, 127.005515);
    private MapView mMapView;
    private MapPOIItem mCustomMarker;
    private LocationManager locationManager;
    private Location mLastlocation = null;
    private Boolean isRunning = true;
    private TextToSpeech textToSpeech;
    String meter;
    String value;
    float distance;
    JSONArray jarray;
    JSONObject jObject;
    GPXParser mParser = new GPXParser();
    Gpx parsedGpx = null;

    //뷰에서 상단정보
    TextView m_speed, m_distance, m_time, m_t, m_rest;

    //속도계탭에서 보는 정보
    TextView reststatus, nowspeed, avgspeed, maxspeed, godo, dis, timeView, getGodo, resttime, courseinfo;

    Thread timeThread = null;
    LinearLayout map_layout, speed_tap,status_layout;
    GridLayout speed_pre;
    String test, test2, test3;
    //각종 변수
    double latitude, lonngitude, getgodo, getSpeed = 0, hap = 0, getgodoval = 0, intime = 0, avg = 0, maX = 0, maxLat = 0, maxLon = 0, minLat = 1000, minLon = 1000, total_time;
    int cnt = 0, restcnt = 0;
    ArrayList<Double> witch_lat = new ArrayList<>();
    ArrayList<Double> witch_lon = new ArrayList<>();
    ArrayList<Double> ele = new ArrayList<>();
    ArrayList<Float> godoArray = new ArrayList<>();
    private TextToSpeech tts;
    //휴식시간 계산
    int hour, min, sec;
    //형변환용변수
    String cha_dis = "0", cha_max = "0", cha_avg = "0";

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


        status_layout = (LinearLayout) findViewById(R.id.status_layout);
        status_layout.setVisibility(View.GONE);
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

        timeThread = new Thread(new timeThread());
        timeThread.start();

        speed_pre = (GridLayout) findViewById(R.id.speed_pre);
        map_layout = (LinearLayout) findViewById(R.id.map_layout);
        speed_tap = (LinearLayout) findViewById(R.id.speed_tap);

        map_layout.setVisibility(View.VISIBLE); //지도
        speed_pre.setVisibility(View.VISIBLE); //지도탭에서 보여지는거
        speed_tap.setVisibility(View.GONE); //속도계

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

        Intent intentt = new Intent(this.getIntent());
        intentt.getStringExtra("c_name");
        int check = intentt.getIntExtra("check", 0);
        String comp_num = intentt.getStringExtra("comp_num");
        courseinfo.setText(intentt.getStringExtra("c_name"));
        Log.d("확인",intentt.getStringExtra("c_name") + ""+ intentt.getStringExtra("gpx"));
        Thread uThread = new Thread() {

            @Override

            public void run() {

                try {
                    MapPolyline polyline = new MapPolyline();
                    polyline.setTag(1000);
                    polyline.setLineColor(Color.argb(255, 255, 51, 0)); // Polyline 컬러 지정.
                    //서버에 올려둔 이미지 URL
                    URL url = new URL("http://13.209.229.237:8080/app/getGPX/gpx/"+intentt.getStringExtra("gpx"));
                    //Web에서 이미지 가져온 후 ImageView에 지정할 Bitmap 만들기
                    /* URLConnection 생성자가 protected로 선언되어 있으므로
                     개발자가 직접 HttpURLConnection 객체 생성 불가 */
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    /* openConnection()메서드가 리턴하는 urlConnection 객체는
                    HttpURLConnection의 인스턴스가 될 수 있으므로 캐스팅해서 사용한다*/

                    conn.setDoInput(true); //Server 통신에서 입력 가능한 상태로 만듦
                    conn.connect(); //연결된 곳에 접속할 때 (connect() 호출해야 실제 통신 가능함)


                    InputStream is = conn.getInputStream(); //inputStream 값 가져오기
                    // InputStream in = getAssets().open("and.gpx");
                    parsedGpx = mParser.parse(is);

                    if (parsedGpx != null) {
                        // log stuff
                        List<Track> tracks = parsedGpx.getTracks();
                        for (int i = 0; i < tracks.size(); i++) {
                            Track track = tracks.get(i);
                            Log.d("track ", i + ":");
                            List<TrackSegment> segments = track.getTrackSegments();
                            for (int j = 0; j < segments.size(); j++) {
                                TrackSegment segment = segments.get(j);

                                for (TrackPoint trackPoint : segment.getTrackPoints()) {

                                    polyline.addPoint(MapPoint.mapPointWithGeoCoord(trackPoint.getLatitude(), trackPoint.getLongitude()));
                                    Log.d("point: lat ", + trackPoint.getLatitude() + ", lon " + trackPoint.getLongitude());
                                }
                            }
                        }
                        // Polyline 지도에 올리기.
                        mMapView.addPolyline(polyline);

                        // 지도뷰의 중심좌표와 줌레벨을 Polyline이 모두 나오도록 조정.
                        MapPointBounds mapPointBounds = new MapPointBounds(polyline.getMapPoints());
                        int padding = 100; // px
                        mMapView.moveCamera(CameraUpdateFactory.newMapPointBounds(mapPointBounds, padding));
                    } else {
                        Log.e("error","Error parsing gpx track!");
                    }


                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException | XmlPullParserException e) {
                    e.printStackTrace();
                }
            }
        };
        uThread.start(); // 작업 Thread 실행

        timeThread = new Thread(new timeThread());
        timeThread.start();

        button2.setOnClickListener(v -> {
            //형변환한거
            if (cha_dis.equals("")) {
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
                Intent intent = new Intent(FollowStart.this, endActivity.class);
                intent.putExtra("cha_dis", cha_dis); //이동거리(소수점X)
                intent.putExtra("cha_max", cha_max); //최대속도(소수점X)
                intent.putExtra("cha_avg", cha_avg); //평균속도(소수점X)

                intent.putExtra("distence", String.valueOf(dis.getText())); //이동거리
                intent.putExtra("endmax", String.valueOf(maxspeed.getText())); //최대속도
                intent.putExtra("endavg", String.valueOf(avgspeed.getText())); //평균속도
                intent.putExtra("getgodo", String.valueOf(getGodo.getText())); //획득고도
                intent.putExtra("resttime", String.valueOf(resttime.getText())); //휴식시간
                intent.putExtra("ingtime", String.valueOf(timeView.getText())); //경과시간
                intent.putExtra("endsec", String.valueOf(m_t.getText())); //라이딩 시간(초)
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
                intent.putExtra("comp_name", intentt.getStringExtra("comp_name"));
                if ( check == 1 )
                    intent.putExtra("rr_comp", comp_num);
                else
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
            try{
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
            }catch(Exception e){
                e.printStackTrace();
            }
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
            @SuppressLint("DefaultLocale") String result2 = String.format("%02d", sectotal);
            total_time = Double.parseDouble(result2);
            timeView.setText(result); //현재시간
            m_time.setText(result); //현재시간(상단뷰)
            m_t.setText(result2); //초만표시
        }
    };

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
            Log.d(test3+"거리는", String.valueOf(distance));
            //float를 정수로
            int a = (int) distance;
            //정수로바꾼거를 문자열로
            String cut_format = String.valueOf(a);
            //문자열의 길이를 구한다
            int leget = cut_format.length();
            //

            if(leget==2) {
                String get = cut_format.substring(cut_format.length()-2);
                String get2 = get.substring(0,1);
                if(get2.equals("7")) {
                    value = test3 + "위험구간이 "+get+"미터 앞입니다. 주의하세요";
                    textToSpeech = new TextToSpeech(this,this);

                    Log.d("십의자리숫자","70m 전방에 주의구간입니동");
                }
            }
            if(leget==3){
                String get = cut_format.substring(cut_format.length()-3);
                String get2 = get.substring(0,1);
                if(get2.equals("3")) {
                    value="300m앞에 위험구간입니다";
                    textToSpeech = new TextToSpeech(this,this);
                    Log.d("십의자리숫자","300m 전방에 주의구간입니동");
                }
            }


            }

            } catch (JSONException e) {
                e.printStackTrace();
            }

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
        cnt = cnt + 1;
        //km당 알림주는거

        //현재속도
        getSpeed = Double.parseDouble(String.format("%.1f", location.getSpeed() * 3600 / 1000));
        nowspeed.setText(String.format("%.1f", getSpeed));  //Get Speed
        m_speed.setText(String.format("%.1f", getSpeed) + "km/h");


        //만약 속도가 0.1미만이라면 휴식시간이 늘어남(뭔가 이상함)
        if (getSpeed < 0.0) {
            restcnt = restcnt + 1;
            reststatus.setText("일시정지");
            sec = restcnt;
            min = sec / 60;
            hour = min / 60;
            sec = sec % 60;
            min = min % 60;
            resttime.setText(String.valueOf(hour + ":" + min + ":" + sec));
            m_rest.setText(String.valueOf(restcnt));
        } else { //그게 아니면 경과시간이 늘어남
            intime = intime + 1;
            reststatus.setText("열심히^^");
        }

        MapPolyline polyline = new MapPolyline();
        polyline.setLineColor(Color.argb(128,255,51,0));

        polyline.addPoint(MapPoint.mapPointWithGeoCoord(latitude,lonngitude));
        mMapView.addPolyline(polyline);
        mMapView.isShowingCurrentLocationMarker();

        // 지도뷰의 중심좌표와 줌레벨을 Polyline이 모두 나오도록 조정.
        MapPointBounds mapPointBounds = new MapPointBounds(polyline.getMapPoints());
        int padding = 50; // px
        mMapView.moveCamera(CameraUpdateFactory.newMapPointBounds(mapPointBounds,padding,2,3));


        if (mLastlocation != null) {

            /*폴리라인 그리기 */
            MapPolyline polyline2 = new MapPolyline();
            polyline.setLineColor(Color.argb(128,255,51,0));
            polyline.addPoint(MapPoint.mapPointWithGeoCoord(mLastlocation.getLatitude(),mLastlocation.getLongitude()));
            mMapView.addPolyline(polyline);

            // 지도뷰의 중심좌표와 줌레벨을 Polyline이 모두 나오도록 조정.
            MapPointBounds mapPointBounds2 = new MapPointBounds(polyline.getMapPoints());
            int padding2 = 50; // px
            mMapView.moveCamera(CameraUpdateFactory.newMapPointBounds(mapPointBounds2,padding,2,3));

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
            m_distance.setText(String.format("%.1f", hap)+"m");

            double killlo;
            if(hap>=1000){
                killlo = hap/1000.0;
                dis.setText(String.format("%.2f", killlo));
                m_distance.setText(String.format("%.1f", killlo)+"km");
            }

            //평균속도
            avg = hap / total_time;
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

    @Override
    public void onInit(int i) {
        if (i == TextToSpeech.SUCCESS) {
            textToSpeech.setLanguage(Locale.KOREAN);
            textToSpeech.setPitch(0.6f);
            textToSpeech.setSpeechRate(1f);
            textToSpeech.speak(value, TextToSpeech.QUEUE_FLUSH,null);
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
    // 메모리 누출을 방지하게 위해 TTS를 중지
    @Override
    protected void onStop() {
        super.onStop();
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
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