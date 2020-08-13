package com.capston.mtbcraft.Activity.Course;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.capston.mtbcraft.Activity.Competition.CompetitionList;
import com.capston.mtbcraft.Activity.Control.NoMtb;
import com.capston.mtbcraft.Activity.Danger.DangerList;
import com.capston.mtbcraft.Activity.Main.SubActivity;
import com.capston.mtbcraft.Activity.Mission.MissionList;
import com.capston.mtbcraft.Activity.Riding.FollowStart;
import com.capston.mtbcraft.Activity.Riding.MyReport;
import com.capston.mtbcraft.Activity.Scrap.MyScrap;
import com.capston.mtbcraft.R;
import com.capston.mtbcraft.dto.CourseReview;
import com.capston.mtbcraft.gpxparser.GPXParser;
import com.capston.mtbcraft.gpxparser.Gpx;
import com.capston.mtbcraft.gpxparser.Track;
import com.capston.mtbcraft.gpxparser.TrackPoint;
import com.capston.mtbcraft.gpxparser.TrackSegment;
import com.capston.mtbcraft.network.HttpClient;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import net.daum.mf.map.api.CameraUpdateFactory;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapPointBounds;
import net.daum.mf.map.api.MapPolyline;
import net.daum.mf.map.api.MapReverseGeoCoder;
import net.daum.mf.map.api.MapView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CourseDetail extends AppCompatActivity implements MapView.CurrentLocationEventListener, MapReverseGeoCoder.ReverseGeoCodingResultListener, TextWatcher {
    private String c_num, course_name, gpx, Nickname, register_Name="", Save_Path, change_Name="", r_image;
    private TextView c_name, c_date, c_addr, c_dis, c_avg, c_getgodo, like_count, r_rider_name, c_ride_time, c_rest_time, c_ride_max;
    private ImageView like_push, userImage, register_image, modify_image;
    private Button button,button2, share_bt, register_button;
    private DrawerLayout mDrawerLayout;
    private GPXParser mParser = new GPXParser();
    private Gpx parsedGpx = null;
    private MapView mapView;
    private String LoginId;
    private SharedPreferences auto;
    private LinearLayout testLayout;
    boolean register = false;
    private ConstraintLayout constraintLayout;
    private NestedScrollView nestedScrollView;
    private RecyclerView recycleView;
    private EditText editText;
    private Uri temp;
    private CourseReviewAdapter adapter3;
    private String rr_rider="", rr_date="", rr_area="", rr_name="", rr_like="";
    int rr_distance=0, rr_topspeed=0, rr_avgspeed=0, rr_high=0, rr_breaktime=0, rr_time=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coursedetail);

        share_bt = (Button) findViewById(R.id.share_bt);
        c_name = (TextView)findViewById(R.id.c_name );
        c_date = (TextView)findViewById(R.id.c_date );

        testLayout = (LinearLayout)findViewById(R.id.like_layout);

        c_ride_time = (TextView) findViewById(R.id.c_ride_time);
        c_rest_time = (TextView)findViewById(R.id.c_rest_time);
        c_dis = (TextView)findViewById(R.id.c_dis);
        c_ride_max = (TextView) findViewById(R.id.c_ride_max);
        c_avg = (TextView)findViewById(R.id.c_avg);
        c_getgodo = (TextView)findViewById(R.id.c_get);
        c_addr = (TextView)findViewById(R.id.c_addr);
        like_count = (TextView)findViewById(R.id.like_count);
        editText = (EditText)findViewById(R.id.register_edit);
        editText.addTextChangedListener(this);
        register_image = (ImageView)findViewById(R.id.register_image);
        register_button = (Button)findViewById(R.id.register_button);
        constraintLayout = (ConstraintLayout)findViewById(R.id.register_const);
        nestedScrollView = findViewById(R.id.nested);
        recycleView = (RecyclerView) findViewById(R.id.recycle_review);

        Save_Path = getFilesDir().getPath();

        button = (Button)findViewById(R.id.scrap_bt);
        button2 = (Button)findViewById(R.id.follow_bt);
        like_push = (ImageView)findViewById(R.id.like_push);
        r_rider_name = (TextView) findViewById(R.id.c_rider_name);
        Intent intent = new Intent(this.getIntent());

        c_num = intent.getStringExtra("c_num");
        gpx = intent.getStringExtra("c_gpx");
        course_name = intent.getStringExtra("c_name");


        mapView = new MapView(CourseDetail.this);
        ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);

        mapView.setCurrentLocationEventListener(this);
        mapView.isShowingCurrentLocationMarker();


        /* 로그인 정보 가져오기 */
        auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
        LoginId = auto.getString("LoginId", "");
        Nickname = auto.getString("r_nickname", "");
        r_image = auto.getString("r_image", "");


        /* 드로우 레이아웃 네비게이션 부분들 */
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setDisplayHomeAsUpEnabled(true);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        View header = navigationView.getHeaderView(0);
        userImage = (ImageView) header.findViewById(R.id.user_image);
        TextView InFoUserId = (TextView) header.findViewById(R.id.infouserid);
        InFoUserId.setText(Nickname + "님 환영합니다");

        Bitmap user_image = BitmapFactory.decodeFile(new File(getFilesDir().getPath() + "/" + r_image).getAbsolutePath());
        userImage.setImageBitmap(user_image);

        navigationView.setNavigationItemSelectedListener(menuItem -> {
            menuItem.setChecked(true);
            mDrawerLayout.closeDrawers();

            int id = menuItem.getItemId();
            switch (id) {
                //홈
                case R.id.nav_home:
                    Intent home = new Intent(getApplicationContext(), SubActivity.class);
                    home.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(home);
                    break;

                //라이딩 기록
                case R.id.nav_mylist:
                    Intent home2 = new Intent(getApplicationContext(), SubActivity.class);
                    home2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(home2);
                    Intent mylist=new Intent(getApplicationContext(), MyReport.class);
                    startActivity(mylist);
                    finish();
                    break;

                //코스보기
                case R.id.nav_courselist:
                    Intent home3 = new Intent(getApplicationContext(), SubActivity.class);
                    home3.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(home3);
                    Intent courselist=new Intent(getApplicationContext(), CourseList.class);
                    courselist.putExtra("rider_id", LoginId);
                    startActivity(courselist);
                    finish();
                    break;

                //코스검색
                case R.id.nav_course_search:
                    Intent home4 = new Intent(getApplicationContext(), SubActivity.class);
                    home4.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(home4);
                    Intent coursesearch=new Intent(getApplicationContext(), CourseSearch.class);
                    startActivity(coursesearch);
                    finish();
                    break;

                //스크랩 보관함
                case R.id.nav_course_get:
                    Intent home5 = new Intent(getApplicationContext(), SubActivity.class);
                    home5.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(home5);
                    Intent courseget=new Intent(getApplicationContext(), MyScrap.class);
                    startActivity(courseget);
                    finish();
                    break;

                //경쟁전
                case R.id.nav_comp:
                    Intent home6 = new Intent(getApplicationContext(), SubActivity.class);
                    home6.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(home6);
                    Intent comp=new Intent(getApplicationContext(), CompetitionList.class);
                    startActivity(comp);
                    finish();
                    break;

                //미션
                case R.id.nav_mission:
                    Intent home7 = new Intent(getApplicationContext(), SubActivity.class);
                    home7.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(home7);
                    Intent mission=new Intent(getApplicationContext(), MissionList.class);
                    startActivity(mission);
                    finish();
                    break;

                case R.id.friend_chodae:
                    Intent friend = new Intent();
                    friend.setAction(Intent.ACTION_SEND);
                    friend.setType("text/plain");
                    friend.putExtra(Intent.EXTRA_SUBJECT, LoginId + "님이 귀하를 초대합니다. 앱 설치하기");
                    friend.putExtra(Intent.EXTRA_TEXT, "tmarket://details?id=com.capston.mtbcraft");

                    Intent chooser = Intent.createChooser(friend, "초대하기");
                    startActivity(chooser);
                    break;

                //위험구역
                case R.id.nav_danger:
                    Intent home8 = new Intent(getApplicationContext(), SubActivity.class);
                    home8.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(home8);
                    Intent danger = new Intent(getApplicationContext(), DangerList.class);
                    startActivity(danger);
                    finish();
                    break;

                //위험구역
                case R.id.no_mtb:
                    Intent home9 = new Intent(getApplicationContext(), SubActivity.class);
                    home9.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(home9);
                    Intent nomtb = new Intent(getApplicationContext(), NoMtb.class);
                    startActivity(nomtb);
                    finish();
                    break;
            }
            return true;
        });

        try{
            GetLikeTask liketask = new GetLikeTask();
            liketask.execute();

            GetLikeStatusTask likestatustask = new GetLikeStatusTask();
            likestatustask.execute();

            new getReviewTask().execute();
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

        share_bt.setOnClickListener(v -> {
            Intent intent1 = new Intent();
            intent1.setAction(Intent.ACTION_SEND);
            intent1.setType("text/plain");
            intent1.putExtra(Intent.EXTRA_SUBJECT, Nickname+"님이"+rr_name+"코스를 공유하였습니다. 해당URL을 누르면 해당 페이지로 이동합니다. ");
            intent1.putExtra(Intent.EXTRA_TEXT, "http://13.209.229.237:8080/app/riding/course_share/"+c_num);

            Intent chooser = Intent.createChooser(intent1, "친구에게 공유하기");
            startActivity(chooser);
            mapViewContainer.removeAllViews();
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
            Log.d("스크랩",c_num+" "+LoginId);
            ScrapTask scrap = new ScrapTask();
            Map<String, String> params = new HashMap<String, String>();
            params.put("c_num", c_num);
            params.put("ss_rider", LoginId);
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

        register_image.setOnClickListener(v -> {
            register = true;
            Intent register = new Intent();
            register.setType("image/*");
            register.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(register,1);
        });

        //등록 버튼 클릭 시
        register_button.setOnClickListener(v -> {
            InsertTask insertTask = new InsertTask();
            Map<String, String> params = new HashMap<String, String>();

            params.put("cr_rider", LoginId);
            params.put("cr_rnum", String.valueOf(c_num));
            params.put("cr_content", String.valueOf(editText.getText()));
            if ( register_Name == null )
                register_Name = "null";
            params.put("cr_image", register_Name);

            insertTask.execute(params);

            Log.e("레지스터 이미지", register_Name);

            UploadTask2 uploadTask = new UploadTask2(getFilesDir().getPath() + "/", register_Name, register_image);
            uploadTask.execute();

            editText.setText("");
            register_image.setImageResource(R.drawable.image_plus);

            try {
                new getReviewTask().execute();
                recycleView.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                e.printStackTrace();
            }

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

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        Log.i("Detail", "before"+s);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        Log.i("Detail", "on"+s);
    }

    @Override
    public void afterTextChanged(Editable s) {
        Log.i("Detail", "after"+s.toString());
    }


    public class ScrapTask extends AsyncTask<Map<String, String>, Integer, String> {
        @Override
        protected String doInBackground(Map<String, String>... maps) {

            // Http 요청 준비 작업
            //URL은 현재 자기 아이피번호를 입력해야합니다.
            HttpClient.Builder http = new HttpClient.Builder("POST", "/app/riding/coursescrap");
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
            HttpClient.Builder http = new HttpClient.Builder("GET", "/app/riding/course/"+c_num);
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
                String tempData = s;

                //json값을 받기위한 변수들
                JSONArray jarray = new JSONArray(tempData);

                for (int i = 0; i < jarray.length(); i++) {
                    JSONObject jObject = jarray.getJSONObject(i);

                    /*상단 데이터 */
                    rr_rider = jObject.getString("rr_rider");
                    rr_date = jObject.getString("rr_date");
                    rr_name = jObject.getString("rr_name");

                    /*하단 데이터 */
                    rr_distance = jObject.getInt("rr_distance");
                    rr_topspeed = jObject.getInt("rr_topspeed");
                    rr_avgspeed = jObject.getInt("rr_avgspeed");
                    rr_time = jObject.getInt("rr_time");
                    rr_high = jObject.getInt("rr_high");
                    rr_breaktime = jObject.getInt("rr_breaktime");
                    rr_like = jObject.getString("rr_like");
                    rr_area = jObject.getString("rr_area");
                }

                /*상단 데이터 */
                c_name.setText(rr_name);
                r_rider_name.setText(rr_rider);
                c_date.setText(rr_date);

                /* 하단 데이터 */
                int hour, min, sec = rr_time;
                min = sec/60; hour = min/60; sec = sec % 60; min = min % 60;

                Log.d("라이딩시간",hour+"시간 "+min+"분 "+sec+"초");
                //휴식시간 계산
                int b_hour, b_min, b_sec = rr_breaktime;
                b_min = b_sec/60; b_hour = b_min/60; b_sec = b_sec % 60; b_min = b_min % 60;

                int des = rr_distance;
                float km = (float) (des/1000.0);
                String total = km+"Km";

                c_ride_max.setText(rr_topspeed+"km/h");
                c_avg.setText(rr_avgspeed+"km/h");
                c_getgodo.setText(rr_high+"m");

                String time = hour+"시간 "+min+"분 "+sec+"초";
                String r_time = b_hour+"시간 "+b_min+"분 "+b_sec+"초";
                c_ride_time.setText(time);
                c_rest_time.setText(r_time);

                c_addr.setText(rr_area);
                like_count.setText(rr_like);
                c_dis.setText(total);

            } catch (Exception e) {
                e.printStackTrace();

            }
        }
    }

    public class getReviewTask extends AsyncTask<Map<String, String>, Integer, String> {

        @Override
        protected String doInBackground(Map<String, String>... maps) {
            // Http 요청 준비 작업
            //URL은 현재 자기 아이피번호를 입력해야합니다.
            HttpClient.Builder http = new HttpClient.Builder("GET", "/app/getCourseReview/" + c_num);
            // Parameter 를 전송한다.

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
                String tempData = s;

                Gson gson = new Gson();
                ArrayList<CourseReview> reviewList = new ArrayList<>();
                CourseReview[] items = gson.fromJson(tempData, CourseReview[].class);

                for (CourseReview item : items) {
                    reviewList.add(item);

                }

                adapter3 = new CourseReviewAdapter(CourseDetail.this, getApplicationContext(), reviewList, Save_Path);
                recycleView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

                if (reviewList.size() < 1) {
                    recycleView.setVisibility(View.GONE);

                    DrawerLayout.LayoutParams param = (DrawerLayout.LayoutParams) mDrawerLayout.getLayoutParams();
                    param.setMargins(0, 0, 0, 200);
                    mDrawerLayout.setLayoutParams(param);
                    mDrawerLayout.requestLayout();
                }
                recycleView.setAdapter(adapter3);
                recycleView.requestLayout();

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
            HttpClient.Builder http = new HttpClient.Builder("POST", "/app/riding/course/like");
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
                GetTask getTask = new GetTask();
                Map<String, String> params = new HashMap<String, String>();
                params.put("c_num", c_num);
                getTask.execute(params);
                testLayout.requestLayout();
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
            HttpClient.Builder http = new HttpClient.Builder("GET", "/app/riding/course/"+c_num);
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
            HttpClient.Builder http = new HttpClient.Builder("GET", "/app/riding/course/like/"+c_num+"/"+LoginId);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                if (register == false) {
                    try {
                        // 선택한 이미지에서 비트맵 생성
                        if ( data.getData() != null ) {
                            InputStream in = getContentResolver().openInputStream(data.getData());
                            temp = data.getData();
                            Bitmap img = BitmapFactory.decodeStream(in);
                            in.close();
                            modify_image.setImageBitmap(img);
                            change_Name = getImageNameToUri(data.getData());
                        }

                        // 이미지 표시
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else {
                    try {
                        // 선택한 이미지에서 비트맵 생성
                        InputStream in = getContentResolver().openInputStream(data.getData());
                        temp = data.getData();
                        Bitmap img = BitmapFactory.decodeStream(in);
                        in.close();
                        register_image.setImageBitmap(img);
                        register_Name = getImageNameToUri(data.getData());

                        // 이미지 표시
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public String getImageNameToUri(Uri data) {
        String[] proj = {MediaStore.Images.Media.DISPLAY_NAME};
        Cursor cursor = getContentResolver().query(data, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);
        cursor.moveToFirst();
        String imgPath = cursor.getString(column_index);
        String imgName = imgPath.substring(imgPath.lastIndexOf("/") + 1);

        return imgName;
    }

    public class DownloadTask extends AsyncTask<Map<String, String>, Integer, String> {
        String directory = "";
        String url = "";
        ImageView imageView;
        String LocalPath;

        public DownloadTask(String directory, String url, ImageView imageView) {
            this.directory = directory;
            this.url = url;
            this.imageView = imageView;
        }

        @Override
        protected String doInBackground(Map<String, String>... maps) {

            File dir = new File(Save_Path);
            //상위 디렉토리가 존재하지 않을 경우 생성

            if (!dir.exists()) {
                dir.mkdirs();
            }

            String fileURL = "http://13.209.229.237:8080/app/getGPX/" + directory + "/" + url;
            LocalPath = Save_Path + "/" + url;

            if (new File(Save_Path + "/" + url).exists() == false) {
                URL imgUrl = null;
                try {
                    imgUrl = new URL(fileURL);

                    //서버와 접속하는 클라이언트 객체 생성
                    HttpURLConnection conn = (HttpURLConnection) imgUrl.openConnection();
                    int response = conn.getResponseCode();

                    File file = new File(LocalPath);

                    InputStream is = conn.getInputStream();
                    OutputStream outStream = new FileOutputStream(file);

                    byte[] buf = new byte[1024];
                    int len = 0;

                    while ((len = is.read(buf)) > 0) {
                        outStream.write(buf, 0, len);
                    }

                    outStream.close();
                    is.close();
                    conn.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            Bitmap myBitmap1, myBitmap2;
            try {
/*                if ( itemList.get(position).getR_image() != null)
                    myBitmap1 = BitmapFactory.decodeFile(new File(Save_Path + "/" + itemList.get(position).getR_image()).getAbsolutePath());
                else
                    myBitmap1 = BitmapFactory.decodeFile(String.valueOf(new File(Save_Path + "/" + "noImage.jpg")));

                if ( itemList.get(position).getCr_images() != null)
                    myBitmap2 = BitmapFactory.decodeFile(new File(Save_Path + "/" + itemList.get(position).getCr_images()).getAbsolutePath());
                else
                    myBitmap2 = null;*/
                imageView.setImageBitmap(BitmapFactory.decodeFile(String.valueOf(new File(LocalPath))));
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "저장에 실패했습니다. 관리자에게 문의하세요", Toast.LENGTH_LONG).show();
            }
        }
    }

    public class ModifyDownTask extends AsyncTask<Map<String, String>, Integer, String> {
        String directory = "";
        String url = "";
        ImageView imageView;

        public ModifyDownTask(String directory, String url, ImageView imageView) {
            this.directory = directory;
            this.url = url;
            this.imageView = imageView;
        }

        @Override
        protected String doInBackground(Map<String, String>... maps) {

            File dir = new File(Save_Path);
            //상위 디렉토리가 존재하지 않을 경우 생성

            if (!dir.exists()) {
                dir.mkdirs();
            }

            String fileURL = "http://13.209.229.237:8080/app/getGPX/" + directory + "/" + url;
            String LocalPath = Save_Path + "/" + url;

            if (new File(Save_Path + "/" + url).exists() == false) {
                URL imgUrl = null;
                try {
                    imgUrl = new URL(fileURL);

                    //서버와 접속하는 클라이언트 객체 생성
                    HttpURLConnection conn = (HttpURLConnection) imgUrl.openConnection();
                    int response = conn.getResponseCode();

                    File file = new File(LocalPath);

                    InputStream is = conn.getInputStream();
                    OutputStream outStream = new FileOutputStream(file);

                    byte[] buf = new byte[1024];
                    int len = 0;

                    while ((len = is.read(buf)) > 0) {
                        outStream.write(buf, 0, len);
                    }

                    outStream.close();
                    is.close();
                    conn.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {

        }
    }

    public class UploadTask extends AsyncTask<Map<String, String>, Integer, String> {
        String filepath = "";
        String fileName = "";
        ImageView imageView;

        public UploadTask(String filepath, String fileName, ImageView imageView) {
            this.filepath = filepath;
            this.fileName = fileName;
            this.imageView = imageView;
        }

        @Override
        protected String doInBackground(Map<String, String>... maps) {

            final String url_address = "http://13.209.229.237:8080/android/fileUpload/review/" + change_Name;

            String str; //결과를 저장할 변수

            String fileName = "";
            try {
                InputStream tempIs = getContentResolver().openInputStream(temp);
                File tempFile = new File(Save_Path + "/" + change_Name);

                // 복사
                FileOutputStream outputStream = new FileOutputStream(tempFile);
                int read2;
                byte[] bytes2 = new byte[1024];

                while ((read2 = tempIs.read(bytes2)) != -1) {
                    outputStream.write(bytes2, 0, read2);
                }

                FileInputStream fis = new FileInputStream(tempFile);
                URL url = new URL(url_address);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                //웹서버를 통해 입출력 가능하도록 설정
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(false);//캐쉬 사용하지 않음
                conn.setRequestMethod("POST");
                //정해진 시간 내에 재접속할 경우 소켓을 새로 생성하지 않고 기존연결 사용
                conn.setRequestProperty("Connection", "Keep-Alive");
                //첨부파일에 대한 정보
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=files");


                DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
                //form-data; name=파일변수명; filename="첨부파일이름"
                dos.writeBytes("--files\r\n"); // --은 파일 시작 알림 표시
                dos.writeBytes("Content-Disposition: form-data;"
                        + "name=\"file1\";"
                        + "filename=\"" + fileName
                        + "\"" + "\r\n");

                dos.writeBytes("\r\n");//줄바꿈 문자
                int bytes = fis.available();
                int maxBufferSize = 1024;
                //Math.min(A, B)둘중 작은값;
                int bufferSize = Math.min(bytes, maxBufferSize);
                byte[] buffer = new byte[bufferSize];
                int read = fis.read(buffer, 0, bufferSize);
                Log.d("Test", "image byte is " + read);
                while (read > 0) {
                    //서버에 업로드
                    dos.write(buffer, 0, bufferSize);
                    bytes = fis.available();
                    bufferSize = Math.min(bytes, maxBufferSize);
                    //읽은 바이트 수
                    read = fis.read(buffer, 0, bufferSize);
                }
                dos.writeBytes("\r\n");//줄바꿈 문자

                dos.writeBytes("--files--\r\n");
                fis.close();
                dos.flush();
                dos.close();

                //서버 응답 처리
                int ch;

                int status = conn.getResponseCode();
                Log.e("status", String.valueOf(status));
                Log.e("method", conn.getRequestMethod());
                InputStream is = conn.getInputStream();
                StringBuffer sb = new StringBuffer();
                while ((ch = is.read()) != -1) { // 내용이 없을 때까지 반복
                    sb.append((char) ch); // 문자 읽어서 저장
                }
                // 스트링.trim() 스트링의 좌우 공백 제거
                str = sb.toString().trim();
                if (str.equals("success")) {
                    str = "파일이 업로드되었습니다.";
                } else if (str.equals("fail")) {
                    str = "파일 업로드 실패...";
                }

                is.close();
                conn.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                ModifyDownTask modifyDownTask = new ModifyDownTask("review", change_Name, imageView);
                modifyDownTask.execute();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "저장에 실패했습니다. 관리자에게 문의하세요", Toast.LENGTH_LONG).show();
            }
        }
    }

    public class UploadTask2 extends AsyncTask<Map<String, String>, Integer, String> {
        String filepath = "";
        String fileName = "";
        ImageView imageView;

        public UploadTask2(String filepath, String fileName, ImageView imageView) {
            this.filepath = filepath;
            this.fileName = fileName;
            this.imageView = imageView;
        }

        @Override
        protected String doInBackground(Map<String, String>... maps) {

            final String url_address = "http://13.209.229.237:8080/android/fileUpload/review/" + register_Name;

            String str; //결과를 저장할 변수

            String fileName = "";
            try {
                InputStream tempIs = getContentResolver().openInputStream(temp);
                File tempFile = new File(Save_Path + "/" + register_Name);

                // 복사
                FileOutputStream outputStream = new FileOutputStream(tempFile);
                int read2;
                byte[] bytes2 = new byte[1024];

                while ((read2 = tempIs.read(bytes2)) != -1) {
                    outputStream.write(bytes2, 0, read2);
                }

                FileInputStream fis = new FileInputStream(tempFile);
                URL url = new URL(url_address);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                //웹서버를 통해 입출력 가능하도록 설정
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(false);//캐쉬 사용하지 않음
                conn.setRequestMethod("POST");
                //정해진 시간 내에 재접속할 경우 소켓을 새로 생성하지 않고 기존연결 사용
                conn.setRequestProperty("Connection", "Keep-Alive");
                //첨부파일에 대한 정보
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=files");


                DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
                //form-data; name=파일변수명; filename="첨부파일이름"
                dos.writeBytes("--files\r\n"); // --은 파일 시작 알림 표시
                dos.writeBytes("Content-Disposition: form-data;"
                        + "name=\"file1\";"
                        + "filename=\"" + fileName
                        + "\"" + "\r\n");

                dos.writeBytes("\r\n");//줄바꿈 문자
                int bytes = fis.available();
                int maxBufferSize = 1024;
                //Math.min(A, B)둘중 작은값;
                int bufferSize = Math.min(bytes, maxBufferSize);
                byte[] buffer = new byte[bufferSize];
                int read = fis.read(buffer, 0, bufferSize);
                Log.d("Test", "image byte is " + read);
                while (read > 0) {
                    //서버에 업로드
                    dos.write(buffer, 0, bufferSize);
                    bytes = fis.available();
                    bufferSize = Math.min(bytes, maxBufferSize);
                    //읽은 바이트 수
                    read = fis.read(buffer, 0, bufferSize);
                }
                dos.writeBytes("\r\n");//줄바꿈 문자

                dos.writeBytes("--files--\r\n");
                fis.close();
                dos.flush();
                dos.close();

                //서버 응답 처리
                int ch;

                int status = conn.getResponseCode();
                Log.e("status", String.valueOf(status));
                Log.e("method", conn.getRequestMethod());
                InputStream is = conn.getInputStream();
                StringBuffer sb = new StringBuffer();
                while ((ch = is.read()) != -1) { // 내용이 없을 때까지 반복
                    sb.append((char) ch); // 문자 읽어서 저장
                }
                // 스트링.trim() 스트링의 좌우 공백 제거
                str = sb.toString().trim();
                if (str.equals("success")) {
                    str = "파일이 업로드되었습니다.";
                } else if (str.equals("fail")) {
                    str = "파일 업로드 실패...";
                }

                is.close();
                conn.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                ModifyDownTask modifyDownTask = new ModifyDownTask("review", register_Name, imageView);
                modifyDownTask.execute();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "저장에 실패했습니다. 관리자에게 문의하세요", Toast.LENGTH_LONG).show();
            }
        }
    }

    public class UpdateTask extends AsyncTask<Map<String, String>, Integer, String> {
        @Override
        protected String doInBackground(Map<String, String>... maps) {
            // Http 요청 준비 작업
            //URL은 현재 자기 아이피번호를 입력해야합니다.
            HttpClient.Builder http = new HttpClient.Builder("POST", "/app/updateCourseReview");
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

        }
    }

    public class DeleteTask extends AsyncTask<Map<String, String>, Integer, String> {
        @Override
        protected String doInBackground(Map<String, String>... maps) {
            // Http 요청 준비 작업
            //URL은 현재 자기 아이피번호를 입력해야합니다.
            HttpClient.Builder http = new HttpClient.Builder("POST", "/app/deleteCourseReview");
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

        }
    }

    public class InsertTask extends AsyncTask<Map<String, String>, Integer, String> {
        @Override
        protected String doInBackground(Map<String, String>... maps) {
            // Http 요청 준비 작업
            //URL은 현재 자기 아이피번호를 입력해야합니다.
            HttpClient.Builder http = new HttpClient.Builder("POST", "/app/insertCourseReview");
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

        }
    }

    // 코스 리뷰 어댑터 - 사진 불러와서 setImageBitmap 떄문에 여기다 어댑터 붙임
    public class CourseReviewAdapter extends RecyclerView.Adapter<CourseReviewAdapter.CourseReviewHolder>{
        public Activity activity;
        public Context mContext;
        public ArrayList<CourseReview> itemList;
        String Save_Path;

        public CourseReviewAdapter(Activity activity, Context mContext, ArrayList<CourseReview> itemList, String save_path) {
            this.activity = activity;
            this.mContext = mContext;
            this.itemList = itemList;
            this.Save_Path = save_path;
        }

        @NonNull
        @Override
        public CourseReviewAdapter.CourseReviewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_coursereviewitem , viewGroup,false);

            return new CourseReviewAdapter.CourseReviewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull CourseReviewAdapter.CourseReviewHolder holder, int position) {

            if (itemList.get(position).getR_image() != null) {
                DownloadTask downloadTask = new DownloadTask("rider", itemList.get(position).getR_image(), holder.imageView2);
                downloadTask.execute();
            }
            else {
                DownloadTask downloadTask = new DownloadTask("rider", "noImage.jpg", holder.imageView2);
                downloadTask.execute();
            }

            if (itemList.get(position).getCr_images() != null) {
                DownloadTask downloadTask = new DownloadTask("review", itemList.get(position).getCr_images(), holder.imageView2);
                downloadTask.execute();
            }



            SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy-MM-dd");
            String temp = sdfNow.format(itemList.get(position).getCr_time());

            //holder.imageView.setImageBitmap(myBitmap);
            holder.textView1.setText(itemList.get(position).getR_nickname());
            holder.textView2.setText(temp);
            holder.textView3.setText(itemList.get(position).getCr_content());

            Bitmap user_image = BitmapFactory.decodeFile(new File(getFilesDir().getPath() + "/" + r_image).getAbsolutePath());
            holder.imageView1.setImageBitmap(user_image);



            if ( itemList.get(position).getCr_images() == null)
                holder.imageView2.setVisibility(View.GONE);

            //수정 버튼 클릭 시
            holder.button1.setOnClickListener( v -> {
                View view1 = v;
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                Bitmap myBitmap2;

                if ( itemList.get(position).getCr_images() != null)
                    myBitmap2 = BitmapFactory.decodeFile(new File(Save_Path + "/" + itemList.get(position).getCr_images()).getAbsolutePath());
                else
                    myBitmap2 = null;

                View view = LayoutInflater.from(activity)
                        .inflate(R.layout.activity_coursereviewmodify, null, false);
                builder.setView(view);

                final AlertDialog dialog = builder.create();

                modify_image = view.findViewById(R.id.modify_image);
                Button button3 = view.findViewById(R.id.button);
                Button button4 = view.findViewById(R.id.cr_cancel);
                EditText editText2 = view.findViewById(R.id.editText);

                editText2.setText(itemList.get(position).getCr_content());
                modify_image.setImageBitmap(myBitmap2);
                if ( itemList.get(position).getCr_images() == null)
                    modify_image.setImageResource(R.drawable.image_plus);

                // 수정에서 이미지 클릭 시
                modify_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        register = false;
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(intent,1);
                    }

                });

                // 수정에서 확인 버튼 클릭 시
                button3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if ( change_Name.equals("")){
                            change_Name = itemList.get(position).getCr_images();
                            holder.imageView2.setImageBitmap(BitmapFactory.decodeFile(String.valueOf(new File(Save_Path + "/" + itemList.get(position).getCr_images()))));
                        }
                        long now = System.currentTimeMillis();
                        Date date = new Date(now);

                        itemList.get(position).setCr_images(change_Name);
                        itemList.get(position).setCr_content(String.valueOf(editText2.getText()));
                        itemList.get(position).setCr_time(date);

                        notifyItemChanged(position);

                        // 수정한 파일 서버 업로드, 내부저장소에 다운, 이미지뷰에 띄워줌
                        UploadTask uploadTask = new UploadTask(getFilesDir().getPath() + "/", change_Name, holder.imageView2);
                        uploadTask.execute();

                        UpdateTask updateTask = new UpdateTask();
                        Map<String, String> params = new HashMap<String, String>();

                        params.put("cr_num", String.valueOf(itemList.get(position).getCr_num()));
                        params.put("cr_content", itemList.get(position).getCr_content());
                        params.put("cr_image", itemList.get(position).getCr_images());
                        params.put("cr_time", String.valueOf(itemList.get(position).getCr_time()));

                        updateTask.execute(params);

                        change_Name = "";

                        dialog.dismiss();
                    }
                });

                button4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            });

            holder.cr_delete.setOnClickListener( v -> {
                DeleteTask deleteTask = new DeleteTask();
                Map<String, String> params = new HashMap<String, String>();

                params.put("cr_num", String.valueOf(itemList.get(position).getCr_num()));

                deleteTask.execute(params);


                try {
                    new getReviewTask().execute();
                }catch ( Exception e){

                }

            });
        }

        @Override
        public int getItemCount() {
            return itemList.size();
        }

        class CourseReviewHolder extends RecyclerView.ViewHolder {
            public TextView textView1, textView2, textView3;
            public ImageView imageView1, imageView2;
            public LinearLayout viewClick;
            public final View mView;
            public Button button1, button2, cr_delete;

            public CourseReviewHolder( View itemView) {
                super(itemView);
                mView = itemView;
                textView1 = itemView.findViewById(R.id.comp_name);
                textView2 = itemView.findViewById(R.id.comp_day);
                button1 = itemView.findViewById(R.id.button);
                button2 = itemView.findViewById(R.id.cr_cancel);
                imageView1 = itemView.findViewById(R.id.imageView5);
                imageView2 = itemView.findViewById(R.id.modify_image);
                textView3 = itemView.findViewById(R.id.textView9);
                viewClick = itemView.findViewById(R.id.viewClick);
                cr_delete = itemView.findViewById(R.id.cr_delete);
            }
        }

    }
}
