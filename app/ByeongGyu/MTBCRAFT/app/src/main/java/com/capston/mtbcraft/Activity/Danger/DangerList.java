package com.capston.mtbcraft.Activity.Danger;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.capston.mtbcraft.Activity.Competition.CompetitionList;
import com.capston.mtbcraft.Activity.Control.NoMtb;
import com.capston.mtbcraft.Activity.Course.CourseList;
import com.capston.mtbcraft.Activity.Course.CourseSearch;
import com.capston.mtbcraft.Activity.Main.SubActivity;
import com.capston.mtbcraft.Activity.Mission.MissionList;
import com.capston.mtbcraft.Activity.Riding.MyReport;
import com.capston.mtbcraft.Activity.Scrap.MyScrap;
import com.capston.mtbcraft.R;
import com.capston.mtbcraft.Recycler.Adapter.DangerAdapter;
import com.capston.mtbcraft.dto.DangerousArea;
import com.capston.mtbcraft.network.HttpClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

public class DangerList extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener{

    public GoogleMap mMap;
    private DrawerLayout mDrawerLayout;
    private RecyclerView recyclerView;
    private ImageView userImage;

    private String LoginId, Nickname, r_image;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dangerlist);

        /* 로그인 정보 가져오기 */
        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
        LoginId = auto.getString("LoginId","");
        Nickname = auto.getString("r_nickname","");
        r_image = auto.getString("r_image","");

        recyclerView = findViewById(R.id.recycle_danger);
        Button registerButton = findViewById(R.id.button3);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

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
        Bitmap user_image = BitmapFactory.decodeFile(new File(getFilesDir().getPath() + "/" + r_image).getAbsolutePath());
        userImage.setImageBitmap(user_image);
        TextView InFoUserId = (TextView) header.findViewById(R.id.infouserid);
        InFoUserId.setText(Nickname+"님 환영합니다");

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
                    Intent mylist=new Intent(getApplicationContext(), MyReport.class);
                    startActivity(mylist);
                    finish();
                    break;

                //코스보기
                case R.id.nav_courselist:
                    Intent courselist=new Intent(getApplicationContext(), CourseList.class);
                    courselist.putExtra("rider_id", LoginId);
                    startActivity(courselist);
                    finish();
                    break;

                //코스검색
                case R.id.nav_course_search:
                    Intent coursesearch=new Intent(getApplicationContext(), CourseSearch.class);
                    startActivity(coursesearch);
                    finish();
                    break;

                //스크랩 보관함
                case R.id.nav_course_get:
                    Intent courseget=new Intent(getApplicationContext(), MyScrap.class);
                    startActivity(courseget);
                    finish();
                    break;

                //경쟁전
                case R.id.nav_comp:
                    Intent comp=new Intent(getApplicationContext(), CompetitionList.class);
                    startActivity(comp);
                    finish();
                    break;

                //미션
                case R.id.nav_mission:
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
                    Intent danger = new Intent(getApplicationContext(), DangerList.class);
                    startActivity(danger);
                    finish();
                    break;

                //위험구역
                case R.id.no_mtb:
                    Intent nomtb = new Intent(getApplicationContext(), NoMtb.class);
                    startActivity(nomtb);
                    finish();
                    break;
            }
            return true;
        });

        registerButton.setOnClickListener( v-> {

            Intent i = new Intent(getApplicationContext(), DangerRegister.class);
            startActivity(i);
        });

        try {
            new GetDangerTask().execute();

        } catch (Exception e) {

        }

    }

    @Override
    public void onMapClick(LatLng latLng) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng Seoul = new LatLng(37.5642135, 127.0016985);
        mMap.addMarker(new MarkerOptions().position(Seoul).title("초기 설정 서울"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Seoul, 15));

        UiSettings mapUiSettings = mMap.getUiSettings();
        mapUiSettings.setZoomControlsEnabled(true);  // 줌버튼
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


    public class GetDangerTask extends AsyncTask<Map<String, String>, Integer, String> {

        @Override
        protected String doInBackground(Map<String, String>... maps) {
            // Http 요청 준비 작업
            //URL은 현재 자기 아이피번호를 입력해야합니다.
            HttpClient.Builder http = new HttpClient.Builder("GET", "/app/danger/" + LoginId);
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
                Log.e("테스트", "테스트");
                Gson gson = new Gson();
                ArrayList<DangerousArea> itemList = new ArrayList<>();
                DangerousArea[] items = gson.fromJson(tempData, DangerousArea[].class);

                Log.e("테스트2", items[0].getDa_content());
                for (DangerousArea item : items) {
                    itemList.add(item);
                }

                Log.e("테스트", "테스트");

                DangerAdapter nowAdapter = new DangerAdapter(getApplicationContext(), itemList, mMap);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                recyclerView.setAdapter(nowAdapter);
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
}
