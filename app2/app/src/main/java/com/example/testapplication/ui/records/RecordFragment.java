package com.example.testapplication.ui.records;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testapplication.R;
import com.example.testapplication.dto.RidingRecord;
import com.example.testapplication.ui.BaseFragment;
import com.example.testapplication.ui.recycler.MyReportAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecordFragment extends BaseFragment implements MyReportAdapter.OnItemClick{
    private Call<List<RidingRecord>> request;
    private RecyclerView recyclerView;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // 최소한의 코드
        return inflater.inflate(R.layout.fragment_records, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recyclerView);
        request = serverApi.getRecord(model.r_Id.getValue());
        request.enqueue(new Callback<List<RidingRecord>>() {
            @Override
            public void onResponse(Call<List<RidingRecord>> call, Response<List<RidingRecord>> response) {
                if(response.code()==200){
                    ArrayList<RidingRecord> itemList = new ArrayList<>();
                    List<RidingRecord> itmes = response.body();

                    for(RidingRecord record : itmes){
                        itemList.add(record);
                    }
                    MyReportAdapter adapter = new MyReportAdapter(getActivity(), itemList);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                    recyclerView.setAdapter(adapter);

                    }
                }

            @Override
            public void onFailure(Call<List<RidingRecord>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public void onItemClick(int position, RidingRecord memo) {

    }
}