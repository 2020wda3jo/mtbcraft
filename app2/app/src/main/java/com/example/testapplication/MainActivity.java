package com.example.testapplication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.internal.NavigationMenuItemView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ImageView img;
    private TextView lay;
    private View header;
    private View view;
    private ImageView userImage;
    private SharedPreferences auto;
    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_records, R.id.nav_course_view, R.id.nav_course_search, R.id.nav_course_scrap)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        MainViewModel model = new ViewModelProvider(this).get(MainViewModel.class);
        model.message.setValue("Hello");

        navigationView.findViewById(R.id.infouserid);
        header = navigationView.getHeaderView(0);
        lay = (TextView) header.findViewById(R.id.infouserid);
        userImage = (ImageView) header.findViewById(R.id.user_image);

        auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
        model.r_Id.setValue(auto.getString("LoginId", ""));
        model.r_Image.setValue(auto.getString("r_image", ""));
        model.r_Nickname.setValue(auto.getString("r_nickname", ""));
        model.r_ClubName.setValue(auto.getString("r_clubname", ""));
        lay.setText(model.r_Nickname.getValue() + "님 환영합니다");


        Bitmap user_image = BitmapFactory.decodeFile(new File(getFilesDir().getPath() + "/" + model.r_Image.getValue()).getAbsolutePath());
        userImage.setImageBitmap(user_image);


    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


}