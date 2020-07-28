package com.capston.mtbcraft.Activity.Setting;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.capston.mtbcraft.databinding.SettingActivityBinding;

public class SettingActivity extends AppCompatActivity {
    private SettingActivityBinding binding;
    private SharedPreferences set;
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = SettingActivityBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.saveBt.setOnClickListener(v ->{
            //저장을 하기위해 editor를 이용하여 값을 저장시켜준다.
            SharedPreferences.Editor editor = sharedPreferences.edit();
            String text = binding.phoneNum.getText().toString(); // 사용자가 입력한 저장할 데이터
            editor.putString("sosphone",text); // key, value를 이용하여 저장하는 형
            editor.commit();
            finish();
        });

    }
}
