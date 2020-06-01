package com.mtbcraft.Activity.Main;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import com.mtbcraft.Activity.Mission.Mission;
import com.mtbcraft.Activity.Riding.MyReport;
import com.mtbcraft.Activity.Scrap.MyScrap;

public class SubActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submain);

        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
        String LoginId = auto.getString("LoginId","");
        Toast toast = Toast.makeText(getApplicationContext(), LoginId+"님 로그인되었습니다", Toast.LENGTH_SHORT); toast.show();


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setDisplayHomeAsUpEnabled(true);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        View header = navigationView.getHeaderView(0);
        TextView InFoUserId = (TextView) header.findViewById(R.id.infouserid);
        InFoUserId.setText(LoginId+"환영합니다");
        navigationView.setNavigationItemSelectedListener(menuItem -> {
            menuItem.setChecked(true);
            mDrawerLayout.closeDrawers();

            int id = menuItem.getItemId();
            switch (id) {
                case R.id.nav_home:
                    Intent intent=new Intent(SubActivity.this,SubActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case R.id.nav_mylist:
                    Intent intent2=new Intent(SubActivity.this, MyReport.class);
                    startActivity(intent2);
                    break;

                case R.id.nav_alllist:
                    Intent intent3=new Intent(SubActivity.this, CourseList.class);
                    startActivity(intent3);
                    break;

                case R.id.nav_course:
                    Intent intent4=new Intent(SubActivity.this, MyScrap.class);
                    startActivity(intent4);
                    break;

                case R.id.nav_comp:
                    Intent intent5=new Intent(SubActivity.this, Competition.class);
                    startActivity(intent5);
                    break;
                case R.id.nav_mission:
                    Intent intent6=new Intent(SubActivity.this, Mission.class);
                    startActivity(intent6);
                    break;
            }
            return true;
        });



        Button startbt = (Button)findViewById(R.id.ridingstart);
        //라이딩 시작
        startbt.setOnClickListener(v -> {

            Intent intent=new Intent(SubActivity.this, StartActivity.class);
            startActivity(intent);
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
