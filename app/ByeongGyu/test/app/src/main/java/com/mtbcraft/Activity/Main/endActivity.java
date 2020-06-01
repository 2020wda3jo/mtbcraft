package com.mtbcraft.Activity.Main;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gpstest.GpxInfo;
import com.example.gpstest.R;
import com.example.gpstest.SendGPXFile;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class endActivity extends AppCompatActivity implements MapView.CurrentLocationEventListener, MapReverseGeoCoder.ReverseGeoCodingResultListener{

    //스타트 액티비티에서 가져오는 String형변수
    String MaxSpeed, AvgSpeed, Getgodo, RestTime, IngTime, Distence, endsec, restsectime;
    //스타트 액티비티에서 가져온 값들을 텍스트로 설정
    TextView avgsoeed, maxspeed, getgodo, resttime, ingtime, distence, result;

    MapView mapView;
    //DB로 전송하기 위한 변수
    String open;

    //로그인정보 받아오기
    SharedPreferences auto;
    String LoginId;

    //형 변환
    String cha_dis, cha_avg, cha_max;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainend);

        //로그인정보 받아오기
        auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
        LoginId  = auto.getString("LoginId","");

        //넘어온 intent값 넣기
        Intent intent = new Intent(this.getIntent());

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
        RestTime = intent.getStringExtra("resttime"); //휴식시간
        IngTime = intent.getStringExtra("ingtime"); //라이딩시간(시분초)
        endsec = intent.getStringExtra("endsec"); //라이딩 시간(초)
        restsectime = intent.getStringExtra("restsectime"); //휴식시간(초)
      //  ArrayList<Double> test1 = (ArrayList<Double>)
        ArrayList<Double> witch_lat = (ArrayList<Double>)intent.getSerializableExtra("witch_lat");
        ArrayList<Double> witch_lon = (ArrayList<Double>)intent.getSerializableExtra("witch_lon");
        ArrayList<Double> ele = (ArrayList<Double>)intent.getSerializableExtra("ele");
        double maxLat = intent.getExtras().getDouble("maxLat",0);
        double maxLon = intent.getExtras().getDouble("maxLon", 0);
        double minLat = intent.getExtras().getDouble("minLat", 0);
        double minLon = intent.getExtras().getDouble("minLon", 0);


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
        result = (TextView) findViewById(R.id.result);

        Button save = (Button) findViewById(R.id.saveriding);
        RadioGroup openselect = (RadioGroup)findViewById(R.id.selectgroup);



        //앞에서 받아온값들을 텍스트로 설정
        avgsoeed.setText(AvgSpeed); //평균속도
        maxspeed.setText(MaxSpeed); //최대속도
        getgodo.setText(Getgodo); //획득고도
        resttime.setText(RestTime); //휴식시간
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
        openselect.setOnCheckedChangeListener((RadioGroup.OnCheckedChangeListener) (group, checkedId) -> {
            RadioGroup rg = (RadioGroup)findViewById(R.id.selectgroup); // 라디오그룹 객체 맵핑
            RadioButton selectedRdo = (RadioButton)findViewById(rg.getCheckedRadioButtonId()); // rg 라디오그룹의 체크된(getCheckedRadioButtonId) 라디오버튼 객체 맵핑
            String selectedValue = selectedRdo.getText().toString();
            String value="";
            switch (selectedValue){
                case "비공개":
                    value="0";
                case "클럽 공개":
                    value="1";
                case "전체 공개":
                    value="2";
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
                SendGPXFile sendObj = new SendGPXFile();
                sendObj.setFileName("gpx_" + nowTime + ".gpx");
                sendObj.setFilepath(getFilesDir().getPath() + "/");
                sendObj.upload();

                NetworkTask2 networkTask1 = new NetworkTask2();
                Map<String, String> params = new HashMap<String, String>();

              Log.d("로그임","아이디"+LoginId+"거리"+cha_dis+"최대속도"+cha_max+"평균속도"+cha_avg+"고도"+Getgodo+"공개"+open+"휴식"+restsectime+"시간"+endsec);

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
                networkTask1.execute(params);
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
            HttpClient.Builder http = new HttpClient.Builder("POST", "http://100.92.32.8:8080/api/upload");
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
                Toast.makeText(getApplicationContext(), "기록이 저장되었습니다.", Toast.LENGTH_LONG).show();
            }catch(Exception e){
                Toast.makeText(getApplicationContext(), "저장에 실패했습니다. 관리자에게 문의하세요", Toast.LENGTH_LONG).show();
            }


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