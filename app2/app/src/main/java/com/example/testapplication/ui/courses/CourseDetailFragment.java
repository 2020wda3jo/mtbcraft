package com.example.testapplication.ui.courses;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testapplication.R;
import com.example.testapplication.gpx.GPXParser;
import com.example.testapplication.gpx.Gpx;
import com.example.testapplication.gpx.Track;
import com.example.testapplication.gpx.TrackPoint;
import com.example.testapplication.gpx.TrackSegment;
import com.example.testapplication.net.HttpClient;
import com.example.testapplication.ui.BaseFragment;
import com.example.testapplication.ui.riding.FollowStart;
import com.squareup.picasso.Picasso;

import net.daum.mf.map.api.CameraUpdateFactory;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapPointBounds;
import net.daum.mf.map.api.MapPolyline;
import net.daum.mf.map.api.MapView;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CourseDetailFragment extends BaseFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private String rr_num, LoginId;
    private int rr_open;
    private GPXParser mParser = new GPXParser();
    private Gpx parsedGpx = null;
    private MapView mapView;
    private MapPolyline polyline = new MapPolyline();
    private ImageView userImage;
    private Button share_bt, follow_bt, scrap_bt;
    List<Track> tracks;
    private TextView RidingTime, RidingRest, RidingDistance, RidingMax, RidingAvg, RidingGet, RidingAddr, Course_name, CourseRiderName, CourseDate, LikeCnt;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_course_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        model.courseName.observe(getViewLifecycleOwner(), name->{
            if ( name != null ) {
                AppCompatActivity appCompatActivity = (AppCompatActivity) requireActivity();
                appCompatActivity.getSupportActionBar().setTitle(name);
            }
        });

        mapView = new MapView(requireActivity());
        ViewGroup mapViewContainer = (ViewGroup) view.findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);

        //폴리라인 그리자~
        polyline.setTag(1000);
        polyline.setLineColor(Color.argb(255, 255, 51, 0)); // Polyline 컬러 지정.

        /* 로그인 정보 가져오기 */
        LoginId = model.r_Id.getValue();
        rr_num = model.r_num.getValue();
        share_bt = (Button) view.findViewById(R.id.share_bt);
        RidingTime = (TextView) view.findViewById(R.id.Riding_time);
        RidingRest = (TextView) view.findViewById(R.id.Riding_rest);
        RidingDistance = (TextView) view.findViewById(R.id.Riding_distance);
        RidingMax = (TextView) view.findViewById(R.id.Riding_max);
        RidingAvg = (TextView) view.findViewById(R.id.Riding_avg);
        RidingGet = (TextView) view.findViewById(R.id.Riding_godo);
        RidingAddr = (TextView) view.findViewById(R.id.Riding_addr);
        Course_name = (TextView) view.findViewById(R.id.Course_name);
        CourseRiderName = (TextView) view.findViewById(R.id.CourseRiderName);
        CourseDate = (TextView) view.findViewById(R.id.CourseDate);
        LikeCnt = (TextView) view.findViewById(R.id.LikeCnt);
        follow_bt = (Button) view.findViewById(R.id.follow_bt);
        scrap_bt = (Button) view.findViewById(R.id.scrap_bt);
        userImage = (ImageView) view.findViewById(R.id.imageView5);

        RidingTime.setText(model.my_rec_time.getValue());
        RidingRest.setText(model.my_rec_rest.getValue());
        RidingDistance.setText(model.my_rec_dis.getValue());
        RidingMax.setText(model.my_rec_max.getValue());
        RidingAvg.setText(model.my_rec_avg.getValue());
        RidingGet.setText(model.my_rec_get.getValue());
        RidingAddr.setText(model.my_rec_adress.getValue());
        Course_name.setText(model.my_rec_name.getValue());
        CourseRiderName.setText(model.CourseRider.getValue());
        CourseDate.setText(model.my_rec_date.getValue());
        LikeCnt.setText(String.valueOf(model.like_count.getValue()));
        Picasso.get().load("http://13.209.229.237:8080/app/getGPX/rider/" + model.CourseR_image.getValue())
                .into(userImage);

        Log.e("이미지", model.CourseR_image.getValue());


        Thread uThread = new Thread() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://13.209.229.237:8080/app/getGPX/gpx/"+model.my_rec_gpx.getValue());
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setDoInput(true); //Server 통신에서 입력 가능한 상태로 만듦
                    conn.connect(); //연결된 곳에 접속할 때 (connect() 호출해야 실제 통신 가능함)
                    InputStream is = conn.getInputStream(); //inputStream 값 가져오기
                    // InputStream in = getAssets().open("and.gpx");
                    parsedGpx = mParser.parse(is);

                    if (parsedGpx != null) {
                        // log stuff
                        tracks = parsedGpx.getTracks();
                        for (int i = 0; i < tracks.size(); i++) {
                            Track track = tracks.get(i);
                            Log.d("track ", i + ":");
                            List<TrackSegment> segments = track.getTrackSegments();
                            for (int j = 0; j < segments.size(); j++) {
                                TrackSegment segment = segments.get(j);
                                for (TrackPoint trackPoint : segment.getTrackPoints()) {
                                    polyline.addPoint(MapPoint.mapPointWithGeoCoord(trackPoint.getLatitude(), trackPoint.getLongitude()));
                                    Log.d("point: lat ", + trackPoint.getLatitude() + ", lon " + trackPoint.getLongitude());
                                }
                            }
                        }

                        // Polyline 지도에 올리기.
                        mapView.addPolyline(polyline);

                        // 지도뷰의 중심좌표와 줌레벨을 Polyline이 모두 나오도록 조정.
                        MapPointBounds mapPointBounds = new MapPointBounds(polyline.getMapPoints());
                        int padding = 100; // px
                        mapView.moveCamera(CameraUpdateFactory.newMapPointBounds(mapPointBounds, padding));

                    } else {
                        Log.e("error","Error parsing gpx track!");
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException | XmlPullParserException e) {
                    e.printStackTrace();
                }
            }
        };
        uThread.start(); // 작업 Thread 실행

        //공유버튼
        share_bt.setOnClickListener(v -> {
            Intent intent1 = new Intent();
            intent1.setAction(Intent.ACTION_SEND);
            intent1.setType("text/plain");
            intent1.putExtra(Intent.EXTRA_SUBJECT, model.r_Nickname.getValue()+"님이"+model.my_rec_name.getValue()+"코스를 공유하였습니다. 해당URL을 누르면 해당 페이지로 이동합니다. ");
            intent1.putExtra(Intent.EXTRA_TEXT, "http://13.209.229.237:8080/app/riding/course_share/"+rr_num);

            Intent chooser = Intent.createChooser(intent1, "친구에게 공유하기");
            startActivity(chooser);

        });

        //스크랩 보관함
        scrap_bt.setOnClickListener(v -> {
            Log.d("스크랩",rr_num+" "+LoginId);
            ScrapTask scrap = new ScrapTask();
            Map<String, String> params = new HashMap<String, String>();
            params.put("c_num", rr_num);
            params.put("ss_rider", LoginId);
            scrap.execute(params);
            Log.d("따라가기", rr_num+ " "+ LoginId);

        });

        //따라가기
        follow_bt.setOnClickListener(v->{
            Intent intent2=new Intent(requireContext(), FollowStart.class);
            intent2.putExtra("gpx",String.valueOf(model.my_rec_gpx));
            intent2.putExtra("c_name",String.valueOf(model.my_rec_name));
            startActivity(intent2);

            mapViewContainer.removeAllViews();
            Log.d("따라가기", String.valueOf(model.my_rec_gpx.getValue())+ " "+ String.valueOf(model.my_rec_name.getValue()));
        });

    }

    public class ScrapTask extends AsyncTask<Map<String, String>, Integer, String> {
        @Override
        protected String doInBackground(Map<String, String>... maps) {

            // Http 요청 준비 작업
            //URL은 현재 자기 아이피번호를 입력해야합니다.
            HttpClient.Builder http = new HttpClient.Builder("POST", "/app/riding/coursescrap");
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
            try {
                Toast.makeText(requireContext(), "스크랩 보관함에 저장되었습니다.", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(requireContext(), "저장에 실패하였습니다", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
