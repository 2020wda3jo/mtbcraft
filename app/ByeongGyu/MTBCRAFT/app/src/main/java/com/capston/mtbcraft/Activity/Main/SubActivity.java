package com.capston.mtbcraft.Activity.Main;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.capston.mtbcraft.Activity.Competition.CompetitionList;
import com.capston.mtbcraft.Activity.Control.NoMtb;
import com.capston.mtbcraft.Activity.Course.CourseList;
import com.capston.mtbcraft.Activity.Course.CourseSearch;
import com.capston.mtbcraft.Activity.Danger.DangerList;
import com.capston.mtbcraft.Activity.Mission.MissionList;
import com.capston.mtbcraft.Activity.Riding.MyReport;
import com.capston.mtbcraft.Activity.Scrap.MyScrap;
import com.capston.mtbcraft.Activity.Setting.SettingActivity;
import com.capston.mtbcraft.R;
import com.capston.mtbcraft.databinding.ActivitySubmainBinding;
import com.capston.mtbcraft.network.HttpClient;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SubActivity extends AppCompatActivity{
    private ActivitySubmainBinding binding;
    private DrawerLayout mDrawerLayout;
    private ViewFlipper v_fillipper;
    private JSONArray jarray;
    private JSONObject jObject;
    private String LoginId, Nickname;
    private ImageView userImage;
    private SharedPreferences auto;
    private Toolbar toolbar;
    private ActionBar actionBar;
    private NavigationView navigationView;
    private View header;
    private TextView InFoUserId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySubmainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("키해시는 :", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        /* 로그인 정보 가져오기 */
        auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
        LoginId = auto.getString("LoginId", "");
        Nickname = auto.getString("r_nickname", "");
        binding.idinfo.setText(Nickname+"님 저희와 함께");


        /* 드로우 레이아웃 네비게이션 부분들 */
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setDisplayHomeAsUpEnabled(true);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        header = navigationView.getHeaderView(0);
        userImage = (ImageView) header.findViewById(R.id.user_image);
        InFoUserId = (TextView) header.findViewById(R.id.infouserid);
        InFoUserId.setText(Nickname + "님 환영합니다");


        //닉네임명에 따른 이미지변경(임시)
        switch(Nickname){
            case "배고파":
                userImage.setImageDrawable(getResources().getDrawable(R.drawable.peo1));
                break;

            case "2병규":
                userImage.setImageDrawable(getResources().getDrawable(R.drawable.peo2));
                break;
            case "괴물쥐":
                userImage.setImageDrawable(getResources().getDrawable(R.drawable.peo3));
                break;
            default:
                break;
        }

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
                    Intent mylist = new Intent(getApplicationContext(), MyReport.class);
                    startActivity(mylist);

                    break;
                //코스보기
                case R.id.nav_courselist:
                    Intent courselist = new Intent(getApplicationContext(), CourseList.class);
                    courselist.putExtra("rider_id", LoginId);
                    startActivity(courselist);
                    break;
                //코스검색
                case R.id.nav_course_search:
                    Intent coursesearch = new Intent(getApplicationContext(), CourseSearch.class);
                    startActivity(coursesearch);
                    break;
                //스크랩 보관함
                case R.id.nav_course_get:
                    Intent courseget = new Intent(getApplicationContext(), MyScrap.class);
                    startActivity(courseget);
                    break;
                //경쟁전
                case R.id.nav_comp:
                    Intent comp = new Intent(getApplicationContext(), CompetitionList.class);
                    startActivity(comp);
                    break;
                //미션
                case R.id.nav_mission:
                    Intent mission = new Intent(getApplicationContext(), MissionList.class);
                    startActivity(mission);
                    break;
                case R.id.friend_chodae:
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_SUBJECT, LoginId + "님이 귀하를 초대합니다. 앱 설치하기");
                    intent.putExtra(Intent.EXTRA_TEXT, "tmarket://details?id=com.capston.mtbcraft");

                    Intent chooser = Intent.createChooser(intent, "초대하기");
                    startActivity(chooser);
                    break;

                //위험구역
                case R.id.nav_danger:
                    Intent danger = new Intent(getApplicationContext(), DangerList.class);
                    startActivity(danger);
                    break;

                //입산통제
                case R.id.no_mtb:
                    Intent nomtb = new Intent(getApplicationContext(), NoMtb.class);
                    startActivity(nomtb);
                    break;

                //설정
                case R.id.settings:
                    Intent setting = new Intent(getApplicationContext(), SettingActivity.class);
                    startActivity(setting);
                    break;
            }
            return true;
        });


        ImageView startbt = (ImageView) findViewById(R.id.ridingstart);
        //라이딩 시작
        startbt.setOnClickListener(v -> {

            Intent intent = new Intent(SubActivity.this, StartActivity.class);
            startActivity(intent);
        });

        AddRecord addRecord = new AddRecord();
        Map<String, String> params = new HashMap<String, String>();
        params.put("rr_rider", LoginId);
        addRecord.execute(params);

    }

    public class AddRecord extends AsyncTask<Map<String, String>, Integer, String> {
        @Override
        protected String doInBackground(Map<String, String>... maps) {
            // Http 요청 준비 작업
            //URL은 현재 자기 아이피번호를 입력해야합니다.
            HttpClient.Builder http = new HttpClient.Builder("GET", "/api/get/" + LoginId);

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
                Log.d("키로수 더할꺼임", s);
                String tempData = s;
                jarray = new JSONArray(tempData);

                double killlo = 0;
                int dis = 0;
                int riding_time=0;
                int total_dis=0;

                String oTime="";//현재날짜

                SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date currentTime = new Date();
                oTime = mSimpleDateFormat.format(currentTime);

                String test="";
                for (int i = 0; i < jarray.length(); i++) {


                    jObject = jarray.getJSONObject(i);
                    total_dis+=jObject.getInt("rr_distance");
                    riding_time+=jObject.getInt("rr_time");
                    test =  jObject.getString("rr_date");

                    //오늘 주행한거
                    if(oTime.equals(test.substring(0,10))){

                        int hour;
                        int min;
                        int sec = riding_time;

                        min = sec/60;
                        hour = min/60;
                        sec = sec % 60;
                        min = min % 60;
                        if(hour == 0){
                            hour=0;
                        }
                        if(min==0){
                            min=0;
                        }
                        binding.mainTime.setText(hour + "시간 " + min + "분 " + sec + "초");

                        dis+= jObject.getInt("rr_distance");
                        killlo = (int) (dis / 1000.0);

                        if (dis >= 1000) {
                            binding.mainKm.setText(killlo +"km");
                        }else{
                            binding.mainKm.setText(dis+"m");
                        }
                    }else{
                        Log.d("응 달라~","다르다고");
                    }
                }



                killlo = (int) (total_dis / 1000.0);
                if (total_dis >= 1000) {
                    binding.mainDis.setText(killlo +"km를 주행하셨어요");
                }else{
                    binding.mainDis.setText(total_dis+"m");
                }


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

    /* 네이베이션 끝 */
}
