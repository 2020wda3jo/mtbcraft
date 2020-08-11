package com.example.testapplication.ui.competitions;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.testapplication.Adapter.CompetitionAdapter;
import com.example.testapplication.R;
import com.example.testapplication.dto.Competition;
import com.example.testapplication.net.HttpClient;
import com.example.testapplication.ui.BaseFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class CompetitionViewFragment extends BaseFragment {
    private ImageView userImage;
    private TextView userId, userNickname, userClubName;
    private Bitmap userBitmap;
    private DrawerLayout mDrawerLayout;
    private RecyclerView recycleView;
    private String Save_Path;
    private int nowSize = 0;
    private ArrayList<String> joinedList = new ArrayList<>();
    private ArrayList<Competition> nowItemList;
    private ArrayList<Competition> pastItemList;
    private ArrayList<Competition> itemList;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_competition_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userImage = view.findViewById(R.id.comp_mem_image);
        userBitmap = BitmapFactory.decodeFile(new File(requireContext().getFilesDir().getPath() + "/" + model.r_Image.getValue()).getAbsolutePath());
        userImage.setImageBitmap(userBitmap);

        userId = view.findViewById(R.id.memberId);
        userNickname = view.findViewById(R.id.nickname);
        userClubName = view.findViewById(R.id.club_name);

        userId.setText(model.r_Id.getValue());
        userNickname.setText(model.r_Nickname.getValue());
        userClubName.setText(model.r_ClubName.getValue());

        Save_Path = requireContext().getFilesDir().getPath();

        recycleView = view.findViewById(R.id.recycleView1);

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // TODO : process tab selection event.
                int pos = tab.getPosition();
                changeView(pos);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // do nothing
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // do nothing
            }
        });

        try {
            new GetTask2().execute();

        }catch (Exception e){

        }
    }

    private void changeView(int index) {
        switch (index) {
            case 0:
                CompetitionAdapter nowAdapter = new CompetitionAdapter(requireContext().getApplicationContext(), nowItemList, Save_Path, controller, model);
                recycleView.setLayoutManager(new LinearLayoutManager(requireContext().getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                recycleView.setAdapter(nowAdapter);
                break;
            case 1:
                CompetitionAdapter pastAdapter = new CompetitionAdapter(requireContext().getApplicationContext(), pastItemList, Save_Path, controller, model);
                recycleView.setLayoutManager(new LinearLayoutManager(requireContext().getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                recycleView.setAdapter(pastAdapter);
                break;

            case 2:
                CompetitionAdapter pastAdapter2 = new CompetitionAdapter(requireContext().getApplicationContext(), itemList, Save_Path, controller, model);
                recycleView.setLayoutManager(new LinearLayoutManager(requireContext().getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                recycleView.setAdapter(pastAdapter2);
                break;
        }
    }

    public class GetTask extends AsyncTask<Map<String, String>, Integer, String> {

        @Override
        protected String doInBackground(Map<String, String>... maps) {

            // Http 요청 준비 작업
            //URL은 현재 자기 아이피번호를 입력해야합니다.
            HttpClient.Builder http = new HttpClient.Builder("GET", "/app/competition");
            // Parameter 를 전송한다.

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
                String tempData = s;

                Gson gson = new Gson();
                pastItemList = new ArrayList<>();
                itemList = new ArrayList<>();
                nowItemList = new ArrayList<>();

                Competition[] items = gson.fromJson(tempData, Competition[].class);

                for (Competition item : items) {
                    for (int i = 0; i < joinedList.size(); i++) {
                        if (item.getComp_num() == Integer.parseInt(joinedList.get(i))) {
                            itemList.add(item);
                        }
                    }

                    String fullDay = item.getComp_period();
                    long howDay = 0;
                    try {
                        howDay = getDate(fullDay);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    if (howDay < 0) {
                        nowItemList.add(item);
                    } else {
                        pastItemList.add(item);
                    }
                }

                CompetitionAdapter adapter = new CompetitionAdapter(requireContext().getApplicationContext(), nowItemList, Save_Path, controller, model);
                recycleView.setLayoutManager(new LinearLayoutManager(requireContext().getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                recycleView.setAdapter(adapter);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public long getDate(String getPeriod) throws ParseException {
            long now = System.currentTimeMillis();
            Date date = new Date(now);
            SimpleDateFormat sdfNow = new SimpleDateFormat("yyyyMMdd");
            String temp = sdfNow.format(date);
            Date nowTime = sdfNow.parse(temp);
            Date periodTime = sdfNow.parse(String.valueOf(getPeriod.substring(8, 16)));

            return (nowTime.getTime() - periodTime.getTime()) / (24 * 60 * 60 * 1000);

        }
    }

    public class GetTask2 extends AsyncTask<Map<String, String>, Integer, String> {

        @Override
        protected String doInBackground(Map<String, String>... maps) {
            // Http 요청 준비 작업
            //URL은 현재 자기 아이피번호를 입력해야합니다.
            HttpClient.Builder http = new HttpClient.Builder("GET", "/app/competition/" + model.r_Id.getValue());
            // Parameter 를 전송한다.

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
                String tempData = s;

                Gson gson = new Gson();
                ArrayList<String> itemList = new ArrayList<>();
                String[] items = gson.fromJson(tempData, String[].class);
                String pastItem = "";

                for (String item : items) {
                    if (!pastItem.equals(item))
                        joinedList.add(item);
                    pastItem = item;
                }

                new GetTask().execute();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}