package com.capston.mtbcraft.Activity.Competition;

import android.Manifest;
import android.app.*;
import android.content.Context;
import android.content.*;
import android.content.pm.PackageManager;
import android.graphics.*;
import android.location.*;
import android.net.Uri;
import android.os.*;
import android.util.Log;
import android.view.*;
import android.webkit.*;
import android.widget.*;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.capston.mtbcraft.Activity.Control.NoMtb;
import com.capston.mtbcraft.Activity.Course.CourseList;
import com.capston.mtbcraft.Activity.Course.CourseSearch;
import com.capston.mtbcraft.Activity.Danger.DangerList;
import com.capston.mtbcraft.Activity.Main.SubActivity;
import com.capston.mtbcraft.Activity.Mission.MissionList;
import com.capston.mtbcraft.Activity.Riding.*;
import com.capston.mtbcraft.Activity.Scrap.MyScrap;
import com.capston.mtbcraft.Recycler.Adapter.CompClubAdapter;
import com.capston.mtbcraft.Recycler.Adapter.CompScoreAdapter;
import com.capston.mtbcraft.dto.Badge;
import com.capston.mtbcraft.dto.CompClub;
import com.capston.mtbcraft.dto.CompScore;
import com.capston.mtbcraft.network.HttpClient;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import com.capston.mtbcraft.R;
public class CompetitionDetail extends AppCompatActivity implements LocationListener{
    private String comp_num, comp_period, comp_badge, comp_course, comp_image, comp_content, comp_name, Save_Path, badge_path, c_gpx, c_name, comp_point, LoginId, Nickname, r_image;
    private TextView name_textView, period_textView2, textView3, textView4;
    private Button joinButton;
    private ImageView imageView6, imageView7, imageView8, imageView9;
    private WebView webView;
    private RecyclerView recyclerView, recyclerView2;
    private LocationManager locationManager;
    private Location lastKnownLocation;
    private String[] a = new String[4];
    private ArrayList<CompClub> clubItemList;
    private ArrayList<CompScore> newScoreItemList;
    private int clubCount = 0, badgeCount = 0, scoreCount = 0;
    private DrawerLayout mDrawerLayout;
    SharedPreferences auto;
    ImageView userImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_competitiondetail);

        name_textView = findViewById(R.id.comp_name2);
        period_textView2 = findViewById(R.id.comp_day2);
        imageView6 = findViewById(R.id.imageView6);
        imageView7 = findViewById(R.id.badge_image);
        imageView8 = findViewById(R.id.badge_image2);
        imageView9 = findViewById(R.id.badge_image3);
        textView3 = findViewById(R.id.comp_content);
        webView = findViewById(R.id.comp_Course);
        recyclerView = findViewById(R.id.compClub_recycle);
        textView4 = findViewById(R.id.textView14);
        recyclerView2 = findViewById(R.id.compScore_recycle);
        joinButton = findViewById(R.id.compjoin_button);

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


        Intent intent = new Intent(this.getIntent());
        comp_num = intent.getStringExtra("comp_num");
        comp_period = intent.getStringExtra("comp_period");
        comp_badge = intent.getStringExtra("comp_badge");
        comp_course = intent.getStringExtra("comp_course");
        comp_image = intent.getStringExtra("comp_image");
        comp_content = intent.getStringExtra("comp_content");
        comp_name = intent.getStringExtra("comp_name");
        Save_Path = intent.getStringExtra("save_path");
        c_gpx = intent.getStringExtra("c_gpx");
        c_name = intent.getStringExtra("c_name");
        comp_point = intent.getStringExtra("comp_point");

        DownloadgpxTask downloadgpxTask = new DownloadgpxTask("gpx", c_gpx);
        downloadgpxTask.execute();


        try {
            new GetTask().execute();
            new GetTask2().execute();
            new GetTask3().execute();

        } catch (Exception e) {

        }

        //권한 체크
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (lastKnownLocation != null) {
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000,10, this);

        joinButton.setOnClickListener( v -> {

            double latitude = lastKnownLocation.getLatitude();
            double longitude = lastKnownLocation.getLongitude();

            double kmLat = 1 / 88.74;
            double kmLot = 1 / 109.958489129649955;

            if ( (Double.parseDouble(a[1])-kmLat) < latitude && latitude < ( Double.parseDouble(a[1] ) + kmLat ) &&
                    ( Double.parseDouble(a[3])-kmLot) < longitude && longitude < ( Double.parseDouble(a[3]) + kmLot ))
            {

                Context context = v.getContext();
                Intent intent3 = new Intent(v.getContext(), FollowStart.class);
                intent3.putExtra("comp_num", comp_num);
                intent3.putExtra("check", 1);
                intent3.putExtra("comp_gpx", c_gpx);
                intent3.putExtra("c_name", c_name);
                intent3.putExtra("comp_name", comp_name);
                intent3.putExtra("comp_point", comp_point);
                intent3.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                v.getContext().startActivity(intent3);

                finish();
            }
            else{
                // 다이얼로그 바디
                AlertDialog.Builder alert_confirm = new AlertDialog.Builder((Context) v.getContext());
                // 메세지
                alert_confirm.setMessage("지정된 코스 위치가 아닙니다\n\n현재 위치 : " + latitude + "\n경도 : " + longitude);
                // 확인 버튼 리스너
                alert_confirm.setPositiveButton("확인", null);
                // 다이얼로그 생성
                AlertDialog alert = alert_confirm.create();
                // 다이얼로그 타이틀
                alert.setTitle("코스 위치를 확인해주십시요");
                // 다이얼로그 보기
                alert.show();
            }

        });

        Bitmap comp_Image = BitmapFactory.decodeFile(new File(Save_Path + "/" + comp_image).getAbsolutePath());


        name_textView.setText(comp_name);
        period_textView2.setText(comp_period);
        imageView6.setImageBitmap(comp_Image);
        textView3.setText(comp_content);

        webView.getSettings().setJavaScriptEnabled(true);//자바스크립트 허용

        WebSettings wbs = webView.getSettings();
        wbs.setLoadWithOverviewMode(true);
        wbs.setJavaScriptEnabled(true);
        webView.loadUrl("http://13.209.229.237:8080/app/getAppCompCourse?c_num=" + comp_course);//웹뷰 실행
        webView.setWebChromeClient(new WebChromeClient());//웹뷰에 크롬 사용 허용//이 부분이 없으면 크롬에서 alert가 뜨지 않음
        webView.setWebViewClient(new WebViewClientClass());//새창열기 없이 웹뷰 내에서 다시 열기//페이지 이동 원활히 하기위해 사용

    }

    @Override
    public void onLocationChanged(Location location) {
        lastKnownLocation = location;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

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
    public void onProviderEnabled(String provider) {
        //권한 체크
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        // 위치정보 업데이트
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000,5, this);
    }

    @Override
    public void onProviderDisabled(String provider) {

    }


    // 클럽 순위 및 정보 가져오기
    public class GetTask extends AsyncTask<Map<String, String>, Integer, String> {

        @Override
        protected String doInBackground(Map<String, String>... maps) {

            // Http 요청 준비 작업
            //URL은 현재 자기 아이피번호를 입력해야합니다.
            HttpClient.Builder http = new HttpClient.Builder("GET", "/app/getCompClub/" + comp_num);
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
            try{
                String tempData = s;

                Gson gson = new Gson();
                clubItemList = new ArrayList<>();
                CompClub[] items = gson.fromJson(tempData, CompClub[].class);

                for ( int i = 0; i<5 && i<items.length; i++){
                    CompClub item = items[i];
                    clubItemList.add(item);

                    DownloadClubTask downloadTask = new DownloadClubTask("club", item.getCb_image());
                    downloadTask.execute();
                }

            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    // 경쟁전 배지 가져오기
    public class GetTask2 extends AsyncTask<Map<String, String>, Integer, String> {

        @Override
        protected String doInBackground(Map<String, String>... maps) {

            // Http 요청 준비 작업
            //URL은 현재 자기 아이피번호를 입력해야합니다.
            HttpClient.Builder http = new HttpClient.Builder("GET", "/app/getCompBadge/" + comp_badge);
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
            try{
                String tempData = s;

                Gson gson = new Gson();
                ArrayList<Badge> itemList = new ArrayList<>();
                Badge[] items = gson.fromJson(tempData, Badge[].class);
                String badge_name="";

                for(Badge item: items) {
                    itemList.add(item);

                    DownloadBadgeTask downloadBadgeTask1 = new DownloadBadgeTask("badge", item.getBg_image() + "1.png");
                    downloadBadgeTask1.execute();
                    DownloadBadgeTask downloadBadgeTask2 = new DownloadBadgeTask("badge", item.getBg_image() + "2.png");
                    downloadBadgeTask2.execute();
                    DownloadBadgeTask downloadBadgeTask3 = new DownloadBadgeTask("badge", item.getBg_image() + "3.png");
                    downloadBadgeTask3.execute();

                    badge_path = item.getBg_image();
                    badge_name = item.getBg_name();
                }
                textView4.setText(badge_name);


            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    // 경쟁전 개인 점수 가져오기
    public class GetTask3 extends AsyncTask<Map<String, String>, Integer, String> {

        @Override
        protected String doInBackground(Map<String, String>... maps) {

            // Http 요청 준비 작업
            //URL은 현재 자기 아이피번호를 입력해야합니다.
            HttpClient.Builder http = new HttpClient.Builder("GET", "/app/getCompScore/" + comp_num);
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
            try{
                String tempData = s;

                Gson gson = new Gson();
                ArrayList<CompScore> itemList = new ArrayList<>();
                newScoreItemList = new ArrayList<>();
                CompScore[] items = gson.fromJson(tempData, CompScore[].class);
                String past_name="";
                int i = 0, cnt = 1;

                // 이름 같으면 평균속도 더해줌
                for(CompScore item: items) {
                    if (past_name.equals(item.getR_nickname())) {
                        itemList.get(i - cnt).setRr_avgspeed((itemList.get(i - cnt).getRr_avgspeed() + item.getRr_avgspeed()));
                        cnt++;
                    }
                    else {
                        itemList.add(item);
                    }
                    past_name = item.getR_nickname();
                    i++;
                }

                ArrayList<Integer> tempInt = new ArrayList<>();

                // tempInt 배열에 평균속도 넣어줌
                for ( int j = 0; j < itemList.size(); j++) {
                    tempInt.add(itemList.get(j).getRr_avgspeed());
                }

                // 크기순 정렬
                Collections.sort(tempInt);
                Collections.reverse(tempInt);

                // newItemList 리스트에 5개만 저장
                for ( int l = 0; l < 5 && l < itemList.size() ; l++) {
                    for ( int k = 0; k < itemList.size(); k++) {
                        if (tempInt.get(l) == itemList.get(k).getRr_avgspeed()) {
                            if(itemList.get(k).getR_image() == null){
                                itemList.get(k).setR_image("noImage.jpg");
                            }
                            newScoreItemList.add(itemList.get(k));
                            DownloadScoreTask downloadScoreTask = new DownloadScoreTask("rider", itemList.get(k).getR_image(), newScoreItemList.size());
                            downloadScoreTask.execute();
                        }
                    }
                }

            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    public class DownloadgpxTask extends AsyncTask<Map<String, String>, Integer, String> {
        String directory = "";
        String url = "";

        public DownloadgpxTask(String directory, String url) {
            this.directory = directory;
            this.url = url;
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

            Serializer serializer = new Persister();
            InputStream is = null;
            String line;
            try {
                is = new FileInputStream(new File(getFilesDir().getPath() + "/" + c_gpx));
                StringBuffer strBuffer = new StringBuffer();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                line="";
                boolean stop = false;
                while((line=reader.readLine())!=null && stop == false){
                    if ( line.contains("trkpt lat=")) {
                        a = line.split("\"");
                        stop = true;
                    }
                }

                reader.close();
                is.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public class DownloadClubTask extends AsyncTask<Map<String, String>, Integer, String> {
        String directory = "";
        String url = "";

        public DownloadClubTask(String directory, String url) {
            this.directory = directory;
            this.url = url;
        }

        @Override
        protected synchronized String doInBackground(Map<String, String>... maps) {

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
        protected synchronized void onPostExecute(String s) {
            try {
                clubCount++;

                if ( clubCount == 5) {
                    CompClubAdapter adapter = new CompClubAdapter(getApplicationContext(), clubItemList, Save_Path);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                    recyclerView.setAdapter(adapter);
                }

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "저장에 실패했습니다. 관리자에게 문의하세요", Toast.LENGTH_LONG).show();
            }
        }
    }

    public class DownloadBadgeTask extends AsyncTask<Map<String, String>, Integer, String> {
        String directory = "";
        String url = "";

        public DownloadBadgeTask(String directory, String url) {
            this.directory = directory;
            this.url = url;
        }

        @Override
        protected synchronized String doInBackground(Map<String, String>... maps) {

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
        protected synchronized void onPostExecute(String s) {
            try {
                badgeCount++;

                if ( badgeCount == 3) {
                    CompClubAdapter adapter = new CompClubAdapter(getApplicationContext(), clubItemList, Save_Path);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                    recyclerView.setAdapter(adapter);

                    Bitmap comp_Badge1 = BitmapFactory.decodeFile(new File(Save_Path + "/" + badge_path + "1.png").getAbsolutePath());
                    Bitmap comp_Badge2 = BitmapFactory.decodeFile(new File(Save_Path + "/" + badge_path + "2.png").getAbsolutePath());
                    Bitmap comp_Badge3 = BitmapFactory.decodeFile(new File(Save_Path + "/" + badge_path + "3.png").getAbsolutePath());
                    imageView7.setImageBitmap(comp_Badge1);
                    imageView8.setImageBitmap(comp_Badge2);
                    imageView9.setImageBitmap(comp_Badge3);
                }

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "저장에 실패했습니다. 관리자에게 문의하세요", Toast.LENGTH_LONG).show();
            }
        }
    }

    public class DownloadScoreTask extends AsyncTask<Map<String, String>, Integer, String> {
        String directory = "";
        String url = "";
        int ListSize;

        public DownloadScoreTask(String directory, String url, int listSize) {
            this.directory = directory;
            this.url = url;
            this.ListSize = listSize;
        }

        @Override
        protected synchronized String doInBackground(Map<String, String>... maps) {

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
        protected synchronized void onPostExecute(String s) {
            try {
                scoreCount++;

                if ( scoreCount == ListSize) {
                    CompScoreAdapter adapter = new CompScoreAdapter(getApplicationContext(), newScoreItemList, Save_Path);
                    recyclerView2.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                    recyclerView2.setAdapter(adapter);
                }

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "저장에 실패했습니다. 관리자에게 문의하세요", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {//뒤로가기 버튼 이벤트
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {//웹뷰에서 뒤로가기 버튼을 누르면 뒤로가짐
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private class WebViewClientClass extends WebViewClient {//페이지 이동
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            final Uri url = request.getUrl();
            view.loadUrl(String.valueOf(url));
            return true;
        }
    }
}