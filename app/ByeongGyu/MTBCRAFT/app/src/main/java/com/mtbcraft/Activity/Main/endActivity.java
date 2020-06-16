package com.mtbcraft.Activity.Main;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.capston.mtbcraft.*;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.gson.Gson;
import com.mtbcraft.GpxInfo;
import com.mtbcraft.SendGPXFile;
import com.mtbcraft.dto.Competition_Status;
import com.mtbcraft.dto.Mission;
import com.mtbcraft.dto.Mission_Status;
import com.mtbcraft.network.HttpClient;

import net.daum.mf.map.api.CameraUpdateFactory;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapPointBounds;
import net.daum.mf.map.api.MapPolyline;
import net.daum.mf.map.api.MapReverseGeoCoder;
import net.daum.mf.map.api.MapView;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class endActivity extends AppCompatActivity implements MapView.CurrentLocationEventListener, MapReverseGeoCoder.ReverseGeoCodingResultListener{

    //스타트 액티비티에서 가져오는 String형변수
    String MaxSpeed, AvgSpeed, Getgodo, RestTime, IngTime, Distence, endsec, restsectime, rr_comp;
    //스타트 액티비티에서 가져온 값들을 텍스트로 설정
    TextView avgsoeed, maxspeed, getgodo, resttime, ingtime, distence, result;

    int check, clearCount=0;

    MapView mapView;
    //DB로 전송하기 위한 변수
    String open;

    //로그인정보 받아오기
    SharedPreferences auto;
    String LoginId;
    int[] typeScore;

    int r_club;

    //형 변환
    String cha_dis, cha_avg, cha_max, clear, comp_name;

    Intent intent;

    //라이딩 이름 짓기
    LinearLayout openset;
    TextView riding_nameinput;
    CharSequence riding_name;
    private LineChart lineChart;
    private Object endActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.riding_end);

        //로그인정보 받아오기
        auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
        LoginId  = auto.getString("LoginId","");
        r_club = auto.getInt("r_club", 0);

        endActivity = this;

        //넘어온 intent값 넣기
        intent = new Intent(this.getIntent());

        cha_dis= intent.getStringExtra("cha_dis"); //이동거리(소수점X)
        cha_max =  intent.getStringExtra("cha_max"); //최대속도(소수점X)
        cha_avg = intent.getStringExtra("cha_avg"); //평균속도(소수점X)
        cha_dis = intent.getStringExtra("cha_dis");
        cha_avg = intent.getStringExtra("cha_avg");
        cha_max = intent.getStringExtra("cha_max");
        Distence = intent.getStringExtra("distence"); //이동거리
        MaxSpeed = intent.getStringExtra("endmax"); //최대속도
        AvgSpeed = intent.getStringExtra("endavg"); //평균속도
        Getgodo = intent.getStringExtra("getgodo"); //획득고도
        RestTime = intent.getStringExtra("m_rest"); //휴식시간
        IngTime = intent.getStringExtra("ingtime"); //라이딩시간(시분초)
        endsec = intent.getStringExtra("endsec"); //라이딩 시간(초)
        restsectime = intent.getStringExtra("restsectime"); //휴식시간(초)
        check = intent.getIntExtra("check", 0);
        rr_comp = intent.getStringExtra("rr_comp");
        comp_name = intent.getStringExtra("comp_name");


        ArrayList<Double> witch_lat = (ArrayList<Double>)intent.getSerializableExtra("witch_lat");
        ArrayList<Double> witch_lon = (ArrayList<Double>)intent.getSerializableExtra("witch_lon");
        ArrayList<Double> ele = (ArrayList<Double>)intent.getSerializableExtra("ele");

        //그래프 그리기 위한 변수
        ArrayList<Float> godolist = (ArrayList<Float>)intent.getSerializableExtra("godoarray");
        double maxLat = intent.getExtras().getDouble("maxLat",0);
        double maxLon = intent.getExtras().getDouble("maxLon", 0);
        double minLat = intent.getExtras().getDouble("minLat", 0);
        double minLon = intent.getExtras().getDouble("minLon", 0);


        //선 그래프
       // ArrayList<Entry> entry_chart = new ArrayList<>();
        //lineChart = (LineChart) findViewById(R.id.chart);//layout의 id
       // LineData chartData = new LineData();
       // for(int g=0; g<godolist.size(); g++){
           // entry_chart.add(new Entry(g, godolist.get(g)));
       // }

            /* 만약 (2, 3) add하고 (2, 5)한다고해서
            기존 (2, 3)이 사라지는게 아니라 x가 2인곳에 y가 3, 5의 점이 찍힘 */
       // LineDataSet lineDataSet = new LineDataSet(entry_chart, "고도상승");
        //chartData.addDataSet(lineDataSet);

        //lineChart.setData(chartData);
        //lineChart.invalidate();


        mapView = new MapView(this);
        ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);

        mapView.setCurrentLocationEventListener(this);
        mapView.isShowingCurrentLocationMarker();

        MapPolyline polyline = new MapPolyline();
        polyline.setTag(1000);
        polyline.setLineColor(Color.argb(128, 255, 51, 0)); // Polyline 컬러 지정.

        // Polyline 좌표 지정.
        for(int i=0; i<witch_lat.size(); i++){
            polyline.addPoint(MapPoint.mapPointWithGeoCoord(witch_lat.get(i), witch_lon.get(i)));
        }

        // Polyline 지도에 올리기.
        mapView.addPolyline(polyline);

        // 지도뷰의 중심좌표와 줌레벨을 Polyline이 모두 나오도록 조정.
        MapPointBounds mapPointBounds = new MapPointBounds(polyline.getMapPoints());
        int padding = 100; // px
        mapView.moveCamera(CameraUpdateFactory.newMapPointBounds(mapPointBounds, padding));

        avgsoeed = (TextView) findViewById(R.id.endavg);
        maxspeed = (TextView) findViewById(R.id.endmax);
        getgodo = (TextView) findViewById(R.id.endget);
        resttime = (TextView) findViewById(R.id.endresttime);
        ingtime = (TextView) findViewById(R.id.ending);
        distence = (TextView) findViewById(R.id.enddis);
        riding_nameinput = (EditText)findViewById(R.id.riding_nameinput); //라이딩 이름 넣기

        Button save = (Button) findViewById(R.id.saveriding);
        RadioGroup openselect = (RadioGroup)findViewById(R.id.selectgroup);

        //앞에서 받아온값들을 텍스트로 설정
        avgsoeed.setText(AvgSpeed); //평균속도
        maxspeed.setText(MaxSpeed); //최대속도
        getgodo.setText(Getgodo); //획득고도


        int hour;
        int min;
        int sec = Integer.parseInt(restsectime);

        min = sec/60;
        hour = min/60;
        sec = sec % 60;
        min = min % 60;
        if(hour == 0){
            hour=00;
        }
        if(min==0){
            min=00;
        }

        resttime.setText(String.valueOf(hour+":"+min+":"+sec)); //휴식시간
        ingtime.setText(IngTime); //지속시간
        distence.setText(Distence); //이동거리
        // gpx파일 제목을 위해 현재 시간 구하기
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String nowTime = sdfNow.format(date);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        NetworkTask2 networkTask = new NetworkTask2();
        final String select;

        //공개여부 선택
        openselect.setOnCheckedChangeListener((RadioGroup.OnCheckedChangeListener) (group, checkedId) -> {
            RadioGroup rg = (RadioGroup)findViewById(R.id.selectgroup); // 라디오그룹 객체 맵핑
            RadioButton selectedRdo = (RadioButton)findViewById(rg.getCheckedRadioButtonId()); // rg 라디오그룹의 체크된(getCheckedRadioButtonId) 라디오버튼 객체 맵핑
            String selectedValue = selectedRdo.getText().toString();
            String value="";
            switch (selectedValue){
                case "비공개":
                    value="0";
                case "전체 공개":
                    value="1";
            }
            open = value;
        });


        // gpx 파일 생성
        GpxInfo gpxInfoObj = new GpxInfo();
        gpxInfoObj.setCreator("MTBcraft");

        GpxInfo.metadata metaObj = new GpxInfo.metadata();
        gpxInfoObj.setMetadata(metaObj);

        metaObj.setDesc("MTBcraft");

        GpxInfo.bounds boundsObj = new GpxInfo.bounds();
        metaObj.setBounds(boundsObj);

        boundsObj.setMaxLat(maxLat);
        boundsObj.setMaxLon(maxLon);
        boundsObj.setMinLat(minLat);
        boundsObj.setMinLon(minLon);

        GpxInfo.trk trkObj = new GpxInfo.trk();
        gpxInfoObj.setTrk(trkObj);

        GpxInfo.trkseg trksegObj = new GpxInfo.trkseg();
        trkObj.setTrkseg(trksegObj);

        List<GpxInfo.trkpt> trkptArray = new ArrayList<GpxInfo.trkpt>();
        ArrayList<Entry> entries = new ArrayList<>();
        for ( int i = 0; i < witch_lat.size() ; i++){
            GpxInfo.trkpt trkptObj = new GpxInfo.trkpt(witch_lat.get(i), witch_lon.get(i), ele.get(i));
            trkptArray.add(trkptObj);
        }
        trksegObj.setTrkptList(trkptArray);
        // Create a file to save to and make sure to use the path provided from
        // getFilesDir().getPath().
        File xmlFile = new File(getFilesDir().getPath() + "/gpx_" + nowTime + ".gpx");
        // Serialize the Person
        try
        {
            Serializer serializer = new Persister();
            serializer.write(gpxInfoObj, xmlFile);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        // Create a second person object
        GpxInfo gpxInfoObj2 = null;

        // Deserialize the Person
        if (xmlFile.exists())
        {
            try
            {
                Serializer serializer = new Persister();
                gpxInfoObj2 = serializer.read(GpxInfo.class, xmlFile);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        boolean b = gpxInfoObj.equals(gpxInfoObj2);
        // 여기까지 gpx파일 생성


        save.setOnClickListener(v -> {
            try{
                NetworkTask2 networkTask1 = new NetworkTask2();
                Map<String, String> params = new HashMap<String, String>();

                Log.d("로그임","아이디"+LoginId+"거리"+cha_dis+"최대속도"+cha_max+"평균속도"+cha_avg+"고도"+Getgodo+"공개"+open+"휴식"+restsectime+"시간"+endsec+"이름"+riding_nameinput.getText());
                params.put("rr_rider", LoginId);
                params.put("rr_distance", cha_dis);
                params.put("rr_topspeed", cha_max);
                params.put("rr_avgspeed", cha_avg);
                params.put("rr_high",Getgodo);
                params.put("rr_gpx", "gpx_" + nowTime + ".gpx");
                params.put("rr_open", open);
                params.put("rr_breaktime", restsectime);
                params.put("rr_time", endsec);
                params.put("rr_area", "대구 북구");
                params.put("rr_like", "0");
                params.put("rr_name", riding_nameinput.getText().toString());
                params.put("rr_comp", rr_comp);
                networkTask1.execute(params);

                new getMissionStatus().execute();



                //경쟁전 관련 업데이트
                if (!rr_comp.equals("null")) {
                    new getCompAllScore().execute();


                    UpdateCompScore updateCompScore = new UpdateCompScore();
                    Map<String, String> params2 = new HashMap<>();

                    params2.put("avg_speed", cha_avg);
                    params2.put("rr_comp", rr_comp);
                    params2.put("r_club", String.valueOf(r_club));
                    params2.put("LoginId", LoginId);
                    updateCompScore.execute(params2);
                }

                // gpx 파일 보냄
                SendGPXFile sendObj = new SendGPXFile();
                sendObj.setFileName("gpx_" + nowTime + ".gpx");
                sendObj.setFilepath(getFilesDir().getPath() + "/");
                sendObj.upload();
            }catch(Exception e){
                e.printStackTrace();
            }
        });
    }


    public class NetworkTask2 extends AsyncTask<Map<String, String>, Integer, String> {
        @Override
        protected String doInBackground(Map<String, String>... maps) {
            // Http 요청 준비 작업
            //URL은 현재 자기 아이피번호를 입력해야합니다.
            HttpClient.Builder http = new HttpClient.Builder("POST", "http://53.92.32.2:8080/api/upload");
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
            try{
                Log.d("JSON_RESULT", s);

            }catch(Exception e){
                Toast.makeText(getApplicationContext(), "저장에 실패했습니다. 관리자에게 문의하세요", Toast.LENGTH_LONG).show();
            }
        }
    }

    // 경쟁전 현황 점수, 횟수 업데이트
    public class UpdateCompScore extends AsyncTask<Map<String, String>, Integer, String> {
        @Override
        protected String doInBackground(Map<String, String>... maps) {
            // Http 요청 준비 작업
            //URL은 현재 자기 아이피번호를 입력해야합니다.
            HttpClient.Builder http = new HttpClient.Builder("PUT", "http://53.92.32.2:8080/app/updateCompScore");
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
        }
    }

    //경쟁전 모든 점수현황 가져오기
    public class getCompAllScore extends AsyncTask<Map<String, String>, Integer, String> {

        @Override
        protected String doInBackground(Map<String, String>... maps) {

            // Http 요청 준비 작업
            //URL은 현재 자기 아이피번호를 입력해야합니다.
            HttpClient.Builder http = new HttpClient.Builder("GET", "http://53.92.32.2:8080/app/getCompAllScore/" + rr_comp);
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
            try{
                String tempData = s;

                Gson gson = new Gson();
                ArrayList<Competition_Status> itemList = new ArrayList<>();
                Competition_Status[] items = gson.fromJson(tempData, Competition_Status[].class);

                for ( int i = 0; i<items.length; i++){
                    if ( items[i].getCs_club() == r_club)
                        items[i].setCs_score( items[i].getCs_score() + Integer.parseInt(cha_avg));
                    itemList.add(items[i]);
                }

                ArrayList<Integer> tempInt = new ArrayList<>();

                for ( int j = 0; j < itemList.size(); j++) {
                    tempInt.add(itemList.get(j).getCs_score());
                }

                // 크기순 정렬
                Collections.sort(tempInt);
                Collections.reverse(tempInt);

                ArrayList<Competition_Status> NewItemList = new ArrayList<>();

                for ( int l = 0; l < itemList.size(); l ++){
                    for ( int k = 0; k < itemList.size(); k++){
                        if ( tempInt.get(l) == itemList.get(k).getCs_score() )
                            NewItemList.add(itemList.get(k));
                    }
                }

                int count = NewItemList.size();

                updateRank[] UpdateRank = new updateRank[count];
                Map<String, String>[] params3 = new HashMap[count];

                for ( int k = 0; k < count; k++) {
                    UpdateRank[k] = new updateRank();
                    params3[k] = new HashMap<>();
                    params3[k].put("rr_comp", rr_comp);
                    params3[k].put("r_club", String.valueOf(NewItemList.get(k).getCs_club()));
                    params3[k].put("score", String.valueOf(NewItemList.get(k).getCs_score()));
                    params3[k].put("rank", String.valueOf(k+1));
                    UpdateRank[k].executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params3[k]);
                }

            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    // 순위 업데이트
    public class updateRank extends AsyncTask<Map<String, String>, Integer, String> {
        @Override
        protected synchronized String doInBackground( Map<String, String>... maps) {
            // Http 요청 준비 작업
            //URL은 현재 자기 아이피번호를 입력해야합니다.
            HttpClient.Builder http = new HttpClient.Builder("PUT", "http://53.92.32.2:8080/app/updateRank/");
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
        }
    }

    public class getMissionStatus extends AsyncTask<Map<String, String>, Integer, String> {

        @Override
        protected String doInBackground(Map<String, String>... maps) {

            // Http 요청 준비 작업
            //URL은 현재 자기 아이피번호를 입력해야합니다.
            HttpClient.Builder http = new HttpClient.Builder("GET", "http://53.92.32.2:8080/app/getMissionStatus/" + LoginId);
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
            try{
                String tempData = s;

                Gson gson = new Gson();
                ArrayList<Mission_Status> itemList = new ArrayList<>();
                Mission_Status[] items = gson.fromJson(tempData, Mission_Status[].class);

                for ( int i = 0; i<items.length; i++){
                    itemList.add(items[i]);
                }

                typeScore = new int[itemList.size()];

                for ( int j = 0; j<itemList.size(); j++) {
                    if ( itemList.get(j).getMs_type() == 1)
                        typeScore[0] = itemList.get(0).getMs_score() + Integer.parseInt(cha_dis); // 총 거리 계산
                    else if ( itemList.get(j).getMs_type() == 2) {
                        typeScore[1] = itemList.get(1).getMs_score(); // 경쟁전 참여 횟수 계산
                        if (!rr_comp.equals("null"))
                            typeScore[1] += 1;
                    }
                    else if ( itemList.get(j).getMs_type() == 3) {
                        typeScore[2] = itemList.get(j).getMs_score();
                    }

                }

                new getNoClearMission().execute();

            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    public class getNoClearMission extends AsyncTask<Map<String, String>, Integer, String> {

        @Override
        protected String doInBackground(Map<String, String>... maps) {

            // Http 요청 준비 작업
            //URL은 현재 자기 아이피번호를 입력해야합니다.
            HttpClient.Builder http = new HttpClient.Builder("GET", "http://53.92.32.2:8080/app/getNoClearMission/" + LoginId);
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
            try{
                String tempData = s;

                Gson gson = new Gson();
                ArrayList<Mission> itemList = new ArrayList<>();
                ArrayList<Mission> clearList = new ArrayList<>();
                Mission[] items = gson.fromJson(tempData, Mission[].class);

                for ( int i = 0; i<items.length; i++){
                    itemList.add(items[i]);
                }

                for ( int j = 0; j<items.length; j++) {
                    if (itemList.get(j).getM_type() == 1) {
                        String tempStr = itemList.get(j).getM_content();
                        String changeStr = tempStr.replaceAll("[^0-9]", "");
                        if (typeScore[0] >= Integer.parseInt(changeStr)) {
                            clearList.add(itemList.get(j));
                            typeScore[2]++;
                            clearCount++;
                        }
                    }
                    else if ( itemList.get(j).getM_type() == 2){
                        String tempStr = itemList.get(j).getM_content();
                        String changeStr = tempStr.replaceAll("[^0-9]", "");
                        if ( typeScore[1] >= Integer.parseInt(changeStr)){
                            clearList.add(itemList.get(j));
                            typeScore[2]++;
                            clearCount++;
                        }
                    }

                    if ( itemList.get(j).getM_type() == 3){
                        String tempStr = itemList.get(j).getM_content();
                        String changeStr = tempStr.replaceAll("[^0-9]", "");
                        if ( typeScore[2] >= Integer.parseInt(changeStr)){
                            clearList.add(itemList.get(j));
                            typeScore[2]++;
                            clearCount++;
                        }
                    }

                    if ( itemList.get(j).getM_type() == 4){
                        String tempStr = itemList.get(j).getM_content();
                        String changeStr = tempStr.replaceAll("[^0-9]", "");
                        if ( Integer.parseInt(cha_avg) >= Integer.parseInt(changeStr)){
                            clearList.add(itemList.get(j));
                            typeScore[2]++;
                            clearCount++;
                        }
                    }
                }

                updateMissionStatus UMS = new updateMissionStatus();

                Map<String, String> params4 = new HashMap<>();

                params4.put("LoginId", LoginId);
                params4.put("typeScore1", String.valueOf(typeScore[0]));
                params4.put("typeScore2", String.valueOf(typeScore[2]));
                UMS.execute(params4);


                int count = clearList.size();
                if ( count >= 1 ){
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date time = new Date();
                    String time2 = format.format(time);
                    insertMissionCom[] IMC = new insertMissionCom[count];
                    Map<String, String>[] params5 = new HashMap[count];
                    for ( int k = 0; k < clearList.size(); k++){
                        IMC[k] = new insertMissionCom();
                        params5[k] = new HashMap<>();
                        params5[k].put("LoginId", LoginId);
                        params5[k].put("mc_mission", String.valueOf(clearList.get(k).getM_num()));
                        params5[k].put("mc_time", time2);
                        IMC[k].executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params5[k]);
                    }
                }

                // 다이얼로그 바디
                AlertDialog.Builder alert_confirm = new AlertDialog.Builder((Context) endActivity);
                // 메세지
                String text = "라이딩 기록이 저장되었습니다";
                String text1 = "";
                String text2 = "";

                alert_confirm.setMessage(text);

                if (!rr_comp.equals("null")) {
                    text1 = comp_name;
                    alert_confirm.setMessage( text +"\n" + text1+"에 참가하셨습니다\n");
                }

                if ( !rr_comp.equals("null") || clearList.size() >= 1){
                    text1 = comp_name;
                    for ( int l = 0; l < clearList.size(); l++){
                        text2 += clearList.get(l).getM_name() + " 미션 성공\n";
                    }
                    alert_confirm.setMessage( text +"\n" + text1+"에 참가하셨습니다\n" + text2);
                }
                // 확인 버튼 리스너
                alert_confirm.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent2 = new Intent(endActivity.this, SubActivity.class);
                        startActivity(intent2);
                        finish();
                    }
                });
                // 다이얼로그 생성
                AlertDialog alert = alert_confirm.create();
                // 다이얼로그 타이틀
                alert.setTitle("업데이트 로그");
                // 다이얼로그 보기
                alert.show();

            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    public class updateMissionStatus extends AsyncTask<Map<String, String>, Integer, String> {
        @Override
        protected synchronized String doInBackground( Map<String, String>... maps) {
            // Http 요청 준비 작업
            //URL은 현재 자기 아이피번호를 입력해야합니다.
            HttpClient.Builder http = new HttpClient.Builder("PUT", "http://53.92.32.2:8080/app/updateMissionStatus/");
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
        }
    }

    public class insertMissionCom extends AsyncTask<Map<String, String>, Integer, String> {
        @Override
        protected synchronized String doInBackground( Map<String, String>... maps) {
            // Http 요청 준비 작업
            //URL은 현재 자기 아이피번호를 입력해야합니다.
            HttpClient.Builder http = new HttpClient.Builder("PUT", "http://53.92.32.2:8080/app/insertMissionCom/");
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

        }
    }


    @Override
    public void onReverseGeoCoderFoundAddress(MapReverseGeoCoder mapReverseGeoCoder, String s) {

    }
    @Override
    public void onReverseGeoCoderFailedToFindAddress(MapReverseGeoCoder mapReverseGeoCoder) {

    }
    @Override
    public void onCurrentLocationUpdate(MapView mapView, MapPoint mapPoint, float v) {

    }
    @Override
    public void onCurrentLocationDeviceHeadingUpdate(MapView mapView, float v) {

    }
    @Override
    public void onCurrentLocationUpdateFailed(MapView mapView) {

    }
    @Override
    public void onCurrentLocationUpdateCancelled(MapView mapView) {

    }
}