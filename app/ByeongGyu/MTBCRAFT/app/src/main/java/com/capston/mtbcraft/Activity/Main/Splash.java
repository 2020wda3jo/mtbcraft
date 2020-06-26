package com.capston.mtbcraft.Activity.Main;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.capston.mtbcraft.R;

import java.util.ArrayList;
import java.util.List;

public class Splash extends Activity {

    private static final int MULTIPLE_PERMISSIONS = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.splash_activity);

        Handler hd = new Handler();
        hd.postDelayed(new splashhandler(), 2000); // 1초 후에 hd handler 실행  3000ms = 3초




    }



    private void showToast_PermissionDeny() {
        Toast.makeText(this, "권한 요청에 동의 해주셔야 이용 가능합니다. 설정에서 권한 허용 하시기 바랍니다.", Toast.LENGTH_SHORT).show();
        finish();
    }

    private class splashhandler implements Runnable{
        public void run(){
            startActivity(new Intent(getApplication(), SubActivity.class)); //로딩이 끝난 후, ChoiceFunction 이동
            Splash.this.finish(); // 로딩페이지 Activity stack에서 제거
        }
    }

    @Override
    public void onBackPressed() {
        //초반 플래시 화면에서 넘어갈때 뒤로가기 버튼 못누르게 함
    }

}
