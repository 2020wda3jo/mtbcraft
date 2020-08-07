package com.example.testapplication.ui.records;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.testapplication.R;
import com.example.testapplication.dto.User;
import com.example.testapplication.ui.BaseFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecordFragment extends BaseFragment {

    private Call<User> request;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // 최소한의 코드
        return inflater.inflate(R.layout.fragment_records, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        request = serverApi.getUser(1);
        request.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if ( response.code() == 200 ){
                    User user = response.body();

                    Log.i("이메일", user.email);
                    Log.i("이메일2", user.getEmail());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                t.printStackTrace();
                showServerFailure();
            }
        });
    }
}