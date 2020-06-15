package com.mtbcraft.Activity.Competition;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.capston.mtbcraft.R;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.mtbcraft.Activity.Course.CourseList;
import com.mtbcraft.Activity.Course.CourseSearch;
import com.mtbcraft.Activity.Mission.Mission;
import com.mtbcraft.Activity.Riding.FollowStart;
import com.mtbcraft.Activity.Riding.MyReport;
import com.mtbcraft.Activity.Scrap.MyScrap;
import com.mtbcraft.Recycler.Adapter.CompClubAdapter;
import com.mtbcraft.Recycler.Adapter.CompScoreAdapter;
import com.mtbcraft.dto.Badge;
import com.mtbcraft.dto.CompClub;
import com.mtbcraft.dto.CompScore;
import com.mtbcraft.network.HttpClient;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class CompetitionDetail extends AppCompatActivity implements LocationListener{
    String comp_num, comp_period, comp_badge, comp_course, comp_image, comp_content, comp_name, Save_Path, badge_path, c_gpx, c_name;
    TextView name_textView, period_textView2, textView3, textView4;
    Button joinButton;
    ImageView imageView6, imageView7;
    WebView webView;
    RecyclerView recyclerView, recyclerView2;
    LocationManager locationManager;
    Location lastKnownLocation;
    String[] a = new String[4];

    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_competitiondetail);


        /* 로그인관련 */
        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
        String LoginId = auto.getString("LoginId", "");

        /*네비게이션 바 */
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setDisplayHomeAsUpEnabled(true);


        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        name_textView = findViewById(R.id.comp_name2);
        period_textView2 = findViewById(R.id.comp_day2);
        imageView6 = findViewById(R.id.imageView6);
        imageView7 = findViewById(R.id.badge_image);
        textView3 = findViewById(R.id.comp_content);
        webView = findViewById(R.id.comp_Course);
        recyclerView = findViewById(R.id.compClub_recycle);
        textView4 = findViewById(R.id.textView14);
        recyclerView2 = findViewById(R.id.compScore_recycle);
        joinButton = findViewById(R.id.compjoin_button);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(menuItem -> {
            menuItem.setChecked(true);
            mDrawerLayout.closeDrawers();

            int id = menuItem.getItemId();
            switch (id) {
                //홈
                case R.id.nav_home:
                    break;
                //라이딩 기록
                case R.id.nav_mylist:
                    Intent mylist = new Intent(CompetitionDetail.this, MyReport.class);
                    startActivity(mylist);

                    break;
                //코스보기
                case R.id.nav_courselist:
                    Intent courselist = new Intent(CompetitionDetail.this, CourseList.class);
                    startActivity(courselist);
                    break;
                //코스검색
                case R.id.nav_course_search:
                    Intent coursesearch = new Intent(CompetitionDetail.this, CourseSearch.class);
                    startActivity(coursesearch);
                    //스크랩 보관함
                case R.id.nav_course_get:
                    Intent courseget = new Intent(CompetitionDetail.this, MyScrap.class);
                    startActivity(courseget);
                    break;
                //경쟁전
                case R.id.nav_comp:
                    Intent comp = new Intent(CompetitionDetail.this, CompetitionList.class);
                    startActivity(comp);
                    break;
                //미션
                case R.id.nav_mission:
                    Intent mission = new Intent(CompetitionDetail.this, Mission.class);
                    startActivity(mission);
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

        FileDownload("gpx", c_gpx);


        try {
            new GetTask().execute();
            new GetTask2().execute();
            new GetTask3().execute();

        } catch (Exception e) {

        }

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

            Toast.makeText(CompetitionDetail.this, "gpx 위도" + a[1] + "경도" + a[3] , Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
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

                Toast.makeText(CompetitionDetail.this, "현재위치 \n위도 " + latitude + "\n경도 " + longitude, Toast.LENGTH_LONG).show();

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
                    intent3.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getContext().startActivity(intent3);

                    finish();
                }
                else{
                    Toast.makeText(CompetitionDetail.this, "현재위치 \n위도 " + latitude + "\n경도 " + longitude, Toast.LENGTH_LONG).show();
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
            HttpClient.Builder http = new HttpClient.Builder("GET", "http://100.92.32.8:8080/app/getCompClub/" + comp_num);
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
                ArrayList<CompClub> itemList = new ArrayList<>();
                CompClub[] items = gson.fromJson(tempData, CompClub[].class);

                for ( int i = 0; i<5 && i<items.length; i++){
                    CompClub item = items[i];
                    itemList.add(item);

                    FileDownload("club" , item.getCb_image());
                }

                CompClubAdapter adapter = new CompClubAdapter(getApplicationContext(), itemList, Save_Path);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                recyclerView.setAdapter(adapter);

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
            HttpClient.Builder http = new HttpClient.Builder("GET", "http://100.92.32.8:8080/app/getCompBadge/" + comp_badge);
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
                    FileDownload("badge" , item.getBg_image());
                    badge_path = item.getBg_image();
                    badge_name = item.getBg_name();
                }

                Bitmap comp_Badge = BitmapFactory.decodeFile(new File(Save_Path + "/" + badge_path).getAbsolutePath());
                imageView7.setImageBitmap(comp_Badge);
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
            HttpClient.Builder http = new HttpClient.Builder("GET", "http://100.92.32.8:8080/app/getCompScore/" + comp_num);
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
                ArrayList<CompScore> newItemList = new ArrayList<>();
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
                            newItemList.add(itemList.get(k));
                            FileDownload("member" , itemList.get(k).getR_image());
                        }
                    }
                }

                CompScoreAdapter adapter = new CompScoreAdapter(getApplicationContext(), newItemList, Save_Path);
                recyclerView2.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                recyclerView2.setAdapter(adapter);

        }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    public void FileDownload(String directory, String url)
    {
        String fileURL = "http://13.209.229.237:8080/app/getGPX/" + directory + "/" + url; // URL
        DownloadThread dThread;
        File dir = new File(Save_Path);
        // 폴더가 존재하지 않을 경우 폴더를 만듦
        if (!dir.exists()) {
            dir.mkdir();
        }
        // 다운로드 폴더에 동일한 파일명이 존재하는지 확인
        if (new File(Save_Path + "/" + url).exists() == false) {
            dThread = new DownloadThread(fileURL, Save_Path + "/" + url);
            dThread.start();
        }
    }


    // 쓰레드로 다운로드 돌림
    class DownloadThread extends Thread {
        String ServerUrl;
        String LocalPath;

        DownloadThread(String serverPath, String localPath) {
            ServerUrl = serverPath;
            LocalPath = localPath;
        }

        @Override
        public void run() {
            URL gpxUrl;
            int Read;
            try {
                gpxUrl = new URL(ServerUrl);
                HttpURLConnection conn = (HttpURLConnection) gpxUrl
                        .openConnection();
                int len = conn.getContentLength();
                byte[] tmpByte = new byte[len];
                InputStream is = conn.getInputStream();
                File file = new File(LocalPath);
                FileOutputStream fos = new FileOutputStream(file);
                for (;;) {
                    Read = is.read(tmpByte);
                    if (Read <= 0) {
                        break;
                    }
                    fos.write(tmpByte, 0, Read);
                }
                is.close();
                fos.close();
                conn.disconnect();
            } catch (IOException e) {
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
