package com.mtbcraft.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

//import org.apache.commons.io.IOUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.mtbcraft.dto.AnLogin;
import com.mtbcraft.dto.Course;
import com.mtbcraft.dto.Login;
import com.mtbcraft.dto.RidingRecord;
import com.mtbcraft.dto.Scrap_Status;
import com.mtbcraft.service.AndroidService;
import com.mtbcraft.service.MemberService;

@Controller
public class AndroidController {

	@Autowired
	MemberService memberService;

	@Autowired
	AndroidService androidService;

	// 안드로이드 세션로그인
	@RequestMapping(value = "/android/login")
	public @ResponseBody Map<String, String> login(AnLogin login) throws Exception {

		List<AnLogin> list = androidService.LoginProcess(login);

		System.out.println("dfdfdf" + list);
		System.out.println(login.getR_pw());

		Map<String, String> result = new HashMap<String, String>();

		if (list.toString() == "[]") {
			result.put("Status", "로그인실패");
			return result;
		} else {
			result.put("Status", "Ok");
			result.put("r_id", login.getR_id());
			return result;
		}
	}

	// 주행기록 등록(안드로이드)
	@RequestMapping(value = "/api/upload")
	// 주소변경예정 @RequestMapping(value = "/android/recordInsert")
	@ResponseBody
	public Map<String, String> insertriding(HttpServletRequest request) throws Exception {
		RidingRecord record = new RidingRecord();

		// 현재날짜 timestamp
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// formatter.setTimeZone(TimeZone.getTimeZone("GMT+09"));
		Calendar cal = Calendar.getInstance();
		String today = null;
		today = formatter.format(cal.getTime());
		Timestamp ts = Timestamp.valueOf(today);
		record.setRr_rider(request.getParameter("rr_rider"));
		record.setRr_date(ts);
		record.setRr_distance(Integer.parseInt(request.getParameter("rr_distance")));
		record.setRr_topspeed(Integer.parseInt(request.getParameter("rr_topspeed")));
		record.setRr_avgspeed(Integer.parseInt(request.getParameter("rr_avgspeed")));
		record.setRr_high(Integer.parseInt(request.getParameter("rr_high")));
		record.setRr_gpx(request.getParameter("rr_gpx"));
		record.setRr_open(Integer.parseInt(request.getParameter("rr_open")));
		record.setRr_breaktime(Integer.parseInt(request.getParameter("rr_breaktime")));
		record.setRr_time(Integer.parseInt(request.getParameter("rr_time")));
		record.setRr_area(request.getParameter("rr_area"));

		androidService.insertRecord(record);
		// 안드로이드로부터 받은 데이터
		System.out.println("rr_rider " + request.getParameter("rr_rider")); // 회원아이디
		System.out.println("rr_distance " + request.getParameter("rr_distance")); // 오늘날짜
		System.out.println("rr_topspeed " + request.getParameter("rr_topspeed")); // 소요시간
		System.out.println("rr_avgspeed " + request.getParameter("rr_avgspeed")); // 이동거리
		System.out.println("rr_high " + request.getParameter("rr_high")); // 최대속도
		System.out.println("rr_gpx " + request.getParameter("rr_gpx")); // 평균속도
		System.out.println("rr_open " + request.getParameter("rr_open")); // 획득고도
		System.out.println("rr_breaktime " + request.getParameter("rr_breaktime")); // gpx
		System.out.println("rr_time " + request.getParameter("rr_time")); // 공개여부
		System.out.println("rr_area " + request.getParameter("rr_area")); // 획득고도

		// 안드로이드에게 전달하는 데이터
		Map<String, String> result = new HashMap<String, String>();
		result.put("data1", request.getParameter("rr_rider"));
		result.put("data2", request.getParameter("rr_distance"));
		result.put("data3", request.getParameter("rr_topspeed"));
		result.put("data4", request.getParameter("rr_avgspeed"));
		result.put("data5", request.getParameter("rr_high"));
		result.put("data6", request.getParameter("rr_gpx"));
		result.put("data7", request.getParameter("rr_open"));
		result.put("data8", request.getParameter("rr_breaktime"));
		result.put("data9", request.getParameter("rr_time"));
		result.put("data10", request.getParameter("rr_area"));

		return result;
	}

	// 주행기록전체 가져오기
	@RequestMapping(value = "/api/get/ridingrecord")
	public @ResponseBody List<RidingRecord> getRidingRecordAll() throws Exception {
		return androidService.getRidingRecordAll();
	}
	
