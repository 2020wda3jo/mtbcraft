package com.capston.mtbcraft.Activity.Danger;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.capston.mtbcraft.Activity.Competition.CompetitionList;
import com.capston.mtbcraft.Activity.Control.NoMtb;
import com.capston.mtbcraft.Activity.Course.CourseList;
import com.capston.mtbcraft.Activity.Course.CourseSearch;
import com.capston.mtbcraft.Activity.Main.SubActivity;
import com.capston.mtbcraft.Activity.Mission.MissionList;
import com.capston.mtbcraft.Activity.Riding.MyReport;
import com.capston.mtbcraft.Activity.Scrap.MyScrap;
import com.capston.mtbcraft.R;
import com.capston.mtbcraft.network.HttpClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.navigation.NavigationView;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DangerRegister extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

    private GoogleMap mMap;
    private Geocoder geocoder;
    private EditText searchEdit, contentEdit, image_edit;
    private Button searchButton, registerButton;
    private ImageView danger_image, userImage;
    private DrawerLayout mDrawerLayout;
    private Uri temp;

    private boolean click = false;
    private String LoginId, Nickname, change_Name="", searchAddress="", clickAddress="", r_image;
    private Double searchLatitude, searchLongitude, clickLatitude, clickLongitude;

    private static int AUTOCOMPLETE_REQUEST_CODE = 1;

    List<Marker> previous_marker = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dangerdetail);

        /* 로그인 정보 가져오기 */
        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
        LoginId = auto.getString("LoginId","");
        Nickname = auto.getString("r_nickname","");
        r_image = auto.getString("r_image", "");

        searchEdit = findViewById(R.id.editText4);
        searchButton = findViewById(R.id.button7);
        contentEdit = findViewById(R.id.editText6);
        danger_image = findViewById(R.id.danger_image);
        image_edit = findViewById(R.id.editText7);
        registerButton = findViewById(R.id.dg_register);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setDisplayHomeAsUpEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        View header = navigationView.getHeaderView(0);
        userImage = (ImageView) header.findViewById(R.id.user_image);
        Bitmap user_image = BitmapFactory.decodeFile(new File(getFilesDir().getPath() + "/" + r_image).getAbsolutePath());
        userImage.setImageBitmap(user_image);
        TextView InFoUserId = (TextView) header.findViewById(R.id.infouserid);
        InFoUserId.setText(Nickname+"님 환영합니다");

        navigationView.setNavigationItemSelectedListener(menuItem -> {
            menuItem.setChecked(true);
            mDrawerLayout.closeDrawers();

            int id = menuItem.getItemId();
            switch (id) {
                //홈
                case R.id.nav_home:
                    Intent home = new Intent(getApplicationContext(), SubActivity.class);
                    home.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(home);
                    break;

                //라이딩 기록
                case R.id.nav_mylist:
                    Intent home2 = new Intent(getApplicationContext(), SubActivity.class);
                    home2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(home2);
                    Intent mylist=new Intent(getApplicationContext(), MyReport.class);
                    startActivity(mylist);
                    finish();
                    break;

                //코스보기
                case R.id.nav_courselist:
                    Intent home3 = new Intent(getApplicationContext(), SubActivity.class);
                    home3.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(home3);
                    Intent courselist=new Intent(getApplicationContext(), CourseList.class);
                    courselist.putExtra("rider_id", LoginId);
                    startActivity(courselist);
                    finish();
                    break;

                //코스검색
                case R.id.nav_course_search:
                    Intent home4 = new Intent(getApplicationContext(), SubActivity.class);
                    home4.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(home4);
                    Intent coursesearch=new Intent(getApplicationContext(), CourseSearch.class);
                    startActivity(coursesearch);
                    finish();
                    break;

                //스크랩 보관함
                case R.id.nav_course_get:
                    Intent home5 = new Intent(getApplicationContext(), SubActivity.class);
                    home5.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(home5);
                    Intent courseget=new Intent(getApplicationContext(), MyScrap.class);
                    startActivity(courseget);
                    finish();
                    break;

                //경쟁전
                case R.id.nav_comp:
                    Intent home6 = new Intent(getApplicationContext(), SubActivity.class);
                    home6.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(home6);
                    Intent comp=new Intent(getApplicationContext(), CompetitionList.class);
                    startActivity(comp);
                    finish();
                    break;

                //미션
                case R.id.nav_mission:
                    Intent home7 = new Intent(getApplicationContext(), SubActivity.class);
                    home7.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(home7);
                    Intent mission=new Intent(getApplicationContext(), MissionList.class);
                    startActivity(mission);
                    finish();
                    break;

                case R.id.friend_chodae:
                    Intent friend = new Intent();
                    friend.setAction(Intent.ACTION_SEND);
                    friend.setType("text/plain");
                    friend.putExtra(Intent.EXTRA_SUBJECT, LoginId + "님이 귀하를 초대합니다. 앱 설치하기");
                    friend.putExtra(Intent.EXTRA_TEXT, "tmarket://details?id=com.capston.mtbcraft");

                    Intent chooser = Intent.createChooser(friend, "초대하기");
                    startActivity(chooser);
                    break;

                //위험구역
                case R.id.nav_danger:
                    Intent home8 = new Intent(getApplicationContext(), SubActivity.class);
                    home8.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(home8);
                    Intent danger = new Intent(getApplicationContext(), DangerList.class);
                    startActivity(danger);
                    finish();
                    break;

                //위험구역
                case R.id.no_mtb:
                    Intent home9 = new Intent(getApplicationContext(), SubActivity.class);
                    home9.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(home9);
                    Intent nomtb = new Intent(getApplicationContext(), NoMtb.class);
                    startActivity(nomtb);
                    finish();
                    break;
            }
            return true;
        });

        danger_image.setOnClickListener(v -> {
            Intent register = new Intent();
            register.setType("image/*");
            register.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(register,1);
        });

        registerButton.setOnClickListener( v-> {

            UploadTask uploadTask = new UploadTask(getFilesDir().getPath() + "/", change_Name);
            uploadTask.execute();

            InsertTask insertTask = new InsertTask();
            Map<String, String> params = new HashMap<String, String>();

            params.put("da_rider", LoginId);
            if ( click == false )
                params.put("da_addr", searchAddress);
            else
                params.put("da_addr", clickAddress);
            params.put("da_content", String.valueOf(contentEdit.getText()));
            params.put("da_image", change_Name);
            if ( click == false ){
                params.put("da_latitude", String.valueOf(searchLatitude));
                params.put("da_longitude", String.valueOf(searchLongitude));
            }
            else {
                params.put("da_latitude", String.valueOf(clickLatitude));
                params.put("da_longitude", String.valueOf(clickLongitude));
            }

            insertTask.execute(params);

            finish();

        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        geocoder = new Geocoder(this);

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                googleMap.clear();
                MarkerOptions mOptions = new MarkerOptions();
                // 마커 타이틀
                mOptions.title("마커 좌표");
                clickLatitude = point.latitude; // 위도
                clickLongitude = point.longitude; // 경도

                mOptions.snippet(clickLatitude.toString() + ", " + clickLongitude.toString());

                mOptions.position(new LatLng(clickLatitude, clickLongitude));

                googleMap.addMarker(mOptions);

                Log.e("맵 좌표", "위도 : " + String.valueOf(point.latitude) + "\n 경도 : " + String.valueOf(point.longitude));

                List<Address> list = null;

                try {
                    list = geocoder.getFromLocation(
                            clickLatitude, // 위도
                            clickLongitude, // 경도
                            10); // 얻어올 값의 개수

                    if (list != null) {
                        if (list.size() == 0) {
                            Toast.makeText(getApplicationContext(), "해당하는 위치의 주소가 없습니다", Toast.LENGTH_LONG).show();
                        } else {
                            click = true;
                            String[] splitStr = list.get(0).toString().split(",");
                            clickAddress = splitStr[0].substring(splitStr[0].indexOf("\"") + 1, splitStr[0].length() - 2); // 주소
                            searchEdit.setText(clickAddress);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        // 버튼 이벤트
        searchButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleMap.clear();
                String str = searchEdit.getText().toString();
                List<Address> addressList = null;
                try {

                    addressList = geocoder.getFromLocationName(
                            str, // 주소
                            10);

                    if (addressList != null) {
                        if (addressList.size() == 0) {
                            Toast.makeText(getApplicationContext(), "검색어를 다시 입력해주십시오.", Toast.LENGTH_LONG).show();
                        } else {
                            click = false;
                            Log.e("전체", addressList.get(0).toString());

                            // 콤마를 기준으로 split
                            String[] splitStr = addressList.get(0).toString().split(",");
                            searchAddress = splitStr[0].substring(splitStr[0].indexOf("\"") + 1, splitStr[0].length() - 2); // 주소
                            Log.e("주소", searchAddress);

                            searchLatitude = Double.parseDouble(splitStr[10].substring(splitStr[10].indexOf("=") + 1)); // 위도
                            searchLongitude = Double.parseDouble(splitStr[12].substring(splitStr[12].indexOf("=") + 1)); // 경도

                            // 좌표(위도, 경도) 생성
                            LatLng point = new LatLng(searchLatitude, searchLongitude);
                            // 마커 생성
                            MarkerOptions mOptions2 = new MarkerOptions();
                            mOptions2.title("검색 결과");
                            mOptions2.snippet(searchAddress);
                            mOptions2.position(point);
                            mMap.addMarker(mOptions2);
                            // 해당 좌표로 화면 줌
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 15));
                        }
                    }
                } catch (Exception e) {

                }
            }
        });

        LatLng Seoul = new LatLng(37.5642135, 127.0016985);
        mMap.addMarker(new MarkerOptions().position(Seoul).title("초기 설정 서울"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Seoul, 15));

        UiSettings mapUiSettings = mMap.getUiSettings();
        mapUiSettings.setZoomControlsEnabled(true);  // 줌버튼

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                try {
                    // 선택한 이미지에서 비트맵 생성
                    if ( data.getData() != null ) {
                        InputStream in = getContentResolver().openInputStream(data.getData());
                        temp = data.getData();
                        Bitmap img = BitmapFactory.decodeStream(in);
                        in.close();
                        danger_image.setImageBitmap(img);
                        change_Name = getImageNameToUri(data.getData());
                        image_edit.setText(change_Name);
                    }
                    // 이미지 표시
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String getImageNameToUri(Uri data) {
        String[] proj = {MediaStore.Images.Media.DISPLAY_NAME};
        Cursor cursor = getContentResolver().query(data, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);
        cursor.moveToFirst();
        String imgPath = cursor.getString(column_index);
        String imgName = imgPath.substring(imgPath.lastIndexOf("/") + 1);

        return imgName;
    }

    public class UploadTask extends AsyncTask<Map<String, String>, Integer, String> {
        String filepath = "";
        String fileName = "";

        public UploadTask(String filepath, String fileName) {
            this.filepath = filepath;
            this.fileName = fileName;
        }

        @Override
        protected String doInBackground(Map<String, String>... maps) {

            final String url_address = "http://13.209.229.237:8080/android/fileUpload/danger/" + fileName;

            String str; //결과를 저장할 변수

            String fileName = "";
            try {
                InputStream tempIs = getContentResolver().openInputStream(temp);
                File tempFile = new File(filepath + "/" + change_Name);

                // 복사
                FileOutputStream outputStream = new FileOutputStream(tempFile);
                int read2;
                byte[] bytes2 = new byte[1024];

                while ((read2 = tempIs.read(bytes2)) != -1) {
                    outputStream.write(bytes2, 0, read2);
                }

                FileInputStream fis = new FileInputStream(tempFile);
                URL url = new URL(url_address);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                //웹서버를 통해 입출력 가능하도록 설정
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(false);//캐쉬 사용하지 않음
                conn.setRequestMethod("POST");
                //정해진 시간 내에 재접속할 경우 소켓을 새로 생성하지 않고 기존연결 사용
                conn.setRequestProperty("Connection", "Keep-Alive");
                //첨부파일에 대한 정보
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=files");


                DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
                //form-data; name=파일변수명; filename="첨부파일이름"
                dos.writeBytes("--files\r\n"); // --은 파일 시작 알림 표시
                dos.writeBytes("Content-Disposition: form-data;"
                        + "name=\"file1\";"
                        + "filename=\"" + fileName
                        + "\"" + "\r\n");

                dos.writeBytes("\r\n");//줄바꿈 문자
                int bytes = fis.available();
                int maxBufferSize = 1024;
                //Math.min(A, B)둘중 작은값;
                int bufferSize = Math.min(bytes, maxBufferSize);
                byte[] buffer = new byte[bufferSize];
                int read = fis.read(buffer, 0, bufferSize);
                Log.d("Test", "image byte is " + read);
                while (read > 0) {
                    //서버에 업로드
                    dos.write(buffer, 0, bufferSize);
                    bytes = fis.available();
                    bufferSize = Math.min(bytes, maxBufferSize);
                    //읽은 바이트 수
                    read = fis.read(buffer, 0, bufferSize);
                }
                dos.writeBytes("\r\n");//줄바꿈 문자

                dos.writeBytes("--files--\r\n");
                fis.close();
                dos.flush();
                dos.close();

                //서버 응답 처리
                int ch;

                int status = conn.getResponseCode();
                Log.e("status", String.valueOf(status));
                Log.e("method", conn.getRequestMethod());
                InputStream is = conn.getInputStream();
                StringBuffer sb = new StringBuffer();
                while ((ch = is.read()) != -1) { // 내용이 없을 때까지 반복
                    sb.append((char) ch); // 문자 읽어서 저장
                }
                // 스트링.trim() 스트링의 좌우 공백 제거
                str = sb.toString().trim();
                if (str.equals("success")) {
                    str = "파일이 업로드되었습니다.";
                } else if (str.equals("fail")) {
                    str = "파일 업로드 실패...";
                }

                is.close();
                conn.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {

        }
    }

    public class InsertTask extends AsyncTask<Map<String, String>, Integer, String> {
        @Override
        protected String doInBackground(Map<String, String>... maps) {
            // Http 요청 준비 작업
            //URL은 현재 자기 아이피번호를 입력해야합니다.
            HttpClient.Builder http = new HttpClient.Builder("PUT", "/app/insertDanger");
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
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onMapClick(LatLng point) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
