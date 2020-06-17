package com.mtbcraft.Activity.Course;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.capston.mtbcraft.R;
import com.google.android.material.navigation.NavigationView;
import com.mtbcraft.Activity.Competition.CompetitionList;
import com.mtbcraft.Activity.Mission.Mission;
import com.mtbcraft.Activity.Riding.MyReport;
import com.mtbcraft.Activity.Scrap.MyScrap;
public class CourseSearch extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    WebView webview;
    private static final String ENTRY_URL = "http://192.168.0.3:8080/riding/Android_CourseSearch";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_search);

        webview = (WebView) findViewById(R.id.searchMap);
        webview.getSettings().setJavaScriptEnabled(true);//자바스크립트 허용
        webview.setWebChromeClient(new WebChromeClient());
        webview.getSettings().setLoadWithOverviewMode(true);

        webview.setWebViewClient(new WebViewClient());
        webview.loadUrl(ENTRY_URL);

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

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(menuItem -> {
            menuItem.setChecked(true);
            mDrawerLayout.closeDrawers();

            int id = menuItem.getItemId();
            switch (id) {
                //홈
                case R.id.nav_home:
                    Intent home = new Intent(CourseSearch.this, MyReport.class);
                    startActivity(home);
                    break;
                //라이딩 기록
                case R.id.nav_mylist:
                    Intent mylist = new Intent(CourseSearch.this, MyReport.class);
                    startActivity(mylist);
                    break;
                //코스보기
                case R.id.nav_courselist:
                    Intent courselist = new Intent(CourseSearch.this, CourseList.class);
                    startActivity(courselist);
                    finish();
                    break;
                //코스검색
                case R.id.nav_course_search:
                    Intent coursesearch = new Intent(CourseSearch.this, CourseSearch.class);
                    startActivity(coursesearch);
                    break;
                    //스크랩 보관함
                case R.id.nav_course_get:
                    Intent courseget = new Intent(CourseSearch.this, MyScrap.class);
                    startActivity(courseget);
                    break;
                //경쟁전
                case R.id.nav_comp:
                    Intent comp = new Intent(CourseSearch.this, CompetitionList.class);
                    startActivity(comp);
                    break;
                //미션
                case R.id.nav_mission:
                    Intent mission = new Intent(CourseSearch.this, Mission.class);
                    startActivity(mission);
                    break;
            }
            return true;
        });
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
