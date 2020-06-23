package com.capston.mtbcraft.Activity.Course;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.capston.mtbcraft.Activity.Competition.CompetitionList;
import com.capston.mtbcraft.Activity.Mission.Mission;
import com.capston.mtbcraft.Activity.Riding.FollowStart;
import com.capston.mtbcraft.Activity.Riding.MyReport;
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

public class CourseDetail extends AppCompatActivity implements MapView.CurrentLocationEventListener, MapReverseGeoCoder.ReverseGeoCodingResultListener {
    String c_num, course_name, gpx;
    TextView c_rider_name, c_name, c_date, c_addr, c_dis, c_avg, c_getgodo, like_count;
    ImageView like_push;
    Button button,button2;
    int Sta;
    private DrawerLayout mDrawerLayout;
    GPXParser mParser = new GPXParser();
    Gpx parsedGpx = null;
    MapView mapView;String LoginId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coursedetail);

        c_rider_name = (TextView)findViewById(R.id.c_rider_name);
        c_name = (TextView)findViewById(R.id.c_name );
        c_date = (TextView)findViewById(R.id.c_date );
        c_addr = (TextView)findViewById(R.id.c_addr);
        c_dis = (TextView)findViewById(R.id.c_dis);
        c_avg = (TextView)findViewById(R.id.c_avg);
        c_getgodo = (TextView)findViewById(R.id.c_getgodo);
        like_count = (TextView)findViewById(R.id.like_count);
        button = (Button)findViewById(R.id.scrap_bt);
        button2 = (Button)findViewById(R.id.follow_bt);
        like_push = (ImageView)findViewById(R.id.like_push);
        Intent intent = new Intent(this.getIntent());

        c_num = intent.getStringExtra("c_num");
        gpx = intent.getStringExtra("c_gpx");
        course_name = intent.getStringExtra("c_name");


        mapView = new MapView(CourseDetail.this);
        ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);

        mapView.setCurrentLocationEventListener(this);
        mapView.isShowingCurrentLocationMarker();


        /* 로그인관련 */
        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
        LoginId = auto.getString("LoginId","");
        Toast toast = Toast.makeText(getApplicationContext(), LoginId+"님 로그인되었습니다", Toast.LENGTH_SHORT); toast.show();

        /*네비게이션 바 */
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
            switch (id) {
                //홈
                case R.id.nav_home:
                    Intent home=new Intent(CourseDetail.this, MyReport.class);
                    startActivity(home);
                    break;
                //라이딩 기록
                case R.id.nav_mylist:
                    Intent mylist=new Intent(CourseDetail.this, MyReport.class);
                    startActivity(mylist);
                    break;
                //코스보기
                case R.id.nav_courselist:
                    Intent courselist=new Intent(CourseDetail.this, CourseList.class);
                    startActivity(courselist);
                    finish();
                    break;
                //코스검색
                case R.id.nav_course_search:
                    Intent coursesearch=new Intent(CourseDetail.this, CourseSearch.class);
                    startActivity(coursesearch);
                    //스크랩 보관함
                case R.id.nav_course_get:
                    Intent courseget=new Intent(CourseDetail.this, MyScrap.class);
                    startActivity(courseget);
                    break;
                //경쟁전
                case R.id.nav_comp:
                    Intent comp=new Intent(CourseDetail.this, CompetitionList.class);
                    startActivity(comp);
                    break;
                //미션
                case R.id.nav_mission:
                    Intent mission=new Intent(CourseDetail.this, Mission.class);
                    startActivity(mission);
                    break;
            }
            return true;
        });

        try{
            GetLikeTask liketask = new GetLikeTask();
            liketask.execute();

            GetLikeStatusTask likestatustask = new GetLikeStatusTask();
            likestatustask.execute();


        }catch(Exception e){
            e.printStackTrace();
        }

        like_push.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    LikeTask liketask = new LikeTask();
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("rr_rider", LoginId);
                    params.put("rr_num", c_num);
                    liketask.execute(params);

                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });

        MapPolyline polyline = new MapPolyline();
        polyline.setTag(1000);
        polyline.setLineColor(Color.argb(255, 255, 51, 0)); // Polyline 컬러 지정.

        Thread uThread = new Thread() {

            @Override

            public void run() {

                try {
                    //서버에 올려둔 이미지 URL
                    URL url = new URL("http://13.209.229.237:8080/app/getGPX/gpx/"+gpx);
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

        button.setOnClickListener(v -> {
            ScrapTask scrap = new ScrapTask();
            Map<String, String> params = new HashMap<String, String>();
                params.put("c_num", c_num);
                params.put("r_rider", LoginId);
            scrap.execute(params);

        });

        button2.setOnClickListener(v->{
            Intent intent2=new Intent(CourseDetail.this, FollowStart.class);
            intent2.putExtra("gpx",gpx);
            intent2.putExtra("c_name",course_name);
            startActivity(intent2);

            finish();
            mapViewContainer.removeAllViews();
        });

        try {
            GetTask getTask = new GetTask();
            Map<String, String> params = new HashMap<String, String>();
            params.put("c_num", c_num);
            getTask.execute(params);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public class ScrapTask extends AsyncTask<Map<String, String>, Integer, String> {
        @Override
        protected String doInBackground(Map<String, String>... maps) {

            // Http 요청 준비 작업
            //URL은 현재 자기 아이피번호를 입력해야합니다.
            HttpClient.Builder http = new HttpClient.Builder("POST", "http://13.209.229.237:8080/app/riding/coursescrap");
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
            try {
                Toast.makeText(getApplicationContext(), "스크랩 보관함에 저장되었습니다.", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "저장에 실패하였습니다", Toast.LENGTH_SHORT).show();
            }
        }
    }

        public class GetTask extends AsyncTask<Map<String, String>, Integer, String> {
            @Override
            protected String doInBackground(Map<String, String>... maps) {
                // Http 요청 준비 작업
                //URL은 현재 자기 아이피번호를 입력해야합니다.
                HttpClient.Builder http = new HttpClient.Builder("GET", "http://13.209.229.237:8080/app/riding/course/"+c_num);
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
                try {

                    String tempData = s;

                    //json값을 받기위한 변수들


                    String dis = "";
                    String addr = "";
                    String name = "";
                    String avg = "";
                    String godo="";
                    String like="";
                    String date = "";
                    JSONArray jarray = new JSONArray(tempData);
                    for (int i = 0; i < jarray.length(); i++) {
                        JSONObject jObject = jarray.getJSONObject(i);
                        dis = jObject.getString("rr_distance");
                        addr = jObject.getString("rr_area");
                        name = jObject.getString("rr_name");
                        avg = jObject.getString("rr_avgspeed");
                        godo = jObject.getString("rr_high");
                        date = jObject.getString("rr_date");
                        like = jObject.getString("rr_like");
                    }
                    c_name.setText(name);
                    c_date.setText(date);
                    c_addr.setText(addr);
                    c_dis.setText(dis);
                    c_avg.setText(avg);
                    c_getgodo.setText(godo);
                    like_count.setText(like);

                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
        }

    public class LikeTask extends AsyncTask<Map<String, String>, Integer, String> {
        @Override
        protected String doInBackground(Map<String, String>... maps) {
            // Http 요청 준비 작업
            //URL은 현재 자기 아이피번호를 입력해야합니다.
            HttpClient.Builder http = new HttpClient.Builder("POST", "http://13.209.229.237:8080/app/riding/course/like");
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
            try{
                Log.d("JSON_RESULT", s);

            }catch(Exception e){
                Toast.makeText(getApplicationContext(), "저장에 실패했습니다. 관리자에게 문의하세요", Toast.LENGTH_LONG).show();
            }
        }
    }

    public class GetLikeTask extends AsyncTask<Map<String, String>, Integer, String> {
        @Override
        protected String doInBackground(Map<String, String>... maps) {
            // Http 요청 준비 작업
            //URL은 현재 자기 아이피번호를 입력해야합니다.
            HttpClient.Builder http = new HttpClient.Builder("GET", "http://13.209.229.237:8080/app/riding/course/"+c_num);
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
                Log.d("좋아요가져오기", s);


            }catch(Exception e){
                Toast.makeText(getApplicationContext(), "저장에 실패했습니다. 관리자에게 문의하세요", Toast.LENGTH_LONG).show();
            }
        }
    }

    public class GetLikeStatusTask extends AsyncTask<Map<String, String>, Integer, String> {
        @Override
        protected String doInBackground(Map<String, String>... maps) {
            // Http 요청 준비 작업
            //URL은 현재 자기 아이피번호를 입력해야합니다.
            HttpClient.Builder http = new HttpClient.Builder("GET", "http://13.209.229.237:8080/app/riding/course/like/"+c_num+"/"+LoginId);
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
                Log.d("좋아요 상태", s);

            }catch(Exception e){
                Toast.makeText(getApplicationContext(), "저장에 실패했습니다. 관리자에게 문의하세요", Toast.LENGTH_LONG).show();
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
