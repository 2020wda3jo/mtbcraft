package com.capston.mtbcraft.Activity.Riding;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.capston.mtbcraft.Activity.Competition.CompetitionList;
import com.capston.mtbcraft.Activity.Course.CourseList;
import com.capston.mtbcraft.Activity.Course.CourseSearch;
import com.capston.mtbcraft.Activity.Main.SubActivity;
import com.capston.mtbcraft.Activity.Mission.MissionList;
import com.capston.mtbcraft.Activity.Scrap.MyScrap;
import com.capston.mtbcraft.R;
import com.capston.mtbcraft.gpxparser.GPXParser;
import com.capston.mtbcraft.gpxparser.Gpx;
import com.capston.mtbcraft.gpxparser.Track;
import com.capston.mtbcraft.gpxparser.TrackPoint;
import com.capston.mtbcraft.gpxparser.TrackSegment;
import com.capston.mtbcraft.network.HttpClient;
import com.google.android.material.navigation.NavigationView;

import net.daum.mf.map.api.CameraUpdateFactory;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapPointBounds;
import net.daum.mf.map.api.MapPolyline;
import net.daum.mf.map.api.MapReverseGeoCoder;
import net.daum.mf.map.api.MapView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailActivity extends AppCompatActivity implements MapView.CurrentLocationEventListener, MapReverseGeoCoder.ReverseGeoCodingResultListener{
    String rr_num;
    String rr_rider;
    GPXParser mParser = new GPXParser();
    Gpx parsedGpx = null;
    MapView mapView;
    MapPolyline polyline = new MapPolyline();
    TextView textView1, textView2, textView3, textView4, textView5, textView6;
    private DrawerLayout mDrawerLayout;
    String secS, test, disS, avgS, highS, maxS, breakS, gpx, open;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myreport_detail);


        mapView = new MapView(this);
        ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);

        //폴리라인 그리자~
        polyline.setTag(1000);
        polyline.setLineColor(Color.argb(255, 255, 51, 0)); // Polyline 컬러 지정.

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setDisplayHomeAsUpEnabled(true);


        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(menuItem -> {
            menuItem.setChecked(true);
            mDrawerLayout.closeDrawers();
            int id = menuItem.getItemId();
            /* 로그인 정보 가져오기 */
            SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
            String LoginId = auto.getString("LoginId","");
            String Nickname = auto.getString("r_nickname","");


            switch (id) {
                //홈
                case R.id.nav_home:
                    Intent home = new Intent(getApplicationContext(), SubActivity.class);
                    startActivity(home);
                    break;
                //라이딩 기록
                case R.id.nav_mylist:
                    Intent mylist=new Intent(getApplicationContext(), MyReport.class);
                    startActivity(mylist);

                    break;
                //코스보기
                case R.id.nav_courselist:
                    Intent courselist=new Intent(getApplicationContext(), CourseList.class);
                    courselist.putExtra("rider_id", LoginId);
                    startActivity(courselist);
                    break;
                //코스검색
                case R.id.nav_course_search:
                    Intent coursesearch=new Intent(getApplicationContext(), CourseSearch.class);
                    startActivity(coursesearch);
                    break;
                //스크랩 보관함
                case R.id.nav_course_get:
                    Intent courseget=new Intent(getApplicationContext(), MyScrap.class);
                    startActivity(courseget);
                    break;
                //경쟁전
                case R.id.nav_comp:
                    Intent comp=new Intent(getApplicationContext(), CompetitionList.class);
                    startActivity(comp);
                    break;
                //미션
                case R.id.nav_mission:
                    Intent mission=new Intent(getApplicationContext(), MissionList.class);
                    startActivity(mission);
                    break;
            }
            return true;
        });

        textView1 = (TextView)findViewById(R.id.Riding_time);
        textView2 = (TextView)findViewById(R.id.Riding_distance );
        textView3 = (TextView)findViewById(R.id.Riding_rest );
        textView4 = (TextView)findViewById(R.id.Riding_avg );
        textView5 = (TextView)findViewById(R.id.Riding_godo );
        textView6 = (TextView)findViewById(R.id.Riding_max );


        Intent intent = new Intent(this.getIntent());

        rr_num = intent.getStringExtra("rr_num");
        rr_rider = intent.getStringExtra("rr_rider");
       // open = intent.getStringExtra("open");
        try {
            GetTask getTask = new GetTask();
            Map<String, String> params = new HashMap<String, String>();
            params.put("rr_num", rr_num);
            params.put("rr_rider", rr_rider);
            getTask.execute(params);
        } catch (Exception e) {
            e.printStackTrace();
        }

        RadioGroup openselect = (RadioGroup)findViewById(R.id.selectgroup);
        //공개여부 선택
        openselect.setOnCheckedChangeListener((RadioGroup.OnCheckedChangeListener) (group, checkedId) -> {
            RadioGroup rg = (RadioGroup)findViewById(R.id.selectgroup); // 라디오그룹 객체 맵핑
            RadioButton selectedRdo = (RadioButton)findViewById(rg.getCheckedRadioButtonId()); // rg 라디오그룹의 체크된(getCheckedRadioButtonId) 라디오버튼 객체 맵핑
            String selectedValue = selectedRdo.getText().toString();
            String value="";
            switch (selectedValue){
                case "비공개":
                    value="0";
                    try {
                        OpenSet openset = new OpenSet();
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("rr_num", rr_num);
                        params.put("rr_rider", rr_rider);
                        params.put("open","0");
                        openset.execute(params);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case "전체 공개":
                    value="1";
                    try {
                        OpenSet openset = new OpenSet();
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("rr_num", rr_num);
                        params.put("rr_rider", rr_rider);
                        params.put("open","1");
                        openset.execute(params);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    value="1";
                    break;
            }
        });

    }

    public class OpenSet extends AsyncTask<Map<String, String>, Integer, String> {

        @Override
        protected String doInBackground(Map<String, String>... maps) {

            // Http 요청 준비 작업
            //URL은 현재 자기 아이피번호를 입력해야합니다.
            HttpClient.Builder http = new HttpClient.Builder("POST", "http://13.209.229.237/:8080/android/recordset/open");
            // Parameter 를 전송한다.
            http.addAllParameters(maps[0]);
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
            Log.d("디테일 ", s);
        }
    }


    public class GetTask extends AsyncTask<Map<String, String>, Integer, String> {

        @Override
        protected String doInBackground(Map<String, String>... maps) {

            // Http 요청 준비 작업
            //URL은 현재 자기 아이피번호를 입력해야합니다.
            HttpClient.Builder http = new HttpClient.Builder("GET", "http://13.209.229.237:8080/api/get/"+rr_rider+"/"+rr_num);
            // Parameter 를 전송한다.
            http.addAllParameters(maps[0]);
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
            //Log.d("디테일 ", s);
            try {

                String tempData = s;
                //json값을 받기위한 변수들
                JSONArray jarray = new JSONArray(tempData);
                for(int i=0; i<jarray.length(); i++){
                    JSONObject jObject = jarray.getJSONObject(i);
                    test = jObject.getString("rr_rider");
                    secS = jObject.getString("rr_time");
                    disS = jObject.getString("rr_distance");
                    breakS = jObject.getString("rr_breaktime");
                    avgS = jObject.getString("rr_avgspeed");
                    highS = jObject.getString("rr_high");
                    maxS = jObject.getString("rr_topspeed");
                    gpx = jObject.getString("rr_gpx");
                }

                //주행시간 계산
                int hour, min, sec = Integer.parseInt(secS);
                min = sec/60; hour = min/60; sec = sec % 60; min = min % 60;

                //휴식시간 계산
                int b_hour, b_min, b_sec = Integer.parseInt(breakS);
                b_min = b_sec/60; b_hour = b_min/60; b_sec = b_sec % 60; b_min = b_min % 60;

                int des = Integer.parseInt(disS);
                float km = (float) (des/1000.0);
                String total = km+"Km";

                textView1.setText(hour+"시간 "+min+"분 "+sec+"초");
                textView2.setText(total);
                textView3.setText(b_hour+"시간 "+b_min+"분 "+b_sec+"초");
                textView4.setText(avgS+"km/h");
                textView5.setText(highS+"m");
                textView6.setText(maxS+"km/h");

                Thread uThread = new Thread() {
                    @Override
                    public void run() {
                        try {
                            URL url = new URL("http://13.209.229.237:8080/app/getGPX/gpx/"+gpx);
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
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
                                mapView.addPolyline(polyline);

                                // 지도뷰의 중심좌표와 줌레벨을 Polyline이 모두 나오도록 조정.
                                MapPointBounds mapPointBounds = new MapPointBounds(polyline.getMapPoints());
                                int padding = 100; // px
                                mapView.moveCamera(CameraUpdateFactory.newMapPointBounds(mapPointBounds, padding));

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

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
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
