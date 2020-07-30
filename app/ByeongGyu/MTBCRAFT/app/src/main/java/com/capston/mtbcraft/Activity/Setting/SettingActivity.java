package com.capston.mtbcraft.Activity.Setting;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.capston.mtbcraft.databinding.SettingActivityBinding;

public class SettingActivity extends AppCompatActivity {
    private SettingActivityBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = SettingActivityBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.saveBt.setOnClickListener(v ->{
            //저장을 하기위해 editor를 이용하여 값을 저장시켜준다.
            String text = binding.phoneNum.getText().toString(); // 사용자가 입력한 저장할 데이터


            SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
            SharedPreferences.Editor autoLogin = auto.edit();
            autoLogin.putString("sosphone", text);
            autoLogin.commit();
            finish();
        });

    }
}
