package com.mtbcraft.Activity.Scrap;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.gpstest.R;
import com.google.android.material.navigation.NavigationView;
import com.mtbcraft.Activity.Competition.Competition;
import com.mtbcraft.Activity.Course.CourseList;
import com.mtbcraft.Activity.Main.SubActivity;
import com.mtbcraft.Activity.Mission.Mission;
import com.mtbcraft.Activity.Riding.FollowStart;
import com.mtbcraft.Activity.Riding.MyReport;
import com.mtbcraft.Activity.Riding.RidingRecordAll;
import com.mtbcraft.network.HttpClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MyScrapDetail extends AppCompatActivity {
    String c_num, gpx;
    TextView textView1, textView2, textView3;
    Button button,button2;
    int Sta;
    private DrawerLayout mDrawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myscrap_detail);


        /* 로그인관련 */
        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
        String LoginId = auto.getString("LoginId","");
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
                case R.id.nav_home:
                    Intent intent=new Intent(MyScrapDetail.this,SubActivity.class);
                    startActivity(intent);
                    break;
                case R.id.nav_mylist:
                    Intent intent2=new Intent(MyScrapDetail.this, MyReport.class);
                    startActivity(intent2);
                    break;
                case R.id.nav_alllist:
                    Intent intent3=new Intent(MyScrapDetail.this, RidingRecordAll.class);
                    startActivity(intent3);
                    break;
                case R.id.nav_courselist:
                    Intent intent4=new Intent(MyScrapDetail.this, CourseList.class);
                    startActivity(intent4);
                    break;

                case R.id.nav_course:
                    Intent intent5=new Intent(MyScrapDetail.this, MyScrap.class);
                    startActivity(intent5);
                    finish();
                    break;

                case R.id.nav_comp:
                    Intent intent6=new Intent(MyScrapDetail.this, Competition.class);
                    startActivity(intent6);
                    break;
                case R.id.nav_mission:
                    Intent intent7=new Intent(MyScrapDetail.this, Mission.class);
                    startActivity(intent7);
                    break;
            }
            return true;
        });

        textView1 = (TextView)findViewById(R.id.course_add);
        textView2 = (TextView)findViewById(R.id.course_dis );
        textView3 = (TextView)findViewById(R.id.course_level );
        button = (Button)findViewById(R.id.scrap_bt);
        button2 = (Button)findViewById(R.id.follow_bt);
        Intent intent = new Intent(this.getIntent());
        c_num = intent.getStringExtra("c_num");
        gpx = intent.getStringExtra("c_gpx");


        button2.setOnClickListener(v->{
            Intent intent2=new Intent(MyScrapDetail.this, FollowStart.class);
            intent2.putExtra("gpx",gpx);
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

        public class GetTask extends AsyncTask<Map<String, String>, Integer, String> {
            @Override
            protected String doInBackground(Map<String, String>... maps) {
                // Http 요청 준비 작업
                //URL은 현재 자기 아이피번호를 입력해야합니다.
                HttpClient.Builder http = new HttpClient.Builder("GET", "http://100.92.32.8:8080/app/riding/course/"+c_num);
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

                    String c_distance = "";
                    String c_level = "";
                    String c_area = "";

                    JSONArray jarray = new JSONArray(tempData);
                    for (int i = 0; i < jarray.length(); i++) {
                        JSONObject jObject = jarray.getJSONObject(i);
                        c_distance = jObject.getString("c_distance");
                        c_level = jObject.getString("c_level");
                        c_area = jObject.getString("c_area");
                    }
                    Log.d("씨발", c_distance);
                    textView1.setText(c_area);
                    textView2.setText(c_distance);
                    textView3.setText(c_level);
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