	// 코스 조회
	@RequestMapping(value = "/app/riding/ridingrecord/{rr_num}")
	public @ResponseBody List<Course> getRidingRecordAllItem(@PathVariable(value = "rr_num") String rr_num) throws Exception {
		return androidService.getRidingRecordAllItem(rr_num);
	}
		
	
	// 주행기록 가져오기
	@RequestMapping(value = "/api/get/{rr_rider}")
	public @ResponseBody List<RidingRecord> getRidingRecord(@PathVariable String rr_rider) throws Exception {
		return memberService.getRidingRecord(rr_rider);
	}

	// 주행기록 가져오기
	@RequestMapping(value = { "/api/get/{rr_rider}/{rr_num}" })
	public @ResponseBody List<RidingRecord> getRidingRecordDetail(@PathVariable(value = "rr_rider") String rr_rider,
			@PathVariable(value = "rr_num") String rr_num) throws Exception {
		return memberService.getRidingRecordDetail(rr_rider, rr_num);
	}

	@RequestMapping(value = "/android/fileUpload", method = RequestMethod.POST)
	public String upload(HttpServletRequest request, MultipartFile file1) {
		try {

			// String path =
			// "/home/ec2-user/apps/mtbcraft/spring/mtbcraft/src/main/resources/static/gpx";
			
			String path = "/home/ec2-user/data/gpx";
			String fileName = "";

			if (!file1.isEmpty()) { // 첨부파일이 존재?
				fileName = file1.getOriginalFilename();
				try {
					// 디렉토리 생성
					new File(path).mkdir();
					// 지정된 업로드 경로로 저장됨
					file1.transferTo(new File(path + "/" + fileName));
					System.out.println();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:/";
	}

	// 코스 조회
	@RequestMapping(value = "/app/riding/course")
	public @ResponseBody List<Course> getCourse() throws Exception {
		return memberService.getCourse();
	}

	// 코스 조회
	@RequestMapping(value = "/app/riding/course/{c_num}")
	public @ResponseBody List<Course> getCourseItem(@PathVariable(value = "c_num") String c_num) throws Exception {
		System.out.println(c_num);
		return androidService.getCourseItem(c_num);
	}

	// 코스 스크랩하기
	@RequestMapping(value = "/app/riding/coursescrap")
	public @ResponseBody Map<String, String> coursescrap(HttpServletRequest request) throws Exception {
		Scrap_Status scrap = new Scrap_Status();
		scrap.setSs_course(Integer.parseInt(request.getParameter("c_num")));
		scrap.setSs_rider(request.getParameter("r_rider"));
		androidService.insertScrap(scrap);

		System.out.println(request.getParameter("c_num"));
		System.out.println(request.getParameter("r_rider"));
		Map<String, String> result = new HashMap<String, String>();
		result.put("Status", "Ok");

		return result;
	}

	// 사용자 스크랩 코스 조회
	@RequestMapping(value = "/app/riding/scrap/{rr_rider}")
	public @ResponseBody List<Course> getScrap(@PathVariable(value = "rr_rider") String rr_rider) throws Exception {
		return memberService.getScrapCourse(rr_rider);
	}

	// 사용자 스크랩 코스 상세보기
	@RequestMapping(value = "/app/riding/scrap/{rr_rider}/{ss_course}", method = RequestMethod.GET)
	public @ResponseBody List<Course> getScrapDetail(@PathVariable(value = "rr_rider") String rr_rider,
			@PathVariable(value = "ss_course") String ss_course) throws Exception {
		return androidService.getScrapDetail(rr_rider, ss_course);
	}

//	@RequestMapping(value = "/app/getGPX/{file_name}", method = RequestMethod.GET, produces = MediaType.APPLICATION_XML_VALUE)
//	public ResponseEntity<byte[]> getImageAsResponseEntity( @PathVariable("file_name") String fileName ) {
//		
//	    HttpHeaders headers = new HttpHeaders();
//	    byte[] media = null;
//		try {
//		    InputStream in = new FileInputStream("C://test/"+fileName);
//			media = IOUtils.toByteArray(in);
//		    headers.setCacheControl(CacheControl.noCache().getHeaderValue());
//		    
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	    ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(media, headers, HttpStatus.OK);
//	    return responseEntity;
//	}

	@RequestMapping(value = "/app/getRidingRecord")
	public String getAppRidingRecord() {
		return "riding/appRidingRecord";
	}
}
